package com.trungtam.ui.hocvien;

import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

/**
 * Thời khóa biểu cá nhân học viên — thiết kế lưới tuần giống LichGiangDayPanel.
 */
public class ThoiKhoaBieuHVPanel extends JPanel {

    private LocalDate currentMonday;
    private final JLabel lblWeek = new JLabel();
    private final JPanel gridPanel = new JPanel();

    // {thứ (2–7), ca, tên lớp, phòng, giảng viên}
    private static final Object[][] LICH_HV = {
            { 2, "Sáng", "Toán 12A", "P.101", "Thầy Nguyễn Văn An" },
            { 4, "Sáng", "Toán 12A", "P.101", "Thầy Nguyễn Văn An" },
            { 6, "Sáng", "Toán 12A", "P.101", "Thầy Nguyễn Văn An" },
            { 3, "Chiều", "Tiếng Anh 11B", "P.204", "Cô Phạm Thị Dung" },
            { 5, "Chiều", "Tiếng Anh 11B", "P.204", "Cô Phạm Thị Dung" },
            { 7, "Chiều", "Tiếng Anh 11B", "P.204", "Cô Phạm Thị Dung" },
    };

    private static final String[] CLASS_NAMES = { "Toán 12A", "Tiếng Anh 11B" };
    private static final Color[] CLASS_BG = {
            new Color(0xBBDEFB), new Color(0xC8E6C9)
    };
    private static final Color[] CLASS_FG = {
            new Color(0x0D47A1), new Color(0x1B5E20)
    };

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM");

    public ThoiKhoaBieuHVPanel() {
        setLayout(new BorderLayout(0, 10));
        setBorder(new EmptyBorder(UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M));
        setBackground(UiTheme.APP_BG);
        currentMonday = LocalDate.now().with(DayOfWeek.MONDAY);

        add(buildTopBar(), BorderLayout.NORTH);
        add(buildLegend(), BorderLayout.SOUTH);

        gridPanel.setLayout(new GridBagLayout());
        gridPanel.setBackground(UiTheme.APP_BG);
        add(gridPanel, BorderLayout.CENTER);
        render();
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout(12, 0));
        bar.setOpaque(false);

