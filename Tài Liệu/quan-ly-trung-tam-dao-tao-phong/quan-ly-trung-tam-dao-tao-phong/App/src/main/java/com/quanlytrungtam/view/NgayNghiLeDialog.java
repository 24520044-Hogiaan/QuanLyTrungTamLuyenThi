package com.quanlytrungtam.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.quanlytrungtam.model.NgayNghiLe;
import com.quanlytrungtam.ui.UiComponents;
import com.quanlytrungtam.ui.UiTheme;
import com.toedter.calendar.JDateChooser;

public class NgayNghiLeDialog extends JDialog {

    private final JDateChooser dateBatDau = new JDateChooser();
    private final JDateChooser dateKetThuc = new JDateChooser();

    private NgayNghiLe result = null;
    private final NgayNghiLe editingTarget;

    public NgayNghiLeDialog(Frame parent, NgayNghiLe existing) {
        super(parent, existing == null ? "Thêm Ngày Nghỉ" : "Sửa Ngày Nghỉ", true);
        this.editingTarget = existing;

        setSize(400, 300);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel content = new JPanel(new BorderLayout(0, 16));
        content.setBorder(new EmptyBorder(20, 20, 20, 20));
        content.setBackground(UiTheme.APP_BG);

        content.add(buildForm(), BorderLayout.CENTER);
        content.add(buildButtons(), BorderLayout.SOUTH);

        add(content);

        if (existing != null) {
            dateBatDau.setDate(existing.getNgayBatDau());
            dateKetThuc.setDate(existing.getNgayKetThuc());
        }
    }

    private JPanel buildForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addFormRow(form, "Ngày bắt đầu *", dateBatDau, 0, gbc);
        addFormRow(form, "Ngày kết thúc *", dateKetThuc, 1, gbc);

        return form;
    }

    private void addFormRow(JPanel form, String labelText, JDateChooser dateChooser, int y, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.weightx = 0.4;
        JLabel label = new JLabel(labelText);
        label.setFont(UiTheme.BODY);
        form.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.6;
        dateChooser.setDateFormatString("dd/MM/yyyy");
        dateChooser.setPreferredSize(new Dimension(200, 30));
        form.add(dateChooser, gbc);
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

    private void save() {
        Date ngayBD = dateBatDau.getDate();
        Date ngayKT = dateKetThuc.getDate();

        if (ngayBD == null || ngayKT == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đầy đủ ngày.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (ngayKT.before(ngayBD)) {
            JOptionPane.showMessageDialog(this, "Ngày kết thúc không được nhỏ hơn ngày bắt đầu.", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận lưu thông tin?", "Thông báo",
                JOptionPane.OK_CANCEL_OPTION);
        if (confirm == JOptionPane.OK_OPTION) {
            int id = (editingTarget != null) ? editingTarget.getMaNgayNghi() : 0;
            result = new NgayNghiLe(id, ngayBD, ngayKT);
            dispose();
        }
    }

    public NgayNghiLe getResult() {
        return result;
    }
}