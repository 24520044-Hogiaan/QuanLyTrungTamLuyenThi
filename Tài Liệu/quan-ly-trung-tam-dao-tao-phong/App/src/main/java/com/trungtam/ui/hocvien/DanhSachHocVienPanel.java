package com.trungtam.ui.hocvien;

import com.trungtam.model.HocVien;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Danh sách học viên – chỉ xem, có tìm kiếm và lọc theo khối thi / trạng thái.
 */
public class DanhSachHocVienPanel extends JPanel {

    private static final String[] COT = {
        "Mã HV", "Họ Tên", "Email", "SĐT", "Ngày Sinh", "Khối Thi", "Trạng Thái", "Ngày ĐK"
    };
    private static final String[] KHOI_LIST = {"Tất cả", "Khối A", "Khối B", "Khối D"};
    private static final String[] STATUS_LIST = {"Tất cả", "Đang học", "Bảo lưu", "Đã tốt nghiệp"};

    private final DefaultTableModel tableModel;
    private final TableRowSorter<DefaultTableModel> rowSorter;
    private final List<HocVien> hocVienList = new ArrayList<>();
    private final JTextField txtSearch = new JTextField(20);
    private final JComboBox<String> cboKhoi   = new JComboBox<>(KHOI_LIST);
    private final JComboBox<String> cboStatus = new JComboBox<>(STATUS_LIST);

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

        // Cột cố định độ rộng
        int[] widths = {60, 170, 210, 110, 100, 80, 100};
        for (int i = 0; i < widths.length; i++) {
            TableColumn col = table.getColumnModel().getColumn(i);
            col.setPreferredWidth(widths[i]);
            if (i < widths.length - 1) { col.setMaxWidth(widths[i]); col.setResizable(false); }
        }

        table.setDefaultRenderer(Object.class, new StatusAwareRenderer());

        rowSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(rowSorter);

        add(new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);

        add(buildNoteBar(), BorderLayout.SOUTH);
        loadSampleData();
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
        cboKhoi.addActionListener(e -> applyFilter());
        cboStatus.addActionListener(e -> applyFilter());

        filterBar.add(new JLabel("Khối:"));
        filterBar.add(cboKhoi);
        filterBar.add(new JLabel("Trạng thái:"));
        filterBar.add(cboStatus);
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
        String khoi    = (String) cboKhoi.getSelectedItem();
        String status  = (String) cboStatus.getSelectedItem();

        RowFilter<DefaultTableModel, Object> kw  = keyword.isEmpty() ? null : RowFilter.regexFilter("(?i)" + keyword);
        RowFilter<DefaultTableModel, Object> kf  = "Tất cả".equals(khoi)   ? null : RowFilter.regexFilter("(?i)" + khoi, 5);
        RowFilter<DefaultTableModel, Object> sf  = "Tất cả".equals(status) ? null : RowFilter.regexFilter("(?i)" + status, 6);

        List<RowFilter<DefaultTableModel, Object>> filters = new ArrayList<>();
        if (kw != null) filters.add(kw);
        if (kf != null) filters.add(kf);
        if (sf != null) filters.add(sf);
        rowSorter.setRowFilter(filters.isEmpty() ? null : RowFilter.andFilter(filters));
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (HocVien hv : hocVienList) {
            tableModel.addRow(new Object[]{
                hv.getMaHocVien(), hv.getHoTen(), hv.getEmail(), hv.getSoDienThoai(),
                hv.getNgaySinh(), hv.getKhoiThi(), hv.getTrangThai(), hv.getNgayDangKy()
            });
        }
    }

    private void loadSampleData() {
        hocVienList.add(new HocVien(1,  "Nguyễn Minh Anh",  "anh.nm@mail.com",    "0901111111", "Khối A", "Đang học",       LocalDate.of(2007, 3, 10), LocalDate.of(2025, 9, 1)));
        hocVienList.add(new HocVien(2,  "Trần Bảo Châu",    "chau.tb@mail.com",   "0902222222", "Khối D", "Đang học",       LocalDate.of(2007, 6, 22), LocalDate.of(2025, 9, 1)));
        hocVienList.add(new HocVien(3,  "Lê Văn Dũng",      "dung.lv@mail.com",   "0903333333", "Khối A", "Bảo lưu",        LocalDate.of(2006, 11, 5), LocalDate.of(2024, 9, 1)));
        hocVienList.add(new HocVien(4,  "Phạm Thị Hoa",     "hoa.pt@mail.com",    "0904444444", "Khối B", "Đang học",       LocalDate.of(2007, 1, 18), LocalDate.of(2025, 9, 1)));
        hocVienList.add(new HocVien(5,  "Đỗ Quốc Hùng",     "hung.dq@mail.com",   "0905555555", "Khối A", "Đã tốt nghiệp", LocalDate.of(2005, 8, 30), LocalDate.of(2023, 9, 1)));
        hocVienList.add(new HocVien(6,  "Võ Thị Lan",       "lan.vt@mail.com",    "0906666666", "Khối D", "Đang học",       LocalDate.of(2007, 4, 15), LocalDate.of(2025, 9, 1)));
        hocVienList.add(new HocVien(7,  "Bùi Đức Mạnh",     "manh.bd@mail.com",   "0907777777", "Khối B", "Đang học",       LocalDate.of(2007, 9, 20), LocalDate.of(2025, 9, 1)));
        hocVienList.add(new HocVien(8,  "Đinh Thị Nga",     "nga.dt@mail.com",    "0908888888", "Khối D", "Bảo lưu",        LocalDate.of(2006, 12, 3), LocalDate.of(2024, 9, 1)));
        hocVienList.add(new HocVien(9,  "Hoàng Văn Phúc",   "phuc.hv@mail.com",   "0909999999", "Khối A", "Đang học",       LocalDate.of(2007, 2, 7),  LocalDate.of(2025, 9, 1)));
        hocVienList.add(new HocVien(10, "Ngô Thị Quỳnh",    "quynh.nt@mail.com",  "0910111222", "Khối B", "Đang học",       LocalDate.of(2007, 7, 14), LocalDate.of(2025, 9, 1)));
        refreshTable();
    }

    // Màu xen kẽ dòng + màu cột Trạng Thái
    private static class StatusAwareRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int r, int c) {
            super.getTableCellRendererComponent(t, val, sel, foc, r, c);
            if (!sel) {
                setBackground(r % 2 == 0 ? Color.WHITE : new Color(0xF0F4FF));
                if (c == 6) {
                    String s = String.valueOf(val);
                    setForeground("Đang học".equals(s) ? new Color(0x2E7D32)
                                : "Bảo lưu".equals(s)  ? new Color(0xE65100)
                                :                         new Color(0x616161));
                } else {
                    setForeground(new Color(0x212121));
                }
            }
            return this;
        }
    }
}
