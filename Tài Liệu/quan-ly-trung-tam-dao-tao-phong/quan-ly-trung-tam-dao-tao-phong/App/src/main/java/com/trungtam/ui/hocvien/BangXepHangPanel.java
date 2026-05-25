package com.trungtam.ui.hocvien;

import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;

/**
 * Bảng xếp hạng học viên trong lớp.
 */
public class BangXepHangPanel extends JPanel {

    private static final String CURRENT_HV = "Nguyễn Minh Anh";

    private static final Object[][] RANKING = {
            { 1, "Hoàng Văn Phúc", 9.5, "Xuất Sắc" },
            { 2, "Nguyễn Minh Anh", 8.5, "Giỏi" },
            { 3, "Ngô Thị Quỳnh", 8.0, "Giỏi" },
            { 4, "Trần Bảo Châu", 7.5, "Khá" },
            { 5, "Bùi Đức Mạnh", 7.5, "Khá" },
            { 6, "Phạm Thị Hoa", 6.5, "Khá" },
            { 7, "Võ Thị Lan", 6.0, "Trung Bình" },
            { 8, "Đinh Thị Nga", 5.5, "Trung Bình" },
            { 9, "Lê Văn Dũng", 5.0, "Trung Bình" },
            { 10, "Đỗ Quốc Hùng", 4.5, "Yếu" },
    };

    private static final Object[][] DIEM_THEO_MON = {
            { "Toán học", 8.5, "Tốt" },
            { "Tiếng Anh", 7.4, "Cần cải thiện" },
    };

    public BangXepHangPanel() {
        setLayout(new BorderLayout(0, 14));
        setBorder(new EmptyBorder(UiTheme.PAD_L, UiTheme.PAD_L, UiTheme.PAD_L, UiTheme.PAD_L));
        setBackground(UiTheme.APP_BG);

        JLabel title = new JLabel("BẢNG XẾP HẠNG LỚP HỌC");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.SECONDARY);
        add(title, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                buildLeftPanel(), buildRightPanel());
        split.setDividerLocation(480);
        split.setDividerSize(5);
        split.setBorder(null);
        add(split, BorderLayout.CENTER);
    }

    private JPanel buildLeftPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setOpaque(false);

        int rank = 2, total = RANKING.length;
        double pct = (1.0 - (double) (rank - 1) / total) * 100;
        JPanel card = new JPanel(new GridLayout(1, 3, 12, 0));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(0, 0, 4, 0));

        JLabel vRank = new JLabel(rank + " / " + total);
        JLabel vTop = new JLabel(String.format("Top %.0f%%", pct));
        JLabel vDiem = new JLabel("8.5");
        card.add(UiComponents.statCard("Hạng của bạn", vRank, UiTheme.INFO));
        card.add(UiComponents.statCard("Top phần trăm", vTop, UiTheme.SUCCESS));
        card.add(UiComponents.statCard("Điểm TB của bạn", vDiem, new Color(0x6A1B9A)));
        panel.add(card, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(
                new String[] { "Hạng", "Học Viên", "Điểm TB", "Xếp Loại" }, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        for (Object[] row : RANKING)
            model.addRow(row);

        JTable table = new JTable(model);
        UiComponents.styleTable(table);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        int[] w = { 60, 180, 80 };
        for (int i = 0; i < w.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(w[i]);
            table.getColumnModel().getColumn(i).setMaxWidth(w[i]);
            table.getColumnModel().getColumn(i).setResizable(false);
        }
        table.setDefaultRenderer(Object.class, new RankRenderer(model));
        // Hạng: center; Học Viên: left; Điểm TB: center; Xếp Loại: center
        UiComponents.setColumnAlignments(table,
                SwingConstants.CENTER, SwingConstants.LEFT,
                SwingConstants.CENTER, SwingConstants.CENTER);

        panel.add(UiComponents.tableScroll(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildRightPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 8, 0, 0));

        JLabel lblTitle = UiComponents.divider("PHÂN TÍCH KẾT QUẢ", UiTheme.SECONDARY);
        lblTitle.setFont(UiTheme.TITLE_S);
        panel.add(lblTitle, BorderLayout.NORTH);

        JPanel analysis = new JPanel();
        analysis.setLayout(new BoxLayout(analysis, BoxLayout.Y_AXIS));
        analysis.setOpaque(false);

        for (Object[] mon : DIEM_THEO_MON) {
            String tenMon = (String) mon[0];
            double diem = (double) mon[1];
            String trangThai = (String) mon[2];
            boolean yeu = "Cần cải thiện".equals(trangThai);

            JPanel row = new JPanel(new BorderLayout(8, 0));
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
            row.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 3, 0, 0, yeu ? UiTheme.DANGER : UiTheme.SUCCESS),
                    new EmptyBorder(8, 10, 8, 10)));
            row.setBackground(yeu ? new Color(0xFFEBEE) : new Color(0xF1F8E9));

            JLabel lblMon = new JLabel(tenMon);
            lblMon.setFont(UiTheme.TITLE_S);
            JLabel lblDiem = new JLabel(String.format("%.1f", diem));
            lblDiem.setFont(new Font("Segoe UI", Font.BOLD, 22));
            lblDiem.setForeground(yeu ? UiTheme.DANGER : UiTheme.SUCCESS);
            JLabel lblStatus = new JLabel(trangThai);
            lblStatus.setFont(UiTheme.CAPTION_I);
            lblStatus.setForeground(yeu ? UiTheme.DANGER : UiTheme.SUCCESS);

            JPanel info = new JPanel(new GridLayout(2, 1));
            info.setOpaque(false);
            info.add(lblMon);
            info.add(lblStatus);
            row.add(info, BorderLayout.CENTER);
            row.add(lblDiem, BorderLayout.EAST);
            analysis.add(row);
            analysis.add(Box.createVerticalStrut(8));
        }

        JTextArea note = new JTextArea(
                "Ghi chú của giảng viên:\n\n"
                        + "- Toán học: Kết quả tốt! Tiếp tục ôn tập phần tích phân.\n"
                        + "- Tiếng Anh: Cần tăng cường từ vựng và kỹ năng đọc hiểu.\n"
                        + "  Gợi ý: luyện 15-20 từ mới mỗi ngày.");
        note.setFont(UiTheme.BODY);
        note.setEditable(false);
        note.setLineWrap(true);
        note.setWrapStyleWord(true);
        note.setBackground(new Color(0xFFFDE7));
        note.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xFFCC80)),
                new EmptyBorder(10, 12, 10, 12)));
        analysis.add(note);

        panel.add(UiComponents.scrollPane(analysis), BorderLayout.CENTER);
        return panel;
    }

    private static class RankRenderer extends UiComponents.AltRowRenderer {
        private final DefaultTableModel model;

        RankRenderer(DefaultTableModel model) {
            this.model = model;
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable t, Object val, boolean sel, boolean foc, int r, int c) {
            super.getTableCellRendererComponent(t, val, sel, foc, r, c);
            String tenHV = (String) model.getValueAt(r, 1);
            if (!sel) {
                if (CURRENT_HV.equals(tenHV)) {
                    setBackground(new Color(0xE8F5E9));
                    setFont(UiTheme.BODY_B);
                    setForeground(UiTheme.SECONDARY);
                } else {
                    setFont(UiTheme.BODY);
                }
                if (c == 0 && (int) model.getValueAt(r, 0) <= 3) {
                    setForeground(new Color(0xF57F17));
                    setFont(UiTheme.BODY_B);
                }
            }
            return this;
        }
    }
}
