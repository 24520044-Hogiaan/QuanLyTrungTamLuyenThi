package com.trungtam.ui.quanly;

import com.trungtam.controller.GiaoVienController;
import com.trungtam.model.GiaoVien;
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

public class QuanLyGiaoVienPanel extends JPanel {

    private final DefaultTableModel tableModel;
    private final TableRowSorter<DefaultTableModel> rowSorter;
    private final GiaoVienController giaoVienController = new GiaoVienController();

    private static final String[] COT = {
            "Mã GV", "Mã Nhân Viên", "Họ Tên", "Bằng Cấp", "Trạng Thái"
    };

    public QuanLyGiaoVienPanel() {
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
        UiComponents.setColumnAlignments(table,
                SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.LEFT,
                SwingConstants.LEFT, SwingConstants.CENTER);
        add(UiComponents.tableScroll(table), BorderLayout.CENTER);
        add(buildBottomBar(), BorderLayout.SOUTH);
        loadData();
    }

    private JPanel buildTopBar() {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setOpaque(false);
        JLabel title = new JLabel("Danh Sách Giáo Viên");
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
        JButton refreshBtn = UiComponents.ghostButton("Làm Mới");
        refreshBtn.addActionListener(e -> loadData());
        bar.add(refreshBtn);
        bar.add(UiComponents.primaryButton("Phân Công", UiTheme.QUANLY));
        return bar;
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<GiaoVien> list = giaoVienController.layDanhSach();
        for (GiaoVien gv : list) {
            tableModel.addRow(new Object[]{
                    gv.getMaGiaoVien(), gv.getMaNhanVien(), gv.getHoTen(),
                    gv.getBangCap(), gv.getTrangThai()
            });
        }
    }
}
