package com.quanlytrungtam.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.quanlytrungtam.controller.BoMonController;
import com.quanlytrungtam.model.BoMon;
import com.quanlytrungtam.model.KhoaHoc;
import com.quanlytrungtam.ui.UiComponents;
import com.quanlytrungtam.ui.UiTheme;

import java.awt.*;

public class KhoaHocDialog extends JDialog {

    private final JTextField txtTenKhoaHoc = new JTextField(24);
    private final JTextArea txtMoTa = new JTextArea(3, 24);
    private final JComboBox<String> cboHinhThucHoc = new JComboBox<>(
            new String[] { "Đóng theo tháng", "Đóng toàn khóa" });
    private final JTextField txtHocPhiThang = new JTextField("1", 24);
    private final JTextField txtHocPhiToanKhoa = new JTextField("1", 24);
    private final JComboBox<String> cboBoMon = new JComboBox<>();
    private final JComboBox<String> cboCapDo = new JComboBox<>(new String[] { "Co ban", "Nang cao" });

    private final JLabel lblHocPhiThang = new JLabel("Học phí tháng");
    private final JLabel lblHocPhiToanKhoa = new JLabel("Học phí toàn khóa");

    private KhoaHoc result = null;
    private final KhoaHoc editingTarget;

    public KhoaHocDialog(Frame parent, KhoaHoc existing) {
        super(parent, existing == null ? "Thêm Khóa Học" : "Sửa Thông Tin Khóa Học", true);
        this.editingTarget = existing;

        setSize(500, 480);
        setMinimumSize(new Dimension(450, 420));
        setLocationRelativeTo(parent);
        setResizable(true);

        loadBoMonList();

        JPanel content = new JPanel(new BorderLayout(0, 16));
        content.setBorder(new EmptyBorder(UiTheme.PAD_L, UiTheme.PAD_XL, UiTheme.PAD_L, UiTheme.PAD_XL));
        content.setBackground(UiTheme.APP_BG);

        content.add(buildForm(), BorderLayout.CENTER);
        content.add(buildButtons(), BorderLayout.SOUTH);

        add(content);

        if (existing != null) {
            prefillForm(existing);
        }
    }

