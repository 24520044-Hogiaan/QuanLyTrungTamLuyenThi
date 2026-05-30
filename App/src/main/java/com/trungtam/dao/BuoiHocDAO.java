package com.trungtam.dao;

import com.trungtam.model.BuoiHoc;
import com.trungtam.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BuoiHocDAO {

    public List<BuoiHoc> findByLop(int maLopHoc) throws SQLException {
        List<BuoiHoc> list = new ArrayList<>();
        String sql = "SELECT * FROM BUOIHOC WHERE MALOPHOC = ? ORDER BY NGAYHOC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maLopHoc);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BuoiHoc bh = new BuoiHoc();
                    bh.setMaBuoiHoc(rs.getInt("MABUOIHOC"));
                    bh.setMaLopHoc(rs.getInt("MALOPHOC"));
                    bh.setMaLich(rs.getInt("MALICH"));
                    Date ngay = rs.getDate("NGAYHOC");
                    if (ngay != null) bh.setNgayHoc(ngay.toLocalDate());
                    bh.setGioHoc(rs.getString("GIOHOC"));
                    bh.setMaPhongHoc(rs.getInt("MAPHONGHOC"));
                    bh.setTrangThai(rs.getString("TRANGTHAI"));
                    bh.setLoaiBuoi(rs.getString("LOAIBUOI"));
                    list.add(bh);
                }
            }
        }
        return list;
    }

    public void insert(BuoiHoc bh) throws SQLException {
        String getIdSql = "SELECT NVL(MAX(MABUOIHOC), 0) + 1 FROM BUOIHOC";
        String insertSql = "INSERT INTO BUOIHOC (MABUOIHOC, MALOPHOC, NGAYHOC, GIOHOC, TRANGTHAI, LOAIBUOI) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(getIdSql)) {
            if (rs.next()) {
                bh.setMaBuoiHoc(rs.getInt(1));
            }
            try (PreparedStatement ps = con.prepareStatement(insertSql)) {
                ps.setInt(1, bh.getMaBuoiHoc());
                ps.setInt(2, bh.getMaLopHoc());
                if (bh.getNgayHoc() != null) {
                    ps.setDate(3, Date.valueOf(bh.getNgayHoc()));
                } else {
                    ps.setNull(3, Types.DATE);
                }
                ps.setString(4, bh.getGioHoc());
                ps.setString(5, bh.getTrangThai());
                ps.setString(6, bh.getLoaiBuoi());
                ps.executeUpdate();
            }
        }
    }
}
