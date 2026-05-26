package com.trungtam.ui.giaovien;

import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;

/**
 * Panel thống kê học tập của lớp phụ trách.
 * Gồm: 4 thẻ số liệu + bảng chi tiết từng học viên có lọc theo trạng thái.
 */
public class ThongKeLopPanel extends JPanel {

    private static final String[] LOP_LIST = { "Toán 12A", "Lý 11B", "Hóa 10C" };

    // {tên HV, tổng buổi, có mặt, vắng, trễ}
    private static final Object[][][] HV_STATS = {
            { { "Nguyễn Minh Anh", 10, 9, 0, 1 }, { "Trần Bảo Châu", 10, 10, 0, 0 },
                    { "Lê Văn Dũng", 10, 7, 2, 1 }, { "Phạm Thị Hoa", 10, 8, 1, 1 },
                    { "Đỗ Quốc Hùng", 10, 5, 4, 1 } },
            { { "Võ Thị Lan", 8, 8, 0, 0 }, { "Bùi Đức Mạnh", 8, 6, 1, 1 },
                    { "Đinh Thị Nga", 8, 7, 0, 1 } },
            { { "Hoàng Văn Phúc", 9, 9, 0, 0 }, { "Ngô Thị Quỳnh", 9, 7, 1, 1 },
                    { "Phan Minh Tâm", 9, 5, 3, 1 }, { "Lý Thị Uyên", 9, 8, 0, 1 } }
    };

    private final JComboBox<String> cboLop = new JComboBox<>(LOP_LIST);
    private final JComboBox<String> cboFilter = new JComboBox<>(
            new String[] { "Tất cả", "Vắng >= 2 buổi", "Trễ >= 1 buổi", "Đi đầy đủ" });
    private final JLabel[] statValues = { new JLabel(), new JLabel(), new JLabel(), new JLabel() };
    private final DefaultTableModel tableModel;
    private final JTable table;

    public ThongKeLopPanel() {
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M));
        setBackground(UiTheme.APP_BG);

        add(buildTopBar(), BorderLayout.NORTH);
        add(buildStatCards(), BorderLayout.SOUTH);

        tableModel = new DefaultTableModel(
                new String[] { "Học Viên", "Tổng Buổi", "Có Mặt", "Vắng", "Đi Trễ", "Chuyên Cần %" }, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table = new JTable(tableModel);
        UiComponents.styleTable(table);
        table.getColumnModel().getColumn(5).setCellRenderer(new ProgressCellRenderer());
        table.setDefaultRenderer(Object.class, new AlternatingRowRenderer());

        // Column widths
        int[] widths = { 180, 90, 80, 70, 80, 120 };
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
        // Học Viên: left; Tổng/Có Mặt/Vắng/Trễ: center; Chuyên Cần: center
        UiComponents.setColumnAlignments(table,
                SwingConstants.LEFT, SwingConstants.CENTER, SwingConstants.CENTER,
                SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.CENTER);

        JPanel tableWrapper = new JPanel(new BorderLayout(0, 6));
        tableWrapper.setOpaque(false);
        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        filterBar.setOpaque(false);
        JLabel lblFilter = new JLabel("Lọc:");
        lblFilter.setFont(UiTheme.BODY_B);
        cboFilter.setFont(UiTheme.BODY);
        filterBar.add(lblFilter);
        filterBar.add(cboFilter);
        tableWrapper.add(filterBar, BorderLayout.NORTH);
        tableWrapper.add(UiComponents.tableScroll(table), BorderLayout.CENTER);
        add(tableWrapper, BorderLayout.CENTER);

        cboLop.addActionListener(e -> refresh());
        cboFilter.addActionListener(e -> applyFilter());
        refresh();
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout(12, 0));
        bar.setOpaque(false);
        JLabel title = new JLabel("Thống Kê Lớp Học");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.TEXT_PRIMARY);
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);
        JLabel lblLop = new JLabel("Lớp:");
        lblLop.setFont(UiTheme.BODY);
        cboLop.setFont(UiTheme.BODY);
        right.add(lblLop);
        right.add(cboLop);
        bar.add(title, BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    private JPanel buildStatCards() {
        JPanel cards = new JPanel(new GridLayout(1, 4, 10, 0));
        cards.setOpaque(false);
        cards.setBorder(new EmptyBorder(8, 0, 0, 0));
        String[] titles = { "Tổng buổi học", "Chuyên cần trung bình", "Tổng vắng mặt", "Đi đầy đủ" };
        Color[] colors = { UiTheme.INFO, UiTheme.SUCCESS, UiTheme.DANGER, new Color(0x00897B) };
        for (int i = 0; i < 4; i++) {
            cards.add(UiComponents.statCard(titles[i], statValues[i], colors[i]));
        }
        return cards;
    }

    private void refresh() {
        int lopIdx = cboLop.getSelectedIndex();
        Object[][] data = HV_STATS[lopIdx];
        int totalBuoi = data.length > 0 ? (int) data[0][1] : 0;
        int totalVang = 0, daDayDu = 0;
        for (Object[] row : data) {
            totalVang += (int) row[3];
            if ((int) row[3] == 0)
                daDayDu++;
        }
        double tbPct = data.length > 0
                ? java.util.Arrays.stream(data).mapToDouble(r -> (int) r[2] * 100.0 / (int) r[1]).average().orElse(0)
                : 0;

        // KPI: count units (not currency)
        statValues[0].setText(totalBuoi + " buổi");
        statValues[1].setText(String.format("%.0f%%", tbPct));
        statValues[2].setText(totalVang + " buổi");
        statValues[3].setText(daDayDu + " HV");
        applyFilter();
    }

    private void applyFilter() {
        int lopIdx = cboLop.getSelectedIndex();
        Object[][] data = HV_STATS[lopIdx];
        String filter = (String) cboFilter.getSelectedItem();
        tableModel.setRowCount(0);
        for (Object[] row : data) {
            int vang = (int) row[3], tre = (int) row[4], total = (int) row[1], coMat = (int) row[2];
            boolean show = switch (filter) {
                case "Vắng >= 2 buổi" -> vang >= 2;
                case "Trễ >= 1 buổi" -> tre >= 1;
                case "Đi đầy đủ" -> vang == 0 && tre == 0;
                default -> true;
            };
            if (show)
                tableModel.addRow(new Object[] { row[0], total, coMat, vang, tre, coMat * 100 / total });
        }
    }

    // ── Progress bar in Chuyên Cần % column ──────────────────────────────────
    private static class ProgressCellRenderer extends JProgressBar implements TableCellRenderer {
        ProgressCellRenderer() {
            setStringPainted(true);
            setFont(UiTheme.BODY_B);
            setMaximum(100);
            setBorder(new EmptyBorder(4, 4, 4, 4));
        }

        @Override
        public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int r, int c) {
            int pct = val instanceof Integer v ? v : 0;
            setValue(pct);
            setString(pct + "%");
            setForeground(pct >= 80 ? UiTheme.SUCCESS : pct >= 60 ? UiTheme.WARNING : UiTheme.DANGER);
            return this;
        }
    }

    // ── Alternating row renderer ──────────────────────────────────────────────
    private static class AlternatingRowRenderer extends UiComponents.AltRowRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int r, int c) {
            super.getTableCellRendererComponent(t, val, sel, foc, r, c);
            // Column 5 is handled by ProgressCellRenderer; skip here
            return this;
        }
    }
}