    private void loadBoMonList() {
        try {
            BoMonController ctrl = new BoMonController();
            for (BoMon bm : ctrl.getListBoMon()) {
                cboBoMon.addItem(String.valueOf(bm.getMaBoMon()) + " " + bm.getTenBoMon());
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

        txtMoTa.setLineWrap(true);
        JScrollPane scrollMoTa = new JScrollPane(txtMoTa);

        lblHocPhiThang.setFont(UiTheme.BODY);
        lblHocPhiThang.setForeground(UiTheme.TEXT_SECONDARY);
        lblHocPhiToanKhoa.setFont(UiTheme.BODY);
        lblHocPhiToanKhoa.setForeground(UiTheme.TEXT_SECONDARY);

        JLabel lblHinhThuc = new JLabel("Hình thức đóng *");
        lblHinhThuc.setFont(UiTheme.BODY);
        lblHinhThuc.setForeground(UiTheme.TEXT_SECONDARY);

        Object[][] rows = {
                { new JLabel("Tên khóa học *"), txtTenKhoaHoc },
                { new JLabel("Mô tả"), scrollMoTa },
                { lblHinhThuc, cboHinhThucHoc },
                { lblHocPhiThang, txtHocPhiThang },
                { lblHocPhiToanKhoa, txtHocPhiToanKhoa },
                { new JLabel("Mã bộ môn"), cboBoMon },
                { new JLabel("Cấp độ"), cboCapDo }
        };

        for (int i = 0; i < rows.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0.35;
            JLabel label = (JLabel) rows[i][0];
            label.setFont(UiTheme.BODY);
            label.setForeground(UiTheme.TEXT_SECONDARY);
            form.add(label, gbc);

            gbc.gridx = 1;
            gbc.weightx = 0.65;
            JComponent field = (JComponent) rows[i][1];
            field.setFont(UiTheme.BODY);
            form.add(field, gbc);
        }

        boolean isThang = cboHinhThucHoc.getSelectedIndex() == 0;
        lblHocPhiThang.setVisible(isThang);
        txtHocPhiThang.setVisible(isThang);
        lblHocPhiToanKhoa.setVisible(!isThang);
        txtHocPhiToanKhoa.setVisible(!isThang);

        cboHinhThucHoc.addActionListener(e -> {
            boolean isT = cboHinhThucHoc.getSelectedIndex() == 0;
            lblHocPhiThang.setVisible(isT);
            txtHocPhiThang.setVisible(isT);
            if (!isT) {
                txtHocPhiThang.setText("1");
            }
            lblHocPhiToanKhoa.setVisible(!isT);
            txtHocPhiToanKhoa.setVisible(!isT);
            if (isT) {
                txtHocPhiToanKhoa.setText("1");
            }
            form.revalidate();
            form.repaint();
        });

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

    private void prefillForm(KhoaHoc kh) {
        txtTenKhoaHoc.setText(kh.getTenkh());
        txtMoTa.setText(kh.getMota());
        txtHocPhiThang.setText(String.valueOf(kh.getHocphithang()));
        txtHocPhiToanKhoa.setText(String.valueOf(kh.getHocphitoankh()));

        for (int i = 0; i < cboBoMon.getItemCount(); i++) {
            String item = cboBoMon.getItemAt(i);
            if (item.startsWith(String.valueOf(kh.getMabomon()) + " ")) {
                cboBoMon.setSelectedIndex(i);
                break;
            }
        }

        cboCapDo.setSelectedItem(kh.getCapdo());

        double hpToanKhoa = kh.getHocphitoankh() != null ? kh.getHocphitoankh() : 0.0;
        double hpThang = kh.getHocphithang() != null ? kh.getHocphithang() : 0.0;

        if (hpToanKhoa > 0 && hpThang <= 1.0) {
            cboHinhThucHoc.setSelectedIndex(1);
        } else {
            cboHinhThucHoc.setSelectedIndex(0);
        }

        boolean isThang = cboHinhThucHoc.getSelectedIndex() == 0;
        lblHocPhiThang.setVisible(isThang);
        txtHocPhiThang.setVisible(isThang);
        lblHocPhiToanKhoa.setVisible(!isThang);
        txtHocPhiToanKhoa.setVisible(!isThang);
    }

    private void save() {
        String tenKhoaHoc = txtTenKhoaHoc.getText().trim();
        String moTa = txtMoTa.getText().trim();

        if (tenKhoaHoc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên khóa học.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Double hocPhiThang = null;
        Double hocPhiToanKhoa = null;

        if (cboHinhThucHoc.getSelectedIndex() == 0) {
            try {
                hocPhiThang = Double.parseDouble(txtHocPhiThang.getText().trim());
                if (hocPhiThang <= 0) {
                    JOptionPane.showMessageDialog(this, "Học phí tháng phải lớn hơn 0.", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Học phí tháng không hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            try {
                hocPhiToanKhoa = Double.parseDouble(txtHocPhiToanKhoa.getText().trim());
                if (hocPhiToanKhoa <= 0) {
                    JOptionPane.showMessageDialog(this, "Học phí toàn khóa phải lớn hơn 0.", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Học phí toàn khóa không hợp lệ.", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        String fullText = cboBoMon.getSelectedItem().toString();
        String idString = fullText.split(" ")[0];
        int maBoMon = Integer.parseInt(idString);
        String capDo = (String) cboCapDo.getSelectedItem();

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Xác nhận hành động ?",
                "Thông báo",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (confirm == JOptionPane.OK_OPTION) {
            int maKhoaHoc = (editingTarget != null) ? editingTarget.getMakhoahoc() : 0;
            result = new KhoaHoc(maKhoaHoc, tenKhoaHoc, moTa, hocPhiThang, hocPhiToanKhoa, maBoMon, capDo);
            dispose();
        }
    }

    public KhoaHoc getResult() {
        return result;
    }
}
