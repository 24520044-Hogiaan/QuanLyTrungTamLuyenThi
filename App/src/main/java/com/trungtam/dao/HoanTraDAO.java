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

    public void processRefund(int refundId, int staffId, String approveOrReject, String note) throws SQLException {
        String sql = "{ CALL SP_PROCESS_REFUND(?, ?, ?, ?) }";
        try (Connection con = DatabaseConnection.getConnection();
             java.sql.CallableStatement cs = con.prepareCall(sql)) {
            cs.setInt(1, refundId);
            cs.setInt(2, staffId);
            cs.setString(3, approveOrReject);
            cs.setString(4, note);
            cs.execute();
        }
    }

    public boolean insert(HoanTra ht) {
        String sqlMax = "SELECT COALESCE(MAX(MAHOANTRA), 0) + 1 FROM HOANTRA";
        String sqlIns = "INSERT INTO HOANTRA(MAHOANTRA, MAHOADON, MAHOCVIEN, SOTIEN, LYDO, NGAYYEUCAU, TRANGTHAI, HINHTHUC) " +
                        "VALUES (?, ?, ?, ?, ?, SYSDATE, 'Cho duyet', 'Chuyen Khoan')";
        try {
            try (Connection con = DatabaseConnection.getConnection();
                 Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(sqlMax)) {
                if (rs.next()) ht.setMaHoanTra(rs.getInt(1));
            }
            try (Connection con = DatabaseConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sqlIns)) {
                ps.setInt(1, ht.getMaHoanTra());
                ps.setInt(2, ht.getMaHoaDon());
                ps.setInt(3, ht.getMaHocVien());
                ps.setDouble(4, ht.getSoTien());
                ps.setString(5, ht.getLyDo());
                ps.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
