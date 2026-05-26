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
}
