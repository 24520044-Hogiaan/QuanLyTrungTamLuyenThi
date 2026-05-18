package com.trungtam.ui.giaovien;

import com.trungtam.model.GiaoVien;

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
 * Panel xem danh sách giáo viên (chỉ đọc – giáo viên không được thêm/sửa/xóa).
 * Hỗ trợ tìm kiếm nhanh theo tên hoặc chuyên môn.
 */
public class GiaoVienPanel extends JPanel {

    private final DefaultTableModel tableModel;
    private final JTable table;
    private final TableRowSorter<DefaultTableModel> rowSorter;
    private final List<GiaoVien> giaoVienList = new ArrayList<>();

    private static final String[] COT = {
        "Mã GV", "Họ Tên", "Email", "SĐT", "Chuyên Môn", "Bằng Cấp", "Trạng Thái"
    };

    public GiaoVienPanel() {
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(16, 16, 16, 16));

        add(buildTopBar(), BorderLayout.NORTH);

        tableModel = new DefaultTableModel(COT, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(36);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowGrid(true);
        table.setGridColor(new Color(0xE0E0E0));
        table.setFillsViewportHeight(true);
        table.setDefaultRenderer(Object.class, new AlternatingRenderer());

        rowSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(rowSorter);

        add(new JScrollPane(table), BorderLayout.CENTER);
        loadSampleData();
    }

    private JPanel buildTopBar() {
        JPanel panel = new JPanel(new BorderLayout(12, 0));

        JLabel title = new JLabel("Danh Sách Giáo Viên");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JTextField searchField = new JTextField(22);
        searchField.putClientProperty("JTextField.placeholderText", "Tìm theo tên, chuyên môn...");
        searchField.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) {
                String kw = searchField.getText().trim();
                rowSorter.setRowFilter(kw.isEmpty() ? null : RowFilter.regexFilter("(?i)" + kw));
            }
        });

        JLabel note = new JLabel("(Chỉ xem – liên hệ quản trị viên để thay đổi thông tin)");
        note.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        note.setForeground(Color.GRAY);

        JPanel right = new JPanel(new BorderLayout(0, 2));
        right.add(searchField, BorderLayout.CENTER);
        right.add(note, BorderLayout.SOUTH);

        panel.add(title, BorderLayout.WEST);
        panel.add(right, BorderLayout.EAST);
        return panel;
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (GiaoVien gv : giaoVienList) {
            tableModel.addRow(new Object[]{
                gv.getMaGiaoVien(), gv.getHoTen(), gv.getEmail(),
                gv.getSoDienThoai(), gv.getChuyenMon(), gv.getBangCap(), gv.getTrangThai()
            });
        }
    }

    private void loadSampleData() {
        giaoVienList.add(new GiaoVien(1, "Nguyễn Văn An",    "an.nv@trungtam.edu.vn",    "0901234567", "Toán học",    "Thạc sĩ",  "Đang làm",  LocalDate.of(1985, 3, 15)));
        giaoVienList.add(new GiaoVien(2, "Trần Thị Bình",    "binh.tt@trungtam.edu.vn",  "0912345678", "Vật lý",      "Tiến sĩ",  "Đang làm",  LocalDate.of(1979, 7, 22)));
        giaoVienList.add(new GiaoVien(3, "Lê Hoàng Cường",   "cuong.lh@trungtam.edu.vn", "0923456789", "Hóa học",     "Cử nhân",  "Đang làm",  LocalDate.of(1990, 11, 5)));
        giaoVienList.add(new GiaoVien(4, "Phạm Thị Dung",    "dung.pt@trungtam.edu.vn",  "0934567890", "Tiếng Anh",   "Thạc sĩ",  "Nghỉ việc", LocalDate.of(1988, 6, 18)));
        refreshTable();
    }

    // Màu xen kẽ dòng; cột Trạng Thái tô màu theo giá trị
    private static class AlternatingRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int r, int c) {
            super.getTableCellRendererComponent(t, val, sel, foc, r, c);
            if (!sel) {
                setBackground(r % 2 == 0 ? Color.WHITE : new Color(0xF3F6FB));
                if (c == 6) {
                    // Cột Trạng Thái tô màu riêng
                    boolean active = "Đang làm".equals(String.valueOf(val));
                    setForeground(active ? new Color(0x2E7D32) : new Color(0xC62828));
                } else {
                    setForeground(new Color(0x212121));
                }
            }
            return this;
        }
    }

}
