package com.trungtam.dao;

import com.trungtam.model.VaiTro;
import com.trungtam.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VaiTroDAO {

    public List<VaiTro> findAll() throws SQLException {
        List<VaiTro> list = new ArrayList<>();
        String sql = "SELECT MAVAITRO, TENVAITRO FROM VAITRO";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                VaiTro vt = new VaiTro();
                vt.setMaVaiTro(rs.getInt("MAVAITRO"));
                vt.setTenVaiTro(rs.getString("TENVAITRO"));
                list.add(vt);
            }
        }
        return list;
    }

    public String getTenVaiTro(int maVaiTro) throws SQLException {
        String sql = "SELECT TENVAITRO FROM VAITRO WHERE MAVAITRO = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maVaiTro);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString("TENVAITRO") : null;
            }
        }
    }
}
