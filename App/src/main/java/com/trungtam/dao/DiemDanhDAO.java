package com.trungtam.dao;

import com.trungtam.model.DiemDanh;
import com.trungtam.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiemDanhDAO {

    public List<DiemDanh> findByBuoiHoc(int maBuoiHoc) throws SQLException {
        List<DiemDanh> list = new ArrayList<>();
        String sql = "SELECT * FROM DIEMDANH WHERE MABUOIHOC = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maBuoiHoc);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DiemDanh dd = new DiemDanh();
                    dd.setMaBuoiHoc(rs.getInt("MABUOIHOC"));
                    dd.setMaHocVien(rs.getInt("MAHOCVIEN"));
                    dd.setTrangThai(rs.getString("TRANGTHAI"));
                    list.add(dd);
                }
            }
        }
        return list;
    }

    public void upsert(DiemDanh dd) throws SQLException {
        String sql = "MERGE INTO DIEMDANH d USING (SELECT ? MABUOIHOC, ? MAHOCVIEN FROM DUAL) s " +
                "ON (d.MABUOIHOC = s.MABUOIHOC AND d.MAHOCVIEN = s.MAHOCVIEN) " +
                "WHEN MATCHED THEN UPDATE SET TRANGTHAI = ? " +
                "WHEN NOT MATCHED THEN INSERT (MABUOIHOC, MAHOCVIEN, TRANGTHAI) VALUES (?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, dd.getMaBuoiHoc());
            ps.setInt(2, dd.getMaHocVien());
            ps.setString(3, dd.getTrangThai());
            ps.setInt(4, dd.getMaBuoiHoc());
            ps.setInt(5, dd.getMaHocVien());
            ps.setString(6, dd.getTrangThai());
            ps.executeUpdate();
        }
    }
}
