package com.trungtam.ui.admin;

import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class QuanLyTaiKhoanPanel extends JPanel {

    private final DefaultTableModel tableModel;
    private final JTable table;
    private final TableRowSorter<DefaultTableModel> rowSorter;

    private static final String[] COT = {
            "Mã TK", "Tên Tài Khoản", "Vai Trò", "Trạng Thái", "Ngày Tạo", "Đăng Nhập Cuối"
    };

    public QuanLyTaiKhoanPanel() {
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M));
        setBackground(UiTheme.APP_BG);

        add(buildTopBar(), BorderLayout.NORTH);

        tableModel = new DefaultTableModel(COT, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        UiComponents.styleTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        rowSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(rowSorter);

        int[] widths = { 60, 180, 140, 120, 150, 150 };
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        UiComponents.setColumnAlignments(table,
                SwingConstants.CENTER, SwingConstants.LEFT, SwingConstants.CENTER,
                SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.CENTER);

        add(UiComponents.tableScroll(table), BorderLayout.CENTER);
        loadSampleData();
    }

    private JPanel buildTopBar() {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setOpaque(false);

        JLabel title = new JLabel("Quản Lý Tài Khoản");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.TEXT_PRIMARY);

        JTextField searchField = new JTextField(22);
        searchField.putClientProperty("JTextField.placeholderText", "Tìm theo tên tài khoản, vai trò...");
        searchField.setFont(UiTheme.BODY);
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String kw = searchField.getText().trim();
                rowSorter.setRowFilter(kw.isEmpty() ? null : RowFilter.regexFilter("(?i)" + kw));
            }
        });

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.setOpaque(false);
        buttons.add(UiComponents.primaryButton("Thêm mới", UiTheme.ADMIN));

        JButton editBtn = UiComponents.ghostButton("Sửa");
        editBtn.addActionListener(e -> {
            if (table.getSelectedRow() < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản cần sửa.");
                return;
            }
            JOptionPane.showMessageDialog(this, "Chức năng sửa tài khoản — đang phát triển.");
        });
        buttons.add(editBtn);

        JButton delBtn = UiComponents.ghostButton("Vô hiệu hóa");
        delBtn.setForeground(UiTheme.DANGER);
        delBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản cần vô hiệu hóa.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Xác nhận vô hiệu hóa tài khoản này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                int modelRow = table.convertRowIndexToModel(row);
                tableModel.setValueAt("Vô hiệu", modelRow, 3);
            }
        });
        buttons.add(delBtn);

        JPanel rightPanel = new JPanel(new BorderLayout(0, 6));
        rightPanel.setOpaque(false);
        rightPanel.add(searchField, BorderLayout.NORTH);
        rightPanel.add(buttons, BorderLayout.SOUTH);

        panel.add(title, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.EAST);
        return panel;
    }

    private void loadSampleData() {
        Object[][] data = {
                {1, "admin01", "Admin", "Hoạt động", "2026-01-01", "2026-05-25"},
                {2, "quanly01", "Nhân viên QL", "Hoạt động", "2026-01-01", "2026-05-24"},
                {3, "ketoan01", "Kế toán", "Hoạt động", "2026-01-01", "2026-05-23"},
                {4, "gvtoan01", "Giáo viên", "Hoạt động", "2026-01-01", "2026-05-25"},
                {5, "gvvan01", "Giáo viên", "Hoạt động", "2026-01-01", "2026-05-22"},
                {6, "gvanh01", "Giáo viên", "Hoạt động", "2026-01-01", "—"},
                {7, "gvly01", "Giáo viên", "Hoạt động", "2026-01-01", "—"},
                {8, "gvhoa01", "Giáo viên", "Hoạt động", "2026-01-01", "—"},
                {9, "hocvien01", "Học viên", "Hoạt động", "2026-01-01", "2026-05-25"},
                {10, "hocvien02", "Học viên", "Hoạt động", "2026-01-01", "—"},
                {11, "hocvien03", "Học viên", "Hoạt động", "2026-01-01", "—"},
                {12, "hocvien04", "Học viên", "Hoạt động", "2026-01-01", "—"},
        };
        for (Object[] row : data) {
            tableModel.addRow(row);
        }
    }
}
