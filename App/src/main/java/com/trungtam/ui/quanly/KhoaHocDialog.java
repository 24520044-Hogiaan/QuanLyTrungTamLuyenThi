package com.trungtam.ui.quanly;

import com.trungtam.model.KhoaHoc;
import com.trungtam.model.BoMon;
import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class KhoaHocDialog extends JDialog {

    private boolean saved = false;
    private KhoaHoc khoaHoc;
    private JTextField txtTenKhoaHoc;
    private JTextField txtMoTa;
    private JTextField txtHocPhiThang;
    private JTextField txtHocPhiToanKhoa;
    private JComboBox<String> cboBoMon;
    private JTextField txtCapDo;

    private List<BoMon> boMons;

    public KhoaHocDialog(Window owner, KhoaHoc kh, List<BoMon> boMons) {
        super(owner, kh == null ? "Thêm Khóa Học Mới" : "Sửa Khóa Học #" + kh.getMaKhoaHoc(), ModalityType.APPLICATION_MODAL);
        this.khoaHoc = kh == null ? new KhoaHoc() : kh;
        this.boMons = boMons;

        setSize(400, 450);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(0, 16));
        root.setBackground(UiTheme.APP_BG);
        root.setBorder(new EmptyBorder(UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M));

        root.add(buildForm(), BorderLayout.CENTER);
        root.add(buildButtons(), BorderLayout.SOUTH);

        setContentPane(root);
        loadDataToForm();
    }

    private JPanel buildForm() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 8, 12));
        panel.setOpaque(false);

        panel.add(createLabel("Tên Khóa Học:"));
        txtTenKhoaHoc = createTextField();
        panel.add(txtTenKhoaHoc);

        panel.add(createLabel("Mô Tả:"));
        txtMoTa = createTextField();
        panel.add(txtMoTa);

        panel.add(createLabel("Học Phí (Tháng):"));
        txtHocPhiThang = createTextField();
        panel.add(txtHocPhiThang);

        panel.add(createLabel("Học Phí (Toàn khóa):"));
        txtHocPhiToanKhoa = createTextField();
        panel.add(txtHocPhiToanKhoa);

        panel.add(createLabel("Bộ Môn:"));
        cboBoMon = new JComboBox<>();
        cboBoMon.setFont(UiTheme.BODY);
        for (BoMon bm : boMons) cboBoMon.addItem(bm.getTenBoMon());
        panel.add(cboBoMon);

        panel.add(createLabel("Cấp Độ:"));
        txtCapDo = createTextField();
        panel.add(txtCapDo);

        return panel;
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(UiTheme.BODY_B);
        lbl.setForeground(UiTheme.TEXT_PRIMARY);
        return lbl;
    }

    private JTextField createTextField() {
        JTextField txt = new JTextField();
        txt.setFont(UiTheme.BODY);
        return txt;
    }

    private void loadDataToForm() {
        if (khoaHoc.getMaKhoaHoc() > 0) {
            txtTenKhoaHoc.setText(khoaHoc.getTenKhoaHoc());
            txtMoTa.setText(khoaHoc.getMoTa());
            txtHocPhiThang.setText(String.valueOf(khoaHoc.getHocPhiThang()));
            txtHocPhiToanKhoa.setText(String.valueOf(khoaHoc.getHocPhiToanKhoa()));
            txtCapDo.setText(khoaHoc.getCapDo());
            for (int i = 0; i < boMons.size(); i++) {
                if (boMons.get(i).getMaBoMon() == khoaHoc.getMaBoMon()) {
                    cboBoMon.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private JPanel buildButtons() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bar.setOpaque(false);

        JButton btnCancel = UiComponents.ghostButton("Hủy");
        btnCancel.addActionListener(e -> dispose());

        JButton btnSave = UiComponents.primaryButton("Lưu Lại", UiTheme.PRIMARY);
        btnSave.addActionListener(e -> save());

        bar.add(btnCancel);
        bar.add(btnSave);
        return bar;
    }

    private void save() {
        try {
            khoaHoc.setTenKhoaHoc(txtTenKhoaHoc.getText().trim());
            khoaHoc.setMoTa(txtMoTa.getText().trim());
            khoaHoc.setHocPhiThang(Double.parseDouble(txtHocPhiThang.getText().trim()));
            khoaHoc.setHocPhiToanKhoa(Double.parseDouble(txtHocPhiToanKhoa.getText().trim()));
            khoaHoc.setCapDo(txtCapDo.getText().trim());

            int bmIdx = cboBoMon.getSelectedIndex();
            if (bmIdx >= 0) khoaHoc.setMaBoMon(boMons.get(bmIdx).getMaBoMon());

            saved = true;
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Học phí phải là số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSaved() {
        return saved;
    }

    public KhoaHoc getKhoaHoc() {
        return khoaHoc;
    }
}
