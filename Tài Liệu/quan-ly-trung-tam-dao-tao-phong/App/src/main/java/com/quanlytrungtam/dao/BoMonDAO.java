package com.quanlytrungtam.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.quanlytrungtam.model.BoMon;

public class BoMonDAO {
    private DBConnection dbConnection = new DBConnection();

    public BoMonDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            System.out.println("Can't load driver!");
        }
    }

    public List<BoMon> getListBoMon() {
        Connection con = dbConnection.getConnection();
        List<BoMon> list = new ArrayList<>();

        if (con == null)
            return list;

        String sql = "SELECT * FROM bomon";
        try {
            Statement selectStatement = con.createStatement();
            ResultSet result = selectStatement.executeQuery(sql);

            while (result.next()) {
                list.add(new BoMon(
                        result.getInt("MABOMON"),
                        result.getString("TENBM")));
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

    public boolean addBoMon(BoMon bm) {
        Connection con = dbConnection.getConnection();
        if (con == null)
            return false;

        String sql = "INSERT INTO bomon(MABOMON, TENBM) VALUES (?, ?)";
        try {
            PreparedStatement insertStatement = con.prepareStatement(sql);
            insertStatement.setInt(1, bm.getMaBoMon());
            insertStatement.setString(2, bm.getTenBoMon());
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

    public boolean editBoMon(BoMon bm) {
        Connection con = dbConnection.getConnection();
        if (con == null)
            return false;

        String sql = "UPDATE bomon SET TENBM = ? WHERE MABOMON = ?";
        try {
            PreparedStatement updateStatement = con.prepareStatement(sql);
            updateStatement.setString(1, bm.getTenBoMon());
            updateStatement.setInt(2, bm.getMaBoMon());
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

    public boolean deleteBoMon(BoMon bm) {
        Connection con = dbConnection.getConnection();
        if (con == null)
            return false;

        String sql = "DELETE FROM bomon WHERE MABOMON = ?";
        try {
            PreparedStatement deleteStatement = con.prepareStatement(sql);
            deleteStatement.setInt(1, bm.getMaBoMon());
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

    public List<BoMon> searchBoMon(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getListBoMon();
        }

        Connection con = dbConnection.getConnection();
        List<BoMon> list = new ArrayList<>();
        if (con == null) {
            return list;
        }

        String sql = "SELECT * FROM bomon WHERE CAST(MABOMON AS CHAR) LIKE ? OR TENBM LIKE ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new BoMon(
                        rs.getInt("MABOMON"),
                        rs.getString("TENBM")));
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
