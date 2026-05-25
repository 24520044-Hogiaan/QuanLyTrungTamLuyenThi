package com.trungtam.ui.ketoan;

import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BaoCaoDoanhThuPanel extends JPanel {

    private final DefaultTableModel tableModel;

    private static final String[] COT = {
            "Tháng", "Số HĐ Thu", "Tổng Thu", "Số HĐ Hoàn", "Tổng Hoàn", "Doanh Thu Ròng"
    };

    public BaoCaoDoanhThuPanel() {
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M));
        setBackground(UiTheme.APP_BG);

        add(buildTopBar(), BorderLayout.NORTH);

        // KPI cards
        JPanel kpiRow = new JPanel(new GridLayout(1, 3, 16, 0));
        kpiRow.setOpaque(false);

        JLabel lblTotalRevenue = new JLabel("6,000,000 VND");
        kpiRow.add(UiComponents.statCard("Tổng doanh thu", lblTotalRevenue, UiTheme.SUCCESS));

        JLabel lblTotalRefund = new JLabel("100,000 VND");
        kpiRow.add(UiComponents.statCard("Tổng hoàn trả", lblTotalRefund, UiTheme.DANGER));

        JLabel lblNetRevenue = new JLabel("5,900,000 VND");
        kpiRow.add(UiComponents.statCard("Doanh thu ròng", lblNetRevenue, UiTheme.KETOAN));

        JPanel topSection = new JPanel(new BorderLayout(0, 12));
        topSection.setOpaque(false);
        topSection.add(buildTopBar2(), BorderLayout.NORTH);
        topSection.add(kpiRow, BorderLayout.CENTER);

        // Replace the NORTH with a combined section
        removeAll();
        add(buildTopBar(), BorderLayout.NORTH);

        JPanel mainContent = new JPanel(new BorderLayout(0, 12));
        mainContent.setOpaque(false);
        mainContent.add(kpiRow, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(COT, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        UiComponents.styleTable(table);
        UiComponents.setColumnAlignments(table,
                SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.RIGHT,
                SwingConstants.CENTER, SwingConstants.RIGHT, SwingConstants.RIGHT);

        mainContent.add(UiComponents.tableScroll(table), BorderLayout.CENTER);
        add(mainContent, BorderLayout.CENTER);

        loadSampleData();
    }

    private JPanel buildTopBar() {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setOpaque(false);

        JLabel title = new JLabel("Báo Cáo Tài Chính – Doanh Thu");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.TEXT_PRIMARY);

        JButton exportBtn = UiComponents.ghostButton("Xuất file");
        exportBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Chức năng xuất file — đang phát triển."));

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        right.add(exportBtn);

        panel.add(title, BorderLayout.WEST);
        panel.add(right, BorderLayout.EAST);
        return panel;
    }

    private JPanel buildTopBar2() { return new JPanel(); }

    private void loadSampleData() {
        tableModel.addRow(new Object[]{"05/2026", 4, "6,000,000", 1, "100,000", "5,900,000"});
    }
}
