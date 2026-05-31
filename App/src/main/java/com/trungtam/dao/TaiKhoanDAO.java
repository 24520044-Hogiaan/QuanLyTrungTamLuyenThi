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

    public TaiKhoan findById(int maTaiKhoan) throws SQLException {
        String sql = "SELECT MATAIKHOAN, TENTAIKHOAN, MATKHAU, MAVAITRO, TRANGTHAITK, NGAYTAO, LANDN_CUOI FROM TAIKHOAN WHERE MATAIKHOAN = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maTaiKhoan);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    TaiKhoan tk = new TaiKhoan();
                    tk.setMaTaiKhoan(rs.getInt("MATAIKHOAN"));
                    tk.setTenTaiKhoan(rs.getString("TENTAIKHOAN"));
                    tk.setMatKhau(rs.getString("MATKHAU"));
                    tk.setMaVaiTro(rs.getInt("MAVAITRO"));
                    tk.setTrangThaiTK(rs.getString("TRANGTHAITK"));
                    return tk;
                }
            }
        }
        return null;
    }

    public void insert(TaiKhoan tk) throws SQLException {
        String getIdSql = "SELECT NVL(MAX(MATAIKHOAN), 0) + 1 FROM TAIKHOAN";
        String insertSql = "INSERT INTO TAIKHOAN (MATAIKHOAN, TENTAIKHOAN, MATKHAU, MAVAITRO, TRANGTHAITK, NGAYTAO) VALUES (?, ?, ?, ?, ?, SYSDATE)";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(getIdSql)) {
            if (rs.next()) {
                tk.setMaTaiKhoan(rs.getInt(1));
            }
            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setInt(1, tk.getMaTaiKhoan());
                ps.setString(2, tk.getTenTaiKhoan());
                ps.setString(3, tk.getMatKhau());
                ps.setInt(4, tk.getMaVaiTro());
                ps.setString(5, tk.getTrangThaiTK());
                ps.executeUpdate();
            }
        }
    }

    public void update(TaiKhoan tk) throws SQLException {
        String sql = "UPDATE TAIKHOAN SET TENTAIKHOAN = ?, MAVAITRO = ?, TRANGTHAITK = ? WHERE MATAIKHOAN = ?";
        if (tk.getMatKhau() != null && !tk.getMatKhau().isEmpty()) {
            sql = "UPDATE TAIKHOAN SET TENTAIKHOAN = ?, MAVAITRO = ?, TRANGTHAITK = ?, MATKHAU = ? WHERE MATAIKHOAN = ?";
        }
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tk.getTenTaiKhoan());
            ps.setInt(2, tk.getMaVaiTro());
            ps.setString(3, tk.getTrangThaiTK());
            if (tk.getMatKhau() != null && !tk.getMatKhau().isEmpty()) {
                ps.setString(4, tk.getMatKhau());
                ps.setInt(5, tk.getMaTaiKhoan());
            } else {
                ps.setInt(4, tk.getMaTaiKhoan());
            }
            ps.executeUpdate();
        }
    }

    public void updateStatus(int maTaiKhoan, String status) throws SQLException {
        String sql = "UPDATE TAIKHOAN SET TRANGTHAITK = ? WHERE MATAIKHOAN = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, maTaiKhoan);
            ps.executeUpdate();
        }
    }
}
