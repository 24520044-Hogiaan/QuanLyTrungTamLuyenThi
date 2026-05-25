package com.quanlytrungtam.dao;

import java.sql.*;

import org.jfree.data.category.DefaultCategoryDataset;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;

public class HoaDonHocPhiDAO {

    private DBConnection dbConnection = new DBConnection();

    public HoaDonHocPhiDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            System.out.println("Can't load driver!");
        }
    }

    public double[] getThongKeTongQuan() {
        Connection conn = dbConnection.getConnection();
        if (conn == null) {
            System.err.println("Database connection is null!");
            return new double[] { 0, 0, 0, 0 };
        }
        String sql = """
                SELECT
                    COALESCE(SUM(TONGTIEN),0),
                    COUNT(*),
                    COUNT(DISTINCT MAHOCVIEN),
                    COALESCE(AVG(TONGTIEN),0)
                FROM HOADONHOCPHI
                WHERE TRANGTHAIHD = 'Da thanh toan'
                """;

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new double[] { rs.getDouble(1), rs.getInt(2), rs.getInt(3), rs.getDouble(4) };
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new double[] { 0, 0, 0, 0 };
    }

    public DefaultCategoryDataset getDoanhThuDataset(int year) {
        Connection conn = dbConnection.getConnection();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int i = 1; i <= 12; i++) {
            dataset.addValue(0.0, "Doanh thu", "T" + i);
        }

        String sql = """
                SELECT EXTRACT(MONTH FROM NGAYLAP) AS THANG, SUM(TONGTIEN) AS DOANHTHU
                FROM HOADONHOCPHI
                WHERE EXTRACT(YEAR FROM NGAYLAP) = ?
                GROUP BY EXTRACT(MONTH FROM NGAYLAP)
                ORDER BY EXTRACT(MONTH FROM NGAYLAP)
                """;

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, year);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int month = rs.getInt("THANG");
                double revenue = rs.getDouble("DOANHTHU");
                dataset.addValue(revenue, "Doanh thu", "T" + month);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataset;
    }

    public boolean exportExcel(int year, File file) {

        DefaultCategoryDataset dataset = getDoanhThuDataset(year);

        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("DoanhThu_" + year);

            Row header = sheet.createRow(0);

            header.createCell(0).setCellValue("Tháng");
            header.createCell(1).setCellValue("Doanh Thu");
            for (int i = 1; i <= 12; i++) {

                Row row = sheet.createRow(i);

                String month = "T" + i;

                Number revenue = dataset.getValue("Doanh thu", month);

                row.createCell(0).setCellValue(month);

                row.createCell(1).setCellValue(revenue != null ? revenue.doubleValue() : 0);
            }

            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);

            try (FileOutputStream fos = new FileOutputStream(file)) {

                workbook.write(fos);
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}