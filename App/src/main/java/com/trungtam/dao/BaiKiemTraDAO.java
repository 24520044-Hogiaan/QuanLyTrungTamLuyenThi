package com.trungtam.dao;

import com.trungtam.model.BaiKiemTra;
import com.trungtam.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BaiKiemTraDAO {

    public List<BaiKiemTra> findByLop(int maLop) throws SQLException {
        List<BaiKiemTra> list = new ArrayList<>();
        String sql = "SELECT * FROM BAIKIEMTRA WHERE MALOP = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maLop);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BaiKiemTra bkt = new BaiKiemTra();
                    bkt.setMaBaiKiemTra(rs.getInt("MABAIKIEMTRA"));
                    bkt.setTenBKT(rs.getString("TENBKT"));
                    Date ngay = rs.getDate("NGAYKT");
                    if (ngay != null) bkt.setNgayKiemTra(ngay.toLocalDate());
                    bkt.setThoiGian(rs.getInt("THOIGIAN"));
                    bkt.setLoaiBKT(rs.getString("LOAIBKT"));
                    bkt.setMaLop(rs.getInt("MALOP"));
                    bkt.setDiemToiDa(rs.getInt("DIEMTOIDA"));
                    bkt.setDiemDat(rs.getInt("DIEMDAT"));
                    list.add(bkt);
                }
            }
        }
        return list;
    }
}
