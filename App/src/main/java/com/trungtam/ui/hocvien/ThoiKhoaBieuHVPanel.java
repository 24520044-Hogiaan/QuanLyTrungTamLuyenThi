package com.trungtam.ui.hocvien;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Thời khóa biểu cá nhân học viên — chỉ hiển thị lịch của lớp học viên đã đăng ký.
 * Cấu trúc giống LichGiangDayPanel nhưng đơn giản hóa cho góc nhìn học viên.
 */
public class ThoiKhoaBieuHVPanel extends JPanel {

    private LocalDate currentMonday;
    private final JLabel lblWeek  = new JLabel();
    private final JPanel gridPanel = new JPanel();

    // {thứ (2-7), ca, tên lớp, phòng, giảng viên}
    private static final Object[][] LICH_HV = {
        {2, "Sáng",  "Toán 12A",      "P.101", "Thầy Nguyễn Văn An"},
        {4, "Sáng",  "Toán 12A",      "P.101", "Thầy Nguyễn Văn An"},
        {6, "Sáng",  "Toán 12A",      "P.101", "Thầy Nguyễn Văn An"},
        {3, "Chiều", "Tiếng Anh 11B", "P.204", "Cô Phạm Thị Dung"},
        {5, "Chiều", "Tiếng Anh 11B", "P.204", "Cô Phạm Thị Dung"},
        {7, "Chiều", "Tiếng Anh 11B", "P.204", "Cô Phạm Thị Dung"},
    };


    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM");

    public ThoiKhoaBieuHVPanel() {
        setLayout(new BorderLayout(0, 10));
        setBorder(new EmptyBorder(14, 14, 14, 14));
        currentMonday = LocalDate.now().with(DayOfWeek.MONDAY);
        add(buildTopBar(), BorderLayout.NORTH);
        add(buildLegend(), BorderLayout.SOUTH);
        gridPanel.setLayout(new GridBagLayout());
        add(gridPanel, BorderLayout.CENTER);
        render();
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout(12, 0));
        JLabel title = new JLabel("THỜI KHÓA BIỂU CÁ NHÂN");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(0x1B5E20));
        lblWeek.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblWeek.setHorizontalAlignment(SwingConstants.CENTER);

        JButton btnPrev  = navBtn("<");
        JButton btnNext  = navBtn(">");
        JButton btnToday = navBtn("Hôm nay");
        btnPrev.addActionListener(e  -> { currentMonday = currentMonday.minusWeeks(1); render(); });
        btnNext.addActionListener(e  -> { currentMonday = currentMonday.plusWeeks(1);  render(); });
        btnToday.addActionListener(e -> { currentMonday = LocalDate.now().with(DayOfWeek.MONDAY); render(); });

        JPanel nav = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        nav.add(btnPrev); nav.add(lblWeek); nav.add(btnNext);
        nav.add(Box.createHorizontalStrut(10)); nav.add(btnToday);
        bar.add(title, BorderLayout.WEST);
        bar.add(nav, BorderLayout.CENTER);
        return bar;
    }

    private JPanel buildLegend() {
        JPanel legend = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 4));
        legend.setBorder(new EmptyBorder(4, 0, 0, 0));
        JLabel dot = new JLabel("  Lớp có lịch học  ");
        dot.setOpaque(true);
        dot.setBackground(new Color(0xF1F8E9));
        dot.setForeground(new Color(0x1B5E20));
        dot.setBorder(BorderFactory.createLineBorder(new Color(0xA5D6A7), 2));
        dot.setFont(new Font("Segoe UI", Font.BOLD, 13));
        legend.add(dot);
        return legend;
    }

    private void render() {
        LocalDate endSat = currentMonday.plusDays(5);
        lblWeek.setText("Tuần:  " + currentMonday.format(DATE_FMT) + " – " + endSat.format(DATE_FMT));
        gridPanel.removeAll();

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.BOTH; g.weightx = 1;

        // Hàng header ngày
        g.gridy = 0; g.weighty = 0; g.ipady = 14;
        addCell(gridPanel, g, 0, headerCell("Ca \\ Ngày", false));
        for (int d = 0; d < 6; d++) {
            LocalDate day = currentMonday.plusDays(d);
            boolean today = day.equals(LocalDate.now());
            String thu = day.getDayOfWeek().getDisplayName(java.time.format.TextStyle.SHORT, Locale.of("vi")).toUpperCase();
            String lbl  = "<html><center><b>" + thu + "</b><br>" + day.format(DATE_FMT) + "</center></html>";
            g.gridx = d + 1; addCell(gridPanel, g, 1, headerCell(lbl, today));
        }

        // Hàng Sáng và Chiều
        String[] cas = {"Sáng", "Chiều"};
        for (int caIdx = 0; caIdx < cas.length; caIdx++) {
            g.gridy = caIdx + 1; g.weighty = 1; g.ipady = 20;
            g.gridx = 0; addCell(gridPanel, g, 0, caCell(cas[caIdx]));
            for (int d = 0; d < 6; d++) {
                int thu = d + 2; // Thứ 2 = 2, ..., Thứ 7 = 7
                String ca = cas[caIdx];
                JPanel cell = buildClassCell(thu, ca);
                g.gridx = d + 1; addCell(gridPanel, g, 1, cell);
            }
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private JPanel buildClassCell(int thu, String ca) {
        for (Object[] row : LICH_HV) {
            if ((int) row[0] == thu && row[1].equals(ca)) {
                String lop = (String) row[2];
                String phong = (String) row[3];
                String gv    = (String) row[4];
                JPanel cell = new JPanel(new GridLayout(3, 1, 0, 4));
                cell.setBackground(new Color(0xF1F8E9));
                cell.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0xA5D6A7), 2),
                    new EmptyBorder(8, 8, 8, 8)));
                JLabel l1 = new JLabel(lop);  l1.setFont(new Font("Segoe UI", Font.BOLD, 15));  l1.setForeground(new Color(0x1B5E20));
                JLabel l2 = new JLabel(gv);   l2.setFont(new Font("Segoe UI", Font.PLAIN, 13)); l2.setForeground(new Color(0x212121));
                JLabel l3 = new JLabel(phong); l3.setFont(new Font("Segoe UI", Font.ITALIC, 12)); l3.setForeground(new Color(0x616161));
                cell.add(l1); cell.add(l2); cell.add(l3);
                return cell;
            }
        }
        JPanel empty = new JPanel(); empty.setBackground(new Color(0xFAFAFA)); return empty;
    }

    private JPanel headerCell(String text, boolean highlight) {
        JPanel cell = new JPanel(new BorderLayout());
        cell.setBackground(highlight ? new Color(0x1B5E20) : new Color(0x37474F));
        cell.setBorder(new EmptyBorder(4, 6, 4, 6));
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(Color.WHITE);
        cell.add(lbl, BorderLayout.CENTER);
        return cell;
    }

    private JPanel caCell(String ca) {
        JPanel cell = new JPanel(new BorderLayout());
        cell.setBackground(new Color(0x546E7A));
        JLabel lbl = new JLabel(ca, SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(Color.WHITE);
        cell.add(lbl, BorderLayout.CENTER);
        return cell;
    }

    private void addCell(JPanel panel, GridBagConstraints g, int dummy, JPanel cell) {
        panel.add(cell, g);
    }

    private JButton navBtn(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        return btn;
    }
}
