package com.quanlytrungtam.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;

import com.quanlytrungtam.controller.NgayNghiLeController;
import com.quanlytrungtam.model.NgayNghiLe;
import com.quanlytrungtam.ui.UiComponents;
import com.quanlytrungtam.ui.UiTheme;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class NgayNghiLe_View extends JPanel implements Refreshable {

    private final DefaultTableModel tableModel;
    private final JTable table;
    private final TableRowSorter<DefaultTableModel> rowSorter;
    private List<NgayNghiLe> ngayNghiLeList = new ArrayList<>();
    private JTextField searchField;
    private final NgayNghiLeController ngayNghiLeController = new NgayNghiLeController();
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    private static final String[] COT = { "Mã Ngày Nghỉ", "Ngày Bắt Đầu", "Ngày Kết Thúc" };

    public NgayNghiLe_View() {
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

        add(UiComponents.tableScroll(table), BorderLayout.CENTER);
        add(buildBottomBar(), BorderLayout.SOUTH);

        refreshTable();
    }

    private JPanel buildTopBar() {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setOpaque(false);

        JLabel title = new JLabel("Danh Sách Ngày Nghỉ Lễ");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.TEXT_PRIMARY);

        searchField = new JTextField(22);
        searchField.putClientProperty("JTextField.placeholderText", "Tìm kiếm...");
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchField.getText().trim(), 0));
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

        btnThem.addActionListener(e -> addNgayNghiLe());
        btnSua.addActionListener(e -> editNgayNghiLe());
        btnXoa.addActionListener(e -> deleteNgayNghiLe());
        btnLamMoi.addActionListener(e -> refresh());

        panel.add(btnThem);
        panel.add(btnSua);
        panel.add(btnXoa);
        panel.add(btnLamMoi);
        return panel;
    }

    private void addNgayNghiLe() {
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        NgayNghiLeDialog dialog = new NgayNghiLeDialog(parentFrame, null);
        dialog.setVisible(true);

        NgayNghiLe result = dialog.getResult();
        if (result != null) {
            int newId = 1;
            for (NgayNghiLe nnl : ngayNghiLeList) {
                if (nnl.getMaNgayNghi() >= newId) {
                    newId = nnl.getMaNgayNghi() + 1;
                }
            }
            result.setMaNgayNghi(newId);

            if (ngayNghiLeController.addNgayNghiLe(result)) {
                refreshTable();
                JOptionPane.showMessageDialog(this, "Thêm ngày nghỉ lễ mới thành công!", "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Thêm ngày nghỉ lễ mới thất bại!", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editNgayNghiLe() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng!");
            return;
        }

        int ma = (int) tableModel.getValueAt(table.convertRowIndexToModel(row), 0);
        NgayNghiLe existing = ngayNghiLeList.stream().filter(n -> n.getMaNgayNghi() == ma).findFirst().orElse(null);

        if (existing != null) {
            NgayNghiLeDialog dialog = new NgayNghiLeDialog((Frame) SwingUtilities.getWindowAncestor(this), existing);
            dialog.setVisible(true);
            if (dialog.getResult() != null) {
                if (ngayNghiLeController.editNgayNghiLe(dialog.getResult())) {
                    refreshTable();
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                }
            }
        }
    }

    private void deleteNgayNghiLe() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một ngày nghỉ lễ từ bảng để xóa!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = table.convertRowIndexToModel(selectedRow);
        int maNgayNghi = (int) tableModel.getValueAt(modelRow, 0);

        NgayNghiLe existing = null;
        for (int i = 0; i < ngayNghiLeList.size(); i++) {
            if (ngayNghiLeList.get(i).getMaNgayNghi() == maNgayNghi) {
                existing = ngayNghiLeList.get(i);
                break;
            }
        }

        if (existing == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin ngày nghỉ lễ!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xóa ngày nghỉ lễ có mã " + existing.getMaNgayNghi() + " không?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            boolean success = ngayNghiLeController.deleteNgayNghiLe(existing);
            if (success) {
                refreshTable();
                JOptionPane.showMessageDialog(this, "Đã xóa ngày nghỉ lễ thành công!", "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi xóa trong cơ sở dữ liệu!", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void refresh() {
        searchField.setText("");
        rowSorter.setRowFilter(null);
        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        ngayNghiLeList = ngayNghiLeController.getListNgayNghiLe();
        for (NgayNghiLe nnl : ngayNghiLeList) {
            tableModel.addRow(new Object[] {
                    nnl.getMaNgayNghi(),
                    sdf.format(nnl.getNgayBatDau()),
                    sdf.format(nnl.getNgayKetThuc())
            });
        }
    }
}