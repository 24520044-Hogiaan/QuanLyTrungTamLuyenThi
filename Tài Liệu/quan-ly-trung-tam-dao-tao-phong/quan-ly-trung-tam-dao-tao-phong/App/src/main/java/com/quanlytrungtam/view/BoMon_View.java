package com.quanlytrungtam.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;

import com.quanlytrungtam.controller.BoMonController;
import com.quanlytrungtam.model.BoMon;
import com.quanlytrungtam.ui.UiComponents;
import com.quanlytrungtam.ui.UiTheme;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class BoMon_View extends JPanel implements Refreshable {

    private final DefaultTableModel tableModel;
    private final JTable table;
    private final TableRowSorter<DefaultTableModel> rowSorter;
    private List<BoMon> boMonList = new ArrayList<>();
    private JTextField searchField;
    private BoMonController boMonController = new BoMonController();

    private static final String[] COT = {
            "Mã Bộ Môn", "Tên Bộ Môn"
    };

    public BoMon_View() {
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

        int[] widths = { 150, 450 };
        for (int i = 0; i < widths.length && i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        UiComponents.setColumnAlignments(table, SwingConstants.CENTER, SwingConstants.LEFT);
        add(UiComponents.tableScroll(table), BorderLayout.CENTER);
        add(buildBottomBar(), BorderLayout.SOUTH);
        refreshTable();
    }

    private JPanel buildTopBar() {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setOpaque(false);

        JLabel title = new JLabel("Danh Sách Bộ Môn");
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

        btnThem.addActionListener(e -> addBoMon());
        btnSua.addActionListener(e -> editBoMon());
        btnXoa.addActionListener(e -> deleteBoMon());
        btnLamMoi.addActionListener(e -> refreshBoMonList());

        panel.add(btnThem);
        panel.add(btnSua);
        panel.add(btnXoa);
        panel.add(btnLamMoi);

        return panel;
    }

    private void addBoMon() {
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        BoMonDialog dialog = new BoMonDialog(parentFrame, null);
        dialog.setVisible(true);
        BoMon result = dialog.getResult();
        if (result != null) {
            int newId = 1;
            for (BoMon bm : boMonList) {
                if (bm.getMaBoMon() >= newId) {
                    newId = bm.getMaBoMon() + 1;
                }
            }
            result.setMaBoMon(newId);
            if (boMonController.addBoMon(result)) {
                refreshTable();
                JOptionPane.showMessageDialog(this, "Thêm bộ môn mới thành công!", "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                refreshTable();
                JOptionPane.showMessageDialog(this, "Thêm bộ môn mới thất bại!", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editBoMon() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một bộ môn từ bảng để sửa!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = table.convertRowIndexToModel(selectedRow);
        int maBoMon = (int) tableModel.getValueAt(modelRow, 0);

        BoMon existing = null;
        int listIndex = -1;
        for (int i = 0; i < boMonList.size(); i++) {
            if (boMonList.get(i).getMaBoMon() == maBoMon) {
                existing = boMonList.get(i);
                listIndex = i;
                break;
            }
        }

        if (existing == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin bộ môn!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        BoMonDialog dialog = new BoMonDialog(parentFrame, existing);
        dialog.setVisible(true);
        BoMon result = dialog.getResult();
        if (result != null) {
            boMonList.set(listIndex, result);
            if (boMonController.editBoMon(result)) {
                refreshTable();
                JOptionPane.showMessageDialog(this, "Cập nhật thông tin bộ môn thành công!", "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                refreshTable();
                JOptionPane.showMessageDialog(this, "Cập nhật thông tin bộ môn thất bại!", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteBoMon() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một bộ môn từ bảng để xóa!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = table.convertRowIndexToModel(selectedRow);
        int maBoMon = (int) tableModel.getValueAt(modelRow, 0);

        BoMon existing = null;
        for (int i = 0; i < boMonList.size(); i++) {
            if (boMonList.get(i).getMaBoMon() == maBoMon) {
                existing = boMonList.get(i);
                break;
            }
        }

        if (existing == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin bộ môn!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xóa bộ môn " + existing.getTenBoMon() + " không?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            boMonController.deleteBoMon(existing);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Đã xóa bộ môn thành công!", "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
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

    private void refreshBoMonList() {
        refresh();
        JOptionPane.showMessageDialog(this, "Danh sách bộ môn đã được cập nhật!", "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        boMonList = boMonController.getListBoMon();
        for (BoMon bm : boMonList) {
            tableModel.addRow(new Object[] {
                    bm.getMaBoMon(), bm.getTenBoMon()
            });
        }
    }
}
