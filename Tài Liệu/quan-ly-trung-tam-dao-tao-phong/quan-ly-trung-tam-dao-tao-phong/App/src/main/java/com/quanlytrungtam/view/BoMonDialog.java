package com.quanlytrungtam.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.quanlytrungtam.model.BoMon;
import com.quanlytrungtam.ui.UiComponents;
import com.quanlytrungtam.ui.UiTheme;

import java.awt.*;

public class BoMonDialog extends JDialog {

    private final JTextField txtTenBoMon = new JTextField(24);

    private BoMon result = null;
    private final BoMon editingTarget;

    public BoMonDialog(Frame parent, BoMon existing) {
        super(parent, existing == null ? "Thêm Bộ Môn" : "Sửa Bộ Môn", true);
        this.editingTarget = existing;

        setSize(450, 240);
        setMinimumSize(new Dimension(380, 200));
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
        gbc.insets = new Insets(12, 4, 12, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.35;
        JLabel label = new JLabel("Tên bộ môn *");
        label.setFont(UiTheme.BODY);
        label.setForeground(UiTheme.TEXT_SECONDARY);
        form.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.65;
        txtTenBoMon.setFont(UiTheme.BODY);
        form.add(txtTenBoMon, gbc);

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

    private void prefillForm(BoMon bm) {
        txtTenBoMon.setText(bm.getTenBoMon());
    }

    private void save() {
        String tenBoMon = txtTenBoMon.getText().trim();

        if (tenBoMon.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng điền đầy đủ tên bộ môn.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Xác nhận hành động ?",
                "Thông báo",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (confirm == JOptionPane.OK_OPTION) {
            int maBM = (editingTarget != null) ? editingTarget.getMaBoMon() : 0;
            result = new BoMon(maBM, tenBoMon);
        }
        dispose();
    }

    public BoMon getResult() {
        return result;
    }
}
