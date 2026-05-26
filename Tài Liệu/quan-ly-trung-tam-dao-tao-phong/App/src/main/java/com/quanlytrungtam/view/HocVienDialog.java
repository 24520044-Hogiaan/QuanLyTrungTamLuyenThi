package com.quanlytrungtam.view;

import com.quanlytrungtam.model.HocVien;
import com.quanlytrungtam.ui.UiComponents;
import com.quanlytrungtam.ui.UiTheme;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HocVienDialog extends JDialog {

    private final JTextField txtMaHocVien = new JTextField(24);
    private final JTextField txtSTK = new JTextField(24);
    private final JTextField txtHoTen = new JTextField(24);
    private final JDateChooser dateChooserNgaySinh = new JDateChooser();
    private final JComboBox<String> cboGioiTinh = new JComboBox<>(new String[] { "Nam", "Nữ" });
    private final JTextField txtDiaChi = new JTextField(24);
    private final JTextField txtSDT = new JTextField(24);
    private final JTextField txtEmail = new JTextField(24);

    private HocVien result = null;
    private HocVien editingTarget;

    public HocVienDialog(Frame parent, HocVien existing) {
        super(parent, existing == null ? "Thêm Học Viên" : "Sửa Thông Tin Học Viên", true);

        setSize(480, 520);
        setMinimumSize(new Dimension(420, 480));
        setLocationRelativeTo(parent);
        setResizable(true);

        JPanel content = new JPanel(new BorderLayout(0, 16));
        content.setBorder(new EmptyBorder(UiTheme.PAD_L, UiTheme.PAD_XL, UiTheme.PAD_L, UiTheme.PAD_XL));
        content.setBackground(UiTheme.APP_BG);

        content.add(buildForm(), BorderLayout.CENTER);
        content.add(buildButtons(), BorderLayout.SOUTH);

        add(content);

        if (existing != null) {
            prefillForm(existing);
            txtMaHocVien.setEditable(false);
            txtMaHocVien.setEnabled(false);
        }
    }

    private JPanel buildForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 4, 6, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Object[][] rows = {
                { "Họ tên *", txtHoTen },
                { "Số tài khoản", txtSTK },
                { "Ngày sinh", dateChooserNgaySinh },
                { "Giới tính", cboGioiTinh },
                { "Địa chỉ", txtDiaChi },
                { "SĐT", txtSDT },
                { "Email", txtEmail },
        };

        for (int i = 0; i < rows.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0.35;
            JLabel label = new JLabel((String) rows[i][0]);
            label.setFont(UiTheme.BODY);
            label.setForeground(UiTheme.TEXT_SECONDARY);
            form.add(label, gbc);

            gbc.gridx = 1;
            gbc.weightx = 0.65;
            JComponent field = (JComponent) rows[i][1];
            field.setFont(UiTheme.BODY);
            form.add(field, gbc);
        }
        return form;
    }

    private JPanel buildButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panel.setOpaque(false);

        JButton btnHuy = UiComponents.ghostButton("Hủy");
        JButton btnLuu = UiComponents.primaryButton("Lưu", UiTheme.SUCCESS);

        btnLuu.addActionListener(e -> save());
        btnHuy.addActionListener(e -> dispose());

        panel.add(btnHuy);
        panel.add(btnLuu);
        return panel;
    }

    private void prefillForm(HocVien hv) {
        txtMaHocVien.setText(Integer.toString(hv.getMaHocVien()));
        txtSTK.setText(hv.getTaiKhoanNganHang());
        txtHoTen.setText(hv.getHoTen());
        dateChooserNgaySinh.setDate(hv.getNgaySinh());
        cboGioiTinh.setSelectedItem(hv.getGioiTinh());
        txtDiaChi.setText(hv.getDiaChi());
        txtSDT.setText(hv.getSDT());
        txtEmail.setText(hv.getEmail());
    }

    private void save() {
        String hoTen = txtHoTen.getText().trim();
        String stk = txtSTK.getText().trim();
        java.util.Date ngaySinh = dateChooserNgaySinh.getDate();
        String gioiTinh = (String) cboGioiTinh.getSelectedItem();
        String diaChi = txtDiaChi.getText().trim();
        String sdt = txtSDT.getText().trim();
        String email = txtEmail.getText().trim();

        if (hoTen.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng điền đầy đủ họ tên.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int maHV = (this.editingTarget != null) ? this.editingTarget.getMaHocVien() : 0;
        result = new HocVien(maHV, stk, hoTen, ngaySinh, gioiTinh, diaChi,
                sdt, email);
        dispose();
    }

    public HocVien getResult() {
        return result;
    }
}
