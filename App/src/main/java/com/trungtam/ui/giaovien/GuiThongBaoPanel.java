package com.trungtam.ui.giaovien;

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

    private final JComboBox<String> cboLop  = new JComboBox<>(new String[]{"Toán 12A", "Lý 11B", "Hóa 10C", "Tất cả lớp"});
    private final JTextField txtTieuDe      = new JTextField();
    private final JTextArea  txtNoiDung     = new JTextArea(4, 0);
    private final DefaultTableModel histModel;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public GuiThongBaoPanel() {
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(14, 14, 14, 14));

        JLabel title = new JLabel("Gửi Thông Báo Đến Lớp Học");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        histModel = new DefaultTableModel(new String[]{"Thời Gian", "Lớp", "Tiêu Đề", "Nội Dung"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        loadSampleHistory();

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, buildFormPanel(), buildHistoryPanel());
        split.setDividerLocation(230);
        split.setDividerSize(5);
        split.setBorder(null);
        add(split, BorderLayout.CENTER);
    }

    private JPanel buildFormPanel() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xFFCC80), 1),
            new EmptyBorder(14, 14, 14, 14)));

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(6, 4, 6, 8);

        g.gridx = 0; g.gridy = 0; g.weightx = 0;
        form.add(label("Gửi đến:", Font.PLAIN, 13), g);
        g.gridx = 1; g.weightx = 1;
        form.add(cboLop, g);

        g.gridx = 0; g.gridy = 1; g.weightx = 0;
        form.add(label("Tiêu đề *:", Font.PLAIN, 13), g);
        g.gridx = 1; g.weightx = 1;
        txtTieuDe.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtTieuDe.putClientProperty("JTextField.placeholderText", "Nhập tiêu đề thông báo...");
        form.add(txtTieuDe, g);

        g.gridx = 0; g.gridy = 2; g.weightx = 0; g.anchor = GridBagConstraints.NORTHWEST;
        form.add(label("Nội dung *:", Font.PLAIN, 13), g);
        g.gridx = 1; g.weightx = 1; g.anchor = GridBagConstraints.CENTER;
        txtNoiDung.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtNoiDung.setLineWrap(true);
        txtNoiDung.setWrapStyleWord(true);
        JScrollPane scroller = new JScrollPane(txtNoiDung);
        scroller.setPreferredSize(new Dimension(0, 80));
        form.add(scroller, g);

        outer.add(form, BorderLayout.CENTER);

        JButton btnGui = new JButton("Gửi Thông Báo");
        btnGui.setBackground(new Color(0xF57C00));
        btnGui.setForeground(Color.WHITE);
        btnGui.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnGui.setFocusPainted(false);
        btnGui.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnGui.addActionListener(e -> send());

        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 6));
        btnBar.setOpaque(false);
        btnBar.add(btnGui);
        outer.add(btnBar, BorderLayout.SOUTH);
        return outer;
    }

    private JPanel buildHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 6));

        JLabel lblHist = new JLabel("  Lịch Sử Thông Báo Đã Gửi");
        lblHist.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblHist.setOpaque(true);
        lblHist.setBackground(new Color(0xFFF3E0));
        lblHist.setForeground(new Color(0xE65100));
        lblHist.setPreferredSize(new Dimension(0, 30));
        lblHist.setBorder(new EmptyBorder(0, 6, 0, 0));

        JTable table = new JTable(histModel);
        table.setRowHeight(32);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setFillsViewportHeight(true);

        // Đường kẻ rõ ràng như Excel
        table.setShowGrid(true);
        table.setGridColor(new Color(0xBDBDBD));
        table.setIntercellSpacing(new Dimension(1, 1));

        // Cột cố định không kéo dãn; cột Nội Dung fill phần còn lại của cửa sổ
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        int[] fixedWidths = {130, 100, 200}; // Thời Gian / Lớp / Tiêu Đề
        for (int i = 0; i < fixedWidths.length; i++) {
            TableColumn col = table.getColumnModel().getColumn(i);
            col.setPreferredWidth(fixedWidths[i]);
            col.setMinWidth(fixedWidths[i]);
            col.setMaxWidth(fixedWidths[i]);
            col.setResizable(false);
        }
        // Cột Nội Dung: không cho thu hẹp dưới 200px; fill phần còn lại tự động
        table.getColumnModel().getColumn(3).setMinWidth(200);
        table.getColumnModel().getColumn(3).setResizable(false);


        // Màu xen kẽ dòng; reset rõ foreground để tránh kế thừa màu theme
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int r, int c) {
                super.getTableCellRendererComponent(t, val, sel, foc, r, c);
                if (!sel) {
                    setBackground(r % 2 == 0 ? Color.WHITE : new Color(0xFFF8F0));
                    setForeground(new Color(0x212121)); // đen đậm, dễ đọc
                }
                return this;
            }
        });

        panel.add(lblHist, BorderLayout.NORTH);
        panel.add(new JScrollPane(table,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
        return panel;
    }

    private void send() {
        String tieuDe  = txtTieuDe.getText().trim();
        String noiDung = txtNoiDung.getText().trim();
        if (tieuDe.isEmpty() || noiDung.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tiêu đề và nội dung.", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
            return;
        }
        histModel.insertRow(0, new Object[]{LocalDateTime.now().format(FMT), cboLop.getSelectedItem(), tieuDe, noiDung});
        txtTieuDe.setText("");
        txtNoiDung.setText("");
        JOptionPane.showMessageDialog(this, "Đã gửi thông báo thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }

    private void loadSampleHistory() {
        histModel.addRow(new Object[]{"15/05/2026 10:30", "Toán 12A", "Lịch thi giữa kỳ",     "Thi vào thứ 6 tuần sau, ôn bài chương 3-5."});
        histModel.addRow(new Object[]{"13/05/2026 08:15", "Lý 11B",   "Nghỉ buổi 14/05",       "Cô bận công tác, học bù thứ 7 tuần sau."});
        histModel.addRow(new Object[]{"10/05/2026 16:00", "Tất cả lớp", "Hội thảo học thuật",  "Trung tâm tổ chức hội thảo ngày 20/05, mời tham dự."});
    }

    private static JLabel label(String text, int style, int size) {
        JLabel l = new JLabel(text); l.setFont(new Font("Segoe UI", style, size)); return l;
    }
}
