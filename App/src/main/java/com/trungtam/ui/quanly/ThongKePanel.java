package com.trungtam.ui.quanly;

import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ThongKePanel extends JPanel {

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

        // Title
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

        // KPI card
        JPanel kpiRow = new JPanel(new GridLayout(1, 1, 0, 0));
        kpiRow.setOpaque(false);
        JLabel totalLabel = new JLabel("7,600,000 VNĐ");
        kpiRow.add(UiComponents.statCard("Tổng Doanh Thu", totalLabel, UiTheme.SUCCESS));

        JPanel top = new JPanel(new BorderLayout(0, 12));
        top.setOpaque(false);
        top.add(titlePanel, BorderLayout.NORTH);
        top.add(kpiRow, BorderLayout.SOUTH);

        // Chart placeholder
        JPanel chartArea = new JPanel(new BorderLayout());
        chartArea.setBackground(UiTheme.CARD_BG);
        chartArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.CARD_BORDER, 1),
                new EmptyBorder(16, 16, 16, 16)));

        JPanel chartTop = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        chartTop.setOpaque(false);
        chartTop.add(new JLabel("Năm:"));
        JComboBox<String> yearCombo = new JComboBox<>(new String[]{"2026", "2025", "2024"});
        yearCombo.setFont(UiTheme.BODY);
        chartTop.add(yearCombo);
        chartTop.add(UiComponents.ghostButton("Xuất Excel"));

        JLabel chartTitle = new JLabel("Biểu đồ doanh thu theo tháng", SwingConstants.CENTER);
        chartTitle.setFont(UiTheme.TITLE_S);
        chartTitle.setForeground(UiTheme.TEXT_PRIMARY);

        JPanel chartBody = buildBarChart(
                new String[]{"T1","T2","T3","T4","T5","T6","T7","T8","T9","T10","T11","T12"},
                new int[]{0, 0, 0, 0, 8500000, 0, 0, 0, 0, 0, 0, 0},
                "Doanh thu", UiTheme.DANGER
        );

        chartArea.add(chartTop, BorderLayout.NORTH);
        chartArea.add(chartTitle, BorderLayout.CENTER);
        chartArea.add(chartBody, BorderLayout.SOUTH);

        panel.add(top, BorderLayout.NORTH);
        panel.add(chartArea, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildDangKyTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBackground(UiTheme.APP_BG);
        panel.setBorder(new EmptyBorder(16, 0, 0, 0));

        // Title
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);

        JLabel title = new JLabel("Thống Kê Đăng Ký");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.TEXT_PRIMARY);

        JLabel subtitle = new JLabel("Biểu đồ số lượng đăng ký học viên theo tháng");
        subtitle.setFont(UiTheme.CAPTION);
        subtitle.setForeground(UiTheme.TEXT_MUTED);

        titlePanel.add(title, BorderLayout.NORTH);
        titlePanel.add(subtitle, BorderLayout.SOUTH);

        // KPI cards row
        JPanel kpiRow = new JPanel(new GridLayout(1, 4, 16, 0));
        kpiRow.setOpaque(false);

        JLabel totalDK = new JLabel("11");
        kpiRow.add(UiComponents.statCard("Tổng Đăng Ký", totalDK, UiTheme.PRIMARY));

        JLabel dangHoc = new JLabel("9");
        kpiRow.add(UiComponents.statCard("Đang Học", dangHoc, UiTheme.SUCCESS));

        JLabel hoanThanh = new JLabel("1");
        kpiRow.add(UiComponents.statCard("Hoàn Thành", hoanThanh, UiTheme.QUANLY));

        JLabel daHuy = new JLabel("0");
        kpiRow.add(UiComponents.statCard("Đã Hủy", daHuy, UiTheme.DANGER));

        JPanel top = new JPanel(new BorderLayout(0, 12));
        top.setOpaque(false);
        top.add(titlePanel, BorderLayout.NORTH);
        top.add(kpiRow, BorderLayout.SOUTH);

        // Chart placeholder
        JPanel chartArea = new JPanel(new BorderLayout());
        chartArea.setBackground(UiTheme.CARD_BG);
        chartArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.CARD_BORDER, 1),
                new EmptyBorder(16, 16, 16, 16)));

        JPanel chartTop = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        chartTop.setOpaque(false);
        chartTop.add(new JLabel("Năm:"));
        JComboBox<String> yearCombo = new JComboBox<>(new String[]{"2026", "2025", "2024"});
        yearCombo.setFont(UiTheme.BODY);
        chartTop.add(yearCombo);
        chartTop.add(UiComponents.ghostButton("Xuất Excel"));

        JLabel chartTitle = new JLabel("Biểu đồ số lượng đăng ký theo tháng", SwingConstants.CENTER);
        chartTitle.setFont(UiTheme.TITLE_S);
        chartTitle.setForeground(UiTheme.TEXT_PRIMARY);

        JPanel chartBody = buildBarChart(
                new String[]{"T1","T2","T3","T4","T5","T6","T7","T8","T9","T10","T11","T12"},
                new int[]{0, 0, 0, 0, 11, 0, 0, 0, 0, 0, 0, 0},
                "Số lượng đăng ký", UiTheme.DANGER
        );

        chartArea.add(chartTop, BorderLayout.NORTH);
        chartArea.add(chartTitle, BorderLayout.CENTER);
        chartArea.add(chartBody, BorderLayout.SOUTH);

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

                int w = getWidth();
                int h = getHeight();
                int padLeft = 70, padRight = 20, padTop = 10, padBottom = 40;
                int chartW = w - padLeft - padRight;
                int chartH = h - padTop - padBottom;

                int maxVal = 1;
                for (int v : values) maxVal = Math.max(maxVal, v);

                // Y-axis
                g2.setColor(UiTheme.TEXT_MUTED);
                g2.setFont(UiTheme.CAPTION);
                int ySteps = 5;
                for (int i = 0; i <= ySteps; i++) {
                    int yVal = maxVal * i / ySteps;
                    int y = padTop + chartH - (chartH * i / ySteps);
                    g2.setColor(UiTheme.CARD_BORDER);
                    g2.drawLine(padLeft, y, padLeft + chartW, y);
                    g2.setColor(UiTheme.TEXT_MUTED);
                    String valStr = String.format("%,d", yVal);
                    g2.drawString(valStr, padLeft - g2.getFontMetrics().stringWidth(valStr) - 6, y + 4);
                }

                // X-axis labels + plot points
                int[] xPoints = new int[labels.length];
                int[] yPoints = new int[labels.length];
                int step = chartW / Math.max(labels.length - 1, 1);

                g2.setColor(UiTheme.TEXT_MUTED);
                for (int i = 0; i < labels.length; i++) {
                    int x = padLeft + i * step;
                    xPoints[i] = x;
                    yPoints[i] = padTop + chartH - (int) ((double) values[i] / maxVal * chartH);
                    int lblW = g2.getFontMetrics().stringWidth(labels[i]);
                    g2.drawString(labels[i], x - lblW / 2, h - 10);
                }

                // Line
                g2.setColor(lineColor);
                g2.setStroke(new BasicStroke(2));
                for (int i = 0; i < labels.length - 1; i++) {
                    g2.drawLine(xPoints[i], yPoints[i], xPoints[i + 1], yPoints[i + 1]);
                }

                // Points
                for (int i = 0; i < labels.length; i++) {
                    g2.fillOval(xPoints[i] - 4, yPoints[i] - 4, 8, 8);
                }

                // Y-axis label
                g2.setColor(UiTheme.TEXT_SECONDARY);
                g2.setFont(UiTheme.CAPTION);
                Graphics2D g2r = (Graphics2D) g2.create();
                g2r.rotate(-Math.PI / 2, 14, h / 2);
                g2r.drawString(seriesName, 14, h / 2);
                g2r.dispose();

                // X-axis label
                String xLabel = "Tháng";
                int xLabelW = g2.getFontMetrics().stringWidth(xLabel);
                g2.drawString(xLabel, padLeft + chartW / 2 - xLabelW / 2, h - 2);

                g2.dispose();
            }
        };
        chart.setPreferredSize(new Dimension(600, 350));
        chart.setBackground(UiTheme.CARD_BG);
        return chart;
    }
}
