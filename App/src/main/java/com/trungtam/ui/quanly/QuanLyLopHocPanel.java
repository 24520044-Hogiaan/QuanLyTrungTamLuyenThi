package com.trungtam.ui.quanly;

import com.trungtam.controller.LopHocController;
import com.trungtam.model.LopHoc;
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

public class QuanLyLopHocPanel extends JPanel {

    private final DefaultTableModel tableModel;
    private final TableRowSorter<DefaultTableModel> rowSorter;
    private final LopHocController lopHocController = new LopHocController();

    private static final String[] COT = {
            "Mã Lớp", "Tên Lớp", "Ngày BĐ", "Ngày KT", "Mã KH", "Mã GV", "Sĩ Số", "Tần Suất", "Trạng Thái"
    };

    public QuanLyLopHocPanel() {
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
        JLabel title = new JLabel("Tra Cứu Lớp Học");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.QUANLY);
        JTextField searchField = new JTextField(22);
        searchField.putClientProperty("JTextField.placeholderText", "Tìm lớp học...");
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
        btnThem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Chức năng thêm lớp học đang phát triển.", "Thông báo", JOptionPane.INFORMATION_MESSAGE));

        JButton btnChiTiet = UiComponents.primaryButton("Chi tiết", UiTheme.PRIMARY);
        btnChiTiet.addActionListener(e -> JOptionPane.showMessageDialog(this, "Chức năng xem chi tiết lớp đang phát triển.", "Thông báo", JOptionPane.INFORMATION_MESSAGE));

        JButton btnXoa = UiComponents.primaryButton("Xóa", UiTheme.DANGER);
        btnXoa.addActionListener(e -> JOptionPane.showMessageDialog(this, "Chức năng xóa lớp học đang phát triển.", "Thông báo", JOptionPane.INFORMATION_MESSAGE));

        JButton refreshBtn = UiComponents.ghostButton("Làm mới");
        refreshBtn.addActionListener(e -> loadData());

        bar.add(btnThem);
        bar.add(btnChiTiet);
        bar.add(btnXoa);
        bar.add(refreshBtn);
        return bar;
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<LopHoc> list = lopHocController.layDanhSach();
        for (LopHoc lh : list) {
            tableModel.addRow(new Object[]{
                    lh.getMaLopHoc(), lh.getTenLop(), lh.getNgayBatDau(), lh.getNgayKetThuc(),
                    lh.getMaKhoaHoc(), lh.getMaGiaoVien(), lh.getSiSo(),
                    lh.getTanSuat(), lh.getTrangThai()
            });
        }
    }
}
