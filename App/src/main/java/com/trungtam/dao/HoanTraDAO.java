package com.trungtam.dao;

import com.trungtam.model.HoanTra;
import com.trungtam.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HoanTraDAO {

    public List<HoanTra> getListHoanTra() {
        List<HoanTra> list = new ArrayList<>();
        String sql = "SELECT * FROM HOANTRA";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateTrangThai(int maHoanTra, String trangThai, int maNhanVien) {
        String sql = "UPDATE HOANTRA SET TRANGTHAI = ?, MANHANVIEN = ?, NGAYHOANTRA = SYSDATE WHERE MAHOANTRA = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, trangThai);
            ps.setInt(2, maNhanVien);
            ps.setInt(3, maHoanTra);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private HoanTra mapResultSet(ResultSet rs) throws SQLException {
        HoanTra ht = new HoanTra();
        ht.setMaHoanTra(rs.getInt("MAHOANTRA"));
        ht.setMaHoaDon(rs.getInt("MAHOADON"));
        ht.setMaHocVien(rs.getInt("MAHOCVIEN"));
        ht.setSoTien(rs.getDouble("SOTIEN"));
        ht.setLyDo(rs.getString("LYDO"));
        Date ngayYC = rs.getDate("NGAYYEUCAU");
        if (ngayYC != null) ht.setNgayYeuCau(ngayYC.toLocalDate());
        Date ngayHT = rs.getDate("NGAYHOANTRA");
        if (ngayHT != null) ht.setNgayHoanTra(ngayHT.toLocalDate());
        ht.setTrangThai(rs.getString("TRANGTHAI"));
        ht.setMaNhanVien(rs.getInt("MANHANVIEN"));
        ht.setHinhThuc(rs.getString("HINHTHUC"));
        return ht;
    }
}
