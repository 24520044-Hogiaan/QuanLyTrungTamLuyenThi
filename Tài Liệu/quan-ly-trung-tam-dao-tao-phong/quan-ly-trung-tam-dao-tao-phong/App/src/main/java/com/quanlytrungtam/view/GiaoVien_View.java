package com.quanlytrungtam.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;

import com.quanlytrungtam.controller.GiaoVienController;
import com.quanlytrungtam.model.GiaoVien;
import com.quanlytrungtam.ui.UiComponents;
import com.quanlytrungtam.ui.UiTheme;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class GiaoVien_View extends JPanel implements Refreshable {

    private final DefaultTableModel tableModel;
    private final JTable table;
    private final TableRowSorter<DefaultTableModel> rowSorter;
    private List<GiaoVien> giaoVienList = new ArrayList<>();
    private JTextField searchField;
    private GiaoVienController giaoVienController = new GiaoVienController();

    private static final String[] COT = {
            "Mã GV", "Mã Nhân Viên", "Chuyên Môn", "Bằng Cấp", "Trạng Thái"
    };

    public GiaoVien_View() {
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
        table.setDefaultRenderer(Object.class, new StatusRenderer());

        rowSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(rowSorter);

        int[] widths = { 80, 120, 180, 120, 120 };
        for (int i = 0; i < widths.length && i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        UiComponents.setColumnAlignments(table,
                SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.LEFT,
                SwingConstants.LEFT, SwingConstants.CENTER);
        add(UiComponents.tableScroll(table), BorderLayout.CENTER);
        add(buildBottomBar(), BorderLayout.SOUTH);
        refreshTable();
    }

    private JPanel buildTopBar() {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setOpaque(false);

        JLabel title = new JLabel("Danh Sách Giáo Viên");
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
        JButton btnPhanCong = UiComponents.primaryButton("Phân Công", UiTheme.WARNING);

        btnThem.addActionListener(e -> addGiaoVien());
        btnSua.addActionListener(e -> editGiaoVien());
        btnXoa.addActionListener(e -> deleteGiaoVien());
        btnLamMoi.addActionListener(e -> refreshGiaoVienList());
        btnPhanCong.addActionListener(e -> openPhanCong());

        panel.add(btnThem);
        panel.add(btnSua);
        panel.add(btnXoa);
        panel.add(btnLamMoi);
        panel.add(btnPhanCong);

        return panel;
    }

    private void addGiaoVien() {
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        GiaoVienDialog dialog = new GiaoVienDialog(parentFrame, null);
        dialog.setVisible(true);
        GiaoVien result = dialog.getResult();
        if (result != null) {
            int newId = 1;
            for (GiaoVien gv : giaoVienList) {
                if (gv.getMaGiaoVien() >= newId) {
                    newId = gv.getMaGiaoVien() + 1;
                }
            }
            result.setMaGiaoVien(newId);
            if (giaoVienController.addGiaoVien(result)) {
                refreshTable();
                JOptionPane.showMessageDialog(this, "Thêm giáo viên mới thành công!", "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                refreshTable();
                JOptionPane.showMessageDialog(this, "Thêm giáo viên mới thất bại!", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editGiaoVien() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một giáo viên từ bảng để sửa!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = table.convertRowIndexToModel(selectedRow);
        int maGiaoVien = (int) tableModel.getValueAt(modelRow, 0);

        GiaoVien existing = null;
        for (int i = 0; i < giaoVienList.size(); i++) {
            if (giaoVienList.get(i).getMaGiaoVien() == maGiaoVien) {
                existing = giaoVienList.get(i);
                break;
            }
        }

        if (existing == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin giáo viên!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        GiaoVienDialog dialog = new GiaoVienDialog(parentFrame, existing);
        dialog.setVisible(true);
        GiaoVien result = dialog.getResult();
        if (result != null) {
            giaoVienController.editGiaoVien(result);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Cập nhật thông tin giáo viên thành công!", "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void deleteGiaoVien() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một giáo viên từ bảng để xóa!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = table.convertRowIndexToModel(selectedRow);

        int maGiaoVien = (int) tableModel.getValueAt(modelRow, 0);

        GiaoVien existing = null;
        for (int i = 0; i < giaoVienList.size(); i++) {
            if (giaoVienList.get(i).getMaGiaoVien() == maGiaoVien) {
                existing = giaoVienList.get(i);
                break;
            }
        }

        if (existing == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin giáo viên!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xóa giáo viên " + existing.getMaGiaoVien() + " không?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            giaoVienController.deleteGiaoVien(existing);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Đã xóa giáo viên thành công!", "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void openPhanCong() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một giáo viên từ bảng để phân công!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = table.convertRowIndexToModel(selectedRow);
        int maGiaoVien = (int) tableModel.getValueAt(modelRow, 0);

        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        GiaoVienPhanCongDialog dialog = new GiaoVienPhanCongDialog(parentFrame, maGiaoVien);
        dialog.setVisible(true);
    }

    @Override
    public void refresh() {
        if (searchField != null) {
            searchField.setText("");
        }
        rowSorter.setRowFilter(null);
        refreshTable();
    }

    private void refreshGiaoVienList() {
        refresh();
        JOptionPane.showMessageDialog(this, "Danh sách giáo viên đã được cập nhật!", "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        giaoVienList = giaoVienController.getListGiaoVien();
        for (GiaoVien gv : giaoVienList) {
            tableModel.addRow(new Object[] {
                    gv.getMaGiaoVien(), gv.getMaNhanVien(), gv.getChuyenMon(),
                    gv.getBangCap(), gv.getTrangThai()
            });
        }
    }

    private static class StatusRenderer extends UiComponents.AltRowRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable t, Object val, boolean sel, boolean foc, int r, int c) {
            super.getTableCellRendererComponent(t, val, sel, foc, r, c);
            if (!sel && c == 4) {
                boolean active = "Đang làm".equals(String.valueOf(val));
                setForeground(active ? UiTheme.SUCCESS : UiTheme.DANGER);
                setFont(UiTheme.BODY_B);
            } else if (!sel) {
                setFont(UiTheme.BODY);
            }
            return this;
        }
    }
}
