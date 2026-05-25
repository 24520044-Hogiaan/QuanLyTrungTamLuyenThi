package com.trungtam.ui.giaovien;

import com.trungtam.model.GiaoVien;
import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

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
 * Panel xem danh sách giáo viên (chỉ đọc).
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
        setBorder(new EmptyBorder(UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M));
        setBackground(UiTheme.APP_BG);

        add(buildTopBar(), BorderLayout.NORTH);

        tableModel = new DefaultTableModel(COT, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table = new JTable(tableModel);
        UiComponents.styleTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultRenderer(Object.class, new StatusRenderer());

        rowSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(rowSorter);

        // Column widths
        int[] widths = { 60, 180, 200, 110, 130, 90, 100 };
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        // Mã GV: center; Họ Tên/Email/SĐT/Chuyên Môn/Bằng Cấp: left; Trạng Thái: center
        UiComponents.setColumnAlignments(table,
                SwingConstants.CENTER, SwingConstants.LEFT, SwingConstants.LEFT,
                SwingConstants.CENTER, SwingConstants.LEFT, SwingConstants.CENTER,
                SwingConstants.CENTER);
        add(UiComponents.tableScroll(table), BorderLayout.CENTER);
        loadSampleData();
    }

    private JPanel buildTopBar() {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setOpaque(false);

        JLabel title = new JLabel("Danh Sách Giáo Viên");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.TEXT_PRIMARY);

        JTextField searchField = new JTextField(22);
        searchField.putClientProperty("JTextField.placeholderText", "Tìm theo tên, chuyên môn...");
        searchField.setFont(UiTheme.BODY);
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String kw = searchField.getText().trim();
                rowSorter.setRowFilter(kw.isEmpty() ? null : RowFilter.regexFilter("(?i)" + kw));
            }
        });

        JLabel note = new JLabel("(Chỉ xem – liên hệ quản trị viên để thay đổi thông tin)");
        note.setFont(UiTheme.CAPTION_I);
        note.setForeground(UiTheme.TEXT_MUTED);

        JPanel right = new JPanel(new BorderLayout(0, 4));
        right.setOpaque(false);
        right.add(searchField, BorderLayout.CENTER);
        right.add(note, BorderLayout.SOUTH);

        panel.add(title, BorderLayout.WEST);
        panel.add(right, BorderLayout.EAST);
        return panel;
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (GiaoVien gv : giaoVienList) {
            tableModel.addRow(new Object[] {
                    gv.getMaGiaoVien(), gv.getHoTen(), gv.getEmail(),
                    gv.getSoDienThoai(), gv.getChuyenMon(), gv.getBangCap(), gv.getTrangThai()
            });
        }
    }

    private void loadSampleData() {
        giaoVienList.add(new GiaoVien(1, "Nguyễn Văn An", "an.nv@trungtam.edu.vn", "0901234567", "Toán học", "Thạc sĩ",
                "Đang làm", LocalDate.of(1985, 3, 15)));
        giaoVienList.add(new GiaoVien(2, "Trần Thị Bình", "binh.tt@trungtam.edu.vn", "0912345678", "Vật lý", "Tiến sĩ",
                "Đang làm", LocalDate.of(1979, 7, 22)));
        giaoVienList.add(new GiaoVien(3, "Lê Hoàng Cường", "cuong.lh@trungtam.edu.vn", "0923456789", "Hóa học",
                "Cử nhân", "Đang làm", LocalDate.of(1990, 11, 5)));
        giaoVienList.add(new GiaoVien(4, "Phạm Thị Dung", "dung.pt@trungtam.edu.vn", "0934567890", "Tiếng Anh",
                "Thạc sĩ", "Nghỉ việc", LocalDate.of(1988, 6, 18)));
        refreshTable();
    }

    /** Alternating rows + colour-coded Trạng Thái column */
    private static class StatusRenderer extends UiComponents.AltRowRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable t, Object val, boolean sel, boolean foc, int r, int c) {
            super.getTableCellRendererComponent(t, val, sel, foc, r, c);
            if (!sel && c == 6) {
                boolean active = "Đang làm".equals(String.valueOf(val));
                setForeground(active ? UiTheme.SUCCESS : UiTheme.DANGER);
                setFont(UiTheme.BODY_B);
            } else if (!sel) {
                setFont(UiTheme.BODY);
            }
            return this;
        }
    }
}
