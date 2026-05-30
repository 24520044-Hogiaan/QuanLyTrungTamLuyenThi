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

    public void insert(BaiKiemTra bkt) throws SQLException {
        String getIdSql = "SELECT NVL(MAX(MABAIKIEMTRA), 0) + 1 FROM BAIKIEMTRA";
        String insertSql = "INSERT INTO BAIKIEMTRA (MABAIKIEMTRA, TENBKT, NGAYKT, THOIGIAN, LOAIBKT, MALOP, DIEMTOIDA, DIEMDAT) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(getIdSql)) {
            if (rs.next()) {
                bkt.setMaBaiKiemTra(rs.getInt(1));
            }
            try (PreparedStatement ps = con.prepareStatement(insertSql)) {
                ps.setInt(1, bkt.getMaBaiKiemTra());
                ps.setString(2, bkt.getTenBKT());
                if (bkt.getNgayKiemTra() != null) {
                    ps.setDate(3, Date.valueOf(bkt.getNgayKiemTra()));
                } else {
                    ps.setNull(3, Types.DATE);
                }
                ps.setInt(4, bkt.getThoiGian());
                ps.setString(5, bkt.getLoaiBKT());
                ps.setInt(6, bkt.getMaLop());
                ps.setInt(7, bkt.getDiemToiDa());
                ps.setInt(8, bkt.getDiemDat());
                ps.executeUpdate();
            }
        }
    }
}
