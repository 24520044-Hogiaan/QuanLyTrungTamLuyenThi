package com.trungtam.dao;

import com.trungtam.model.HocVien;
import com.trungtam.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HocVienDAO {

    private static final String SELECT_ALL =
            "SELECT MAHOCVIEN, MATAIKHOAN, HOTEN, NGAYSINH, GIOITINH, DIACHI, SDT, EMAIL FROM HOCVIEN";

    private static final String SELECT_BY_ID = SELECT_ALL + " WHERE MAHOCVIEN = ?";

    private static final String INSERT =
            "INSERT INTO HOCVIEN (MAHOCVIEN, MATAIKHOAN, HOTEN, NGAYSINH, GIOITINH, DIACHI, SDT, EMAIL) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE =
            "UPDATE HOCVIEN SET MATAIKHOAN = ?, HOTEN = ?, NGAYSINH = ?, GIOITINH = ?, DIACHI = ?, SDT = ?, EMAIL = ? " +
            "WHERE MAHOCVIEN = ?";

    private static final String DELETE = "DELETE FROM HOCVIEN WHERE MAHOCVIEN = ?";

    public List<HocVien> findAll() throws SQLException {
        List<HocVien> result = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL)) {
            while (rs.next()) {
                result.add(mapRow(rs));
            }
        }
        return result;
    }

    public HocVien findById(int maHocVien) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) {
            ps.setInt(1, maHocVien);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public void insert(HocVien hv) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT)) {
            ps.setInt(1, hv.getMaHocVien());
            ps.setInt(2, hv.getMaTaiKhoan());
            ps.setString(3, hv.getHoTen());
            ps.setDate(4, hv.getNgaySinh() != null ? Date.valueOf(hv.getNgaySinh()) : null);
            ps.setString(5, hv.getGioiTinh());
            ps.setString(6, hv.getDiaChi());
            ps.setString(7, hv.getSoDienThoai());
            ps.setString(8, hv.getEmail());
            ps.executeUpdate();
        }
    }

    public void update(HocVien hv) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE)) {
            ps.setInt(1, hv.getMaTaiKhoan());
            ps.setString(2, hv.getHoTen());
            ps.setDate(3, hv.getNgaySinh() != null ? Date.valueOf(hv.getNgaySinh()) : null);
            ps.setString(4, hv.getGioiTinh());
            ps.setString(5, hv.getDiaChi());
            ps.setString(6, hv.getSoDienThoai());
            ps.setString(7, hv.getEmail());
            ps.setInt(8, hv.getMaHocVien());
            ps.executeUpdate();
        }
    }

    public void delete(int maHocVien) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE)) {
            ps.setInt(1, maHocVien);
            ps.executeUpdate();
        }
    }

    private HocVien mapRow(ResultSet rs) throws SQLException {
        HocVien hv = new HocVien();
        hv.setMaHocVien(rs.getInt("MAHOCVIEN"));
        hv.setMaTaiKhoan(rs.getInt("MATAIKHOAN"));
        hv.setHoTen(rs.getString("HOTEN"));
        Date ngaySinh = rs.getDate("NGAYSINH");
        if (ngaySinh != null) hv.setNgaySinh(ngaySinh.toLocalDate());
        hv.setGioiTinh(rs.getString("GIOITINH"));
        hv.setDiaChi(rs.getString("DIACHI"));
        hv.setSoDienThoai(rs.getString("SDT"));
        hv.setEmail(rs.getString("EMAIL"));
        return hv;
    }
}
