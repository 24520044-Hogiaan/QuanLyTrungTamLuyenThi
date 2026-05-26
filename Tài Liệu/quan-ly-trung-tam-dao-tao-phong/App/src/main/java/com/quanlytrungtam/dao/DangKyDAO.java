package com.quanlytrungtam.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.jfree.data.category.DefaultCategoryDataset;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;

public class DangKyDAO {

    private DBConnection dbConnection = new DBConnection();

    public DangKyDAO() {
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
                    COUNT(*),
                    COALESCE(SUM(CASE WHEN TRANGTHAIDKY = 'Dang hoc' THEN 1 ELSE 0 END), 0),
                    COALESCE(SUM(CASE WHEN TRANGTHAIDKY = 'Hoan thanh' THEN 1 ELSE 0 END), 0),
                    COALESCE(SUM(CASE WHEN TRANGTHAIDKY = 'Huy' THEN 1 ELSE 0 END), 0)
                FROM DANGKY
                """;

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new double[] {
                        rs.getDouble(1),
                        rs.getDouble(2),
                        rs.getDouble(3),
                        rs.getDouble(4)
                };
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbConnection.closeConnection(conn);
        }
        return new double[] { 0, 0, 0, 0 };
    }

    public DefaultCategoryDataset getDangKyDataset(int year) {
        Connection conn = dbConnection.getConnection();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int i = 1; i <= 12; i++) {
            dataset.addValue(0.0, "Số lượng đăng ký", "T" + i);
        }

        if (conn == null) {
            return dataset;
        }

        String sql = """
                SELECT EXTRACT(MONTH FROM NGAYDANGKY) AS THANG, COUNT(*) AS SOLUONG
                FROM DANGKY
                WHERE EXTRACT(YEAR FROM NGAYDANGKY) = ?
                GROUP BY EXTRACT(MONTH FROM NGAYDANGKY)
                ORDER BY EXTRACT(MONTH FROM NGAYDANGKY)
                """;

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, year);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int month = rs.getInt("THANG");
                double count = rs.getDouble("SOLUONG");
                dataset.addValue(count, "Số lượng đăng ký", "T" + month);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbConnection.closeConnection(conn);
        }
        return dataset;
    }

    public boolean exportExcel(int year, File file) {

        DefaultCategoryDataset dataset = getDangKyDataset(year);

        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("DangKy_" + year);

            Row header = sheet.createRow(0);

            header.createCell(0).setCellValue("Tháng");
            header.createCell(1).setCellValue("Số Lượng Đăng Ký");

            for (int i = 1; i <= 12; i++) {

                Row row = sheet.createRow(i);

                String month = "T" + i;

                Number value = dataset.getValue("Số lượng đăng ký", month);

                row.createCell(0).setCellValue(month);

                row.createCell(1).setCellValue(value != null ? value.doubleValue() : 0);
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
