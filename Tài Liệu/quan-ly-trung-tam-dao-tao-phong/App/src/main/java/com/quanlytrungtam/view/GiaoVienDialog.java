package com.quanlytrungtam.view;

import com.quanlytrungtam.model.GiaoVien;
import com.quanlytrungtam.model.BoMon;
import com.quanlytrungtam.controller.BoMonController;
import com.quanlytrungtam.ui.UiComponents;
import com.quanlytrungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class GiaoVienDialog extends JDialog {

    private final JTextField txtMaNhanVien = new JTextField(24);
    private final JComboBox<String> cboChuyenMon = new JComboBox<>();
    private final JTextField txtBangcap = new JTextField(24);
    private final JComboBox<String> cboTrangThai = new JComboBox<>(
            new String[] { "Dang day", "Nghi phep", "Da nghi" });

    private GiaoVien result = null;
    private final GiaoVien editingTarget;

    public GiaoVienDialog(Frame parent, GiaoVien existing) {
        super(parent, existing == null ? "Thêm Giáo Viên" : "Sửa Giáo Viên", true);
        this.editingTarget = existing;

        setSize(480, 420);
        setMinimumSize(new Dimension(420, 380));
        setLocationRelativeTo(parent);
        setResizable(true);

        loadBoMonSpecializations();

        JPanel content = new JPanel(new BorderLayout(0, 16));
        content.setBorder(new EmptyBorder(UiTheme.PAD_L, UiTheme.PAD_XL, UiTheme.PAD_L, UiTheme.PAD_XL));
        content.setBackground(UiTheme.APP_BG);

        content.add(buildForm(), BorderLayout.CENTER);
        content.add(buildButtons(), BorderLayout.SOUTH);

        add(content);

        if (existing != null)
            prefillForm(existing);
    }

    private void loadBoMonSpecializations() {
        try {
            BoMonController ctrl = new BoMonController();
            List<BoMon> list = ctrl.getListBoMon();
            for (BoMon bm : list) {
                cboChuyenMon.addItem(bm.getTenBoMon());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JPanel buildForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 4, 6, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Object[][] rows = {
                { "Mã nhân viên *", txtMaNhanVien },
                { "Chuyên môn", cboChuyenMon },
                { "Bằng cấp", txtBangcap },
                { "Trạng thái", cboTrangThai },
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

    private void prefillForm(GiaoVien gv) {
        txtMaNhanVien.setText(String.valueOf(gv.getMaNhanVien()));
        cboChuyenMon.setSelectedItem(gv.getChuyenMon());
        txtBangcap.setText(gv.getBangCap());
        cboTrangThai.setSelectedItem(gv.getTrangThai());
    }

    private void save() {
        String maNhanVien = txtMaNhanVien.getText().trim();
        String chuyenMon = (String) cboChuyenMon.getSelectedItem();

        if (maNhanVien.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng điền đầy đủ mã nhân viên.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Xác nhận hành động ?",
                "Thông báo",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (confirm == JOptionPane.OK_OPTION) {
            int maGV = (editingTarget != null) ? editingTarget.getMaGiaoVien() : 0;
            result = new GiaoVien(maGV, Integer.parseInt(maNhanVien), chuyenMon,
                    txtBangcap.getText().trim(),
                    (String) cboTrangThai.getSelectedItem());
            dispose();
        }
    }

    public GiaoVien getResult() {
        return result;
    }
}
