package com.trungtam.dao;

import com.trungtam.model.HoaDonHocPhi;
import com.trungtam.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HoaDonHocPhiDAO {

    public List<HoaDonHocPhi> getListHoaDon() {
        List<HoaDonHocPhi> list = new ArrayList<>();
        String sql = "SELECT * FROM HOADONHOCPHI";
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

    public List<HoaDonHocPhi> getHoaDonByHocVien(int maHocVien) {
        List<HoaDonHocPhi> list = new ArrayList<>();
        String sql = "SELECT * FROM HOADONHOCPHI WHERE MAHOCVIEN = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maHocVien);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private HoaDonHocPhi mapResultSet(ResultSet rs) throws SQLException {
        HoaDonHocPhi hd = new HoaDonHocPhi();
        hd.setMaHoaDon(rs.getInt("MAHOADON"));
        hd.setMaHocVien(rs.getInt("MAHOCVIEN"));
        hd.setMaLop(rs.getInt("MALOP"));
        Date ngayLap = rs.getDate("NGAYLAP");
        if (ngayLap != null) hd.setNgayLap(ngayLap.toLocalDate());
        hd.setTongTien(rs.getDouble("TONGTIEN"));
        hd.setTrangThaiHD(rs.getString("TRANGTHAIHD"));
        hd.setLoaiHD(rs.getString("LOAIHD"));
        hd.setHinhThuc(rs.getString("HINHTHUC"));
        hd.setHinhThucTT(rs.getString("HINHTHUCTT"));
        hd.setThangTT(rs.getObject("THANGTT") != null ? rs.getInt("THANGTT") : null);
        hd.setNamTT(rs.getObject("NAMTT") != null ? rs.getInt("NAMTT") : null);
        hd.setGhiChu(rs.getString("GHICHU"));
        return hd;
    }

    public void processPayment(int maHoaDon, String paymentMethod, int staffId) throws SQLException {
        String sql = "{ CALL SP_PROCESS_PAYMENT(?, ?, ?) }";
        try (Connection con = DatabaseConnection.getConnection();
             java.sql.CallableStatement cs = con.prepareCall(sql)) {
            cs.setInt(1, maHoaDon);
            cs.setString(2, paymentMethod);
            cs.setInt(3, staffId);
            cs.execute();
        }
    }
}
