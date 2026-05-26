package com.trungtam.ui.quanly;

import com.trungtam.controller.HocVienController;
import com.trungtam.model.HocVien;
import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class QuanLyHocVienPanel extends JPanel {

    private final DefaultTableModel tableModel;
    private final TableRowSorter<DefaultTableModel> rowSorter;
    private final HocVienController hocVienController = new HocVienController();
    private JTable table;

    private static final String[] COT = {
            "Mã HV", "Họ Tên", "Giới Tính", "Ngày Sinh", "SĐT", "Email", "Địa Chỉ"
    };

    public QuanLyHocVienPanel() {
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M));
        setBackground(UiTheme.APP_BG);
        add(buildTopBar(), BorderLayout.NORTH);
        tableModel = new DefaultTableModel(COT, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        UiComponents.styleTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        rowSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(rowSorter);
        int[] widths = {60, 160, 70, 100, 110, 200, 250};
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
        UiComponents.setColumnAlignments(table,
                SwingConstants.CENTER, SwingConstants.LEFT, SwingConstants.CENTER,
                SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.LEFT,
                SwingConstants.LEFT);
        add(UiComponents.tableScroll(table), BorderLayout.CENTER);
        add(buildBottomBar(), BorderLayout.SOUTH);
        loadData();
    }

    private JPanel buildTopBar() {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setOpaque(false);
        JLabel title = new JLabel("Danh Sách Học Viên");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.TEXT_PRIMARY);
        JTextField searchField = new JTextField(22);
        searchField.putClientProperty("JTextField.placeholderText", "Tìm kiếm...");
        searchField.setFont(UiTheme.BODY);
        searchField.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) {
                String kw = searchField.getText().trim();
                rowSorter.setRowFilter(kw.isEmpty() ? null : RowFilter.regexFilter("(?i)" + kw));
            }
        });
        panel.add(title, BorderLayout.WEST);
        panel.add(searchField, BorderLayout.EAST);
        return panel;
    }

    private JPanel buildBottomBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 6));
        bar.setOpaque(false);

        JButton btnThem = UiComponents.primaryButton("Thêm", UiTheme.SUCCESS);
        btnThem.addActionListener(e -> themHocVien());

        JButton btnSua = UiComponents.primaryButton("Sửa", UiTheme.PRIMARY);
        btnSua.addActionListener(e -> suaHocVien());

        JButton btnXoa = UiComponents.primaryButton("Xóa", UiTheme.DANGER);
        btnXoa.addActionListener(e -> xoaHocVien());

        JButton refreshBtn = UiComponents.ghostButton("Làm Mới");
        refreshBtn.addActionListener(e -> loadData());

        bar.add(btnThem);
        bar.add(btnSua);
        bar.add(btnXoa);
        bar.add(refreshBtn);
        return bar;
    }

    private void themHocVien() {
        JTextField txtHoTen = new JTextField();
        JComboBox<String> cboGioiTinh = new JComboBox<>(new String[]{"Nam", "Nu"});
        JTextField txtSdt = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtDiaChi = new JTextField();

        Object[] fields = {
                "Họ Tên:", txtHoTen,
                "Giới Tính:", cboGioiTinh,
                "SĐT:", txtSdt,
                "Email:", txtEmail,
                "Địa Chỉ:", txtDiaChi
        };
        int result = JOptionPane.showConfirmDialog(this, fields, "Thêm Học Viên", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String hoTen = txtHoTen.getText().trim();
            if (hoTen.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Họ tên không được để trống!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            HocVien hv = new HocVien();
            hv.setHoTen(hoTen);
            hv.setGioiTinh((String) cboGioiTinh.getSelectedItem());
            hv.setSoDienThoai(txtSdt.getText().trim());
            hv.setEmail(txtEmail.getText().trim());
            hv.setDiaChi(txtDiaChi.getText().trim());

            if (hocVienController.themHocVien(hv)) {
                JOptionPane.showMessageDialog(this, "Thêm học viên thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm học viên thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void suaHocVien() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn học viên cần sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(row);
        int maHV = (int) tableModel.getValueAt(modelRow, 0);

        HocVien hv = hocVienController.timTheoMa(maHV);
        if (hv == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy học viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField txtHoTen = new JTextField(hv.getHoTen());
        JComboBox<String> cboGioiTinh = new JComboBox<>(new String[]{"Nam", "Nu"});
        cboGioiTinh.setSelectedItem(hv.getGioiTinh());
        JTextField txtSdt = new JTextField(hv.getSoDienThoai() != null ? hv.getSoDienThoai() : "");
        JTextField txtEmail = new JTextField(hv.getEmail() != null ? hv.getEmail() : "");
        JTextField txtDiaChi = new JTextField(hv.getDiaChi() != null ? hv.getDiaChi() : "");

        Object[] fields = {
                "Họ Tên:", txtHoTen,
                "Giới Tính:", cboGioiTinh,
                "SĐT:", txtSdt,
                "Email:", txtEmail,
                "Địa Chỉ:", txtDiaChi
        };
        int result = JOptionPane.showConfirmDialog(this, fields, "Sửa Học Viên (Mã: " + maHV + ")", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            hv.setHoTen(txtHoTen.getText().trim());
            hv.setGioiTinh((String) cboGioiTinh.getSelectedItem());
            hv.setSoDienThoai(txtSdt.getText().trim());
            hv.setEmail(txtEmail.getText().trim());
            hv.setDiaChi(txtDiaChi.getText().trim());

            if (hocVienController.capNhatHocVien(hv)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void xoaHocVien() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn học viên cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(row);
        int maHV = (int) tableModel.getValueAt(modelRow, 0);
        String hoTen = (String) tableModel.getValueAt(modelRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa học viên \"" + hoTen + "\" (Mã: " + maHV + ")?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            if (hocVienController.xoaHocVien(maHV)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại! Học viên có thể đang đăng ký lớp.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<HocVien> list = hocVienController.layDanhSach();
        for (HocVien hv : list) {
            tableModel.addRow(new Object[]{
                    hv.getMaHocVien(), hv.getHoTen(), hv.getGioiTinh(),
                    hv.getNgaySinh(), hv.getSoDienThoai(), hv.getEmail(), hv.getDiaChi()
            });
        }
    }
}
