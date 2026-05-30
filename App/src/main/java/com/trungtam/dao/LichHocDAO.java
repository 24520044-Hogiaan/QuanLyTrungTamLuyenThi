package com.trungtam.dao;

import com.trungtam.model.LichHoc;
import com.trungtam.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LichHocDAO {

    public List<LichHoc> findByLop(int maLop) throws SQLException {
        List<LichHoc> list = new ArrayList<>();
        String sql = "SELECT * FROM LICHHOC WHERE MALOP = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maLop);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LichHoc lh = new LichHoc();
                    lh.setMaLich(rs.getInt("MALICH"));
                    lh.setMaLop(rs.getInt("MALOP"));
                    lh.setGioBatDau(rs.getString("GIOBATDAU"));
                    lh.setGioKetThuc(rs.getString("GIOKETTHUC"));
                    lh.setThu(rs.getString("THU"));
                    list.add(lh);
                }
            }
        }
        return list;
    }

    public boolean insert(LichHoc lh) throws SQLException {
        String sqlMax = "SELECT COALESCE(MAX(MALICH), 0) + 1 FROM LICHHOC";
        String sqlIns = "INSERT INTO LICHHOC(MALICH, MALOP, GIOBATDAU, GIOKETTHUC, THU) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sqlMax)) {
            if (rs.next()) lh.setMaLich(rs.getInt(1));
        }
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sqlIns)) {
            ps.setInt(1, lh.getMaLich());
            ps.setInt(2, lh.getMaLop());
            ps.setString(3, lh.getGioBatDau());
            ps.setString(4, lh.getGioKetThuc());
            ps.setString(5, lh.getThu());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int maLich) throws SQLException {
        String sql = "DELETE FROM LICHHOC WHERE MALICH = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maLich);
            return ps.executeUpdate() > 0;
        }
    }
}
