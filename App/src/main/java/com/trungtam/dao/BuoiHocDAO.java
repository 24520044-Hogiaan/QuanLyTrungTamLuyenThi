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
}
