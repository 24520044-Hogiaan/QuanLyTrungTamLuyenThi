package com.trungtam.ui.giaovien;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;

/**
 * Panel thống kê học tập của lớp phụ trách.
 * Gồm: 4 thẻ số liệu + bảng chi tiết từng học viên có lọc theo trạng thái.
 */
public class ThongKeLopPanel extends JPanel {

    private static final String[] LOP_LIST = {"Toán 12A", "Lý 11B", "Hóa 10C"};

    // {tên HV, tổng buổi, có mặt, vắng, trễ}
    private static final Object[][][] HV_STATS = {
        {{"Nguyễn Minh Anh", 10, 9, 0, 1}, {"Trần Bảo Châu", 10, 10, 0, 0},
         {"Lê Văn Dũng", 10, 7, 2, 1}, {"Phạm Thị Hoa", 10, 8, 1, 1}, {"Đỗ Quốc Hùng", 10, 5, 4, 1}},
        {{"Võ Thị Lan", 8, 8, 0, 0}, {"Bùi Đức Mạnh", 8, 6, 1, 1}, {"Đinh Thị Nga", 8, 7, 0, 1}},
        {{"Hoàng Văn Phúc", 9, 9, 0, 0}, {"Ngô Thị Quỳnh", 9, 7, 1, 1},
         {"Phan Minh Tâm", 9, 5, 3, 1}, {"Lý Thị Uyên", 9, 8, 0, 1}}
    };

    private final JComboBox<String> cboLop    = new JComboBox<>(LOP_LIST);
    private final JComboBox<String> cboFilter = new JComboBox<>(new String[]{"Tất cả", "Vắng >= 2 buổi", "Trễ >= 1 buổi", "Đi đầy đủ"});
    private final JLabel[] statValues         = {new JLabel(), new JLabel(), new JLabel(), new JLabel()};
    private final DefaultTableModel tableModel;
    private final JTable table;

    public ThongKeLopPanel() {
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(14, 14, 14, 14));

        add(buildTopBar(), BorderLayout.NORTH);
        add(buildStatCards(), BorderLayout.SOUTH);

        tableModel = new DefaultTableModel(
            new String[]{"Học Viên", "Tổng Buổi", "Có Mặt", "Vắng", "Đi Trễ", "Chuyên Cần %"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(32);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(0xEEEEEE));
        table.setFillsViewportHeight(true);
        table.getColumnModel().getColumn(5).setCellRenderer(new ProgressCellRenderer());
        table.setDefaultRenderer(Object.class, new AlternatingRowRenderer());

        JPanel tableWrapper = new JPanel(new BorderLayout(0, 6));
        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        filterBar.add(label("Lọc:", Font.BOLD, 13));
        filterBar.add(cboFilter);
        tableWrapper.add(filterBar, BorderLayout.NORTH);
        tableWrapper.add(new JScrollPane(table), BorderLayout.CENTER);
        add(tableWrapper, BorderLayout.CENTER);

        cboLop.addActionListener(e -> refresh());
        cboFilter.addActionListener(e -> applyFilter());
        refresh();
    }

    // ── Top bar ───────────────────────────────────────────────────────────
    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout(12, 0));
        JLabel title = new JLabel("Thống Kê Lớp Học");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.add(label("Lớp:", Font.PLAIN, 13));
        right.add(cboLop);
        bar.add(title, BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    // ── 4 thẻ số liệu ─────────────────────────────────────────────────────
    private JPanel buildStatCards() {
        JPanel cards = new JPanel(new GridLayout(1, 4, 10, 0));
        cards.setBorder(new EmptyBorder(8, 0, 0, 0));
        String[] titles = {"Tổng buổi học", "Chuyên cần trung bình", "Tổng vắng mặt", "Đi đầy đủ"};
        Color[]  colors = {new Color(0x1976D2), new Color(0x43A047), new Color(0xE53935), new Color(0x00897B)};
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
        card.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 18));
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblTitle.setForeground(new Color(0x757575));
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valueLabel.setForeground(color);
        card.add(lblTitle, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    // ── Logic ─────────────────────────────────────────────────────────────
    private void refresh() {
        int lopIdx      = cboLop.getSelectedIndex();
        Object[][] data = HV_STATS[lopIdx];
        int totalBuoi = data.length > 0 ? (int) data[0][1] : 0;
        int totalVang = 0, daDayDu = 0;
        for (Object[] row : data) {
            totalVang += (int) row[3];
            if ((int) row[3] == 0) daDayDu++;
        }
        double tbPct = data.length > 0
            ? java.util.Arrays.stream(data).mapToDouble(r -> (int) r[2] * 100.0 / (int) r[1]).average().orElse(0)
            : 0;

        statValues[0].setText(totalBuoi + " buổi");
        statValues[1].setText(String.format("%.0f%%", tbPct));
        statValues[2].setText(totalVang + " buổi");
        statValues[3].setText(daDayDu + " HV");
        applyFilter();
    }

    private void applyFilter() {
        int lopIdx      = cboLop.getSelectedIndex();
        Object[][] data = HV_STATS[lopIdx];
        String filter   = (String) cboFilter.getSelectedItem();
        tableModel.setRowCount(0);
        for (Object[] row : data) {
            int vang = (int) row[3], tre = (int) row[4], total = (int) row[1], coMat = (int) row[2];
            boolean show = switch (filter) {
                case "Vắng >= 2 buổi" -> vang >= 2;
                case "Trễ >= 1 buổi"  -> tre  >= 1;
                case "Đi đầy đủ"      -> vang == 0 && tre == 0;
                default               -> true;
            };
            if (show) tableModel.addRow(new Object[]{row[0], total, coMat, vang, tre, coMat * 100 / total});
        }
    }

    private static JLabel label(String text, int style, int size) {
        JLabel l = new JLabel(text); l.setFont(new Font("Segoe UI", style, size)); return l;
    }

    // ── Progress bar trong cột Chuyên Cần % ──────────────────────────────
    private static class ProgressCellRenderer extends JProgressBar implements TableCellRenderer {
        ProgressCellRenderer() {
            setStringPainted(true); setFont(new Font("Segoe UI", Font.BOLD, 12));
            setMaximum(100); setBorder(new EmptyBorder(4, 4, 4, 4));
        }
        @Override
        public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int r, int c) {
            int pct = val instanceof Integer v ? v : 0;
            setValue(pct); setString(pct + "%");
            setForeground(pct >= 80 ? new Color(0x2E7D32) : pct >= 60 ? new Color(0xE65100) : new Color(0xC62828));
            return this;
        }
    }

    // ── Màu xen kẽ dòng bảng ─────────────────────────────────────────────
    private static class AlternatingRowRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int r, int c) {
            super.getTableCellRendererComponent(t, val, sel, foc, r, c);
            if (!sel) setBackground(r % 2 == 0 ? Color.WHITE : new Color(0xF7F9FC));
            return this;
        }
    }
}
