package com.quanlytrungtam.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.quanlytrungtam.model.PhongHoc;
import com.quanlytrungtam.ui.UiComponents;
import com.quanlytrungtam.ui.UiTheme;

public class PhongHocDialog extends JDialog {

    private final JTextField txtTenPhong = new JTextField(24);
    private final JTextField txtSucChua = new JTextField(24);

    private final JComboBox<String> cbTrangThai = new JComboBox<>(new String[] { "San sang", "Dang dung", "Bao tri" });

    private PhongHoc result = null;
    private final PhongHoc editingTarget;

    public PhongHocDialog(Frame parent, PhongHoc existing) {
        super(parent, existing == null ? "Thêm Phòng Học" : "Sửa Phòng Học", true);
        this.editingTarget = existing;

        setSize(450, 350);
        setMinimumSize(new Dimension(400, 300));
        setLocationRelativeTo(parent);
        setResizable(true);

        JPanel content = new JPanel(new BorderLayout(0, 16));
        content.setBorder(new EmptyBorder(UiTheme.PAD_L, UiTheme.PAD_XL, UiTheme.PAD_L, UiTheme.PAD_XL));
        content.setBackground(UiTheme.APP_BG);

        content.add(buildForm(), BorderLayout.CENTER);
        content.add(buildButtons(), BorderLayout.SOUTH);

        add(content);

        if (existing != null)
            prefillForm(existing);
    }

    private JPanel buildForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 4, 8, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addFormRow(form, "Tên phòng *", txtTenPhong, 0, gbc);
        addFormRow(form, "Sức chứa", txtSucChua, 1, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.35;
        JLabel lblTrangThai = new JLabel("Trạng thái");
        lblTrangThai.setFont(UiTheme.BODY);
        lblTrangThai.setForeground(UiTheme.TEXT_SECONDARY);
        form.add(lblTrangThai, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.65;
        cbTrangThai.setFont(UiTheme.BODY);
        form.add(cbTrangThai, gbc);

        return form;
    }

    private void addFormRow(JPanel form, String labelText, JTextField textField, int y, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.weightx = 0.35;
        JLabel label = new JLabel(labelText);
        label.setFont(UiTheme.BODY);
        label.setForeground(UiTheme.TEXT_SECONDARY);
        form.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.65;
        textField.setFont(UiTheme.BODY);
        form.add(textField, gbc);
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

    private void prefillForm(PhongHoc ph) {
        txtTenPhong.setText(ph.getTenPhong());
        txtSucChua.setText(String.valueOf(ph.getSucChua()));
        cbTrangThai.setSelectedItem(ph.getTrangThai());
    }

    private void save() {
        String tenPhong = txtTenPhong.getText().trim();
        String strSucChua = txtSucChua.getText().trim();
        String trangThai = (String) cbTrangThai.getSelectedItem();

        if (tenPhong.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên phòng không được để trống.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int sucChua = 0;
        try {
            sucChua = Integer.parseInt(strSucChua);
            if (sucChua <= 0)
                throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Sức chứa phải là một số nguyên dương.", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận hành động ?", "Thông báo",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (confirm == JOptionPane.OK_OPTION) {
            int maPH = (editingTarget != null) ? editingTarget.getMaPhongHoc() : 0;
            result = new PhongHoc(maPH, tenPhong, sucChua, trangThai);
        }
        dispose();
    }

    public PhongHoc getResult() {
        return result;
    }
}