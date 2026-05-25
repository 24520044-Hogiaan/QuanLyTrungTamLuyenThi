package com.trungtam.ui.ketoan;

import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ThuHocPhiPanel extends JPanel {

    private final DefaultTableModel tableModel;
    private final TableRowSorter<DefaultTableModel> rowSorter;

    private static final String[] COT = {
            "Mã HĐ", "Học Viên", "Lớp Học", "Học Phí", "Hình Thức TT", "Phương Thức", "Trạng Thái"
    };

    public ThuHocPhiPanel() {
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
        loadSampleData();
    }

    private JPanel buildTopBar() {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setOpaque(false);

        JLabel title = new JLabel("Thu Học Phí & Lập Hóa Đơn");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.TEXT_PRIMARY);

        JTextField searchField = new JTextField(22);
        searchField.putClientProperty("JTextField.placeholderText", "Tìm theo tên học viên, lớp...");
        searchField.setFont(UiTheme.BODY);
        searchField.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) {
                String kw = searchField.getText().trim();
                rowSorter.setRowFilter(kw.isEmpty() ? null : RowFilter.regexFilter("(?i)" + kw));
            }
        });

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.setOpaque(false);

        JButton collectBtn = UiComponents.primaryButton("Thu phí", UiTheme.KETOAN);
        collectBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Chức năng thu phí — đang phát triển."));
        buttons.add(collectBtn);

        JButton printBtn = UiComponents.ghostButton("In hóa đơn");
        printBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Chức năng in hóa đơn — đang phát triển."));
        buttons.add(printBtn);

        JPanel rightPanel = new JPanel(new BorderLayout(0, 6));
        rightPanel.setOpaque(false);
        rightPanel.add(searchField, BorderLayout.NORTH);
        rightPanel.add(buttons, BorderLayout.SOUTH);

        panel.add(title, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.EAST);
        return panel;
    }

    private void loadSampleData() {
        tableModel.addRow(new Object[]{1, "Nguyễn An", "Lớp Toán 10A1", "1,500,000", "Toàn khóa", "Tiền mặt", "Đã thanh toán"});
        tableModel.addRow(new Object[]{2, "Trần Bình", "Lớp Toán 10A1", "1,500,000", "Toàn khóa", "Tiền mặt", "Đã thanh toán"});
        tableModel.addRow(new Object[]{3, "Phan Cường", "Lớp Toán 10A1", "1,500,000", "Toàn khóa", "Tiền mặt", "Đã thanh toán"});
        tableModel.addRow(new Object[]{4, "Lê Dung", "Lớp Văn 11B1", "1,500,000", "Toàn khóa", "Tiền mặt", "Đã thanh toán"});
    }
}
