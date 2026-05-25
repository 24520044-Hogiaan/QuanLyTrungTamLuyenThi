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

public class DuyetHoanTraPanel extends JPanel {

    private final DefaultTableModel tableModel;
    private final TableRowSorter<DefaultTableModel> rowSorter;

    private static final String[] COT = {
            "Mã Hoàn Trả", "Mã Hóa Đơn", "Mã Học Viên", "Số Tiền", "Lý Do",
            "Ngày Yêu Cầu", "Trạng Thái", "Mã Nhân Viên", "Ngày Hoàn Trả"
    };

    public DuyetHoanTraPanel() {
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

        add(UiComponents.tableScroll(table), BorderLayout.CENTER);
        add(buildBottomBar(), BorderLayout.SOUTH);
        loadSampleData();
    }

    private JPanel buildTopBar() {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setOpaque(false);

        JLabel title = new JLabel("Yêu Cầu Hoàn Tiền");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.TEXT_PRIMARY);

        JPanel filters = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        filters.setOpaque(false);

        filters.add(new JLabel("Trạng thái:"));
        JComboBox<String> statusFilter = new JComboBox<>(new String[]{"Tất cả", "Chờ duyệt", "Chấp thuận", "Từ chối"});
        statusFilter.setFont(UiTheme.BODY);
        filters.add(statusFilter);

        JTextField searchField = new JTextField(18);
        searchField.putClientProperty("JTextField.placeholderText", "Tìm kiếm...");
        searchField.setFont(UiTheme.BODY);
        searchField.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) {
                String kw = searchField.getText().trim();
                rowSorter.setRowFilter(kw.isEmpty() ? null : RowFilter.regexFilter("(?i)" + kw));
            }
        });
        filters.add(searchField);

        panel.add(title, BorderLayout.WEST);
        panel.add(filters, BorderLayout.EAST);
        return panel;
    }

    private JPanel buildBottomBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 6));
        bar.setOpaque(false);
        bar.add(UiComponents.primaryButton("Duyệt", UiTheme.SUCCESS));
        bar.add(UiComponents.primaryButton("Từ chối", UiTheme.DANGER));
        bar.add(UiComponents.ghostButton("Làm Mới"));
        return bar;
    }

    private void loadSampleData() {
        tableModel.addRow(new Object[]{1, 1, 1, "100,000 VNĐ", "Hoàn trả tiền thừa", "2026-05-21", "Chấp thuận", 4, "2026-05-22"});
        tableModel.addRow(new Object[]{2, 2, 2, "200,000 VNĐ", "Hoàn học phí do ...", "2026-05-21", "Chấp thuận", 4, "2026-05-22"});
        tableModel.addRow(new Object[]{3, 3, 3, "150,000 VNĐ", "Hoàn trả chính sá...", "2026-05-21", "Chấp thuận", 4, "2026-05-22"});
        tableModel.addRow(new Object[]{4, 4, 4, "300,000 VNĐ", "Hoàn phí bảo lưu ...", "2026-05-21", "Chấp thuận", 4, "2026-05-22"});
        tableModel.addRow(new Object[]{5, 5, 5, "200,000 VNĐ", "Hoàn trả sai sót tí...", "2026-05-21", "Chấp thuận", 4, "2026-05-22"});
    }
}
