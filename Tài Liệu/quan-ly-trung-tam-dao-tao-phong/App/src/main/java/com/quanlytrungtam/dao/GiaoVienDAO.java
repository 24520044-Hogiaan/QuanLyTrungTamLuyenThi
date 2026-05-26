package com.quanlytrungtam.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.quanlytrungtam.model.GiaoVien;

public class GiaoVienDAO {
    private DBConnection dbConnection = new DBConnection();

    public GiaoVienDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            System.out.println("Can't load driver!");
        }
    }

    public List<GiaoVien> getListGiaoVien() {
        Connection con = dbConnection.getConnection();
        List<GiaoVien> list = new ArrayList<>();

        if (con == null)
            return list;

        String sql = "SELECT * FROM giaovien";
        try {
            Statement selectStatement = con.createStatement();
            ResultSet result = selectStatement.executeQuery(sql);

            while (result.next()) {
                list.add(new GiaoVien(
                        result.getInt("maGiaoVien"),
                        result.getInt("maNhanVien"),
                        result.getString("maBoMon"),
                        result.getString("bangCap"),
                        result.getString("trangThai")));
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

    public boolean add(GiaoVien gv) {
        Connection con = dbConnection.getConnection();
        if (con == null)
            return false;

        String sql = "INSERT INTO giaovien(maGiaoVien, maNhanVien, maBoMon, bangCap, trangThai) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement insertStatement = con.prepareStatement(sql);
            insertStatement.setInt(1, gv.getMaGiaoVien());
            insertStatement.setInt(2, gv.getMaNhanVien());
            insertStatement.setString(3, gv.getChuyenMon());
            insertStatement.setString(4, gv.getBangCap());
            insertStatement.setString(5, gv.getTrangThai());
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

    public boolean edit(GiaoVien gv) {
        Connection con = dbConnection.getConnection();
        if (con == null)
            return false;

        String sql = "UPDATE giaovien set maNhanVien = ?, maBoMon = ?, bangCap = ?, trangThai = ? WHERE maGiaoVien = ?";
        try {
            PreparedStatement updateStatement = con.prepareStatement(sql);
            updateStatement.setInt(1, gv.getMaNhanVien());
            updateStatement.setString(2, gv.getChuyenMon());
            updateStatement.setString(3, gv.getBangCap());
            updateStatement.setString(4, gv.getTrangThai());
            updateStatement.setInt(5, gv.getMaGiaoVien());
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

    public boolean delete(GiaoVien gv) {
        Connection con = dbConnection.getConnection();
        if (con == null)
            return false;

        String sql = "DELETE FROM giaovien WHERE maGiaoVien = ?";
        try {
            PreparedStatement deleteStatement = con.prepareStatement(sql);
            deleteStatement.setInt(1, gv.getMaGiaoVien());
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

    public List<GiaoVien> searchGiaoVien(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getListGiaoVien();
        }

        Connection con = dbConnection.getConnection();
        List<GiaoVien> giaoviens = new ArrayList<>();
        if (con == null) {
            return giaoviens;
        }

        String sql = "SELECT * FROM giaovien WHERE CAST(maGiaoVien AS CHAR) LIKE ?;";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                giaoviens.add(new GiaoVien(
                        rs.getInt("maGiaoVien"),
                        rs.getInt("maNhanVien"),
                        rs.getString("maBoMon"),
                        rs.getString("bangCap"),
                        rs.getString("trangThai")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConnection.closeConnection(con);
        }
        return giaoviens;
    }
}
