package com.trungtam.ui.hocvien;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;

/**
 * Panel học phí: 4 thẻ tóm tắt + bảng chi tiết trạng thái học phí từng học viên.
 */
public class HocPhiPanel extends JPanel {

    // {mã HV, họ tên, khóa học, học phí (đ), hạn đóng, trạng thái, ngày TT}
    private static final Object[][] HP_DATA = {
        {1, "Nguyễn Minh Anh",  "Toán 12A", 3_500_000, "30/09/2025", "Đã đóng",    "01/09/2025"},
        {2, "Trần Bảo Châu",    "Toán 12A", 3_500_000, "30/09/2025", "Đã đóng",    "05/09/2025"},
        {2, "Trần Bảo Châu",    "Văn 12A",  3_000_000, "30/09/2025", "Đã đóng",    "05/09/2025"},
        {3, "Lê Văn Dũng",      "Toán 12A", 3_500_000, "30/09/2025", "Bảo lưu",   "-"},
        {4, "Phạm Thị Hoa",     "Hóa 10C",  3_200_000, "30/09/2025", "Chưa đóng", "-"},
        {5, "Đỗ Quốc Hùng",     "Toán 12A", 3_500_000, "30/09/2025", "Đã đóng",    "02/09/2025"},
        {6, "Võ Thị Lan",       "Lý 11B",   3_200_000, "30/09/2025", "Quá hạn",   "-"},
        {6, "Võ Thị Lan",       "Anh 11B",  2_800_000, "30/09/2025", "Đã đóng",    "10/09/2025"},
        {7, "Bùi Đức Mạnh",     "Lý 11B",   3_200_000, "30/09/2025", "Chưa đóng", "-"},
        {7, "Bùi Đức Mạnh",     "Anh 11B",  2_800_000, "30/09/2025", "Chưa đóng", "-"},
        {9, "Hoàng Văn Phúc",   "Toán 12A", 3_500_000, "30/09/2025", "Đã đóng",    "01/09/2025"},
        {10,"Ngô Thị Quỳnh",    "Hóa 10C",  3_200_000, "30/09/2025", "Đã đóng",    "03/09/2025"},
    };

    private final JLabel[] statValues = {new JLabel(), new JLabel(), new JLabel(), new JLabel()};
    private final DefaultTableModel tableModel;

    public HocPhiPanel() {
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(16, 16, 16, 16));

        JPanel header = new JPanel(new BorderLayout());
        JLabel title = new JLabel("QUẢN LÝ HỌC PHÍ");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(0x1A237E));
        header.add(title, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        add(buildStatCards(), BorderLayout.AFTER_LINE_ENDS); // sẽ thêm theo flow bên dưới

        tableModel = new DefaultTableModel(
            new String[]{"Mã HV", "Họ Tên", "Khóa Học", "Học Phí (đ)", "Hạn Đóng", "Trạng Thái", "Ngày TT"}, 0) {
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
        int[] w = {60, 170, 120, 130, 110, 110};
        for (int i = 0; i < w.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(w[i]);
            table.getColumnModel().getColumn(i).setMaxWidth(w[i]);
            table.getColumnModel().getColumn(i).setResizable(false);
        }
        table.setDefaultRenderer(Object.class, new HocPhiStatusRenderer());

        // Layout: stat cards trên, bảng dưới
        JPanel content = new JPanel(new BorderLayout(0, 10));
        content.add(buildStatCards(), BorderLayout.NORTH);
        content.add(new JScrollPane(table,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
        add(content, BorderLayout.CENTER);

        loadData();
    }

    private JPanel buildStatCards() {
        JPanel cards = new JPanel(new GridLayout(1, 4, 10, 0));
        cards.setBorder(new EmptyBorder(4, 0, 4, 0));
        String[] titles = {"Tổng khoản phí", "Đã đóng", "Chưa / Quá hạn", "Tổng thu (đ)"};
        Color[]  colors = {new Color(0x1565C0), new Color(0x2E7D32), new Color(0xC62828), new Color(0x00838F)};
        for (int i = 0; i < 4; i++) {
            cards.add(buildCard(titles[i], statValues[i], colors[i]));
        }
        return cards;
    }

    private JPanel buildCard(String title, JLabel valueLabel, Color color) {
        JPanel card = new JPanel(new BorderLayout(0, 4));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.brighter(), 1),
            new EmptyBorder(10, 14, 10, 14)));
        card.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 20));
        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(new Color(0x757575));
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valueLabel.setForeground(color);
        card.add(lbl, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private void loadData() {
        tableModel.setRowCount(0);
        int total = HP_DATA.length, daDong = 0, chuaDong = 0;
        long tongThu = 0;
        for (Object[] row : HP_DATA) {
            tableModel.addRow(row);
            String status = (String) row[5];
            if ("Đã đóng".equals(status)) { daDong++; tongThu += (int) row[3]; }
            else if (!"Bảo lưu".equals(status)) chuaDong++;
        }
        statValues[0].setText(total + " khoản");
        statValues[1].setText(daDong + " khoản");
        statValues[2].setText(chuaDong + " khoản");
        statValues[3].setText(String.format("%,d", tongThu));
    }

    private static class HocPhiStatusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int r, int c) {
            super.getTableCellRendererComponent(t, val, sel, foc, r, c);
            if (!sel) {
                setBackground(r % 2 == 0 ? Color.WHITE : new Color(0xFFF8F0));
                if (c == 5) {
                    String s = String.valueOf(val);
                    setForeground("Đã đóng".equals(s)   ? new Color(0x2E7D32)
                                : "Quá hạn".equals(s)   ? new Color(0xC62828)
                                : "Bảo lưu".equals(s)   ? new Color(0x616161)
                                :                          new Color(0xE65100));
                } else {
                    setForeground(new Color(0x212121));
                }
            }
            return this;
        }
    }
}
