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
    public boolean insert(TaiLieu tl) throws SQLException {
        String getIdSql = "SELECT COALESCE(MAX(MATAILIEU), 0) + 1 FROM TAILIEU";
        String insertSql = "INSERT INTO TAILIEU(MATAILIEU, TENTAILIEU, LINK, MALOPHOC) VALUES (?, ?, ?, ?)";
        
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(getIdSql)) {
            if (rs.next()) {
                tl.setMaTaiLieu(rs.getInt(1));
            }
            try (PreparedStatement ps = con.prepareStatement(insertSql)) {
                ps.setInt(1, tl.getMaTaiLieu());
                ps.setString(2, tl.getTenTaiLieu());
                ps.setString(3, tl.getLink());
                ps.setInt(4, tl.getMaLopHoc());
                return ps.executeUpdate() > 0;
            }
        }
    }
}
