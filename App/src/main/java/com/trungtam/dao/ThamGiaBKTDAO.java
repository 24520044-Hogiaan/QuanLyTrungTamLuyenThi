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
}
