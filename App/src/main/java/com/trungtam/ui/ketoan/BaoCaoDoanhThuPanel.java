package com.trungtam.ui.ketoan;

import com.trungtam.controller.HoaDonController;
import com.trungtam.controller.HoanTraController;
import com.trungtam.model.HoaDonHocPhi;
import com.trungtam.model.HoanTra;
import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class BaoCaoDoanhThuPanel extends JPanel {

    private final DefaultTableModel tableModel;
    private final HoaDonController hoaDonController = new HoaDonController();
    private final HoanTraController hoanTraController = new HoanTraController();
    private JTable table;

    private static final String[] COT = {
            "Tháng", "Số HĐ Thu", "Tổng Thu", "Số HĐ Hoàn", "Tổng Hoàn", "Doanh Thu Ròng"
    };

    private final JLabel lblTotalRevenue = new JLabel("0 VNĐ");
    private final JLabel lblTotalRefund = new JLabel("0 VNĐ");
    private final JLabel lblNetRevenue = new JLabel("0 VNĐ");

    public BaoCaoDoanhThuPanel() {
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M));
        setBackground(UiTheme.APP_BG);
        add(buildTopBar(), BorderLayout.NORTH);

        JPanel kpiRow = new JPanel(new GridLayout(1, 3, 16, 0));
        kpiRow.setOpaque(false);
        kpiRow.add(UiComponents.statCard("Tổng doanh thu", lblTotalRevenue, UiTheme.SUCCESS));
        kpiRow.add(UiComponents.statCard("Tổng hoàn trả", lblTotalRefund, UiTheme.DANGER));
        kpiRow.add(UiComponents.statCard("Doanh thu ròng", lblNetRevenue, UiTheme.KETOAN));

        JPanel mainContent = new JPanel(new BorderLayout(0, 12));
        mainContent.setOpaque(false);
        mainContent.add(kpiRow, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(COT, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        UiComponents.styleTable(table);
        UiComponents.setColumnAlignments(table,
                SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.RIGHT,
                SwingConstants.CENTER, SwingConstants.RIGHT, SwingConstants.RIGHT);
        mainContent.add(UiComponents.tableScroll(table), BorderLayout.CENTER);
        add(mainContent, BorderLayout.CENTER);
        loadData();
    }

    private JPanel buildTopBar() {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setOpaque(false);
        JLabel title = new JLabel("Báo Cáo Tài Chính – Doanh Thu");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.TEXT_PRIMARY);
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);
        JButton refreshBtn = UiComponents.ghostButton("Làm mới");
        refreshBtn.addActionListener(e -> loadData());
        right.add(refreshBtn);
        JButton btnExport = UiComponents.ghostButton("Xuất file");
        btnExport.addActionListener(e -> exportToExcel());
        right.add(btnExport);
        panel.add(title, BorderLayout.WEST);
        panel.add(right, BorderLayout.EAST);
        return panel;
    }

    private void exportToExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn vị trí lưu báo cáo Excel");
        fileChooser.setSelectedFile(new File("BaoCaoDoanhThu.xlsx"));
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("DoanhThu");
                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < table.getColumnCount(); i++) {
                    headerRow.createCell(i).setCellValue(table.getColumnName(i));
                }
                for (int i = 0; i < table.getRowCount(); i++) {
                    Row row = sheet.createRow(i + 1);
                    for (int j = 0; j < table.getColumnCount(); j++) {
                        Object val = table.getValueAt(i, j);
                        if (val != null) row.createCell(j).setCellValue(val.toString());
                    }
                }
                for (int i = 0; i < table.getColumnCount(); i++) {
                    sheet.autoSizeColumn(i);
                }
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    workbook.write(fos);
                }
                JOptionPane.showMessageDialog(this, "Xuất Excel thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi xuất file: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadData() {
        List<HoaDonHocPhi> hoaDonList = hoaDonController.getListHoaDon();
        List<HoanTra> hoanTraList = hoanTraController.getListHoanTra();

        double totalRevenue = hoaDonList.stream().mapToDouble(HoaDonHocPhi::getTongTien).sum();
        double totalRefund = hoanTraList.stream()
                .filter(ht -> "Chap thuan".equalsIgnoreCase(ht.getTrangThai())
                        || "Chấp thuận".equalsIgnoreCase(ht.getTrangThai()))
                .mapToDouble(HoanTra::getSoTien).sum();

        lblTotalRevenue.setText(String.format("%,.0f VNĐ", totalRevenue));
        lblTotalRefund.setText(String.format("%,.0f VNĐ", totalRefund));
        lblNetRevenue.setText(String.format("%,.0f VNĐ", totalRevenue - totalRefund));

        tableModel.setRowCount(0);
        for (int month = 1; month <= 12; month++) {
            final int m = month;
            long soHDThu = hoaDonList.stream()
                    .filter(hd -> hd.getNgayLap() != null && hd.getNgayLap().getMonthValue() == m).count();
            double tongThu = hoaDonList.stream()
                    .filter(hd -> hd.getNgayLap() != null && hd.getNgayLap().getMonthValue() == m)
                    .mapToDouble(HoaDonHocPhi::getTongTien).sum();
            long soHDHoan = hoanTraList.stream()
                    .filter(ht -> ht.getNgayYeuCau() != null && ht.getNgayYeuCau().getMonthValue() == m).count();
            double tongHoan = hoanTraList.stream()
                    .filter(ht -> ht.getNgayYeuCau() != null && ht.getNgayYeuCau().getMonthValue() == m)
                    .mapToDouble(HoanTra::getSoTien).sum();

            if (soHDThu > 0 || soHDHoan > 0) {
                tableModel.addRow(new Object[]{
                        String.format("T%d", m), soHDThu, String.format("%,.0f", tongThu),
                        soHDHoan, String.format("%,.0f", tongHoan),
                        String.format("%,.0f", tongThu - tongHoan)
                });
            }
        }
    }
}
