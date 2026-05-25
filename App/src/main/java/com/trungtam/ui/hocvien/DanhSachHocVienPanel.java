package com.trungtam.ui.hocvien;

import com.trungtam.controller.HocVienController;
import com.trungtam.model.HocVien;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Danh sách học viên – chỉ xem, có tìm kiếm và lọc theo giới tính / trạng thái.
 */
public class DanhSachHocVienPanel extends JPanel {

    private final HocVienController hocVienController = new HocVienController();

    private static final String[] COT = {
        "Mã HV", "Họ Tên", "Email", "SĐT", "Ngày Sinh", "Giới Tính", "Địa Chỉ"
    };
    private static final String[] GENDER_LIST = {"Tất cả", "Nam", "Nu"};

    private final DefaultTableModel tableModel;
    private final TableRowSorter<DefaultTableModel> rowSorter;
    private final List<HocVien> hocVienList = new ArrayList<>();
    private final JTextField txtSearch = new JTextField(20);
    private final JComboBox<String> cboGender = new JComboBox<>(GENDER_LIST);

    public DanhSachHocVienPanel() {
        setLayout(new BorderLayout(0, 10));
        setBorder(new EmptyBorder(16, 16, 16, 16));

        add(buildTopBar(), BorderLayout.NORTH);

        tableModel = new DefaultTableModel(COT, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        table.setRowHeight(34);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowGrid(true);
        table.setGridColor(new Color(0xE0E0E0));
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        int[] widths = {60, 170, 210, 110, 100, 80};
        for (int i = 0; i < widths.length; i++) {
            TableColumn col = table.getColumnModel().getColumn(i);
            col.setPreferredWidth(widths[i]);
            if (i < widths.length - 1) { col.setMaxWidth(widths[i]); col.setResizable(false); }
        }

        table.setDefaultRenderer(Object.class, new AltRowRenderer());

        rowSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(rowSorter);

        add(new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);

        add(buildNoteBar(), BorderLayout.SOUTH);
        loadData();
    }

    private JPanel buildTopBar() {
        JPanel top = new JPanel(new BorderLayout(12, 6));

        JLabel title = new JLabel("DANH SÁCH HỌC VIÊN");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(0x1A237E));

        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        txtSearch.putClientProperty("JTextField.placeholderText", "Tìm theo tên, email...");
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) { applyFilter(); }
        });
        cboGender.addActionListener(e -> applyFilter());

        filterBar.add(new JLabel("Giới tính:"));
        filterBar.add(cboGender);
        filterBar.add(txtSearch);

        top.add(title, BorderLayout.WEST);
        top.add(filterBar, BorderLayout.EAST);
        return top;
    }

    private JLabel buildNoteBar() {
        JLabel note = new JLabel("  (*) Chỉ xem – liên hệ quản trị viên để thêm, sửa, xóa học viên.");
        note.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        note.setForeground(Color.GRAY);
        return note;
    }

    private void applyFilter() {
        String keyword = txtSearch.getText().trim();
        String gender  = (String) cboGender.getSelectedItem();

        RowFilter<DefaultTableModel, Object> kw = keyword.isEmpty() ? null : RowFilter.regexFilter("(?i)" + keyword);
        RowFilter<DefaultTableModel, Object> gf = "Tất cả".equals(gender) ? null : RowFilter.regexFilter("(?i)" + gender, 5);

        List<RowFilter<DefaultTableModel, Object>> filters = new ArrayList<>();
        if (kw != null) filters.add(kw);
        if (gf != null) filters.add(gf);
        rowSorter.setRowFilter(filters.isEmpty() ? null : RowFilter.andFilter(filters));
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (HocVien hv : hocVienList) {
            tableModel.addRow(new Object[]{
                hv.getMaHocVien(), hv.getHoTen(), hv.getEmail(), hv.getSoDienThoai(),
                hv.getNgaySinh(), hv.getGioiTinh(), hv.getDiaChi()
            });
        }
    }

    private void loadData() {
        hocVienList.clear();
        hocVienList.addAll(hocVienController.layDanhSach());
        refreshTable();
    }

    private static class AltRowRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int r, int c) {
            super.getTableCellRendererComponent(t, val, sel, foc, r, c);
            if (!sel) {
                setBackground(r % 2 == 0 ? Color.WHITE : new Color(0xF0F4FF));
                setForeground(new Color(0x212121));
            }
            return this;
        }
    }
}
