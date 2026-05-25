package com.quanlytrungtam.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.quanlytrungtam.model.NgayNghiLe;

public class NgayNghiLeDAO {
    private DBConnection dbConnection = new DBConnection();

    public NgayNghiLeDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            System.out.println("Can't load driver!");
        }
    }

    public List<NgayNghiLe> getListNgayNghiLe() {
        Connection con = dbConnection.getConnection();
        List<NgayNghiLe> list = new ArrayList<>();

        if (con == null)
            return list;

        String sql = "SELECT * FROM NGAYNGHILE";
        try {
            Statement selectStatement = con.createStatement();
            ResultSet result = selectStatement.executeQuery(sql);

            while (result.next()) {
                list.add(new NgayNghiLe(
                        result.getInt("MANGAYNGHI"),
                        result.getDate("NGAYBATDAU"),
                        result.getDate("NGAYKETTHUC")));
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

    public boolean addNgayNghiLe(NgayNghiLe nnl) {
        Connection con = dbConnection.getConnection();
        if (con == null)
            return false;

        String sql = "INSERT INTO NGAYNGHILE(MANGAYNGHI, NGAYBATDAU, NGAYKETTHUC) VALUES (?, ?, ?)";
        try {
            PreparedStatement insertStatement = con.prepareStatement(sql);
            insertStatement.setInt(1, nnl.getMaNgayNghi());
            insertStatement.setDate(2, new java.sql.Date(nnl.getNgayBatDau().getTime()));
            insertStatement.setDate(3, new java.sql.Date(nnl.getNgayKetThuc().getTime()));
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

    public boolean editNgayNghiLe(NgayNghiLe nnl) {
        Connection con = dbConnection.getConnection();
        if (con == null)
            return false;

        String sql = "UPDATE NGAYNGHILE SET NGAYBATDAU = ?, NGAYKETTHUC = ? WHERE MANGAYNGHI = ?";
        try {
            PreparedStatement updateStatement = con.prepareStatement(sql);
            updateStatement.setDate(1, new java.sql.Date(nnl.getNgayBatDau().getTime()));
            updateStatement.setDate(2, new java.sql.Date(nnl.getNgayKetThuc().getTime()));
            updateStatement.setInt(3, nnl.getMaNgayNghi());
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

    public boolean deleteNgayNghiLe(NgayNghiLe nnl) {
        Connection con = dbConnection.getConnection();
        if (con == null)
            return false;

        String sql = "DELETE FROM NGAYNGHILE WHERE MANGAYNGHI = ?";
        try {
            PreparedStatement deleteStatement = con.prepareStatement(sql);
            deleteStatement.setInt(1, nnl.getMaNgayNghi());
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
}