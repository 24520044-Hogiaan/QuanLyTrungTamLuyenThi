package com.quanlytrungtam.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.quanlytrungtam.model.PhongHoc;

public class PhongHocDAO {
    private DBConnection dbConnection = new DBConnection();

    public PhongHocDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            System.out.println("Can't load driver!");
        }
    }

    public List<PhongHoc> getListPhongHoc() {
        Connection con = dbConnection.getConnection();
        List<PhongHoc> list = new ArrayList<>();

        if (con == null)
            return list;

        String sql = "SELECT * FROM phonghoc";
        try {
            Statement selectStatement = con.createStatement();
            ResultSet result = selectStatement.executeQuery(sql);

            while (result.next()) {
                list.add(new PhongHoc(
                        result.getInt("MAPHONGHOC"),
                        result.getString("TENPHONG"),
                        result.getInt("SUCHUA"),
                        result.getString("TRANGTHAI")));
            }
            selectStatement.close();
            result.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(con);
        }
        return list;
    }

    public boolean addPhongHoc(PhongHoc ph) {
        Connection con = dbConnection.getConnection();
        if (con == null)
            return false;

        String sql = "INSERT INTO phonghoc(MAPHONGHOC, TENPHONG, SUCHUA, TRANGTHAI) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement insertStatement = con.prepareStatement(sql);
            insertStatement.setInt(1, ph.getMaPhongHoc());
            insertStatement.setString(2, ph.getTenPhong());
            insertStatement.setInt(3, ph.getSucChua());
            insertStatement.setString(4, ph.getTrangThai());
            insertStatement.executeUpdate();
            insertStatement.close();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            dbConnection.closeConnection(con);
        }
    }

    public boolean editPhongHoc(PhongHoc ph) {
        Connection con = dbConnection.getConnection();
        if (con == null)
            return false;

        String sql = "UPDATE phonghoc SET TENPHONG = ?, SUCHUA = ?, TRANGTHAI = ? WHERE MAPHONGHOC = ?";
        try {
            PreparedStatement updateStatement = con.prepareStatement(sql);
            updateStatement.setString(1, ph.getTenPhong());
            updateStatement.setInt(2, ph.getSucChua());
            updateStatement.setString(3, ph.getTrangThai());
            updateStatement.setInt(4, ph.getMaPhongHoc());
            updateStatement.executeUpdate();
            updateStatement.close();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            dbConnection.closeConnection(con);
        }
    }

    public boolean deletePhongHoc(PhongHoc ph) {
        Connection con = dbConnection.getConnection();
        if (con == null)
            return false;

        String sql = "DELETE FROM phonghoc WHERE MAPHONGHOC = ?";
        try {
            PreparedStatement deleteStatement = con.prepareStatement(sql);
            deleteStatement.setInt(1, ph.getMaPhongHoc());
            deleteStatement.executeUpdate();
            deleteStatement.close();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            dbConnection.closeConnection(con);
        }
    }

    public List<PhongHoc> searchPhongHoc(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getListPhongHoc();
        }

        Connection con = dbConnection.getConnection();
        List<PhongHoc> list = new ArrayList<>();
        if (con == null)
            return list;

        String sql = "SELECT * FROM phonghoc WHERE CAST(MAPHONGHOC AS CHAR) LIKE ? OR TENPHONG LIKE ? OR TRANGTHAI LIKE ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new PhongHoc(
                        rs.getInt("MAPHONGHOC"),
                        rs.getString("TENPHONG"),
                        rs.getInt("SUCHUA"),
                        rs.getString("TRANGTHAI")));
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConnection.closeConnection(con);
        }
        return list;
    }
}