package com.trungtam.ui.hocvien;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;

/**
 * Đăng ký / Chuyển / Hủy lớp — 3 tab riêng biệt cho từng tác vụ.
 */
public class DangKyChuyenHuyPanel extends JPanel {

    private static final String[] LOP_DA_DK = {"Toán 12A", "Tiếng Anh 11B"};
    private static final String[] LOP_MO    = {"Vật Lý 11B", "Hóa Học 10C", "Ngữ Văn 12A",
                                                "Tiếng Anh 12A", "Toán 11B", "Hóa Học 11A"};

    public DangKyChuyenHuyPanel() {
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("ĐĂNG KÝ / CHUYỂN / HỦY LỚP HỌC");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(0x1B5E20));
        add(title, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabs.addTab("  Đăng Ký Lớp  ",  buildDangKyTab());
        tabs.addTab("  Chuyển Lớp  ",    buildChuyenLopTab());
        tabs.addTab("  Hủy Lớp  ",       buildHuyLopTab());
        add(tabs, BorderLayout.CENTER);
    }

    // ── Tab Đăng Ký ──────────────────────────────────────────────────────────

    private JPanel buildDangKyTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel note = infoLabel("Chọn lớp học muốn đăng ký từ danh sách bên dưới.");
        panel.add(note, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(
            new String[]{"Tên Lớp", "Môn", "Giảng Viên", "Buổi/Tuần", "Sĩ Số Còn"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        model.addRow(new Object[]{"Vật Lý 11B",  "Vật lý",   "Cô Trần Thị Bình",   2, "5/20"});
        model.addRow(new Object[]{"Hóa Học 10C", "Hóa học",  "Thầy Lê Hoàng Cường", 2, "8/22"});
        model.addRow(new Object[]{"Ngữ Văn 12A", "Ngữ văn",  "Cô Phạm Thị Dung",    2, "3/18"});
        model.addRow(new Object[]{"Tiếng Anh 12A","Tiếng Anh","Cô Phạm Thị Dung",   3, "10/24"});

        JTable table = buildTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btn = actionButton("Gửi Yêu Cầu Đăng Ký", new Color(0x1B5E20));
        btn.addActionListener(e -> {
            if (table.getSelectedRow() < 0) {
                JOptionPane.showMessageDialog(panel, "Vui lòng chọn lớp muốn đăng ký.", "Chưa chọn lớp", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String tenLop = (String) model.getValueAt(table.convertRowIndexToModel(table.getSelectedRow()), 0);
            JOptionPane.showMessageDialog(panel, "Đã gửi yêu cầu đăng ký lớp: " + tenLop + "\nHệ thống sẽ xác nhận trong 24h.", "Thành Công", JOptionPane.INFORMATION_MESSAGE);
        });
        panel.add(buildBtnBar(btn), BorderLayout.SOUTH);
        return panel;
    }

    // ── Tab Chuyển Lớp ───────────────────────────────────────────────────────

    private JPanel buildChuyenLopTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL; g.insets = new Insets(8, 4, 8, 8);

        JComboBox<String> cboHien = new JComboBox<>(LOP_DA_DK);
        JComboBox<String> cboMoi  = new JComboBox<>(LOP_MO);
        JTextArea txtLyDo = new JTextArea(3, 0);
        txtLyDo.setLineWrap(true); txtLyDo.setWrapStyleWord(true);
        txtLyDo.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        g.gridx = 0; g.gridy = 0; g.weightx = 0; form.add(label("Lớp hiện tại:"), g);
        g.gridx = 1; g.weightx = 1; form.add(cboHien, g);
        g.gridx = 0; g.gridy = 1; g.weightx = 0; form.add(label("Lớp muốn chuyển sang:"), g);
        g.gridx = 1; g.weightx = 1; form.add(cboMoi, g);
        g.gridx = 0; g.gridy = 2; g.weightx = 0; g.anchor = GridBagConstraints.NORTHWEST; form.add(label("Lý do chuyển:"), g);
        g.gridx = 1; g.weightx = 1; g.anchor = GridBagConstraints.CENTER;
        form.add(new JScrollPane(txtLyDo), g);

        panel.add(infoLabel("Lưu ý: Chỉ được chuyển lớp trong 7 ngày đầu sau khi đăng ký."), BorderLayout.NORTH);
        panel.add(form, BorderLayout.CENTER);

        JButton btn = actionButton("Gửi Yêu Cầu Chuyển Lớp", new Color(0xE65100));
        btn.addActionListener(e -> {
            if (cboHien.getSelectedItem().equals(cboMoi.getSelectedItem())) {
                JOptionPane.showMessageDialog(panel, "Lớp cũ và lớp mới không được trùng nhau.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(panel, "Đã gửi yêu cầu chuyển từ [" + cboHien.getSelectedItem() + "] sang [" + cboMoi.getSelectedItem() + "].", "Thành Công", JOptionPane.INFORMATION_MESSAGE);
        });
        panel.add(buildBtnBar(btn), BorderLayout.SOUTH);
        return panel;
    }

    // ── Tab Hủy Lớp ──────────────────────────────────────────────────────────

    private JPanel buildHuyLopTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL; g.insets = new Insets(8, 4, 8, 8);

        JComboBox<String> cboLop = new JComboBox<>(LOP_DA_DK);
        JTextArea txtLyDo = new JTextArea(3, 0);
        txtLyDo.setLineWrap(true); txtLyDo.setWrapStyleWord(true);
        txtLyDo.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        g.gridx = 0; g.gridy = 0; g.weightx = 0; form.add(label("Lớp muốn hủy:"), g);
        g.gridx = 1; g.weightx = 1; form.add(cboLop, g);
        g.gridx = 0; g.gridy = 1; g.weightx = 0; g.anchor = GridBagConstraints.NORTHWEST; form.add(label("Lý do hủy:"), g);
        g.gridx = 1; g.weightx = 1; g.anchor = GridBagConstraints.CENTER;
        form.add(new JScrollPane(txtLyDo), g);

        // Cảnh báo chính sách hoàn phí
        JLabel warning = new JLabel("<html><b>Lưu ý chính sách hoàn phí:</b><br>"
            + "- Hủy trong 7 ngày đầu: hoàn 100% học phí<br>"
            + "- Sau 7 ngày đến 1 tháng: hoàn 50%<br>"
            + "- Sau 1 tháng: không hoàn phí</html>");
        warning.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        warning.setForeground(new Color(0xC62828));
        warning.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xEF9A9A)),
            new EmptyBorder(10, 12, 10, 12)));
        warning.setOpaque(true);
        warning.setBackground(new Color(0xFFEBEE));

        panel.add(warning, BorderLayout.NORTH);
        panel.add(form, BorderLayout.CENTER);

        JButton btn = actionButton("Xác Nhận Hủy Lớp", new Color(0xC62828));
        btn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(panel,
                "Bạn chắc chắn muốn hủy lớp: " + cboLop.getSelectedItem() + "?",
                "Xác Nhận Hủy Lớp", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION)
                JOptionPane.showMessageDialog(panel, "Đã gửi yêu cầu hủy lớp. Hệ thống sẽ xử lý trong 24h.", "Thành Công", JOptionPane.INFORMATION_MESSAGE);
        });
        panel.add(buildBtnBar(btn), BorderLayout.SOUTH);
        return panel;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private JTable buildTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setRowHeight(34);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setShowGrid(true); table.setGridColor(new Color(0xE0E0E0));
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int r, int c) {
                super.getTableCellRendererComponent(t, val, sel, foc, r, c);
                if (!sel) { setBackground(r % 2 == 0 ? Color.WHITE : new Color(0xF1F8E9)); setForeground(new Color(0x212121)); }
                return this;
            }
        });
        return table;
    }

    private JPanel buildBtnBar(JButton btn) {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 6));
        bar.add(btn);
        return bar;
    }

    private JButton actionButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(color); btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false); btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text); l.setFont(new Font("Segoe UI", Font.PLAIN, 13)); return l;
    }

    private JLabel infoLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        l.setForeground(new Color(0x616161));
        return l;
    }
}
