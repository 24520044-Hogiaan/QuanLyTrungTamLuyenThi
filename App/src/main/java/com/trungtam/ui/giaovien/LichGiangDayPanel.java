package com.trungtam.ui.giaovien;

import com.trungtam.controller.LopHocController;
import com.trungtam.model.LopHoc;
import com.trungtam.model.LichHoc;
import com.trungtam.dao.LichHocDAO;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

/**
 * Panel lịch giảng dạy — dữ liệu từ database (lớp được phân công).
 */
public class LichGiangDayPanel extends JPanel {

    private final LopHocController lopHocController = new LopHocController();

    private LocalDate currentMonday;
    private final JLabel lblWeek = new JLabel();
    private final JPanel gridPanel = new JPanel();

    private List<LichEntry> lichEntries = new ArrayList<>();
    private List<String> classNames = new ArrayList<>();

    private static final Color[] CLASS_BG = {
            new Color(0xBBDEFB), new Color(0xC8E6C9), new Color(0xFFE0B2),
            new Color(0xFFCDD2), new Color(0xD1C4E9), new Color(0xB2EBF2)
    };
    private static final Color[] CLASS_FG = {
            new Color(0x0D47A1), new Color(0x1B5E20), new Color(0xBF360C),
            new Color(0xB71C1C), new Color(0x4A148C), new Color(0x006064)
    };

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM");

    public LichGiangDayPanel() {
        setLayout(new BorderLayout(0, 10));
        setBorder(new EmptyBorder(UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M));
        setBackground(UiTheme.APP_BG);
        currentMonday = LocalDate.now().with(DayOfWeek.MONDAY);

        add(buildTopBar(), BorderLayout.NORTH);
        add(buildLegend(), BorderLayout.SOUTH);

        gridPanel.setLayout(new GridBagLayout());
        gridPanel.setBackground(UiTheme.APP_BG);
        add(gridPanel, BorderLayout.CENTER);

        loadData();
        render();
    }

