package com.trungtam.ui.quanly;

import com.trungtam.controller.KhoaHocController;
import com.trungtam.model.KhoaHoc;
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

public class QuanLyKhoaHocPanel extends JPanel {

    private final DefaultTableModel tableModel;
    private final TableRowSorter<DefaultTableModel> rowSorter;
    private final KhoaHocController khoaHocController = new KhoaHocController();

    private static final String[] COT = {
            "Mã KH", "Tên Khóa Học", "Mô Tả", "Học Phí/Tháng", "Học Phí Toàn Khóa", "Mã BM", "Cấp Độ"
    };

    public QuanLyKhoaHocPanel() {
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
        loadData();
    }

    private JPanel buildTopBar() {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setOpaque(false);
        JLabel title = new JLabel("Danh Sách Khóa Học");
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
        btnThem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Chức năng thêm khóa học đang phát triển.", "Thông báo", JOptionPane.INFORMATION_MESSAGE));

        JButton btnSua = UiComponents.primaryButton("Sửa", UiTheme.PRIMARY);
        btnSua.addActionListener(e -> JOptionPane.showMessageDialog(this, "Chức năng sửa khóa học đang phát triển.", "Thông báo", JOptionPane.INFORMATION_MESSAGE));

        JButton btnXoa = UiComponents.primaryButton("Xóa", UiTheme.DANGER);
        btnXoa.addActionListener(e -> JOptionPane.showMessageDialog(this, "Chức năng xóa khóa học đang phát triển.", "Thông báo", JOptionPane.INFORMATION_MESSAGE));

        JButton refreshBtn = UiComponents.ghostButton("Làm Mới");
        refreshBtn.addActionListener(e -> loadData());

        bar.add(btnThem);
        bar.add(btnSua);
        bar.add(btnXoa);
        bar.add(refreshBtn);
        return bar;
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<KhoaHoc> list = khoaHocController.layDanhSach();
        for (KhoaHoc kh : list) {
            tableModel.addRow(new Object[]{
                    kh.getMaKhoaHoc(), kh.getTenKhoaHoc(), kh.getMoTa(),
                    String.format("%,.0f", kh.getHocPhiThang()),
                    String.format("%,.0f", kh.getHocPhiToanKhoa()),
                    kh.getMaBoMon(), kh.getCapDo()
            });
        }
    }
}
