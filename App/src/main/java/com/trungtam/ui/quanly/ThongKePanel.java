package com.trungtam.ui.quanly;

import com.trungtam.controller.DangKyController;
import com.trungtam.controller.HoaDonController;
import com.trungtam.model.DangKy;
import com.trungtam.model.HoaDonHocPhi;
import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class ThongKePanel extends JPanel {

    private final HoaDonController hoaDonController = new HoaDonController();
    private final DangKyController dangKyController = new DangKyController();

    public ThongKePanel() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M));
        setBackground(UiTheme.APP_BG);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(UiTheme.BODY_B);
        tabs.addTab("Doanh Thu", buildDoanhThuTab());
        tabs.addTab("Đăng Ký", buildDangKyTab());
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel buildDoanhThuTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBackground(UiTheme.APP_BG);
        panel.setBorder(new EmptyBorder(16, 0, 0, 0));

        List<HoaDonHocPhi> hoaDonList = hoaDonController.getListHoaDon();
        double tongDoanhThu = hoaDonList.stream()
                .filter(hd -> "Da thanh toan".equalsIgnoreCase(hd.getTrangThaiHD())
                        || "Đã thanh toán".equalsIgnoreCase(hd.getTrangThaiHD()))
                .mapToDouble(HoaDonHocPhi::getTongTien).sum();

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        JLabel title = new JLabel("Thống Kê Doanh Thu");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.TEXT_PRIMARY);
        JLabel subtitle = new JLabel("Biểu đồ doanh thu học phí theo tháng");
        subtitle.setFont(UiTheme.CAPTION);
        subtitle.setForeground(UiTheme.TEXT_MUTED);
        titlePanel.add(title, BorderLayout.NORTH);
        titlePanel.add(subtitle, BorderLayout.SOUTH);

        JPanel kpiRow = new JPanel(new GridLayout(1, 1, 0, 0));
        kpiRow.setOpaque(false);
        JLabel totalLabel = new JLabel(String.format("%,.0f VNĐ", tongDoanhThu));
        kpiRow.add(UiComponents.statCard("Tổng Doanh Thu", totalLabel, UiTheme.SUCCESS));

        JPanel top = new JPanel(new BorderLayout(0, 12));
        top.setOpaque(false);
        top.add(titlePanel, BorderLayout.NORTH);
        top.add(kpiRow, BorderLayout.SOUTH);

        int[] monthlyRevenue = new int[12];
        for (HoaDonHocPhi hd : hoaDonList) {
            if (hd.getNgayLap() != null) {
                int month = hd.getNgayLap().getMonthValue() - 1;
                monthlyRevenue[month] += (int) hd.getTongTien();
            }
        }
        String[] labels = { "T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12" };

        JPanel chartArea = new JPanel(new BorderLayout());
        chartArea.setBackground(UiTheme.CARD_BG);
        chartArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.CARD_BORDER, 1),
                new EmptyBorder(16, 16, 16, 16)));
        chartArea.add(buildBarChart(labels, monthlyRevenue, "Doanh thu", UiTheme.DANGER), BorderLayout.CENTER);

        panel.add(top, BorderLayout.NORTH);
        panel.add(chartArea, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildDangKyTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBackground(UiTheme.APP_BG);
        panel.setBorder(new EmptyBorder(16, 0, 0, 0));

        List<DangKy> dkList = dangKyController.getListDangKy();
        long totalDK = dkList.size();
        long dangHoc = dkList.stream().filter(d -> "Dang hoc".equalsIgnoreCase(d.getTrangThaiDky())
                || "Đang học".equalsIgnoreCase(d.getTrangThaiDky())).count();
        long hoanThanh = dkList.stream().filter(d -> "Hoan thanh".equalsIgnoreCase(d.getTrangThaiDky())
                || "Hoàn thành".equalsIgnoreCase(d.getTrangThaiDky())).count();
        long daHuy = dkList.stream().filter(d -> "Da huy".equalsIgnoreCase(d.getTrangThaiDky())
                || "Đã hủy".equalsIgnoreCase(d.getTrangThaiDky())).count();

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        JLabel title = new JLabel("Thống Kê Đăng Ký");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.TEXT_PRIMARY);
        titlePanel.add(title, BorderLayout.NORTH);

        JPanel kpiRow = new JPanel(new GridLayout(1, 4, 16, 0));
        kpiRow.setOpaque(false);
        kpiRow.add(UiComponents.statCard("Tổng Đăng Ký", new JLabel(String.valueOf(totalDK)), UiTheme.PRIMARY));
        kpiRow.add(UiComponents.statCard("Đang Học", new JLabel(String.valueOf(dangHoc)), UiTheme.SUCCESS));
        kpiRow.add(UiComponents.statCard("Hoàn Thành", new JLabel(String.valueOf(hoanThanh)), UiTheme.QUANLY));
        kpiRow.add(UiComponents.statCard("Đã Hủy", new JLabel(String.valueOf(daHuy)), UiTheme.DANGER));

        JPanel top = new JPanel(new BorderLayout(0, 12));
        top.setOpaque(false);
        top.add(titlePanel, BorderLayout.NORTH);
        top.add(kpiRow, BorderLayout.SOUTH);

        int[] monthlyDK = new int[12];
        for (DangKy dk : dkList) {
            if (dk.getNgayDangKy() != null) {
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.setTime(dk.getNgayDangKy());
                monthlyDK[cal.get(java.util.Calendar.MONTH)] += 1;
            }
        }
        String[] labels = { "T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12" };

        JPanel chartArea = new JPanel(new BorderLayout());
        chartArea.setBackground(UiTheme.CARD_BG);
        chartArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.CARD_BORDER, 1),
                new EmptyBorder(16, 16, 16, 16)));
        chartArea.add(buildBarChart(labels, monthlyDK, "Số lượng đăng ký", UiTheme.DANGER), BorderLayout.CENTER);

        panel.add(top, BorderLayout.NORTH);
        panel.add(chartArea, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildBarChart(String[] labels, int[] values, String seriesName, Color lineColor) {
        JPanel chart = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                int padLeft = 70, padRight = 20, padTop = 10, padBottom = 40;
                int chartW = w - padLeft - padRight, chartH = h - padTop - padBottom;
                int maxVal = 1;
                for (int v : values)
                    maxVal = Math.max(maxVal, v);
                g2.setFont(UiTheme.CAPTION);
                for (int i = 0; i <= 5; i++) {
                    int yVal = maxVal * i / 5;
                    int y = padTop + chartH - (chartH * i / 5);
                    g2.setColor(UiTheme.CARD_BORDER);
                    g2.drawLine(padLeft, y, padLeft + chartW, y);
                    g2.setColor(UiTheme.TEXT_MUTED);
                    String valStr = String.format("%,d", yVal);
                    g2.drawString(valStr, padLeft - g2.getFontMetrics().stringWidth(valStr) - 6, y + 4);
                }
                int[] xPoints = new int[labels.length], yPoints = new int[labels.length];
                int step = chartW / Math.max(labels.length - 1, 1);
                g2.setColor(UiTheme.TEXT_MUTED);
                for (int i = 0; i < labels.length; i++) {
                    int x = padLeft + i * step;
                    xPoints[i] = x;
                    yPoints[i] = padTop + chartH - (int) ((double) values[i] / maxVal * chartH);
                    int lblW = g2.getFontMetrics().stringWidth(labels[i]);
                    g2.drawString(labels[i], x - lblW / 2, h - 10);
                }
                g2.setColor(lineColor);
                g2.setStroke(new BasicStroke(2));
                for (int i = 0; i < labels.length - 1; i++) {
                    g2.drawLine(xPoints[i], yPoints[i], xPoints[i + 1], yPoints[i + 1]);
                }
                for (int i = 0; i < labels.length; i++) {
                    g2.fillOval(xPoints[i] - 4, yPoints[i] - 4, 8, 8);
                }
                g2.dispose();
            }
        };
        chart.setPreferredSize(new Dimension(600, 350));
        chart.setBackground(UiTheme.CARD_BG);
        return chart;
    }
}
