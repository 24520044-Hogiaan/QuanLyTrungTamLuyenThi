package com.trungtam.dao;

import com.trungtam.model.PhongHoc;
import com.trungtam.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhongHocDAO {

    public List<PhongHoc> getListPhongHoc() {
        List<PhongHoc> list = new ArrayList<>();
        String sql = "SELECT * FROM PHONGHOC";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                PhongHoc ph = new PhongHoc();
                ph.setMaPhongHoc(rs.getInt("MAPHONGHOC"));
                ph.setTenPhong(rs.getString("TENPHONG"));
                ph.setSucChua(rs.getInt("SUCHUA"));
                ph.setTrangThai(rs.getString("TRANGTHAI"));
                list.add(ph);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addPhongHoc(PhongHoc ph) {
        String sql = "INSERT INTO PHONGHOC(MAPHONGHOC, TENPHONG, SUCHUA, TRANGTHAI) VALUES (?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, ph.getMaPhongHoc());
            ps.setString(2, ph.getTenPhong());
            ps.setInt(3, ph.getSucChua());
            ps.setString(4, ph.getTrangThai());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean editPhongHoc(PhongHoc ph) {
        String sql = "UPDATE PHONGHOC SET TENPHONG = ?, SUCHUA = ?, TRANGTHAI = ? WHERE MAPHONGHOC = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ph.getTenPhong());
            ps.setInt(2, ph.getSucChua());
            ps.setString(3, ph.getTrangThai());
            ps.setInt(4, ph.getMaPhongHoc());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePhongHoc(int maPhongHoc) {
        String sql = "DELETE FROM PHONGHOC WHERE MAPHONGHOC = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maPhongHoc);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
