package com.trungtam.ui.quanly;

import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class QuanLyHocVienPanel extends JPanel {

    private final DefaultTableModel tableModel;
    private final TableRowSorter<DefaultTableModel> rowSorter;

    private static final String[] COT = {
            "Mã HV", "Họ Tên", "Giới Tính", "Ngày Sinh", "SĐT", "Email", "Địa Chỉ", "Số Tài Khoản"
    };

    public QuanLyHocVienPanel() {
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M));
        setBackground(UiTheme.APP_BG);

        add(buildTopBar(), BorderLayout.NORTH);

        tableModel = new DefaultTableModel(COT, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        UiComponents.styleTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        rowSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(rowSorter);

        int[] widths = {60, 160, 70, 100, 110, 200, 250, 80};
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        UiComponents.setColumnAlignments(table,
                SwingConstants.CENTER, SwingConstants.LEFT, SwingConstants.CENTER,
                SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.LEFT,
                SwingConstants.LEFT, SwingConstants.CENTER);

        add(UiComponents.tableScroll(table), BorderLayout.CENTER);
        add(buildBottomBar(), BorderLayout.SOUTH);
        loadSampleData();
    }

    private JPanel buildTopBar() {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setOpaque(false);

        JLabel title = new JLabel("Danh Sách Học Viên");
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
        bar.add(UiComponents.primaryButton("Thêm", UiTheme.SUCCESS));
        bar.add(UiComponents.primaryButton("Sửa", UiTheme.PRIMARY));
        bar.add(UiComponents.primaryButton("Xóa", UiTheme.DANGER));
        bar.add(UiComponents.ghostButton("Làm Mới"));
        return bar;
    }

    private void loadSampleData() {
        tableModel.addRow(new Object[]{1, "Nguyễn Văn An", "Nam", "2002-01-15", "0301112222",
                "an.nguyen@student.edu.vn", "123 Đường Ba Tháng Hai, Quận 10, TP.HCM", 4});
        tableModel.addRow(new Object[]{2, "Trần Thị Bình", "Nữ", "2003-05-22", "0302223333",
                "binh.tran@student.edu.vn", "456 Đường Nguyễn Trãi, Quận 5, TP.HCM", 5});
        tableModel.addRow(new Object[]{3, "Phan Văn Cường", "Nam", "2001-09-10", "0303334444",
                "cuong.phan@student.edu.vn", "789 Đường Lê Lợi, Quận 1, TP.HCM", 6});
        tableModel.addRow(new Object[]{4, "Lê Thị Dung", "Nữ", "2004-12-01", "0304445555",
                "dung.le@student.edu.vn", "101 Đường Cách Mạng Tháng Tám, Q.3", 9});
        tableModel.addRow(new Object[]{5, "Hoàng Văn Em", "Nam", "2002-07-18", "0305556666",
                "em.hoang@student.edu.vn", "202 Đường Cộng Hòa, Quận Tân Bình", 10});
    }
}
