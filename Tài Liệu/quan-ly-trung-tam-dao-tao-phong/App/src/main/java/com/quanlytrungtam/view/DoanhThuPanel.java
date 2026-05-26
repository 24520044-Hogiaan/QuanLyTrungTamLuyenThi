package com.quanlytrungtam.view;

import java.io.File;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import com.quanlytrungtam.controller.HoaDonController;
import com.quanlytrungtam.ui.UiComponents;
import com.quanlytrungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.DecimalFormat;

public class DoanhThuPanel extends JPanel implements Refreshable {

        private final JLabel lblTongDoanhThu = new JLabel("0 VNĐ");
        private final JLabel lblSoHoaDon = new JLabel("0");
        private final JLabel lblHocVien = new JLabel("0");
        private final JLabel lblTrungBinh = new JLabel("0 VNĐ");

        private JPanel chartContainer;
        private JComboBox<Integer> cboYear;
        private JButton btnExportExcel;

        private final DecimalFormat moneyFormat = new DecimalFormat("#,###");
        private final HoaDonController controller = new HoaDonController();

        public DoanhThuPanel() {
                setLayout(new BorderLayout());
                setBackground(UiTheme.APP_BG);

                initUI();
                loadThongKe();
                loadChart();
        }

        @Override
        public void refresh() {
                loadThongKe();
                loadChart();
        }

        private void initUI() {
                JPanel content = new JPanel(new BorderLayout(0, 18));
                content.setBackground(UiTheme.APP_BG);
                content.setBorder(new EmptyBorder(UiTheme.PAD_L, UiTheme.PAD_L, UiTheme.PAD_L, UiTheme.PAD_L));

                JPanel headerPanel = new JPanel(new BorderLayout());
                headerPanel.setOpaque(false);

                JLabel lblTitle = new JLabel("Thống Kê Doanh Thu");
                lblTitle.setFont(UiTheme.TITLE_L);
                lblTitle.setForeground(UiTheme.TEXT_PRIMARY);

                JLabel lblDesc = new JLabel("Biểu đồ doanh thu học phí theo tháng");
                lblDesc.setFont(UiTheme.BODY);
                lblDesc.setForeground(UiTheme.TEXT_MUTED);

                JPanel titleBox = new JPanel();
                titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));
                titleBox.setOpaque(false);
                titleBox.add(lblTitle);
                titleBox.add(Box.createVerticalStrut(4));
                titleBox.add(lblDesc);

                headerPanel.add(titleBox, BorderLayout.WEST);

                JPanel cardPanel = new JPanel(new GridLayout(1, 4, 16, 0));
                cardPanel.setOpaque(false);
                cardPanel.add(UiComponents.statCard("Tổng Doanh Thu", lblTongDoanhThu, UiTheme.SUCCESS));
                JPanel chartCard = new JPanel(new BorderLayout(0, 12));
                chartCard.setBackground(UiTheme.CARD_BG);
                chartCard.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(UiTheme.CARD_BORDER),
                                new EmptyBorder(16, 16, 16, 16)));

                JPanel chartTop = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                chartTop.setOpaque(false);

                cboYear = new JComboBox<>();
                for (int i = 2023; i <= 2035; i++) {
                        cboYear.addItem(i);
                }
                cboYear.setSelectedItem(2026);

                cboYear.addActionListener(e -> {
                        loadChart();
                });

                btnExportExcel = new JButton("Xuất Excel");
                btnExportExcel.addActionListener(e -> {
                        int year = (Integer) cboYear.getSelectedItem();

                        JFileChooser fileChooser = new JFileChooser();

                        fileChooser.setSelectedFile(new File("DoanhThu_" + year + ".xlsx"));

                        int result = fileChooser.showSaveDialog(this);
                        if (result == JFileChooser.APPROVE_OPTION) {

                                File file = fileChooser.getSelectedFile();

                                if (!file.getName().endsWith(".xlsx")) {
                                        file = new File(file.getAbsolutePath() + ".xlsx");
                                }

                                boolean success = controller.exportExcel(year, file);

                                if (success) {
                                        JOptionPane.showMessageDialog(this, "Xuất Excel thành công!");
                                } else {
                                        JOptionPane.showMessageDialog(this, "Xuất Excel thất bại!");
                                }
                        }
                });

                chartTop.add(new JLabel("Năm:"));
                chartTop.add(cboYear);
                chartTop.add(btnExportExcel);

                chartContainer = new JPanel(new BorderLayout());
                chartContainer.setOpaque(false);

                chartCard.add(chartTop, BorderLayout.NORTH);
                chartCard.add(chartContainer, BorderLayout.CENTER);

                JPanel topPanel = new JPanel(new BorderLayout(0, 18));
                topPanel.setOpaque(false);
                topPanel.add(headerPanel, BorderLayout.NORTH);
                topPanel.add(cardPanel, BorderLayout.CENTER);

                content.add(topPanel, BorderLayout.NORTH);
                content.add(chartCard, BorderLayout.CENTER);

                add(content, BorderLayout.CENTER);
        }

        private void loadThongKe() {
                double[] stats = controller.getThongKeTongQuan();
                lblTongDoanhThu.setText(formatMoney(stats[0]));
                lblSoHoaDon.setText(String.valueOf((int) stats[1]));
                lblHocVien.setText(String.valueOf((int) stats[2]));
                lblTrungBinh.setText(formatMoney(stats[3]));
        }

        private void loadChart() {
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();
                int year = (Integer) cboYear.getSelectedItem();

                dataset = controller.getDoanhThuDataset(year);

                JFreeChart chart = ChartFactory.createLineChart(
                                "Biểu đồ doanh thu theo tháng", "Tháng", "Doanh thu", dataset);

                chart.setBackgroundPaint(Color.WHITE);
                chart.getLegend().setFrame(BlockBorder.NONE);

                CategoryPlot plot = chart.getCategoryPlot();
                plot.setBackgroundPaint(Color.WHITE);
                plot.setRangeGridlinePaint(new Color(230, 230, 230));
                plot.setOutlineVisible(false);

                CategoryAxis domainAxis = plot.getDomainAxis();
                domainAxis.setTickLabelFont(UiTheme.BODY);

                NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
                rangeAxis.setTickLabelFont(UiTheme.BODY);

                rangeAxis.setAutoRangeIncludesZero(true);
                rangeAxis.setLowerBound(-300000);

                LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
                renderer.setSeriesStroke(0, new BasicStroke(3f));
                renderer.setDefaultShapesVisible(true);

                ChartPanel chartPanel = new ChartPanel(chart);
                chartPanel.setPreferredSize(new Dimension(1200, 500));
                chartPanel.setMouseWheelEnabled(true);

                chartContainer.removeAll();
                chartContainer.add(chartPanel, BorderLayout.CENTER);
                chartContainer.revalidate();
                chartContainer.repaint();
        }

        private String formatMoney(double amount) {
                return moneyFormat.format(amount) + " VNĐ";
        }

}
