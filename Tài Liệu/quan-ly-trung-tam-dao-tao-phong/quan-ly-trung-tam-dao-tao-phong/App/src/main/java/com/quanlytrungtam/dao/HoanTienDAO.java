package com.quanlytrungtam.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.quanlytrungtam.model.HoanTienModel;

public class HoanTienDAO {

    private DBConnection dbConnection = new DBConnection();

    public List<HoanTienModel> getListYeuCau() {
        List<HoanTienModel> list = new ArrayList<>();
        Connection con = dbConnection.getConnection();
        if (con == null)
            return list;

        String sql = "SELECT * FROM HOANTRA";
        try {
            PreparedStatement statement = con.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                HoanTienModel ht = new HoanTienModel();
                ht.setMaHoanTra(rs.getInt("MAHOANTRA"));
                ht.setMaHoaDon(rs.getInt("MAHOADON"));
                ht.setMaHocVien(rs.getInt("MAHOCVIEN"));
                ht.setSoTien(rs.getDouble("SOTIEN"));
                ht.setLyDo(rs.getString("LYDO"));
                ht.setNgayYeuCau(rs.getDate("NGAYYEUCAU"));
                ht.setNgayHoanTra(rs.getDate("NGAYHOANTRA"));
                ht.setTrangThai(rs.getString("TRANGTHAI"));
                ht.setMaNhanVien(rs.getInt("MANHANVIEN"));
                ht.setNgayTao(rs.getTimestamp("NGAYTAO"));
                ht.setHinhThuc(rs.getString("HINHTHUC"));
                ht.setNguoiTao(rs.getInt("NGUOITAO"));
                list.add(ht);
            }
            statement.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConnection.closeConnection(con);
        }
        return list;
    }

    public boolean updateProcess(HoanTienModel ht) {
        Connection con = dbConnection.getConnection();
        if (con == null)
            return false;

        String sql = "UPDATE HOANTRA SET TRANGTHAI = ?, MANHANVIEN = ?, NGAYHOANTRA = ? WHERE MAHOANTRA = ?";
        try {
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, ht.getTrangThai());
            statement.setInt(2, ht.getMaNhanVien());
            statement.setDate(3, ht.getNgayHoanTra() != null ? new java.sql.Date(ht.getNgayHoanTra().getTime()) : null);
            statement.setInt(4, ht.getMaHoanTra());

            int rs = statement.executeUpdate();
            statement.close();
            return rs > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            dbConnection.closeConnection(con);
        }
    }
}
