package com.quanlytrungtam.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.quanlytrungtam.model.KhoaHoc;

public class KhoaHocDAO {

    private DBConnection dbConnection = new DBConnection();

    public List<KhoaHoc> getListKhoaHoc() {
        List<KhoaHoc> list = new ArrayList<>();
        Connection con = dbConnection.getConnection();
        if (con == null) {
            return list;
        }
        String sql = "SELECT * FROM KHOAHOC";
        try {
            PreparedStatement statement = con.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                KhoaHoc kh = new KhoaHoc();
                kh.setMakhoahoc(rs.getInt("MAKHOAHOC"));
                kh.setTenkh(rs.getString("TENKH"));
                kh.setMota(rs.getString("MOTA"));
                
                double hpThang = rs.getDouble("HOCPHITHANG");
                kh.setHocphithang(rs.wasNull() ? 0.0 : hpThang);
                
                double hpToanKh = rs.getDouble("HOCPHITOANKH");
                kh.setHocphitoankh(rs.wasNull() ? 0.0 : hpToanKh);
                
                kh.setMabomon(rs.getInt("MABOMON"));
                kh.setCapdo(rs.getString("CAPDO"));
                list.add(kh);
            }
            statement.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dbConnection.closeConnection(con);
        return list;
    }

    public boolean add(KhoaHoc kh) {
        Connection con = dbConnection.getConnection();
        if (con == null) {
            return false;
        }
        String sql = "INSERT INTO KHOAHOC(MAKHOAHOC, TENKH, MOTA, HOCPHITHANG, HOCPHITOANKH, MABOMON, CAPDO) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, kh.getMakhoahoc());
            statement.setString(2, kh.getTenkh());
            statement.setString(3, kh.getMota());
            
            if (kh.getHocphithang() == null || kh.getHocphithang() <= 0.0) {
                statement.setNull(4, java.sql.Types.DOUBLE);
            } else {
                statement.setDouble(4, kh.getHocphithang());
            }
            
            if (kh.getHocphitoankh() == null || kh.getHocphitoankh() <= 0.0) {
                statement.setNull(5, java.sql.Types.DOUBLE);
            } else {
                statement.setDouble(5, kh.getHocphitoankh());
            }
            
            statement.setInt(6, kh.getMabomon());
            statement.setString(7, kh.getCapdo());
            int rs = statement.executeUpdate();
            statement.close();
            dbConnection.closeConnection(con);
            return rs > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            dbConnection.closeConnection(con);
            return false;
        }
    }

    public boolean edit(KhoaHoc kh) {
        Connection con = dbConnection.getConnection();
        if (con == null) {
            return false;
        }
        String sql = "UPDATE KHOAHOC SET TENKH = ?, MOTA = ?, HOCPHITHANG = ?, HOCPHITOANKH = ?, MABOMON = ?, CAPDO = ? WHERE MAKHOAHOC = ?";
        try {
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, kh.getTenkh());
            statement.setString(2, kh.getMota());
            
            if (kh.getHocphithang() == null || kh.getHocphithang() <= 0.0) {
                statement.setNull(3, java.sql.Types.DOUBLE);
            } else {
                statement.setDouble(3, kh.getHocphithang());
            }
            
            if (kh.getHocphitoankh() == null || kh.getHocphitoankh() <= 0.0) {
                statement.setNull(4, java.sql.Types.DOUBLE);
            } else {
                statement.setDouble(4, kh.getHocphitoankh());
            }
            
            statement.setInt(5, kh.getMabomon());
            statement.setString(6, kh.getCapdo());
            statement.setInt(7, kh.getMakhoahoc());
            int rs = statement.executeUpdate();
            statement.close();
            dbConnection.closeConnection(con);
            return rs > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            dbConnection.closeConnection(con);
            return false;
        }
    }

    public boolean delete(KhoaHoc kh) {
        Connection con = dbConnection.getConnection();
        if (con == null) {
            return false;
        }
        String sql = "DELETE FROM KHOAHOC WHERE MAKHOAHOC = ?";
        try {
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, kh.getMakhoahoc());
            int rs = statement.executeUpdate();
            statement.close();
            dbConnection.closeConnection(con);
            return rs > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            dbConnection.closeConnection(con);
            return false;
        }
    }

    public List<KhoaHoc> search(String keyword) {
        List<KhoaHoc> list = new ArrayList<>();
        Connection con = dbConnection.getConnection();
        if (con == null) {
            return list;
        }
        String sql = "SELECT * FROM KHOAHOC WHERE TENKH LIKE ? OR MOTA LIKE ? OR CAPDO LIKE ?";
        try {
            PreparedStatement statement = con.prepareStatement(sql);
            String val = "%" + keyword + "%";
            statement.setString(1, val);
            statement.setString(2, val);
            statement.setString(3, val);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                KhoaHoc kh = new KhoaHoc();
                kh.setMakhoahoc(rs.getInt("MAKHOAHOC"));
                kh.setTenkh(rs.getString("TENKH"));
                kh.setMota(rs.getString("MOTA"));
                
                double hpThang = rs.getDouble("HOCPHITHANG");
                kh.setHocphithang(rs.wasNull() ? 0.0 : hpThang);
                
                double hpToanKh = rs.getDouble("HOCPHITOANKH");
                kh.setHocphitoankh(rs.wasNull() ? 0.0 : hpToanKh);
                
                kh.setMabomon(rs.getInt("MABOMON"));
                kh.setCapdo(rs.getString("CAPDO"));
                list.add(kh);
            }
            statement.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dbConnection.closeConnection(con);
        return list;
    }
}