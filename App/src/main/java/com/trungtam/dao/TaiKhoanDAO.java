package com.trungtam.dao;

import com.trungtam.model.TaiKhoan;
import com.trungtam.util.DatabaseConnection;

import java.sql.*;

public class TaiKhoanDAO {

    private static final String LOGIN_QUERY =
            "SELECT MATAIKHOAN, TENTAIKHOAN, MATKHAU, MAVAITRO, TRANGTHAITK, NGAYTAO, LANDN_CUOI " +
            "FROM TAIKHOAN WHERE TENTAIKHOAN = ? AND TRANGTHAITK = 'Hoat dong'";

    private static final String UPDATE_LAST_LOGIN =
            "UPDATE TAIKHOAN SET LANDN_CUOI = SYSDATE WHERE MATAIKHOAN = ?";

    public TaiKhoan findByUsername(String tenTaiKhoan) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(LOGIN_QUERY)) {
            ps.setString(1, tenTaiKhoan);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    TaiKhoan tk = new TaiKhoan();
                    tk.setMaTaiKhoan(rs.getInt("MATAIKHOAN"));
                    tk.setTenTaiKhoan(rs.getString("TENTAIKHOAN"));
                    tk.setMatKhau(rs.getString("MATKHAU"));
                    tk.setMaVaiTro(rs.getInt("MAVAITRO"));
                    tk.setTrangThaiTK(rs.getString("TRANGTHAITK"));
                    Timestamp ngayTao = rs.getTimestamp("NGAYTAO");
                    if (ngayTao != null) tk.setNgayTao(ngayTao.toLocalDateTime());
                    Timestamp lanDN = rs.getTimestamp("LANDN_CUOI");
                    if (lanDN != null) tk.setLanDangNhapCuoi(lanDN.toLocalDateTime());
                    return tk;
                }
            }
        }
        return null;
    }

    public void updateLastLogin(int maTaiKhoan) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_LAST_LOGIN)) {
            ps.setInt(1, maTaiKhoan);
            ps.executeUpdate();
        }
    }
}
