package com.quanlytrungtam.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.quanlytrungtam.model.HocVien;

public class HocVienDAO {
    private DBConnection dbConnection = new DBConnection();

    public List<HocVien> getListHocVien() {
        List<HocVien> list = new ArrayList<>();
        Connection con = dbConnection.getConnection();
        if (con == null) {
            return list;
        }
        String sql = "SELECT * FROM hocvien";
        try {
            PreparedStatement statement = con.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                list.add(new HocVien(rs.getInt("MAHOCVIEN"), rs.getString("MATAIKHOAN"), rs.getString("HOTEN"),
                        rs.getDate("NGAYSINH"), rs.getString("GIOITINH"), rs.getString("DIACHI"), rs.getString("SDT"),
                        rs.getString("EMAIL")));
            }
            statement.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dbConnection.closeConnection(con);
        return list;
    }

    public boolean add(HocVien hv) {
        Connection con = dbConnection.getConnection();
        if (con == null) {
            return false;
        }
        String sql = "INSERT INTO hocvien(MAHOCVIEN, MATAIKHOAN, HOTEN, NGAYSINH, GIOITINH, DIACHI, SDT, EMAIL) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, hv.getMaHocVien());
            statement.setString(2, hv.getTaiKhoanNganHang());
            statement.setString(3, hv.getHoTen());
            statement.setDate(4, hv.getNgaySinh() != null ? new java.sql.Date(hv.getNgaySinh().getTime()) : null);
            statement.setString(5, hv.getGioiTinh());
            statement.setString(6, hv.getDiaChi());
            statement.setString(7, hv.getSDT());
            statement.setString(8, hv.getEmail());
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

    public boolean edit(HocVien hv) {
        Connection con = dbConnection.getConnection();
        if (con == null) {
            return false;
        }
        String sql = "UPDATE hocvien SET MATAIKHOAN = ?, HOTEN = ?, NGAYSINH = ?, GIOITINH = ?, DIACHI = ?, SDT = ?, EMAIL = ? WHERE MAHOCVIEN = ?";
        try {
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, hv.getTaiKhoanNganHang());
            statement.setString(2, hv.getHoTen());
            statement.setDate(3, hv.getNgaySinh() != null ? new java.sql.Date(hv.getNgaySinh().getTime()) : null);
            statement.setString(4, hv.getGioiTinh());
            statement.setString(5, hv.getDiaChi());
            statement.setString(6, hv.getSDT());
            statement.setString(7, hv.getEmail());
            statement.setInt(8, hv.getMaHocVien());
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

    public boolean delete(int maHocVien) {
        Connection con = dbConnection.getConnection();
        if (con == null) {
            return false;
        }
        String sql = "DELETE FROM hocvien WHERE MAHOCVIEN = ?";
        try {
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, maHocVien);
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

    public List<HocVien> search(String keyword) {
        List<HocVien> list = new ArrayList<>();
        Connection con = dbConnection.getConnection();
        if (con == null) {
            return list;
        }
        String sql = "SELECT * FROM hocvien WHERE CAST(MAHOCVIEN AS VARCHAR2(20)) LIKE ? OR CAST(MATAIKHOAN AS VARCHAR2(20)) LIKE ? OR HOTEN LIKE ? OR CAST(NGAYSINH AS VARCHAR2(20)) LIKE ? OR GIOITINH LIKE ? OR DIACHI LIKE ? OR SDT LIKE ? OR EMAIL LIKE ?";
        try {
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, "%" + keyword + "%");
            statement.setString(2, "%" + keyword + "%");
            statement.setString(3, "%" + keyword + "%");
            statement.setString(4, "%" + keyword + "%");
            statement.setString(5, "%" + keyword + "%");
            statement.setString(6, "%" + keyword + "%");
            statement.setString(7, "%" + keyword + "%");
            statement.setString(8, "%" + keyword + "%");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                list.add(new HocVien(rs.getInt("MAHOCVIEN"), rs.getString("MATAIKHOAN"), rs.getString("HOTEN"),
                        rs.getDate("NGAYSINH"), rs.getString("GIOITINH"), rs.getString("DIACHI"), rs.getString("SDT"),
                        rs.getString("EMAIL")));
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
