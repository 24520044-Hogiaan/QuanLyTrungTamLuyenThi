package com.quanlytrungtam.view.LopHoc;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.quanlytrungtam.controller.LopHocController;
import com.quanlytrungtam.dao.HocVienDAO;
import com.quanlytrungtam.model.HocVien;
import com.quanlytrungtam.ui.UiComponents;
import com.quanlytrungtam.ui.UiTheme;
import com.quanlytrungtam.view.Refreshable;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;

/**
 * Danh sách học viên theo lớp học
 */
public class DanhSachHocVienPanel extends JPanel implements Refreshable {

    private JTable table;
    private DefaultTableModel model;
    private int maLopHoc;
    private JLabel lblTitle;

    public DanhSachHocVienPanel(int maLopHoc) {
        this.maLopHoc = maLopHoc;

        setLayout(new BorderLayout(0, 10));
        setOpaque(false);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        LopHocController lopHocController = new LopHocController();
        int siSoHocVien = lopHocController.getCountStudentInLop(maLopHoc);
        lblTitle = new JLabel(
                "Danh sách học viên đăng ký: " + siSoHocVien + "/"
                        + lopHocController.getLopHocByMaLop(maLopHoc).getSiso());
        lblTitle.setFont(UiTheme.TITLE_S);
        lblTitle.setForeground(UiTheme.TEXT_PRIMARY);
        topPanel.add(lblTitle, BorderLayout.WEST);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setOpaque(false);

        JButton btnDelete = UiComponents.primaryButton("Xóa học viên", UiTheme.DANGER);
        JButton btnAdd = UiComponents.primaryButton("Thêm học viên vào lớp", UiTheme.PRIMARY);

        btnAdd.addActionListener(e -> showAddHocVienDialog(maLopHoc));
        btnDelete.addActionListener(e -> deleteSelectedHocVien());

        btnPanel.add(btnDelete);
        btnPanel.add(btnAdd);
        topPanel.add(btnPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        String[] cols = {
                "Mã HV",
                "Họ Tên",
                "Ngày Sinh",
                "Giới Tính",
                "SĐT",
                "Email"
        };

        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(UiTheme.ROW_HEIGHT);
        table.setFont(UiTheme.BODY);
        table.setShowGrid(true);
        table.setGridColor(UiTheme.CARD_BORDER);

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(UiTheme.CARD_BORDER));

        add(sp, BorderLayout.CENTER);

        loadData(maLopHoc);
    }

    private void loadData(int maLopHoc) {

        model.setRowCount(0);

        LopHocController lopHocController = new LopHocController();
        int siSoHocVien = lopHocController.getCountStudentInLop(maLopHoc);
        if (lblTitle != null) {
            lblTitle.setText("Danh sách học viên đăng ký: " + siSoHocVien + "/"
                    + lopHocController.getLopHocByMaLop(maLopHoc).getSiso());
        }

        List<HocVien> list = lopHocController.getListHocVien(maLopHoc);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (HocVien hv : list) {

            model.addRow(new Object[] {
                    hv.getMaHocVien(),
                    hv.getHoTen(),
                    hv.getNgaySinh() != null
                            ? sdf.format(hv.getNgaySinh())
                            : "",
                    hv.getGioiTinh(),
                    hv.getSDT(),
                    hv.getEmail()
            });
        }
    }

    private void showAddHocVienDialog(int maLopHoc) {
        HocVienDAO hocVienDAO = new HocVienDAO();
        LopHocController lopHocController = new LopHocController();

        List<HocVien> allHocViens = hocVienDAO.getListHocVien();
        List<HocVien> enrolledHocViens = lopHocController.getListHocVien(maLopHoc);

        List<HocVien> availableHocViens = new ArrayList<>();
        for (HocVien hv : allHocViens) {
            boolean enrolled = false;
            for (HocVien enrolledHv : enrolledHocViens) {
                if (hv.getMaHocVien() == enrolledHv.getMaHocVien()) {
                    enrolled = true;
                    break;
                }
            }
            if (!enrolled) {
                availableHocViens.add(hv);
            }
        }

        if (availableHocViens.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Không còn học viên nào chưa đăng ký lớp này!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JComboBox<String> cboHocVien = new JComboBox<>();
        for (HocVien hv : availableHocViens) {
            cboHocVien.addItem(hv.getMaHocVien() + " - " + hv.getHoTen());
        }

        JComboBox<String> cboHinhThuc = new JComboBox<>(new String[] {
                "1 tháng", "Toàn khóa"
        });

        JPanel panel = new JPanel(new GridLayout(0, 1, 6, 6));
        panel.add(new JLabel("Chọn học viên:"));
        panel.add(cboHocVien);
        panel.add(new JLabel("Hình thức thanh toán:"));
        panel.add(cboHinhThuc);

        int result = JOptionPane.showConfirmDialog(this, panel, "Thêm học viên vào lớp",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            int selectedIdx = cboHocVien.getSelectedIndex();
            if (selectedIdx >= 0 && selectedIdx < availableHocViens.size()) {
                HocVien selected = availableHocViens.get(selectedIdx);
                String hinhThuc = "1 thang";
                int selectedHinhThucIdx = cboHinhThuc.getSelectedIndex();
                if (selectedHinhThucIdx == 1) {
                    hinhThuc = "Toan khoa";
                } else if (selectedHinhThucIdx == 2) {
                    hinhThuc = "Theo ky";
                }

                boolean success = lopHocController.addHocVienToLop(selected.getMaHocVien(), maLopHoc, hinhThuc);
                if (success) {
                    loadData(maLopHoc);
                    JOptionPane.showMessageDialog(this,
                            "Thêm học viên vào lớp thành công!",
                            "Thông báo",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Thêm học viên vào lớp thất bại!",
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void deleteSelectedHocVien() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một học viên trong bảng để xóa!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = table.convertRowIndexToModel(row);
        int maHocVien = (int) model.getValueAt(modelRow, 0);
        String tenHocVien = (String) model.getValueAt(modelRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa học viên \"" + tenHocVien + "\" ra khỏi lớp này?",
                "Xác nhận xóa học viên",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            LopHocController lopHocController = new LopHocController();
            boolean success = lopHocController.deleteHocVienFromLop(maHocVien, maLopHoc);
            if (success) {
                JOptionPane.showMessageDialog(this, "Xóa học viên khỏi lớp thành công!", "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
                loadData(maLopHoc);
            } else {
                JOptionPane.showMessageDialog(this, "Xóa học viên khỏi lớp thất bại!", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void refresh() {
        loadData(this.maLopHoc);
    }
}