package com.trungtam.ui.quanly;

import com.trungtam.controller.BoMonController;
import com.trungtam.model.BoMon;
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

public class QuanLyBoMonPanel extends JPanel {

    private final DefaultTableModel tableModel;
    private final TableRowSorter<DefaultTableModel> rowSorter;
    private final BoMonController boMonController = new BoMonController();
    private JTable table;

    private static final String[] COT = { "Mã Bộ Môn", "Tên Bộ Môn" };

    public QuanLyBoMonPanel() {
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

        UiComponents.setColumnAlignments(table, SwingConstants.CENTER, SwingConstants.LEFT);

        add(UiComponents.tableScroll(table), BorderLayout.CENTER);
        add(buildBottomBar(), BorderLayout.SOUTH);
        loadData();
    }

    private JPanel buildTopBar() {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setOpaque(false);
        JLabel title = new JLabel("Danh Sách Bộ Môn");
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
        btnThem.addActionListener(e -> themBoMon());

        JButton btnSua = UiComponents.primaryButton("Sửa", UiTheme.PRIMARY);
        btnSua.addActionListener(e -> suaBoMon());

        JButton btnXoa = UiComponents.primaryButton("Xóa", UiTheme.DANGER);
        btnXoa.addActionListener(e -> xoaBoMon());

        JButton refreshBtn = UiComponents.ghostButton("Làm mới");
        refreshBtn.addActionListener(e -> loadData());

        bar.add(btnThem);
        bar.add(btnSua);
        bar.add(btnXoa);
        bar.add(refreshBtn);
        return bar;
    }

    private void themBoMon() {
        String ten = JOptionPane.showInputDialog(this, "Tên Bộ Môn:", "Thêm Bộ Môn", JOptionPane.PLAIN_MESSAGE);
        if (ten != null && !ten.trim().isEmpty()) {
            BoMon bm = new BoMon();
            bm.setTenBoMon(ten.trim());
            if (boMonController.addBoMon(bm)) {
                JOptionPane.showMessageDialog(this, "Thêm bộ môn thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm bộ môn thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void suaBoMon() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn bộ môn cần sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(row);
        int maBM = (int) tableModel.getValueAt(modelRow, 0);
        String tenCu = (String) tableModel.getValueAt(modelRow, 1);

        String tenMoi = JOptionPane.showInputDialog(this, "Tên Bộ Môn:", tenCu);
        if (tenMoi != null && !tenMoi.trim().isEmpty()) {
            BoMon bm = new BoMon();
            bm.setMaBoMon(maBM);
            bm.setTenBoMon(tenMoi.trim());
            if (boMonController.editBoMon(bm)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void xoaBoMon() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn bộ môn cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(row);
        int maBM = (int) tableModel.getValueAt(modelRow, 0);
        String ten = (String) tableModel.getValueAt(modelRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa bộ môn \"" + ten + "\"?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            BoMon bm = new BoMon();
            bm.setMaBoMon(maBM);
            if (boMonController.deleteBoMon(bm)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại! Bộ môn có thể đang có khóa học liên kết.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<BoMon> list = boMonController.getListBoMon();
        for (BoMon bm : list) {
            tableModel.addRow(new Object[]{ bm.getMaBoMon(), bm.getTenBoMon() });
        }
    }
}
