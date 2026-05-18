package com.trungtam.ui.hocvien;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * Hồ sơ cá nhân học viên — form tự điền/cập nhật thông tin bản thân.
 */
public class HoSoCaNhanPanel extends JPanel {

    // Dữ liệu mẫu của học viên hiện tại đang đăng nhập
    private final JTextField txtHoTen      = new JTextField("Nguyễn Minh Anh");
    private final JTextField txtNgaySinh   = new JTextField("10/03/2007");
    private final JComboBox<String> cboGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ", "Khác"});
    private final JTextField txtEmail      = new JTextField("anh.nm@mail.com");
    private final JTextField txtSDT        = new JTextField("0901111111");
    private final JTextField txtDiaChi     = new JTextField("123 Đường Nguyễn Huệ, Q.1, TP.HCM");
    private final JComboBox<String> cboKhoi = new JComboBox<>(new String[]{"Khối A", "Khối B", "Khối D"});
    private final JTextField txtNV1        = new JTextField("Đại học Bách Khoa TP.HCM - CNTT");
    private final JTextField txtNV2        = new JTextField("Đại học Khoa Học Tự Nhiên - Toán");
    private final JTextField txtNV3        = new JTextField("Đại học Công Nghệ - HTTT");
    private final JLabel lblThongBao       = new JLabel(" ");

    public HoSoCaNhanPanel() {
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(20, 24, 20, 24));

        JLabel title = new JLabel("HỒ SƠ CÁ NHÂN");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(0x1B5E20));
        add(title, BorderLayout.NORTH);

        JPanel formWrapper = new JPanel(new BorderLayout(0, 12));
        formWrapper.add(buildForm(), BorderLayout.CENTER);
        formWrapper.add(buildSaveBar(), BorderLayout.SOUTH);
        add(new JScrollPane(formWrapper,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
    }

    private JPanel buildForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xC8E6C9), 1),
            new EmptyBorder(20, 20, 20, 20)));

        GridBagConstraints g = new GridBagConstraints();
        g.fill   = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(7, 6, 7, 8);

        // Thông tin cơ bản
        addSection(form, g, 0, "THÔNG TIN CƠ BẢN");
        addRow(form, g, 1, "Họ và tên *:", txtHoTen);
        addRow(form, g, 2, "Ngày sinh (dd/MM/yyyy) *:", txtNgaySinh);
        addRow(form, g, 3, "Giới tính:", cboGioiTinh);
        addRow(form, g, 4, "Email:", txtEmail);
        addRow(form, g, 5, "Số điện thoại:", txtSDT);
        addRow(form, g, 6, "Địa chỉ:", txtDiaChi);

        // Học tập
        addSection(form, g, 7, "THÔNG TIN HỌC TẬP");
        addRow(form, g, 8, "Khối thi:", cboKhoi);
        cboKhoi.setSelectedIndex(0);

        addSection(form, g, 9, "NGUYỆN VỌNG ĐẠI HỌC");
        addRow(form, g, 10, "Nguyện vọng 1:", txtNV1);
        addRow(form, g, 11, "Nguyện vọng 2:", txtNV2);
        addRow(form, g, 12, "Nguyện vọng 3:", txtNV3);

        return form;
    }

    private JPanel buildSaveBar() {
        JButton btnLuu = new JButton("Lưu Thay Đổi");
        btnLuu.setBackground(new Color(0x2E7D32));
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLuu.setFocusPainted(false);
        btnLuu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLuu.addActionListener(e -> save());

        JButton btnDatLai = new JButton("Đặt Lại");
        btnDatLai.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnDatLai.setFocusPainted(false);
        btnDatLai.addActionListener(e -> {
            lblThongBao.setText(" ");
            txtHoTen.requestFocus();
        });

        lblThongBao.setFont(new Font("Segoe UI", Font.ITALIC, 12));

        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        bar.add(btnLuu);
        bar.add(btnDatLai);
        bar.add(lblThongBao);
        return bar;
    }

    private void save() {
        if (txtHoTen.getText().trim().isEmpty() || txtNgaySinh.getText().trim().isEmpty()) {
            lblThongBao.setForeground(new Color(0xC62828));
            lblThongBao.setText("Vui lòng nhập đầy đủ các trường bắt buộc (*)");
            return;
        }
        lblThongBao.setForeground(new Color(0x2E7D32));
        lblThongBao.setText("Cập nhật hồ sơ thành công!");
    }

    private void addSection(JPanel form, GridBagConstraints g, int row, String title) {
        g.gridx = 0; g.gridy = row; g.gridwidth = 2; g.weightx = 1;
        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(new Color(0x1B5E20));
        lbl.setBorder(new MatteBorder(0, 0, 1, 0, new Color(0xC8E6C9)));
        form.add(lbl, g);
        g.gridwidth = 1;
    }

    private void addRow(JPanel form, GridBagConstraints g, int row, String label, JComponent field) {
        g.gridx = 0; g.gridy = row; g.weightx = 0;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        form.add(lbl, g);
        g.gridx = 1; g.weightx = 1;
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        form.add(field, g);
    }
}
