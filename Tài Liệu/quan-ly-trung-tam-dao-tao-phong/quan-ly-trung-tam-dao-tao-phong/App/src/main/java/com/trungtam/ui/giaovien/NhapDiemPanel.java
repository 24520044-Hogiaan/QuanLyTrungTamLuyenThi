package com.trungtam.ui.giaovien;

import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

/**
 * Panel nhập điểm kiểm tra.
 * Luồng: Chọn lớp → Chọn / Tạo bài kiểm tra → Nhập điểm + nhận xét → Lưu.
 */
public class NhapDiemPanel extends JPanel {

    record BaiKiemTra(int so, String ten, String loai, LocalDate ngay, double diemToiDa) {
        private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        @Override
        public String toString() {
            return ten + "  [" + ngay.format(FMT) + "]";
        }
    }

    private static final String[] LOP_LIST = { "Toán 12A", "Lý 11B", "Hóa 10C" };
    private static final String[] LOAI_BKT = { "Thường xuyên", "Giữa kỳ", "Cuối kỳ" };
    private static final String[][] HV_PER_LOP = {
            { "Nguyễn Minh Anh", "Trần Bảo Châu", "Lê Văn Dũng", "Phạm Thị Hoa", "Đỗ Quốc Hùng" },
            { "Võ Thị Lan", "Bùi Đức Mạnh", "Đinh Thị Nga" },
            { "Hoàng Văn Phúc", "Ngô Thị Quỳnh", "Phan Minh Tâm", "Lý Thị Uyên" }
    };

    private final JComboBox<String> cboLop = new JComboBox<>(LOP_LIST);
    private final DefaultListModel<BaiKiemTra> listModel = new DefaultListModel<>();
    private final JList<BaiKiemTra> bktList = new JList<>(listModel);
    private final JLabel lblBktInfo = new JLabel("← Chọn bài kiểm tra hoặc tạo mới");
    private final JLabel lblDiemMax = new JLabel();
    private DefaultTableModel tableModel;
    private JTable table;
    private final JButton btnLuu = UiComponents.primaryButton("Lưu Điểm", UiTheme.INFO);

    private final Map<String, List<BaiKiemTra>> lopBktMap = new HashMap<>();

