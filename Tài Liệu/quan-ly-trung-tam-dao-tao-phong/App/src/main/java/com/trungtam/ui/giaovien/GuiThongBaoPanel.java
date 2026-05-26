package com.trungtam.ui.giaovien;

import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Panel gửi thông báo đến lớp học.
 * Layout: form soạn thảo (trên) + bảng lịch sử (dưới).
 */
public class GuiThongBaoPanel extends JPanel {

    private final JComboBox<String> cboLop = new JComboBox<>(
            new String[] { "Toán 12A", "Lý 11B", "Hóa 10C", "Tất cả lớp" });
    private final JTextField txtTieuDe = new JTextField();
    private final JTextArea txtNoiDung = new JTextArea(4, 0);
    private final DefaultTableModel histModel;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public GuiThongBaoPanel() {
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M));
        setBackground(UiTheme.APP_BG);

        JLabel title = new JLabel("Gửi Thông Báo Đến Lớp Học");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.TEXT_PRIMARY);
        add(title, BorderLayout.NORTH);

        histModel = new DefaultTableModel(
                new String[] { "Thời Gian", "Lớp", "Tiêu Đề", "Nội Dung" }, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        loadSampleHistory();

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, buildFormPanel(), buildHistoryPanel());
        split.setDividerLocation(240);
        split.setDividerSize(5);
        split.setBorder(null);
        add(split, BorderLayout.CENTER);
    }

    private JPanel buildFormPanel() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(UiTheme.CARD_BG);
        outer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xFFCC80), 1),
                new EmptyBorder(UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M)));

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(6, 4, 6, 8);

        cboLop.setFont(UiTheme.BODY);
        txtTieuDe.setFont(UiTheme.BODY);
        txtTieuDe.putClientProperty("JTextField.placeholderText", "Nhập tiêu đề thông báo...");
        txtNoiDung.setFont(UiTheme.BODY);
        txtNoiDung.setLineWrap(true);
        txtNoiDung.setWrapStyleWord(true);

        addFormRow(form, g, 0, "Gửi đến:", cboLop);
        addFormRow(form, g, 1, "Tiêu đề *:", txtTieuDe);

        g.gridx = 0;
        g.gridy = 2;
        g.weightx = 0;
        g.anchor = GridBagConstraints.NORTHWEST;
        JLabel lblND = new JLabel("Nội dung *:");
        lblND.setFont(UiTheme.BODY);
        form.add(lblND, g);
        g.gridx = 1;
        g.weightx = 1;
        g.anchor = GridBagConstraints.CENTER;
        JScrollPane scroller = new JScrollPane(txtNoiDung);
        scroller.setPreferredSize(new Dimension(0, 80));
        scroller.getVerticalScrollBar().setUnitIncrement(16);
        form.add(scroller, g);

        outer.add(form, BorderLayout.CENTER);

        JButton btnGui = UiComponents.primaryButton("Gửi Thông Báo", UiTheme.WARNING);
        btnGui.addActionListener(e -> send());

        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 6));
        btnBar.setOpaque(false);
        btnBar.add(btnGui);
        outer.add(btnBar, BorderLayout.SOUTH);
        return outer;
    }

    private JPanel buildHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setBackground(UiTheme.APP_BG);

        JLabel lblHist = new JLabel("  Lịch Sử Thông Báo Đã Gửi");
        lblHist.setFont(UiTheme.BODY_B);
        lblHist.setOpaque(true);
        lblHist.setBackground(new Color(0xFFF3E0));
        lblHist.setForeground(UiTheme.WARNING);
        lblHist.setPreferredSize(new Dimension(0, 32));
        lblHist.setBorder(new EmptyBorder(0, 8, 0, 0));

        JTable table = new JTable(histModel);
        UiComponents.styleTable(table);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        // Fixed-width columns; last column fills remaining space
        int[] fixedWidths = { 130, 100, 200 };
        for (int i = 0; i < fixedWidths.length; i++) {
            TableColumn col = table.getColumnModel().getColumn(i);
            col.setPreferredWidth(fixedWidths[i]);
            col.setMinWidth(fixedWidths[i]);
            col.setMaxWidth(fixedWidths[i]);
            col.setResizable(false);
        }
        table.getColumnModel().getColumn(3).setMinWidth(200);
        table.getColumnModel().getColumn(3).setResizable(true);
        // Thời Gian: center; Lớp: center; Tiêu Đề: left; Nội Dung: left
        UiComponents.setColumnAlignments(table,
                SwingConstants.CENTER, SwingConstants.CENTER,
                SwingConstants.LEFT, SwingConstants.LEFT);
        table.setDefaultRenderer(Object.class, new UiComponents.AltRowRenderer());

        panel.add(lblHist, BorderLayout.NORTH);
        panel.add(UiComponents.tableScroll(table), BorderLayout.CENTER);
        return panel;
    }

    private void send() {
        String tieuDe = txtTieuDe.getText().trim();
        String noiDung = txtNoiDung.getText().trim();
        if (tieuDe.isEmpty() || noiDung.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập đầy đủ tiêu đề và nội dung.", "Thiếu thông tin",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        histModel.insertRow(0,
                new Object[] { LocalDateTime.now().format(FMT), cboLop.getSelectedItem(), tieuDe, noiDung });
        txtTieuDe.setText("");
        txtNoiDung.setText("");
        JOptionPane.showMessageDialog(this, "Đã gửi thông báo thành công!", "Thành công",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void loadSampleHistory() {
        histModel.addRow(new Object[] { "15/05/2026 10:30", "Toán 12A", "Lịch thi giữa kỳ",
                "Thi vào thứ 6 tuần sau, ôn bài chương 3-5." });
        histModel.addRow(new Object[] { "13/05/2026 08:15", "Lý 11B", "Nghỉ buổi 14/05",
                "Cô bận công tác, học bù thứ 7 tuần sau." });
        histModel.addRow(new Object[] { "10/05/2026 16:00", "Tất cả lớp", "Hội thảo học thuật",
                "Trung tâm tổ chức hội thảo ngày 20/05, mời tham dự." });
    }

    private void addFormRow(JPanel form, GridBagConstraints g, int row, String label, JComponent field) {
        g.gridx = 0;
        g.gridy = row;
        g.weightx = 0;
        g.anchor = GridBagConstraints.WEST;
        JLabel lbl = new JLabel(label);
        lbl.setFont(UiTheme.BODY);
        form.add(lbl, g);
        g.gridx = 1;
        g.weightx = 1;
        g.anchor = GridBagConstraints.CENTER;
        form.add(field, g);
    }
}
