package com.trungtam.ui.hocvien;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;

/**
 * Panel đăng ký lớp học:
 * - Bên trái: danh sách lớp học
 * - Bên phải: danh sách học viên đã đăng ký lớp đó + trạng thái học phí
 */
public class DangKyLopPanel extends JPanel {

    private static final String[] LOP_LIST = {"Toán 12A", "Lý 11B", "Hóa 10C", "Văn 12A", "Anh 11B"};

    // {tên lớp, sĩ số tối đa}
    private static final Object[][] LOP_INFO = {
        {"Toán 12A", 25}, {"Lý 11B", 20}, {"Hóa 10C", 22}, {"Văn 12A", 18}, {"Anh 11B", 24}
    };

    // {tên lớp, mã HV, họ tên, ngày đăng ký, trạng thái học phí}
    private static final Object[][] DANG_KY_DATA = {
        {"Toán 12A", 1, "Nguyễn Minh Anh",  "01/09/2025", "Đã đóng"},
        {"Toán 12A", 4, "Phạm Thị Hoa",     "01/09/2025", "Đã đóng"},
        {"Toán 12A", 9, "Hoàng Văn Phúc",   "01/09/2025", "Chưa đóng"},
        {"Toán 12A", 2, "Trần Bảo Châu",    "02/09/2025", "Đã đóng"},
        {"Lý 11B",   7, "Bùi Đức Mạnh",     "01/09/2025", "Đã đóng"},
        {"Lý 11B",   6, "Võ Thị Lan",       "01/09/2025", "Quá hạn"},
        {"Hóa 10C",  10,"Ngô Thị Quỳnh",    "01/09/2025", "Đã đóng"},
        {"Hóa 10C",  4, "Phạm Thị Hoa",     "01/09/2025", "Chưa đóng"},
        {"Văn 12A",  2, "Trần Bảo Châu",    "01/09/2025", "Đã đóng"},
        {"Anh 11B",  6, "Võ Thị Lan",       "01/09/2025", "Đã đóng"},
        {"Anh 11B",  7, "Bùi Đức Mạnh",     "01/09/2025", "Chưa đóng"},
    };

    private final DefaultTableModel tableModel;
    private final JLabel lblLopInfo = new JLabel();

    public DangKyLopPanel() {
        setLayout(new BorderLayout(0, 10));
        setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel title = new JLabel("ĐĂNG KÝ LỚP HỌC");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(0x1A237E));
        add(title, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
            new String[]{"Mã HV", "Họ Tên", "Ngày Đăng Ký", "Học Phí"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(tableModel);
        table.setRowHeight(34);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setShowGrid(true);
        table.setGridColor(new Color(0xE0E0E0));
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        int[] widths = {70, 200, 130};
        for (int i = 0; i < widths.length; i++) {
            TableColumn col = table.getColumnModel().getColumn(i);
            col.setPreferredWidth(widths[i]);
            col.setMaxWidth(widths[i]);
            col.setResizable(false);
        }
        table.setDefaultRenderer(Object.class, new HocPhiRenderer());

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
            buildLopList(), buildRightPanel(table));
        split.setDividerLocation(200);
        split.setDividerSize(5);
        split.setBorder(null);
        add(split, BorderLayout.CENTER);
    }

    private JPanel buildLopList() {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(0xC5CAE9)), "Danh Sách Lớp"));

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String lop : LOP_LIST) listModel.addElement(lop);
        JList<String> lopList = new JList<>(listModel);
        lopList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lopList.setFixedCellHeight(42);
        lopList.setSelectedIndex(0);
        lopList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) loadLopData((String) lopList.getSelectedValue());
        });
        panel.add(new JScrollPane(lopList), BorderLayout.CENTER);
        loadLopData(LOP_LIST[0]);
        return panel;
    }

    private JPanel buildRightPanel(JTable table) {
        JPanel panel = new JPanel(new BorderLayout(0, 6));

        lblLopInfo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblLopInfo.setOpaque(true);
        lblLopInfo.setBackground(new Color(0xE8EAF6));
        lblLopInfo.setForeground(new Color(0x1A237E));
        lblLopInfo.setBorder(new EmptyBorder(6, 10, 6, 10));

        panel.add(lblLopInfo, BorderLayout.NORTH);
        panel.add(new JScrollPane(table,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
        return panel;
    }

    private void loadLopData(String tenLop) {
        // Tìm sĩ số tối đa
        int siSoMax = 0;
        for (Object[] info : LOP_INFO) {
            if (info[0].equals(tenLop)) { siSoMax = (int) info[1]; break; }
        }
        tableModel.setRowCount(0);
        int count = 0;
        for (Object[] row : DANG_KY_DATA) {
            if (row[0].equals(tenLop)) {
                tableModel.addRow(new Object[]{row[1], row[2], row[3], row[4]});
                count++;
            }
        }
        lblLopInfo.setText("  Lop: " + tenLop + "  |  Si so: " + count + "/" + siSoMax + " hoc vien");
    }

    // Tô màu cột Học Phí
    private static class HocPhiRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int r, int c) {
            super.getTableCellRendererComponent(t, val, sel, foc, r, c);
            if (!sel) {
                setBackground(r % 2 == 0 ? Color.WHITE : new Color(0xF0F4FF));
                if (c == 3) {
                    String s = String.valueOf(val);
                    setForeground("Đã đóng".equals(s)   ? new Color(0x2E7D32)
                                : "Quá hạn".equals(s)   ? new Color(0xC62828)
                                :                          new Color(0xE65100));
                } else {
                    setForeground(new Color(0x212121));
                }
            }
            return this;
        }
    }
}
