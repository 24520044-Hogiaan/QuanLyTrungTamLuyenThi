package com.quanlytrungtam.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;

import com.quanlytrungtam.controller.PhongHocController;
import com.quanlytrungtam.model.PhongHoc;
import com.quanlytrungtam.ui.UiComponents;
import com.quanlytrungtam.ui.UiTheme;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class PhongHoc_View extends JPanel implements Refreshable {

    private final DefaultTableModel tableModel;
    private final JTable table;
    private final TableRowSorter<DefaultTableModel> rowSorter;
    private List<PhongHoc> phongHocList = new ArrayList<>();
    private JTextField searchField;
    private PhongHocController phongHocController = new PhongHocController();

    private static final String[] COT = {
            "Mã Phòng", "Tên Phòng", "Sức Chứa", "Trạng Thái"
    };

    public PhongHoc_View() {
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

        // Thiết lập độ rộng cột
        int[] widths = { 80, 200, 100, 150 };
        for (int i = 0; i < widths.length && i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        UiComponents.setColumnAlignments(table, SwingConstants.CENTER, SwingConstants.LEFT, SwingConstants.CENTER,
                SwingConstants.CENTER);
        add(UiComponents.tableScroll(table), BorderLayout.CENTER);
        add(buildBottomBar(), BorderLayout.SOUTH);
        refreshTable();
    }

    private JPanel buildTopBar() {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setOpaque(false);

        JLabel title = new JLabel("Danh Sách Phòng Học");
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
        JButton btnLamMoi = UiComponents.primaryButton("Làm mới", UiTheme.PRIMARY);

        btnThem.addActionListener(e -> addPhongHoc());
        btnSua.addActionListener(e -> editPhongHoc());
        btnXoa.addActionListener(e -> deletePhongHoc());
        btnLamMoi.addActionListener(e -> refreshBoMonList());

        panel.add(btnThem);
        panel.add(btnSua);
        panel.add(btnXoa);
        panel.add(btnLamMoi);

        return panel;
    }

    private void addPhongHoc() {
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        PhongHocDialog dialog = new PhongHocDialog(parentFrame, null);
        dialog.setVisible(true);

        PhongHoc result = dialog.getResult();
        if (result != null) {
            int newId = 1;
            for (PhongHoc ph : phongHocList) {
                if (ph.getMaPhongHoc() >= newId) {
                    newId = ph.getMaPhongHoc() + 1;
                }
            }
            result.setMaPhongHoc(newId);

            if (phongHocController.addPhongHoc(result)) {
                refreshTable();
                JOptionPane.showMessageDialog(this, "Thêm phòng học mới thành công!", "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                refreshTable();
                JOptionPane.showMessageDialog(this, "Thêm phòng học mới thất bại!", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editPhongHoc() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng học để sửa!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = table.convertRowIndexToModel(selectedRow);
        int maPhongHoc = (int) tableModel.getValueAt(modelRow, 0);

        PhongHoc existing = phongHocList.stream()
                .filter(p -> p.getMaPhongHoc() == maPhongHoc)
                .findFirst().orElse(null);

        if (existing == null)
            return;

        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        PhongHocDialog dialog = new PhongHocDialog(parentFrame, existing);
        dialog.setVisible(true);
        PhongHoc result = dialog.getResult();

        if (result != null) {
            if (phongHocController.editPhongHoc(result)) {
                refreshTable();
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deletePhongHoc() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng học để xóa!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = table.convertRowIndexToModel(selectedRow);
        int maPhongHoc = (int) tableModel.getValueAt(modelRow, 0);

        PhongHoc existing = phongHocList.stream()
                .filter(p -> p.getMaPhongHoc() == maPhongHoc)
                .findFirst().orElse(null);

        if (existing != null && JOptionPane.showConfirmDialog(this,
                "Xác nhận xóa " + existing.getTenPhong() + "?") == JOptionPane.YES_OPTION) {
            phongHocController.deletePhongHoc(existing);
            refreshTable();
        }
    }

    @Override
    public void refresh() {
        if (searchField != null)
            searchField.setText("");
        rowSorter.setRowFilter(null);
        refreshTable();
    }

    private void refreshBoMonList() {
        refresh();
        JOptionPane.showMessageDialog(this, "Danh sách đã cập nhật!");
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        phongHocList = phongHocController.getListPhongHoc();
        for (PhongHoc ph : phongHocList) {
            tableModel.addRow(new Object[] {
                    ph.getMaPhongHoc(), ph.getTenPhong(), ph.getSucChua(), ph.getTrangThai()
            });
        }
    }
}