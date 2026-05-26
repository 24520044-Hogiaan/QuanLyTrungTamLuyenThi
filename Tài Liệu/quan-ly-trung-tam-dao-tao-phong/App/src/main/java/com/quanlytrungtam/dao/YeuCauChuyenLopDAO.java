package com.quanlytrungtam.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.quanlytrungtam.model.YeuCauChuyenLop;

public class YeuCauChuyenLopDAO {

    private DBConnection dbConnection = new DBConnection();

    public List<YeuCauChuyenLop> getListYeuCau() {
        List<YeuCauChuyenLop> list = new ArrayList<>();
        Connection con = dbConnection.getConnection();
        if (con == null)
            return list;

        String sql = "SELECT * FROM YEUCAUCHUYENLOP";
        try {
            PreparedStatement statement = con.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                YeuCauChuyenLop yc = new YeuCauChuyenLop();
                yc.setMaYeuCau(rs.getInt("MAYEUCAU"));
                yc.setMaHocVien(rs.getInt("MAHOCVIEN"));
                yc.setMaLopCu(rs.getInt("MALOPCU"));
                yc.setMaLopMoi(rs.getInt("MALOPMOI"));
                yc.setLyDo(rs.getString("LYDO"));
                yc.setNgayYeuCau(rs.getDate("NGAYYEUCAU"));
                yc.setTrangThai(rs.getString("TRANGTHAI"));
                yc.setMaNhanVien(rs.getInt("MANHANVIEN"));
                yc.setNgayXuLy(rs.getDate("NGAYXULY"));
                yc.setGhiChu(rs.getString("GHICHU"));
                list.add(yc);
            }
            statement.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dbConnection.closeConnection(con);
        return list;
    }

    public boolean add(YeuCauChuyenLop yc) {
        Connection con = dbConnection.getConnection();
        if (con == null)
            return false;

        String sql = "INSERT INTO YEUCAUCHUYENLOP (MAYEUCAU, MAHOCVIEN, MALOPCU, MALOPMOI, LYDO, NGAYYEUCAU, TRANGTHAI) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, yc.getMaYeuCau());
            statement.setInt(2, yc.getMaHocVien());
            statement.setInt(3, yc.getMaLopCu());
            statement.setInt(4, yc.getMaLopMoi());
            statement.setString(5, yc.getLyDo());
            statement.setDate(6, yc.getNgayYeuCau() != null ? new java.sql.Date(yc.getNgayYeuCau().getTime()) : null);
            statement.setString(7, yc.getTrangThai());

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

    public boolean updateProcess(YeuCauChuyenLop yc) {
        Connection con = dbConnection.getConnection();
        if (con == null)
            return false;

        String sql = "UPDATE YEUCAUCHUYENLOP SET TRANGTHAI = ?, MANHANVIEN = ?, NGAYXULY = ?, GHICHU = ? WHERE MAYEUCAU = ?";
        try {
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, yc.getTrangThai());
            statement.setInt(2, yc.getMaNhanVien());
            statement.setDate(3, yc.getNgayXuLy() != null ? new java.sql.Date(yc.getNgayXuLy().getTime()) : null);
            statement.setString(4, yc.getGhiChu());
            statement.setInt(5, yc.getMaYeuCau());

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

    public boolean delete(int maYeuCau) {
        Connection con = dbConnection.getConnection();
        if (con == null)
            return false;

        String sql = "DELETE FROM YEUCAUCHUYENLOP WHERE MAYEUCAU = ?";
        try {
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, maYeuCau);
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
}