    public NhapDiemPanel() {
        setLayout(new BorderLayout(0, 10));
        setBorder(new EmptyBorder(UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M));
        setBackground(UiTheme.APP_BG);
        initSampleData();

        add(buildTopBar(), BorderLayout.NORTH);
        add(buildSplitPane(), BorderLayout.CENTER);

        cboLop.addActionListener(e -> refreshBktList());
        refreshBktList();
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout(12, 0));
        bar.setOpaque(false);
        JLabel title = new JLabel("Nhập Điểm Kiểm Tra");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.TEXT_PRIMARY);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);
        JLabel lblLop = new JLabel("Lớp:");
        lblLop.setFont(UiTheme.BODY);
        cboLop.setFont(UiTheme.BODY);
        right.add(lblLop);
        right.add(cboLop);

        bar.add(title, BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    private JSplitPane buildSplitPane() {
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buildLeftPanel(), buildRightPanel());
        split.setDividerLocation(240);
        split.setDividerSize(5);
        split.setBorder(null);
        return split;
    }

    private JPanel buildLeftPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setPreferredSize(new Dimension(240, 0));
        panel.setBackground(UiTheme.CARD_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.CARD_BORDER, 1),
                new EmptyBorder(10, 10, 10, 10)));

        JLabel lbl = new JLabel("DANH SÁCH BÀI KIỂM TRA");
        lbl.setFont(UiTheme.CAPTION);
        lbl.setForeground(UiTheme.INFO);
        panel.add(lbl, BorderLayout.NORTH);

        bktList.setFont(UiTheme.BODY);
        bktList.setFixedCellHeight(44);
        bktList.setCellRenderer(new BktCellRenderer());
        bktList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting())
                onBktSelected();
        });
        JScrollPane listScroll = UiComponents.scrollPane(bktList);
        listScroll.setBorder(BorderFactory.createLineBorder(UiTheme.CARD_BORDER, 1));
        panel.add(listScroll, BorderLayout.CENTER);

        Color purple = new Color(0x6A1B9A);
        JButton btnTao = UiComponents.primaryButton("+  Tạo Bài Kiểm Tra", purple);
        btnTao.addActionListener(e -> showTaoBktDialog());
        panel.add(btnTao, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildRightPanel() {
        String[] cols = { "#", "Họ Tên Học Viên", "Điểm Số", "Nhận Xét" };
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return c == 2 || c == 3;
            }
        };
        table = new JTable(tableModel);
        UiComponents.styleTable(table);
        table.getColumnModel().getColumn(0).setMaxWidth(40);
        table.getColumnModel().getColumn(2).setMaxWidth(90);
        table.getColumnModel().getColumn(2).setCellRenderer(new ScoreCellRenderer());
        // #: center; Họ Tên: left; Điểm: center; Nhận Xét: left
        UiComponents.setColumnAlignments(table,
                SwingConstants.CENTER, SwingConstants.LEFT,
                SwingConstants.CENTER, SwingConstants.LEFT);

        lblBktInfo.setFont(UiTheme.CAPTION_I);
        lblBktInfo.setForeground(UiTheme.TEXT_MUTED);
        lblDiemMax.setFont(UiTheme.BODY_B);
        lblDiemMax.setForeground(new Color(0x6A1B9A));

        JPanel infoRow = new JPanel(new BorderLayout(8, 0));
        infoRow.setOpaque(false);
        infoRow.setBorder(new EmptyBorder(0, 4, 6, 0));
        infoRow.add(lblBktInfo, BorderLayout.CENTER);
        infoRow.add(lblDiemMax, BorderLayout.EAST);

        btnLuu.setEnabled(false);
        btnLuu.addActionListener(e -> validateAndSave());

        JPanel right = new JPanel(new BorderLayout(0, 8));
        right.setOpaque(false);
        right.setBorder(new EmptyBorder(0, 10, 0, 0));
        right.add(infoRow, BorderLayout.NORTH);
        right.add(UiComponents.tableScroll(table), BorderLayout.CENTER);

        JPanel saveBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 4));
        saveBar.setOpaque(false);
        saveBar.add(btnLuu);
        right.add(saveBar, BorderLayout.SOUTH);
        return right;
    }

    private void refreshBktList() {
        listModel.clear();
        lopBktMap.getOrDefault(getSelectedLop(), List.of()).forEach(listModel::addElement);
        tableModel.setRowCount(0);
        lblBktInfo.setText("← Chọn bài kiểm tra hoặc tạo mới");
        lblBktInfo.setFont(UiTheme.CAPTION_I);
        lblBktInfo.setForeground(UiTheme.TEXT_MUTED);
        lblDiemMax.setText("");
        btnLuu.setEnabled(false);
    }

    private void onBktSelected() {
        BaiKiemTra b = bktList.getSelectedValue();
        if (b == null)
            return;

        DateTimeFormatter viFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        lblBktInfo.setFont(UiTheme.BODY_B);
        lblBktInfo.setForeground(new Color(0x6A1B9A));
        lblBktInfo.setText(b.ten() + "  |  " + b.loai() + "  |  " + b.ngay().format(viFmt));
        lblDiemMax.setText("Điểm tối đa: " + (int) b.diemToiDa() + "  ");

        String lop = getSelectedLop();
        int lopIdx = Arrays.asList(LOP_LIST).indexOf(lop);
        tableModel.setRowCount(0);
        String[] hv = HV_PER_LOP[lopIdx];
        for (int i = 0; i < hv.length; i++) {
            tableModel.addRow(new Object[] { i + 1, hv[i], "", "" });
        }
        btnLuu.setEnabled(true);
    }

    private void validateAndSave() {
        BaiKiemTra bkt = bktList.getSelectedValue();
        if (bkt == null)
            return;

        for (int r = 0; r < tableModel.getRowCount(); r++) {
            String raw = String.valueOf(tableModel.getValueAt(r, 2)).trim();
            if (raw.isEmpty())
                continue;
            try {
                double diem = Double.parseDouble(raw);
                if (diem < 0 || diem > bkt.diemToiDa()) {
                    JOptionPane.showMessageDialog(this,
                            "Dòng " + (r + 1) + ": Điểm phải từ 0 đến " + (int) bkt.diemToiDa() + ".",
                            "Điểm không hợp lệ", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Dòng " + (r + 1) + ": \"" + raw + "\" không phải số.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Lưu điểm thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showTaoBktDialog() {
        JTextField txtTen = new JTextField(16);
        JTextField txtNgay = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), 12);
        JTextField txtDiemMax = new JTextField("10", 6);
        JComboBox<String> cboLoai = new JComboBox<>(LOAI_BKT);
        txtTen.setFont(UiTheme.BODY);
        txtNgay.setFont(UiTheme.BODY);
        txtDiemMax.setFont(UiTheme.BODY);
        cboLoai.setFont(UiTheme.BODY);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 4, 6, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        Object[][] rows = {
                { "Tên bài kiểm tra *:", txtTen },
                { "Loại:", cboLoai },
                { "Ngày kiểm tra (dd/MM/yyyy) *:", txtNgay },
                { "Điểm tối đa *:", txtDiemMax }
        };
        for (int i = 0; i < rows.length; i++) {
            g.gridx = 0;
            g.gridy = i;
            g.weightx = 0.4;
            JLabel lbl = new JLabel((String) rows[i][0]);
            lbl.setFont(UiTheme.BODY);
            form.add(lbl, g);
            g.gridx = 1;
            g.weightx = 0.6;
            form.add((JComponent) rows[i][1], g);
        }

        String lop = getSelectedLop();
        int opt = JOptionPane.showConfirmDialog(this, form,
                "Tạo bài kiểm tra – " + lop, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opt != JOptionPane.OK_OPTION)
            return;

        String ten = txtTen.getText().trim();
        if (ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên bài kiểm tra.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            LocalDate ngay = LocalDate.parse(txtNgay.getText().trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            double diemToiDa = Double.parseDouble(txtDiemMax.getText().trim());
            List<BaiKiemTra> list = lopBktMap.computeIfAbsent(lop, k -> new ArrayList<>());
            list.add(new BaiKiemTra(list.size() + 1, ten, (String) cboLoai.getSelectedItem(), ngay, diemToiDa));
            refreshBktList();
            bktList.setSelectedIndex(listModel.size() - 1);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ngày hoặc điểm tối đa không hợp lệ.", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getSelectedLop() {
        return (String) cboLop.getSelectedItem();
    }

    private void initSampleData() {
        lopBktMap.put("Toán 12A", new ArrayList<>(List.of(
                new BaiKiemTra(1, "Kiểm tra 15 phút – Chương 1", "Thường xuyên", LocalDate.of(2026, 5, 6), 10),
                new BaiKiemTra(2, "Giữa kỳ", "Giữa kỳ", LocalDate.of(2026, 5, 14), 100))));
        lopBktMap.put("Lý 11B", new ArrayList<>(List.of(
                new BaiKiemTra(1, "Kiểm tra chương Điện", "Thường xuyên", LocalDate.of(2026, 5, 8), 10))));
        lopBktMap.put("Hóa 10C", new ArrayList<>(List.of(
                new BaiKiemTra(1, "Giữa kỳ", "Giữa kỳ", LocalDate.of(2026, 5, 15), 100),
                new BaiKiemTra(2, "Cuối kỳ", "Cuối kỳ", LocalDate.of(2026, 5, 28), 100))));
    }

    // ── Cell renderers ────────────────────────────────────────────────────────

    private static class ScoreCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int r, int c) {
            super.getTableCellRendererComponent(t, val, sel, foc, r, c);
            setHorizontalAlignment(CENTER);
            setFont(UiTheme.BODY_B);
            String s = String.valueOf(val).trim();
            if (!sel && !s.isEmpty() && !s.equals("null")) {
                try {
                    double d = Double.parseDouble(s);
                    setForeground(d >= 8 ? UiTheme.SUCCESS : d >= 5 ? UiTheme.WARNING : UiTheme.DANGER);
                } catch (NumberFormatException ignored) {
                    setForeground(UiTheme.DANGER);
                }
            }
            return this;
        }
    }

    private static class BktCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object val, int idx, boolean sel, boolean foc) {
            super.getListCellRendererComponent(list, val, idx, sel, foc);
            if (val instanceof BaiKiemTra b) {
                setText("<html><b>" + b.ten() + "</b><br><small style='color:gray'>"
                        + b.loai() + " · " + b.ngay().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                        + "</small></html>");
            }
            setFont(UiTheme.BODY);
            setBorder(new EmptyBorder(4, 10, 4, 8));
            if (!sel) {
                setBackground(idx % 2 == 0 ? UiTheme.CARD_BG : UiTheme.ROW_ALT);
                setForeground(UiTheme.TEXT_PRIMARY);
            }
            return this;
        }
    }
}
