package com.trungtam.ui.hocvien;

import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;

/**
 * Panel học phí: 4 thẻ tóm tắt + bảng chi tiết trạng thái học phí từng học
 * viên.
 */
public class HocPhiPanel extends JPanel {

    // {mã HV, họ tên, khóa học, học phí (đ), hạn đóng, trạng thái, ngày TT}
    private static final Object[][] HP_DATA = {
            { 1, "Nguyễn Minh Anh", "Toán 12A", 3_500_000, "30/09/2025", "Đã đóng", "01/09/2025" },
            { 2, "Trần Bảo Châu", "Toán 12A", 3_500_000, "30/09/2025", "Đã đóng", "05/09/2025" },
            { 2, "Trần Bảo Châu", "Văn 12A", 3_000_000, "30/09/2025", "Đã đóng", "05/09/2025" },
            { 3, "Lê Văn Dũng", "Toán 12A", 3_500_000, "30/09/2025", "Bảo lưu", "-" },
            { 4, "Phạm Thị Hoa", "Hóa 10C", 3_200_000, "30/09/2025", "Chưa đóng", "-" },
            { 5, "Đỗ Quốc Hùng", "Toán 12A", 3_500_000, "30/09/2025", "Đã đóng", "02/09/2025" },
            { 6, "Võ Thị Lan", "Lý 11B", 3_200_000, "30/09/2025", "Quá hạn", "-" },
            { 6, "Võ Thị Lan", "Anh 11B", 2_800_000, "30/09/2025", "Đã đóng", "10/09/2025" },
            { 7, "Bùi Đức Mạnh", "Lý 11B", 3_200_000, "30/09/2025", "Chưa đóng", "-" },
            { 7, "Bùi Đức Mạnh", "Anh 11B", 2_800_000, "30/09/2025", "Chưa đóng", "-" },
            { 9, "Hoàng Văn Phúc", "Toán 12A", 3_500_000, "30/09/2025", "Đã đóng", "01/09/2025" },
            { 10, "Ngô Thị Quỳnh", "Hóa 10C", 3_200_000, "30/09/2025", "Đã đóng", "03/09/2025" },
    };

    private final JLabel[] statValues = { new JLabel(), new JLabel(), new JLabel(), new JLabel() };
    private final DefaultTableModel tableModel;

    public HocPhiPanel() {
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M));
        setBackground(UiTheme.APP_BG);

        JLabel title = new JLabel("QUẢN LÝ HỌC PHÍ");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.PRIMARY);
        add(title, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new String[] { "Mã HV", "Họ Tên", "Khóa Học", "Học Phí (đ)", "Hạn Đóng", "Trạng Thái", "Ngày TT" },
                0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        UiComponents.styleTable(table);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        int[] w = { 60, 170, 120, 130, 110, 110 };
        for (int i = 0; i < w.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(w[i]);
            table.getColumnModel().getColumn(i).setMaxWidth(w[i]);
            table.getColumnModel().getColumn(i).setResizable(false);
        }
        table.setDefaultRenderer(Object.class, new HocPhiStatusRenderer());
        // Mã: center; Họ Tên/Khóa: left; Học Phí/Hạn: center; Trạng Thái: center; Ngày
        // TT: center
        UiComponents.setColumnAlignments(table,
                SwingConstants.CENTER, SwingConstants.LEFT, SwingConstants.LEFT,
                SwingConstants.RIGHT, SwingConstants.CENTER,
                SwingConstants.CENTER, SwingConstants.CENTER);

        JPanel content = new JPanel(new BorderLayout(0, 10));
        content.setOpaque(false);
        content.add(buildStatCards(), BorderLayout.NORTH);
        content.add(UiComponents.tableScroll(table), BorderLayout.CENTER);
        add(content, BorderLayout.CENTER);

        loadData();
    }

    private JPanel buildStatCards() {
        JPanel cards = new JPanel(new GridLayout(1, 4, 10, 0));
        cards.setOpaque(false);
        cards.setBorder(new EmptyBorder(4, 0, 4, 0));
        // KPI titles: "Tổng thu" uses currency; others use count units
        String[] titles = { "Tổng khoản phí", "Đã đóng", "Chưa / Quá hạn", "Tổng thu (đ)" };
        Color[] colors = { UiTheme.INFO, UiTheme.SUCCESS, UiTheme.DANGER, new Color(0x00838F) };
        for (int i = 0; i < 4; i++) {
            cards.add(UiComponents.statCard(titles[i], statValues[i], colors[i]));
        }
        return cards;
    }

    private void loadData() {
        tableModel.setRowCount(0);
        int total = HP_DATA.length, daDong = 0, chuaDong = 0;
        long tongThu = 0;
        for (Object[] row : HP_DATA) {
            tableModel.addRow(row);
            String status = (String) row[5];
            if ("Đã đóng".equals(status)) {
                daDong++;
                tongThu += (int) row[3];
            } else if (!"Bảo lưu".equals(status)) {
                chuaDong++;
            }
        }
        // Count KPIs use count units; only revenue uses currency
        statValues[0].setText(total + " khoản");
        statValues[1].setText(daDong + " khoản");
        statValues[2].setText(chuaDong + " khoản");
        statValues[3].setText(String.format("%,d đ", tongThu));
    }

    private static class HocPhiStatusRenderer extends UiComponents.AltRowRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable t, Object val, boolean sel, boolean foc, int r, int c) {
            super.getTableCellRendererComponent(t, val, sel, foc, r, c);
            if (!sel && c == 5) {
                String s = String.valueOf(val);
                setForeground("Đã đóng".equals(s) ? UiTheme.SUCCESS
                        : "Quá hạn".equals(s) ? UiTheme.DANGER
                                : "Bảo lưu".equals(s) ? UiTheme.MUTED
                                        : UiTheme.WARNING);
                setFont(UiTheme.BODY_B);
            } else if (!sel) {
                setFont(UiTheme.BODY);
            }
            return this;
        }
    }
}
