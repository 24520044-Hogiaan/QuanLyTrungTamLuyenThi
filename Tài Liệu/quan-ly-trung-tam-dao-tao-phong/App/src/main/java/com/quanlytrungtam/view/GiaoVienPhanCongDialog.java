package com.quanlytrungtam.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.quanlytrungtam.controller.LopHocController;
import com.quanlytrungtam.model.LopHoc;
import com.quanlytrungtam.ui.UiComponents;
import com.quanlytrungtam.ui.UiTheme;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class GiaoVienPhanCongDialog extends JDialog {

    private final int maGiaoVien;
    private final LopHocController lopHocController = new LopHocController();
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    private static final String[] COT = {
            "Mã Lớp", "Tên Lớp", "Ngày BD", "Ngày KT", "Tần Suất", "Trạng Thái"
    };

    public GiaoVienPhanCongDialog(Frame parent, int maGiaoVien) {
        super(parent, "Phân Công Giảng Dạy - GV " + maGiaoVien, true);
        this.maGiaoVien = maGiaoVien;

        setSize(700, 450);
        setMinimumSize(new Dimension(600, 350));
        setLocationRelativeTo(parent);
        setResizable(true);

        JPanel content = new JPanel(new BorderLayout(0, 12));
        content.setBorder(new EmptyBorder(16, 20, 16, 20));
        content.setBackground(UiTheme.APP_BG);

        JLabel lblTitle = new JLabel("Danh sách lớp đang phân công (Đang mở)");
        lblTitle.setFont(UiTheme.TITLE_M);
        lblTitle.setForeground(UiTheme.TEXT_PRIMARY);
        content.add(lblTitle, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(COT, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table = new JTable(tableModel);
        UiComponents.styleTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        content.add(UiComponents.tableScroll(table), BorderLayout.CENTER);

        content.add(buildButtons(), BorderLayout.SOUTH);

        add(content);
        refreshTable();
    }

    private JPanel buildButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panel.setOpaque(false);

        JButton btnThem = UiComponents.primaryButton("Thêm phân công", UiTheme.SUCCESS);
        JButton btnXoa = UiComponents.primaryButton("Xóa phân công", UiTheme.DANGER);
        JButton btnDong = UiComponents.ghostButton("Đóng");

        btnThem.addActionListener(e -> addPhanCong());
        btnXoa.addActionListener(e -> removePhanCong());
        btnDong.addActionListener(e -> dispose());

        panel.add(btnThem);
        panel.add(btnXoa);
        panel.add(btnDong);
        return panel;
    }

    private void addPhanCong() {
        List<LopHoc> unassigned = lopHocController.getUnassignedLopHoc();

        if (unassigned.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Không có lớp học nào đang mở chưa được phân công.",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] items = new String[unassigned.size()];
        for (int i = 0; i < unassigned.size(); i++) {
            LopHoc lh = unassigned.get(i);
            items[i] = lh.getMaLopHoc() + " - " + lh.getTenLop();
        }

        JComboBox<String> cboLop = new JComboBox<>(items);
        cboLop.setFont(UiTheme.BODY);

        int result = JOptionPane.showConfirmDialog(
                this,
                cboLop,
                "Chọn lớp để phân công",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION && cboLop.getSelectedItem() != null) {
            String selected = cboLop.getSelectedItem().toString();
            int maLopHoc = Integer.parseInt(selected.split(" - ")[0].trim());

            if (lopHocController.assignGiaoVien(maLopHoc, maGiaoVien)) {
                refreshTable();
                JOptionPane.showMessageDialog(this,
                        "Phân công thành công!", "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Phân công thất bại!", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void removePhanCong() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một lớp từ bảng để xóa phân công!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = table.convertRowIndexToModel(selectedRow);
        int maLopHoc = (int) tableModel.getValueAt(modelRow, 0);
        String tenLop = (String) tableModel.getValueAt(modelRow, 1);

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Xóa phân công lớp \"" + tenLop + "\" ?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            if (lopHocController.unassignGiaoVien(maLopHoc)) {
                refreshTable();
                JOptionPane.showMessageDialog(this,
                        "Đã xóa phân công thành công!", "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Xóa phân công thất bại!", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<LopHoc> assigned = lopHocController.getAssignedLopHoc(maGiaoVien);
        for (LopHoc lh : assigned) {
            tableModel.addRow(new Object[] {
                    lh.getMaLopHoc(),
                    lh.getTenLop(),
                    lh.getNgayBd() != null ? sdf.format(lh.getNgayBd()) : "",
                    lh.getNgayKt() != null ? sdf.format(lh.getNgayKt()) : "",
                    lh.getTansuat(),
                    lh.getTrangThai()
            });
        }
    }
}
