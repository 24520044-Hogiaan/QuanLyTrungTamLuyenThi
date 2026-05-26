package com.quanlytrungtam.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import com.quanlytrungtam.controller.HocVienController;
import com.quanlytrungtam.model.HocVien;
import com.quanlytrungtam.ui.UiComponents;
import com.quanlytrungtam.ui.UiTheme;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class HocVien_View extends JPanel implements Refreshable {

    private final DefaultTableModel tableModel;
    private final JTable table;
    private final TableRowSorter<DefaultTableModel> rowSorter;
    private List<HocVien> hocVienList = new ArrayList<>();
    private JTextField searchField;
    private final HocVienController hocVienController = new HocVienController();

    private static final String[] COT = {
            "Mã HV", "Họ Tên", "Giới Tính", "Ngày Sinh", "SĐT", "Email", "Địa Chỉ", "Số tài khoản"
    };

    public HocVien_View() {
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M));
        setBackground(UiTheme.APP_BG);

        add(buildTopBar(), BorderLayout.NORTH);

        tableModel = new DefaultTableModel(COT, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table = new JTable(tableModel);
        UiComponents.styleTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        rowSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(rowSorter);

        int[] widths = { 80, 150, 80, 100, 100, 150, 200, 120 };
        for (int i = 0; i < widths.length && i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        UiComponents.setColumnAlignments(table,
                SwingConstants.CENTER, SwingConstants.LEFT, SwingConstants.CENTER,
                SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.LEFT,
                SwingConstants.LEFT, SwingConstants.CENTER);

        add(UiComponents.tableScroll(table), BorderLayout.CENTER);
        add(buildBottomBar(), BorderLayout.SOUTH);
        refreshTable();
    }

    private JPanel buildTopBar() {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setOpaque(false);

        JLabel title = new JLabel("Danh Sách Học Viên");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.TEXT_PRIMARY);

        searchField = new JTextField(22);
        searchField.putClientProperty("JTextField.placeholderText", "Tìm kiếm...");
        searchField.setFont(UiTheme.BODY);
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String kw = searchField.getText().trim();
                rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + kw, 0));
            }
        });

        panel.add(title, BorderLayout.WEST);
        panel.add(searchField, BorderLayout.EAST);
        return panel;
    }

    private JPanel buildBottomBar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panel.setOpaque(false);

        JButton btnThem = UiComponents.primaryButton("Thêm", UiTheme.SUCCESS);
        JButton btnSua = UiComponents.primaryButton("Sửa", UiTheme.INFO);
        JButton btnXoa = UiComponents.primaryButton("Xóa", UiTheme.DANGER);
        JButton btnLamMoi = UiComponents.primaryButton("Làm Mới", UiTheme.PRIMARY);

        btnThem.addActionListener(e -> addHocVien());
        btnSua.addActionListener(e -> editHocVien());
        btnXoa.addActionListener(e -> deleteHocVien());
        btnLamMoi.addActionListener(e -> refreshHocVienList());

        panel.add(btnThem);
        panel.add(btnSua);
        panel.add(btnXoa);
        panel.add(btnLamMoi);

        return panel;
    }

    private void addHocVien() {
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        HocVienDialog dialog = new HocVienDialog(parentFrame, null);
        dialog.setVisible(true);
        HocVien result = dialog.getResult();
        if (result != null) {
            int newId = 0;
            for (HocVien hv : hocVienList) {
                if (hv.getMaHocVien() >= newId) {
                    newId = hv.getMaHocVien() + 1;
                }
            }
            result.setMaHocVien(newId);
            boolean success = hocVienController.addHocVien(result);
            if (success) {
                refreshTable();
                JOptionPane.showMessageDialog(this, "Thêm học viên mới thành công!", "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Không thể thêm học viên!", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editHocVien() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một học viên từ bảng để sửa!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = table.convertRowIndexToModel(selectedRow);
        int maHocVien = (Integer) tableModel.getValueAt(modelRow, 0);

        HocVien existing = null;
        for (HocVien hv : hocVienList) {
            if (hv.getMaHocVien() == maHocVien) {
                existing = hv;
                break;
            }
        }

        if (existing == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin học viên!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        HocVienDialog dialog = new HocVienDialog(parentFrame, existing);
        dialog.setVisible(true);
        HocVien result = dialog.getResult();
        if (result != null) {
            boolean success = hocVienController.editHocVien(result);
            if (success) {
                refreshTable();
                JOptionPane.showMessageDialog(this, "Cập nhật thông tin học viên thành công!", "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteHocVien() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một học viên từ bảng để xóa!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = table.convertRowIndexToModel(selectedRow);
        int maHocVien = (Integer) tableModel.getValueAt(modelRow, 0);

        HocVien existing = null;
        for (HocVien hv : hocVienList) {
            if (hv.getMaHocVien() == maHocVien) {
                existing = hv;
                break;
            }
        }

        if (existing == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin học viên!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xóa học viên " + existing.getHoTen() + " (" + existing.getMaHocVien()
                        + ") không?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            boolean success = hocVienController.deleteHocVien(existing.getMaHocVien());
            if (success) {
                refreshTable();
                JOptionPane.showMessageDialog(this, "Đã xóa học viên thành công!", "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Xóa học viên thất bại!", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void refresh() {
        if (searchField != null) {
            searchField.setText("");
        }
        rowSorter.setRowFilter(null);
        refreshTable();
    }

    private void refreshHocVienList() {
        refresh();
        JOptionPane.showMessageDialog(this, "Danh sách học viên đã được cập nhật!", "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        hocVienList = hocVienController.getListHocVien();
        for (HocVien hv : hocVienList) {
            tableModel.addRow(new Object[] {
                    hv.getMaHocVien(), hv.getHoTen(), hv.getGioiTinh(),
                    hv.getNgaySinh(), hv.getSDT(), hv.getEmail(),
                    hv.getDiaChi(), hv.getTaiKhoanNganHang()
            });
        }
    }
}