    private void loadData() {
        lichEntries.clear();
        classNames.clear();

        List<LopHoc> allLop = lopHocController.layDanhSach();

        for (LopHoc lop : allLop) {
            if (!"Dang mo".equalsIgnoreCase(lop.getTrangThai()) &&
                !"Dang hoc".equalsIgnoreCase(lop.getTrangThai())) continue;

            String tenLop = lop.getTenLop();
            if (!classNames.contains(tenLop)) classNames.add(tenLop);

            LichHocDAO lhDao = new LichHocDAO();
            try {
                List<LichHoc> dsLich = lhDao.findByLop(lop.getMaLopHoc());
                for (LichHoc lh : dsLich) {
                    if (lh.getThu() == null) continue;
                    String thuStr = lh.getThu().replaceAll("[^0-9]", "");
                    if (!thuStr.isEmpty()) {
                        int thu = Integer.parseInt(thuStr);
                        if (thu >= 2 && thu <= 7) {
                            String ca = "Sáng";
                            if (lh.getGioBatDau() != null && lh.getGioBatDau().compareTo("12:00") >= 0) {
                                ca = "Chiều";
                            }
                            lichEntries.add(new LichEntry(thu, ca, tenLop));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout(12, 0));
        bar.setOpaque(false);

        JLabel title = new JLabel("Lịch Giảng Dạy");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.TEXT_PRIMARY);

        lblWeek.setFont(UiTheme.BODY_B);
        lblWeek.setHorizontalAlignment(SwingConstants.CENTER);

        JButton btnPrev = navBtn("<");
        JButton btnNext = navBtn(">");
        JButton btnToday = navBtn("Hôm nay");

        btnPrev.addActionListener(e -> { currentMonday = currentMonday.minusWeeks(1); render(); });
        btnNext.addActionListener(e -> { currentMonday = currentMonday.plusWeeks(1); render(); });
        btnToday.addActionListener(e -> { currentMonday = LocalDate.now().with(DayOfWeek.MONDAY); render(); });

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
        for (int i = 0; i < classNames.size(); i++) {
            int ci = i % CLASS_BG.length;
            JLabel dot = new JLabel("  " + classNames.get(i) + "  ");
            dot.setOpaque(true);
            dot.setBackground(CLASS_BG[ci]);
            dot.setForeground(CLASS_FG[ci]);
            dot.setFont(UiTheme.BODY_B);
            dot.setBorder(BorderFactory.createLineBorder(CLASS_FG[ci], 1));
            legend.add(dot);
        }
        if (classNames.isEmpty()) {
            JLabel lbl = new JLabel("Chưa có lịch giảng dạy.");
            lbl.setFont(UiTheme.CAPTION_I);
            lbl.setForeground(UiTheme.TEXT_MUTED);
            legend.add(lbl);
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

        g.gridy = 0; g.weighty = 0; g.gridx = 0; g.weightx = 0.7;
        gridPanel.add(headerCell("CA / NGÀY", false), g);

        for (int d = 0; d < 6; d++) {
            LocalDate day = currentMonday.plusDays(d);
            boolean isToday = day.equals(LocalDate.now());
            String thu = day.getDayOfWeek().getDisplayName(java.time.format.TextStyle.SHORT, Locale.of("vi")).toUpperCase();
            String label = "<html><center><b>" + thu + "</b><br>" + day.format(DATE_FMT) + "</center></html>";
            g.gridx = d + 1; g.weightx = 1.0;
            gridPanel.add(headerCell(label, isToday), g);
        }

        String[] cas = { "Sáng", "Chiều" };
        for (int row = 0; row < cas.length; row++) {
            g.gridy = row + 1; g.weighty = 1.0;
            String ca = cas[row];
            g.gridx = 0; g.weightx = 0.7;
            gridPanel.add(caCell(ca), g);

            for (int d = 0; d < 6; d++) {
                int dayNum = d + 2;
                boolean today = currentMonday.plusDays(d).equals(LocalDate.now());
                String caRef = ca;
                Optional<LichEntry> slot = lichEntries.stream()
                        .filter(r -> r.thu == dayNum && r.ca.equals(caRef)).findFirst();
                g.gridx = d + 1; g.weightx = 1.0;
                gridPanel.add(slot.isPresent() ? classCell(slot.get(), today) : emptyCell(today), g);
            }
        }
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private JLabel headerCell(String html, boolean isToday) {
        JLabel lbl = new JLabel(html, SwingConstants.CENTER);
        lbl.setOpaque(true);
        lbl.setBackground(isToday ? new Color(0x1565C0) : UiTheme.PRIMARY);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(UiTheme.BODY_B);
        lbl.setPreferredSize(new Dimension(0, 52));
        lbl.setBorder(BorderFactory.createLineBorder(new Color(0x0D47A1), 1));
        return lbl;
    }

    private JLabel caCell(String ca) {
        JLabel lbl = new JLabel(ca.toUpperCase(), SwingConstants.CENTER);
        lbl.setOpaque(true);
        lbl.setBackground(new Color(0x1976D2));
        lbl.setForeground(Color.WHITE);
        lbl.setFont(UiTheme.BODY_B);
        lbl.setBorder(BorderFactory.createLineBorder(UiTheme.PRIMARY, 1));
        return lbl;
    }

    private JLabel classCell(LichEntry entry, boolean todayCol) {
        int idx = classNames.indexOf(entry.tenLop);
        int ci = idx >= 0 ? idx % CLASS_BG.length : 0;
        Color bg = todayCol ? CLASS_BG[ci].darker() : CLASS_BG[ci];
        Color fg = CLASS_FG[ci];
        JLabel lbl = new JLabel(
                "<html><center><b style='font-size:13px'>" + entry.tenLop + "</b></center></html>",
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

    private static class LichEntry {
        int thu; String ca; String tenLop;
        LichEntry(int thu, String ca, String tenLop) {
            this.thu = thu; this.ca = ca; this.tenLop = tenLop;
        }
    }
}
