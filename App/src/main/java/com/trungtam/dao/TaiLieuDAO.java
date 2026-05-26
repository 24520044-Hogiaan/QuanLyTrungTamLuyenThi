package com.trungtam.dao;

import com.trungtam.model.TaiLieu;
import com.trungtam.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaiLieuDAO {

    public List<TaiLieu> findByLop(int maLopHoc) throws SQLException {
        List<TaiLieu> list = new ArrayList<>();
        String sql = "SELECT * FROM TAILIEU WHERE MALOPHOC = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maLopHoc);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TaiLieu tl = new TaiLieu();
                    tl.setMaTaiLieu(rs.getInt("MATAILIEU"));
                    tl.setTenTaiLieu(rs.getString("TENTAILIEU"));
                    tl.setLink(rs.getString("LINK"));
                    tl.setMaLopHoc(rs.getInt("MALOPHOC"));
                    list.add(tl);
                }
            }
        }
        return list;
    }
}