        JLabel title = new JLabel("THỜI KHÓA BIỂU CÁ NHÂN");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.SECONDARY);

        lblWeek.setFont(UiTheme.BODY_B);
        lblWeek.setHorizontalAlignment(SwingConstants.CENTER);

        JButton btnPrev = navBtn("<");
        JButton btnNext = navBtn(">");
        JButton btnToday = navBtn("Hôm nay");

        btnPrev.addActionListener(e -> {
            currentMonday = currentMonday.minusWeeks(1);
            render();
        });
        btnNext.addActionListener(e -> {
            currentMonday = currentMonday.plusWeeks(1);
            render();
        });
        btnToday.addActionListener(e -> {
            currentMonday = LocalDate.now().with(DayOfWeek.MONDAY);
            render();
        });

        JPanel navBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        navBar.setOpaque(false);
        navBar.add(btnPrev);
        navBar.add(lblWeek);
        navBar.add(btnNext);
        navBar.add(Box.createHorizontalStrut(10));
        navBar.add(btnToday);

        bar.add(title, BorderLayout.WEST);
        bar.add(navBar, BorderLayout.CENTER);
        return bar;
    }

    private JPanel buildLegend() {
        JPanel legend = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 2));
        legend.setOpaque(false);
        legend.setBorder(new EmptyBorder(4, 0, 0, 0));
        for (int i = 0; i < CLASS_NAMES.length; i++) {
            JLabel dot = new JLabel("  " + CLASS_NAMES[i] + "  ");
            dot.setOpaque(true);
            dot.setBackground(CLASS_BG[i]);
            dot.setForeground(CLASS_FG[i]);
            dot.setFont(UiTheme.BODY_B);
            dot.setBorder(BorderFactory.createLineBorder(CLASS_FG[i], 1));
            legend.add(dot);
        }
        return legend;
    }

    private void render() {
        LocalDate saturday = currentMonday.plusDays(5);
        lblWeek.setText("Tuần:  " + currentMonday.format(DATE_FMT) + " – " + saturday.format(DATE_FMT));
        gridPanel.removeAll();

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.BOTH;
        g.insets = new Insets(1, 1, 1, 1);

        // Header row
        g.gridy = 0;
        g.weighty = 0;
        g.gridx = 0;
        g.weightx = 0.7;
        gridPanel.add(headerCell("CA / NGÀY", false), g);

        for (int d = 0; d < 6; d++) {
            LocalDate day = currentMonday.plusDays(d);
            boolean isToday = day.equals(LocalDate.now());
            String thu = day.getDayOfWeek()
                    .getDisplayName(TextStyle.SHORT, Locale.of("vi")).toUpperCase();
            String label = "<html><center><b>" + thu + "</b><br>"
                    + day.format(DATE_FMT) + "</center></html>";
            g.gridx = d + 1;
            g.weightx = 1.0;
            gridPanel.add(headerCell(label, isToday), g);
        }

        // Content rows — Sáng / Chiều
        String[] cas = { "Sáng", "Chiều" };
        for (int row = 0; row < cas.length; row++) {
            g.gridy = row + 1;
            g.weighty = 1.0;
            String ca = cas[row];

            g.gridx = 0;
            g.weightx = 0.7;
            gridPanel.add(caCell(ca), g);

            for (int d = 0; d < 6; d++) {
                int dayNum = d + 2;
                boolean today = currentMonday.plusDays(d).equals(LocalDate.now());
                String caRef = ca;
                Optional<Object[]> slot = Arrays.stream(LICH_HV)
                        .filter(r -> r[0].equals(dayNum) && r[1].equals(caRef))
                        .findFirst();
                g.gridx = d + 1;
                g.weightx = 1.0;
                gridPanel.add(slot.isPresent() ? classCell(slot.get(), today) : emptyCell(today), g);
            }
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private JLabel headerCell(String html, boolean isToday) {
        JLabel lbl = new JLabel(html, SwingConstants.CENTER);
        lbl.setOpaque(true);
        lbl.setBackground(isToday ? new Color(0x1565C0) : UiTheme.SECONDARY);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(UiTheme.BODY_B);
        lbl.setPreferredSize(new Dimension(0, 52));
        lbl.setBorder(BorderFactory.createLineBorder(new Color(0x1B5E20), 1));
        return lbl;
    }

    private JLabel caCell(String ca) {
        JLabel lbl = new JLabel(ca.toUpperCase(), SwingConstants.CENTER);
        lbl.setOpaque(true);
        lbl.setBackground(UiTheme.SECONDARY_SEL);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(UiTheme.BODY_B);
        lbl.setBorder(BorderFactory.createLineBorder(UiTheme.SECONDARY, 1));
        return lbl;
    }

    private JLabel classCell(Object[] row, boolean todayCol) {
        String lop = (String) row[2];
        String phong = (String) row[3];
        String giangVien = row.length > 4 ? (String) row[4] : "";
        int idx = Arrays.asList(CLASS_NAMES).indexOf(lop);
        Color bg = idx >= 0 ? (todayCol ? CLASS_BG[idx].darker() : CLASS_BG[idx]) : new Color(0xEEEEEE);
        Color fg = idx >= 0 ? CLASS_FG[idx] : Color.DARK_GRAY;

        JLabel lbl = new JLabel(
                "<html><center><b style='font-size:13px'>" + lop + "</b>"
                        + "<br><span style='font-size:11px'>" + phong + "</span>"
                        + "<br><i style='font-size:11px'>" + giangVien + "</i></center></html>",
                SwingConstants.CENTER);
        lbl.setOpaque(true);
        lbl.setBackground(bg);
        lbl.setForeground(fg);
        lbl.setFont(UiTheme.BODY);
        lbl.setBorder(BorderFactory.createLineBorder(fg.brighter(), 1));
        return lbl;
    }

    private JLabel emptyCell(boolean todayCol) {
        JLabel lbl = new JLabel();
        lbl.setOpaque(true);
        lbl.setBackground(todayCol ? new Color(0xFFF9C4) : UiTheme.APP_BG);
        lbl.setBorder(BorderFactory.createLineBorder(UiTheme.CARD_BORDER, 1));
        return lbl;
    }

    private JButton navBtn(String text) {
        JButton btn = new JButton(text);
        btn.setFont(UiTheme.BODY);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
