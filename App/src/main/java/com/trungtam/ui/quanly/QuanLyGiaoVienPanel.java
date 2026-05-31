package com.trungtam.ui.quanly;

import com.trungtam.controller.BoMonController;
import com.trungtam.controller.GiaoVienController;
import com.trungtam.model.BoMon;
import com.trungtam.model.GiaoVien;
import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuanLyGiaoVienPanel extends JPanel {

    private final DefaultTableModel tableModel;
    private final TableRowSorter<DefaultTableModel> rowSorter;
    private final GiaoVienController giaoVienController = new GiaoVienController();
    private final BoMonController boMonController = new BoMonController();
    private JTable table;
    private Map<String, String> boMonMap = new HashMap<>();

    private static final String[] COT = {
            "Mã GV", "Mã Nhân Viên", "Chuyên Môn", "Bằng Cấp", "Trạng Thái"
    };

    public QuanLyGiaoVienPanel() {
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M));
        setBackground(UiTheme.APP_BG);
        loadBoMonMap();
        add(buildTopBar(), BorderLayout.NORTH);
        tableModel = new DefaultTableModel(COT, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        UiComponents.styleTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        rowSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(rowSorter);
        UiComponents.setColumnAlignments(table,
                SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.LEFT,
                SwingConstants.LEFT, SwingConstants.CENTER);
        add(UiComponents.tableScroll(table), BorderLayout.CENTER);
        add(buildBottomBar(), BorderLayout.SOUTH);
        loadData();
    }

    private void loadBoMonMap() {
        boMonMap.clear();
        List<BoMon> list = boMonController.getListBoMon();
        for (BoMon bm : list) {
            boMonMap.put(String.valueOf(bm.getMaBoMon()), bm.getTenBoMon());
        }
    }

    private String getChuyenMon(String maBoMon) {
        if (maBoMon == null) return "";
        String ten = boMonMap.get(maBoMon);
        return ten != null ? ten : maBoMon;
    }

    private JPanel buildTopBar() {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setOpaque(false);
        JLabel title = new JLabel("Danh Sách Giáo Viên");
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
        btnThem.addActionListener(e -> themGiaoVien());

        JButton btnSua = UiComponents.primaryButton("Sửa", UiTheme.PRIMARY);
        btnSua.addActionListener(e -> suaGiaoVien());

        JButton btnXoa = UiComponents.primaryButton("Xóa", UiTheme.DANGER);
        btnXoa.addActionListener(e -> xoaGiaoVien());

        JButton refreshBtn = UiComponents.ghostButton("Làm Mới");
        refreshBtn.addActionListener(e -> loadData());

        JButton btnPhanCong = UiComponents.primaryButton("Phân Công", new Color(0x37474F));
        btnPhanCong.addActionListener(e -> phanCongGiaoVien());

        bar.add(btnThem);
        bar.add(btnSua);
        bar.add(btnXoa);
        bar.add(refreshBtn);
        bar.add(btnPhanCong);
        return bar;
    }

    private void phanCongGiaoVien() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn giáo viên cần phân công!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(row);
        int maGV = (int) tableModel.getValueAt(modelRow, 0);

        com.trungtam.controller.LopHocController lopHocController = new com.trungtam.controller.LopHocController();
        List<com.trungtam.model.LopHoc> tatCaLop = lopHocController.layDanhSach();
        
        if(tatCaLop.isEmpty()) {
             JOptionPane.showMessageDialog(this, "Không có lớp học nào trên hệ thống.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
             return;
        }

        JComboBox<com.trungtam.model.LopHoc> cboLopHoc = new JComboBox<>(tatCaLop.toArray(new com.trungtam.model.LopHoc[0]));
        int res = JOptionPane.showConfirmDialog(this, cboLopHoc, "Chọn lớp để phân công cho Giáo viên (Mã: " + maGV + ")", JOptionPane.OK_CANCEL_OPTION);
        
        if (res == JOptionPane.OK_OPTION) {
             com.trungtam.model.LopHoc selectedLop = (com.trungtam.model.LopHoc) cboLopHoc.getSelectedItem();
             if (selectedLop != null) {
                 selectedLop.setMaGiaoVien(maGV);
                 if (lopHocController.capNhatLopHoc(selectedLop)) {
                     JOptionPane.showMessageDialog(this, "Phân công giáo viên thành công!");
                 } else {
                     JOptionPane.showMessageDialog(this, "Phân công thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                 }
             }
        }
    }

    private void themGiaoVien() {
        JTextField txtMaNV = new JTextField();
        
        JComboBox<String> cboChuyenMon = new JComboBox<>();
        List<BoMon> dsBoMon = boMonController.getListBoMon();
        for (BoMon bm : dsBoMon) {
            cboChuyenMon.addItem(bm.getMaBoMon() + " - " + bm.getTenBoMon());
        }
        
        JTextField txtBangCap = new JTextField();
        JComboBox<String> cboTrangThai = new JComboBox<>(new String[]{"Dang day", "Nghi phep", "Da nghi"});

        Object[] fields = {
                "Mã Nhân Viên:", txtMaNV,
                "Chuyên Môn:", cboChuyenMon,
                "Bằng Cấp:", txtBangCap,
                "Trạng Thái:", cboTrangThai
        };
        int result = JOptionPane.showConfirmDialog(this, fields, "Thêm Giáo Viên", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            GiaoVien gv = new GiaoVien();
            try { gv.setMaNhanVien(Integer.parseInt(txtMaNV.getText().trim())); } catch (NumberFormatException ignored) {}
            
            String selectedBM = (String) cboChuyenMon.getSelectedItem();
            if (selectedBM != null && selectedBM.contains(" - ")) {
                gv.setMaBoMon(selectedBM.split(" - ")[0]);
            }
            
            gv.setBangCap(txtBangCap.getText().trim());
            gv.setTrangThai((String) cboTrangThai.getSelectedItem());

            if (giaoVienController.themGiaoVien(gv)) {
                JOptionPane.showMessageDialog(this, "Thêm giáo viên thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm giáo viên thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void suaGiaoVien() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn giáo viên cần sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(row);
        int maGV = (int) tableModel.getValueAt(modelRow, 0);

        GiaoVien gv = giaoVienController.timTheoMa(maGV);
        if (gv == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy giáo viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField txtMaNV = new JTextField(String.valueOf(gv.getMaNhanVien()));
        
        JComboBox<String> cboChuyenMon = new JComboBox<>();
        List<BoMon> dsBoMon = boMonController.getListBoMon();
        String currentBmStr = null;
        for (BoMon bm : dsBoMon) {
            String item = bm.getMaBoMon() + " - " + bm.getTenBoMon();
            cboChuyenMon.addItem(item);
            if (String.valueOf(bm.getMaBoMon()).equals(gv.getMaBoMon())) {
                currentBmStr = item;
            }
        }
        if (currentBmStr != null) {
            cboChuyenMon.setSelectedItem(currentBmStr);
        }

        JTextField txtBangCap = new JTextField(gv.getBangCap() != null ? gv.getBangCap() : "");
        JComboBox<String> cboTrangThai = new JComboBox<>(new String[]{"Dang day", "Nghi phep", "Da nghi"});
        cboTrangThai.setSelectedItem(gv.getTrangThai());

        Object[] fields = {
                "Mã Nhân Viên:", txtMaNV,
                "Chuyên Môn:", cboChuyenMon,
                "Bằng Cấp:", txtBangCap,
                "Trạng Thái:", cboTrangThai
        };
        int result = JOptionPane.showConfirmDialog(this, fields, "Sửa Giáo Viên (Mã: " + maGV + ")", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try { gv.setMaNhanVien(Integer.parseInt(txtMaNV.getText().trim())); } catch (NumberFormatException ignored) {}
            
            String selectedBM = (String) cboChuyenMon.getSelectedItem();
            if (selectedBM != null && selectedBM.contains(" - ")) {
                gv.setMaBoMon(selectedBM.split(" - ")[0]);
            }
            
            gv.setBangCap(txtBangCap.getText().trim());
            gv.setTrangThai((String) cboTrangThai.getSelectedItem());

            if (giaoVienController.capNhatGiaoVien(gv)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void xoaGiaoVien() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn giáo viên cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(row);
        int maGV = (int) tableModel.getValueAt(modelRow, 0);
        String chuyenMon = (String) tableModel.getValueAt(modelRow, 2);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa giáo viên \"" + chuyenMon + "\" (Mã: " + maGV + ")?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            if (giaoVienController.xoaGiaoVien(maGV)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại! Giáo viên có thể đang được phân công lớp.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadData() {
        tableModel.setRowCount(0);
        loadBoMonMap();
        List<GiaoVien> list = giaoVienController.layDanhSach();
        for (GiaoVien gv : list) {
            tableModel.addRow(new Object[]{
                    gv.getMaGiaoVien(), gv.getMaNhanVien(),
                    getChuyenMon(gv.getMaBoMon()),
                    gv.getBangCap(), gv.getTrangThai()
            });
        }
    }
}
