package com.trungtam.ui.quanly;

import com.trungtam.controller.PhongHocController;
import com.trungtam.model.PhongHoc;
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

public class QuanLyPhongHocPanel extends JPanel {

    private final DefaultTableModel tableModel;
    private final TableRowSorter<DefaultTableModel> rowSorter;
    private final PhongHocController phongHocController = new PhongHocController();
    private JTable table;

    private static final String[] COT = { "Mã Phòng", "Tên Phòng", "Sức Chứa", "Trạng Thái" };

    public QuanLyPhongHocPanel() {
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
        UiComponents.setColumnAlignments(table,
                SwingConstants.CENTER, SwingConstants.LEFT, SwingConstants.CENTER, SwingConstants.CENTER);
        add(UiComponents.tableScroll(table), BorderLayout.CENTER);
        add(buildBottomBar(), BorderLayout.SOUTH);
        loadData();
    }

    private JPanel buildTopBar() {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setOpaque(false);
        JLabel title = new JLabel("Danh Sách Phòng Học");
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
        btnThem.addActionListener(e -> themPhongHoc());

        JButton btnSua = UiComponents.primaryButton("Sửa", UiTheme.PRIMARY);
        btnSua.addActionListener(e -> suaPhongHoc());

        JButton btnXoa = UiComponents.primaryButton("Xóa", UiTheme.DANGER);
        btnXoa.addActionListener(e -> xoaPhongHoc());

        JButton refreshBtn = UiComponents.ghostButton("Làm mới");
        refreshBtn.addActionListener(e -> loadData());

        bar.add(btnThem);
        bar.add(btnSua);
        bar.add(btnXoa);
        bar.add(refreshBtn);
        return bar;
    }

    private void themPhongHoc() {
        JTextField txtTen = new JTextField();
        JTextField txtSucChua = new JTextField();
        JComboBox<String> cboTT = new JComboBox<>(new String[]{"Hoat dong", "Bao tri", "Dong"});

        Object[] fields = { "Tên Phòng:", txtTen, "Sức Chứa:", txtSucChua, "Trạng Thái:", cboTT };
        int result = JOptionPane.showConfirmDialog(this, fields, "Thêm Phòng Học", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            if (txtTen.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tên phòng không được để trống!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            PhongHoc ph = new PhongHoc();
            ph.setTenPhong(txtTen.getText().trim());
            try { ph.setSucChua(Integer.parseInt(txtSucChua.getText().trim())); } catch (NumberFormatException ignored) { ph.setSucChua(30); }
            ph.setTrangThai((String) cboTT.getSelectedItem());
            if (phongHocController.addPhongHoc(ph)) {
                JOptionPane.showMessageDialog(this, "Thêm phòng học thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm phòng học thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void suaPhongHoc() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng học cần sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE); return; }
        int modelRow = table.convertRowIndexToModel(row);
        int maPhong = (int) tableModel.getValueAt(modelRow, 0);

        JTextField txtTen = new JTextField(String.valueOf(tableModel.getValueAt(modelRow, 1)));
        JTextField txtSucChua = new JTextField(String.valueOf(tableModel.getValueAt(modelRow, 2)));
        JComboBox<String> cboTT = new JComboBox<>(new String[]{"Hoat dong", "Bao tri", "Dong"});
        cboTT.setSelectedItem(tableModel.getValueAt(modelRow, 3));

        Object[] fields = { "Tên Phòng:", txtTen, "Sức Chứa:", txtSucChua, "Trạng Thái:", cboTT };
        int result = JOptionPane.showConfirmDialog(this, fields, "Sửa Phòng Học (Mã: " + maPhong + ")", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            PhongHoc ph = new PhongHoc();
            ph.setMaPhongHoc(maPhong);
            ph.setTenPhong(txtTen.getText().trim());
            try { ph.setSucChua(Integer.parseInt(txtSucChua.getText().trim())); } catch (NumberFormatException ignored) { ph.setSucChua(30); }
            ph.setTrangThai((String) cboTT.getSelectedItem());
            if (phongHocController.editPhongHoc(ph)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void xoaPhongHoc() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng học cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE); return; }
        int modelRow = table.convertRowIndexToModel(row);
        int maPhong = (int) tableModel.getValueAt(modelRow, 0);
        String ten = (String) tableModel.getValueAt(modelRow, 1);
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa phòng \"" + ten + "\"?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            if (phongHocController.deletePhongHoc(maPhong)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<PhongHoc> list = phongHocController.getListPhongHoc();
        for (PhongHoc ph : list) {
            tableModel.addRow(new Object[]{
                    ph.getMaPhongHoc(), ph.getTenPhong(), ph.getSucChua(), ph.getTrangThai()
            });
        }
    }
}
