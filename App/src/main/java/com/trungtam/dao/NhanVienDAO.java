package com.trungtam.dao;

import com.trungtam.model.NhanVien;
import com.trungtam.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NhanVienDAO {

    private static final String SELECT_ALL =
            "SELECT MANHANVIEN, HOTEN, GIOITINH, NGAYSINH, SDT, EMAIL, CHUCVU, MATAIKHOAN FROM NHANVIEN";

    private static final String SELECT_BY_ID = SELECT_ALL + " WHERE MANHANVIEN = ?";

    public List<NhanVien> findAll() throws SQLException {
        List<NhanVien> result = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL)) {
            while (rs.next()) {
                result.add(mapRow(rs));
            }
        }
        return result;
    }

    public NhanVien findById(int maNhanVien) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) {
            ps.setInt(1, maNhanVien);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    private NhanVien mapRow(ResultSet rs) throws SQLException {
        NhanVien nv = new NhanVien();
        nv.setMaNhanVien(rs.getInt("MANHANVIEN"));
        nv.setHoTen(rs.getString("HOTEN"));
        nv.setGioiTinh(rs.getString("GIOITINH"));
        Date ngaySinh = rs.getDate("NGAYSINH");
        if (ngaySinh != null) nv.setNgaySinh(ngaySinh.toLocalDate());
        nv.setSoDienThoai(rs.getString("SDT"));
        nv.setEmail(rs.getString("EMAIL"));
        nv.setChucVu(rs.getString("CHUCVU"));
        nv.setMaTaiKhoan(rs.getInt("MATAIKHOAN"));
        return nv;
    }
}
