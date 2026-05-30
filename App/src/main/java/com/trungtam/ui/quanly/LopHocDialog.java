package com.trungtam.ui.quanly;

import com.trungtam.model.LopHoc;
import com.trungtam.model.KhoaHoc;
import com.trungtam.model.GiaoVien;
import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LopHocDialog extends JDialog {

    private boolean saved = false;
    private LopHoc lopHoc;
    private JTextField txtTenLop;
    private JTextField txtNgayBD;
    private JTextField txtNgayKT;
    private JComboBox<String> cboKhoaHoc;
    private JComboBox<String> cboGiaoVien;
    private JTextField txtSiSo;
    private JTextField txtTanSuat;
    private JComboBox<String> cboTrangThai;

    private List<KhoaHoc> khoaHocs;
    private List<GiaoVien> giaoViens;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public LopHocDialog(Window owner, LopHoc lh, List<KhoaHoc> khoaHocs, List<GiaoVien> giaoViens) {
        super(owner, lh == null ? "Thêm Lớp Học Mới" : "Sửa Lớp Học #" + lh.getMaLopHoc(), ModalityType.APPLICATION_MODAL);
        this.lopHoc = lh == null ? new LopHoc() : lh;
        this.khoaHocs = khoaHocs;
        this.giaoViens = giaoViens;

        setSize(450, 550);
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
        JPanel panel = new JPanel(new GridLayout(8, 2, 8, 12));
        panel.setOpaque(false);

        panel.add(createLabel("Tên Lớp:"));
        txtTenLop = createTextField();
        panel.add(txtTenLop);

        panel.add(createLabel("Khóa Học:"));
        cboKhoaHoc = new JComboBox<>();
        cboKhoaHoc.setFont(UiTheme.BODY);
        for (KhoaHoc kh : khoaHocs) cboKhoaHoc.addItem(kh.getTenKhoaHoc());
        panel.add(cboKhoaHoc);

        panel.add(createLabel("Giáo Viên:"));
        cboGiaoVien = new JComboBox<>();
        cboGiaoVien.setFont(UiTheme.BODY);
        for (GiaoVien gv : giaoViens) cboGiaoVien.addItem("GV " + gv.getMaGiaoVien());
        panel.add(cboGiaoVien);

        panel.add(createLabel("Ngày BĐ (dd/MM/yyyy):"));
        txtNgayBD = createTextField();
        panel.add(txtNgayBD);

        panel.add(createLabel("Ngày KT (dd/MM/yyyy):"));
        txtNgayKT = createTextField();
        panel.add(txtNgayKT);

        panel.add(createLabel("Sĩ Số:"));
        txtSiSo = createTextField();
        panel.add(txtSiSo);

        panel.add(createLabel("Tần Suất:"));
        txtTanSuat = createTextField();
        panel.add(txtTanSuat);

        panel.add(createLabel("Trạng Thái:"));
        cboTrangThai = new JComboBox<>(new String[]{"Dang mo", "Da dong", "Chua mo"});
        cboTrangThai.setFont(UiTheme.BODY);
        panel.add(cboTrangThai);

        return panel;
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(UiTheme.BODY_B);
        lbl.setForeground(UiTheme.TEXT_PRIMARY);
        return lbl;
    }

    private JTextField createTextField() {
        JTextField tf = new JTextField();
        tf.setFont(UiTheme.BODY);
        return tf;
    }

    private JPanel buildButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panel.setOpaque(false);

        JButton btnCancel = UiComponents.ghostButton("Hủy");
        btnCancel.addActionListener(e -> dispose());
        panel.add(btnCancel);

        JButton btnSave = UiComponents.primaryButton("Lưu Lớp Học", UiTheme.PRIMARY);
        btnSave.addActionListener(e -> saveAction());
        panel.add(btnSave);

        return panel;
    }

    private void loadDataToForm() {
        if (lopHoc.getMaLopHoc() == 0) return; // Add mode

        txtTenLop.setText(lopHoc.getTenLop());
        txtNgayBD.setText(lopHoc.getNgayBatDau() != null ? lopHoc.getNgayBatDau().format(DATE_FMT) : "");
        txtNgayKT.setText(lopHoc.getNgayKetThuc() != null ? lopHoc.getNgayKetThuc().format(DATE_FMT) : "");
        txtSiSo.setText(String.valueOf(lopHoc.getSiSo()));
        txtTanSuat.setText(lopHoc.getTanSuat());
        cboTrangThai.setSelectedItem(lopHoc.getTrangThai());

        for (int i = 0; i < khoaHocs.size(); i++) {
            if (khoaHocs.get(i).getMaKhoaHoc() == lopHoc.getMaKhoaHoc()) {
                cboKhoaHoc.setSelectedIndex(i);
                break;
            }
        }
        for (int i = 0; i < giaoViens.size(); i++) {
            if (giaoViens.get(i).getMaGiaoVien() == lopHoc.getMaGiaoVien()) {
                cboGiaoVien.setSelectedIndex(i);
                break;
            }
        }
    }

    private void saveAction() {
        try {
            lopHoc.setTenLop(txtTenLop.getText().trim());
            
            String bd = txtNgayBD.getText().trim();
            if (!bd.isEmpty()) lopHoc.setNgayBatDau(LocalDate.parse(bd, DATE_FMT));
            else lopHoc.setNgayBatDau(null);
            
            String kt = txtNgayKT.getText().trim();
            if (!kt.isEmpty()) lopHoc.setNgayKetThuc(LocalDate.parse(kt, DATE_FMT));
            else lopHoc.setNgayKetThuc(null);

            lopHoc.setSiSo(Integer.parseInt(txtSiSo.getText().trim()));
            lopHoc.setTanSuat(txtTanSuat.getText().trim());
            lopHoc.setTrangThai((String) cboTrangThai.getSelectedItem());
            
            int khIdx = cboKhoaHoc.getSelectedIndex();
            if (khIdx >= 0) lopHoc.setMaKhoaHoc(khoaHocs.get(khIdx).getMaKhoaHoc());
            
            int gvIdx = cboGiaoVien.getSelectedIndex();
            if (gvIdx >= 0) lopHoc.setMaGiaoVien(giaoViens.get(gvIdx).getMaGiaoVien());

            saved = true;
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Thông tin nhập không hợp lệ! Vui lòng kiểm tra ngày (dd/MM/yyyy) hoặc sĩ số.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSaved() {
        return saved;
    }

    public LopHoc getLopHoc() {
        return lopHoc;
    }
}
