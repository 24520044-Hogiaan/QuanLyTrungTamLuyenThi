package com.trungtam.ui.quanly;

import com.trungtam.controller.NgayNghiLeController;
import com.trungtam.model.NgayNghiLe;
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

public class NgayNghiPanel extends JPanel {

    private final DefaultTableModel tableModel;
    private final TableRowSorter<DefaultTableModel> rowSorter;
    private final NgayNghiLeController ngayNghiController = new NgayNghiLeController();
    private JTable table;

    private static final String[] COT = { "Mã Ngày Nghỉ", "Ngày Bắt Đầu", "Ngày Kết Thúc" };

    public NgayNghiPanel() {
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M));
        setBackground(UiTheme.APP_BG);
        add(buildTopBar(), BorderLayout.NORTH);
        tableModel = new DefaultTableModel(COT, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        UiComponents.styleTable(table);
        rowSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(rowSorter);
        UiComponents.setColumnAlignments(table, SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.CENTER);
        add(UiComponents.tableScroll(table), BorderLayout.CENTER);
        add(buildBottomBar(), BorderLayout.SOUTH);
        loadData();
    }

    private JPanel buildTopBar() {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setOpaque(false);
        JLabel title = new JLabel("Danh Sách Ngày Nghỉ Lễ");
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
        btnThem.addActionListener(e -> {
            JTextField txtBatDau = new JTextField("yyyy-MM-dd");
            JTextField txtKetThuc = new JTextField("yyyy-MM-dd");
            Object[] fields = { "Ngày Bắt Đầu (yyyy-MM-dd):", txtBatDau, "Ngày Kết Thúc (yyyy-MM-dd):", txtKetThuc };
            int result = JOptionPane.showConfirmDialog(this, fields, "Thêm Ngày Nghỉ", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    NgayNghiLe nn = new NgayNghiLe();
                    nn.setNgayBatDau(java.time.LocalDate.parse(txtBatDau.getText().trim()));
                    nn.setNgayKetThuc(java.time.LocalDate.parse(txtKetThuc.getText().trim()));
                    if (ngayNghiController.addNgayNghi(nn)) {
                        JOptionPane.showMessageDialog(this, "Thêm thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        loadData();
                    } else {
                        JOptionPane.showMessageDialog(this, "Thêm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Định dạng ngày không hợp lệ! (yyyy-MM-dd)", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton btnXoa = UiComponents.primaryButton("Xóa", UiTheme.DANGER);
        btnXoa.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày nghỉ cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE); return; }
            int modelRow = table.convertRowIndexToModel(row);
            int ma = (int) tableModel.getValueAt(modelRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa ngày nghỉ này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (ngayNghiController.deleteNgayNghi(ma)) {
                    JOptionPane.showMessageDialog(this, "Xóa thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton refreshBtn = UiComponents.ghostButton("Làm mới");
        refreshBtn.addActionListener(e -> loadData());

        bar.add(btnThem);
        bar.add(btnXoa);
        bar.add(refreshBtn);
        return bar;
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<NgayNghiLe> list = ngayNghiController.getListNgayNghi();
        for (NgayNghiLe nn : list) {
            tableModel.addRow(new Object[]{ nn.getMaNgayNghi(), nn.getNgayBatDau(), nn.getNgayKetThuc() });
        }
    }
}
