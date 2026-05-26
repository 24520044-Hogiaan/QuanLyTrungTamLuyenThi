package com.trungtam.ui.hocvien;

import com.trungtam.controller.HocVienController;
import com.trungtam.model.HocVien;
import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Hồ sơ cá nhân học viên — dữ liệu từ database
 */
public class HoSoCaNhanPanel extends JPanel {

    private final int maHocVien;
    private final HocVienController hocVienController = new HocVienController();

    private final JTextField txtHoTen = new JTextField();
    private final JTextField txtNgaySinh = new JTextField();
    private final JComboBox<String> cboGioiTinh = new JComboBox<>(new String[] { "Nam", "Nu" });
    private final JTextField txtEmail = new JTextField();
    private final JTextField txtSDT = new JTextField();
    private final JTextField txtDiaChi = new JTextField();
    private final JLabel lblThongBao = new JLabel(" ");

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public HoSoCaNhanPanel(int maHocVien) {
        this.maHocVien = maHocVien;
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

        loadData();
    }

    private void loadData() {
        HocVien hv = hocVienController.timTheoMa(maHocVien);
        if (hv == null) {
            lblThongBao.setForeground(UiTheme.DANGER);
            lblThongBao.setText("Không tìm thấy thông tin học viên (mã: " + maHocVien + ")");
            return;
        }
        txtHoTen.setText(hv.getHoTen() != null ? hv.getHoTen() : "");
        txtNgaySinh.setText(hv.getNgaySinh() != null ? hv.getNgaySinh().format(FMT) : "");
        if (hv.getGioiTinh() != null) {
            cboGioiTinh.setSelectedItem(hv.getGioiTinh());
        }
        txtEmail.setText(hv.getEmail() != null ? hv.getEmail() : "");
        txtSDT.setText(hv.getSoDienThoai() != null ? hv.getSoDienThoai() : "");
        txtDiaChi.setText(hv.getDiaChi() != null ? hv.getDiaChi() : "");
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

        return form;
    }

    private JPanel buildSaveBar() {
        JButton btnLuu = UiComponents.primaryButton("Lưu Thay Đổi", UiTheme.SECONDARY);
        btnLuu.addActionListener(e -> save());

        JButton btnDatLai = UiComponents.ghostButton("Đặt Lại");
        btnDatLai.addActionListener(e -> {
            lblThongBao.setText(" ");
            loadData();
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
        String hoTen = txtHoTen.getText().trim();
        String ngaySinhStr = txtNgaySinh.getText().trim();

        if (hoTen.isEmpty() || ngaySinhStr.isEmpty()) {
            lblThongBao.setForeground(UiTheme.DANGER);
            lblThongBao.setText("Vui lòng nhập đầy đủ các trường bắt buộc (*)");
            return;
        }

        LocalDate ngaySinh;
        try {
            ngaySinh = LocalDate.parse(ngaySinhStr, FMT);
        } catch (DateTimeParseException ex) {
            lblThongBao.setForeground(UiTheme.DANGER);
            lblThongBao.setText("Ngày sinh không hợp lệ (dd/MM/yyyy)");
            return;
        }

        HocVien hv = hocVienController.timTheoMa(maHocVien);
        if (hv == null) {
            lblThongBao.setForeground(UiTheme.DANGER);
            lblThongBao.setText("Lỗi: không tìm thấy học viên");
            return;
        }

        hv.setHoTen(hoTen);
        hv.setNgaySinh(ngaySinh);
        hv.setGioiTinh((String) cboGioiTinh.getSelectedItem());
        hv.setEmail(txtEmail.getText().trim());
        hv.setSoDienThoai(txtSDT.getText().trim());
        hv.setDiaChi(txtDiaChi.getText().trim());

        boolean ok = hocVienController.capNhatHocVien(hv);
        if (ok) {
            lblThongBao.setForeground(UiTheme.SUCCESS);
            lblThongBao.setText("Cập nhật hồ sơ thành công!");
        } else {
            lblThongBao.setForeground(UiTheme.DANGER);
            lblThongBao.setText("Cập nhật thất bại. Vui lòng thử lại.");
        }
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
