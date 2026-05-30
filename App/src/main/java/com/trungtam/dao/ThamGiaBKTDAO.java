package com.trungtam.dao;

import com.trungtam.model.ThamGiaBKT;
import com.trungtam.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ThamGiaBKTDAO {

    public List<ThamGiaBKT> findByBaiKiemTra(int maBaiKiemTra) throws SQLException {
        List<ThamGiaBKT> list = new ArrayList<>();
        String sql = "SELECT * FROM THAMGIABKT WHERE MABAIKIEMTRA = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maBaiKiemTra);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ThamGiaBKT tg = new ThamGiaBKT();
                    tg.setMaHocVien(rs.getInt("MAHOCVIEN"));
                    tg.setMaBaiKiemTra(rs.getInt("MABAIKIEMTRA"));
                    tg.setLanThi(rs.getInt("LANTHI"));
                    tg.setDiemSo(rs.getDouble("DIEMSO"));
                    tg.setNhanXet(rs.getString("NHANXET"));
                    Date ngay = rs.getDate("NGAYTHI");
                    if (ngay != null) tg.setNgayThi(ngay.toLocalDate());
                    Timestamp ts = rs.getTimestamp("THOIGIANTHI");
                    if (ts != null) tg.setThoiGianThi(ts.toLocalDateTime());
                    list.add(tg);
                }
            }
        }
        return list;
    }

    public List<ThamGiaBKT> findByHocVien(int maHocVien) throws SQLException {
        List<ThamGiaBKT> list = new ArrayList<>();
        String sql = "SELECT * FROM THAMGIABKT WHERE MAHOCVIEN = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maHocVien);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ThamGiaBKT tg = new ThamGiaBKT();
                    tg.setMaHocVien(rs.getInt("MAHOCVIEN"));
                    tg.setMaBaiKiemTra(rs.getInt("MABAIKIEMTRA"));
                    tg.setLanThi(rs.getInt("LANTHI"));
                    tg.setDiemSo(rs.getDouble("DIEMSO"));
                    tg.setNhanXet(rs.getString("NHANXET"));
                    Date ngay = rs.getDate("NGAYTHI");
                    if (ngay != null) tg.setNgayThi(ngay.toLocalDate());
                    list.add(tg);
                }
            }
        }
        return list;
    }

    public void upsert(ThamGiaBKT tg) throws SQLException {
        String sql = "MERGE INTO THAMGIABKT t USING (SELECT ? MABAIKIEMTRA, ? MAHOCVIEN FROM DUAL) s " +
                "ON (t.MABAIKIEMTRA = s.MABAIKIEMTRA AND t.MAHOCVIEN = s.MAHOCVIEN) " +
                "WHEN MATCHED THEN UPDATE SET DIEMSO = ?, NHANXET = ?, NGAYTHI = ? " +
                "WHEN NOT MATCHED THEN INSERT (MABAIKIEMTRA, MAHOCVIEN, DIEMSO, NHANXET, NGAYTHI) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, tg.getMaBaiKiemTra());
            ps.setInt(2, tg.getMaHocVien());
            
            ps.setDouble(3, tg.getDiemSo());
            ps.setString(4, tg.getNhanXet());
            if (tg.getNgayThi() != null) ps.setDate(5, Date.valueOf(tg.getNgayThi()));
            else ps.setNull(5, Types.DATE);
            
            ps.setInt(6, tg.getMaBaiKiemTra());
            ps.setInt(7, tg.getMaHocVien());
            ps.setDouble(8, tg.getDiemSo());
            ps.setString(9, tg.getNhanXet());
            if (tg.getNgayThi() != null) ps.setDate(10, Date.valueOf(tg.getNgayThi()));
            else ps.setNull(10, Types.DATE);
            
            ps.executeUpdate();
        }
    }
}
