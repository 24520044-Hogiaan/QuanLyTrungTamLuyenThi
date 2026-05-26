package com.trungtam.dao;

import com.trungtam.model.BoMon;
import com.trungtam.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BoMonDAO {

    public List<BoMon> getListBoMon() {
        List<BoMon> list = new ArrayList<>();
        String sql = "SELECT * FROM BOMON";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                BoMon bm = new BoMon();
                bm.setMaBoMon(rs.getInt("MABOMON"));
                bm.setTenBoMon(rs.getString("TENBM"));
                list.add(bm);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addBoMon(BoMon bm) {
        String sql = "INSERT INTO BOMON(MABOMON, TENBM) VALUES (?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, bm.getMaBoMon());
            ps.setString(2, bm.getTenBoMon());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean editBoMon(BoMon bm) {
        String sql = "UPDATE BOMON SET TENBM = ? WHERE MABOMON = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, bm.getTenBoMon());
            ps.setInt(2, bm.getMaBoMon());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteBoMon(BoMon bm) {
        String sql = "DELETE FROM BOMON WHERE MABOMON = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, bm.getMaBoMon());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
