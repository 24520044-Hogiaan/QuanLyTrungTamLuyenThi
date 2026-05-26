package com.trungtam.ui.hocvien;

import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * Hồ sơ cá nhân học viên
 */
public class HoSoCaNhanPanel extends JPanel {

    private final JTextField txtHoTen = new JTextField("Nguyễn Minh Anh");
    private final JTextField txtNgaySinh = new JTextField("10/03/2007");
    private final JComboBox<String> cboGioiTinh = new JComboBox<>(new String[] { "Nam", "Nữ", "Khác" });
    private final JTextField txtEmail = new JTextField("anh.nm@mail.com");
    private final JTextField txtSDT = new JTextField("0901111111");
    private final JTextField txtDiaChi = new JTextField("123 Đường Nguyễn Huệ, Q.1, TP.HCM");
    private final JComboBox<String> cboKhoi = new JComboBox<>(new String[] { "Khối A", "Khối B", "Khối D" });
    private final JTextField txtNV1 = new JTextField("Đại học Bách Khoa TP.HCM - CNTT");
    private final JTextField txtNV2 = new JTextField("Đại học Khoa Học Tự Nhiên - Toán");
    private final JTextField txtNV3 = new JTextField("Đại học Công Nghệ - HTTT");
    private final JLabel lblThongBao = new JLabel(" ");

    public HoSoCaNhanPanel() {
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(UiTheme.PAD_L, UiTheme.PAD_L, UiTheme.PAD_L, UiTheme.PAD_L));
        setBackground(UiTheme.APP_BG);

        JLabel title = new JLabel("HỒ SƠ CÁ NHÂN");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.SECONDARY);
        add(title, BorderLayout.NORTH);

        JPanel formWrapper = new JPanel(new BorderLayout(0, 12));
        formWrapper.setOpaque(false);
        formWrapper.add(buildForm(), BorderLayout.CENTER);
        formWrapper.add(buildSaveBar(), BorderLayout.SOUTH);

        add(UiComponents.scrollPane(formWrapper), BorderLayout.CENTER);
    }

    private JPanel buildForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UiTheme.CARD_BG);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.SECONDARY_FG, 1),
                new EmptyBorder(UiTheme.PAD_L, UiTheme.PAD_L, UiTheme.PAD_L, UiTheme.PAD_L)));

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(7, 6, 7, 8);

        addSection(form, g, 0, "THÔNG TIN CƠ BẢN");
        addRow(form, g, 1, "Họ và tên *:", txtHoTen);
        addRow(form, g, 2, "Ngày sinh (dd/MM/yyyy) *:", txtNgaySinh);
        addRow(form, g, 3, "Giới tính:", cboGioiTinh);
        addRow(form, g, 4, "Email:", txtEmail);
        addRow(form, g, 5, "Số điện thoại:", txtSDT);
        addRow(form, g, 6, "Địa chỉ:", txtDiaChi);

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
        JButton btnLuu = UiComponents.primaryButton("Lưu Thay Đổi", UiTheme.SECONDARY);
        btnLuu.addActionListener(e -> save());

        JButton btnDatLai = UiComponents.ghostButton("Đặt Lại");
        btnDatLai.addActionListener(e -> {
            lblThongBao.setText(" ");
            txtHoTen.requestFocus();
        });

        lblThongBao.setFont(UiTheme.CAPTION_I);

        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        bar.setOpaque(false);
        bar.add(btnLuu);
        bar.add(btnDatLai);
        bar.add(lblThongBao);
        return bar;
    }

    private void save() {
        if (txtHoTen.getText().trim().isEmpty() || txtNgaySinh.getText().trim().isEmpty()) {
            lblThongBao.setForeground(UiTheme.DANGER);
            lblThongBao.setText("Vui lòng nhập đầy đủ các trường bắt buộc (*)");
            return;
        }
        lblThongBao.setForeground(UiTheme.SUCCESS);
        lblThongBao.setText("Cập nhật hồ sơ thành công!");
    }

    private void addSection(JPanel form, GridBagConstraints g, int row, String title) {
        g.gridx = 0;
        g.gridy = row;
        g.gridwidth = 2;
        g.weightx = 1;
        JLabel lbl = UiComponents.divider(title, UiTheme.SECONDARY);
        form.add(lbl, g);
        g.gridwidth = 1;
    }

    private void addRow(JPanel form, GridBagConstraints g, int row, String label, JComponent field) {
        g.gridx = 0;
        g.gridy = row;
        g.weightx = 0.35;
        JLabel lbl = new JLabel(label);
        lbl.setFont(UiTheme.BODY);
        lbl.setForeground(UiTheme.TEXT_SECONDARY);
        form.add(lbl, g);
        g.gridx = 1;
        g.weightx = 0.65;
        field.setFont(UiTheme.BODY);
        form.add(field, g);
    }
}
