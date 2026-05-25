package com.trungtam.dao;

import com.trungtam.model.GiaoVien;
import com.trungtam.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GiaoVienDAO {

    private static final String SELECT_ALL =
            "SELECT gv.MAGIAOVIEN, gv.MANHANVIEN, gv.MABOMON, gv.BANGCAP, gv.TRANGTHAI, " +
            "nv.HOTEN, nv.EMAIL, nv.SDT, nv.GIOITINH " +
            "FROM GIAOVIEN gv JOIN NHANVIEN nv ON gv.MANHANVIEN = nv.MANHANVIEN";

    private static final String SELECT_BY_ID = SELECT_ALL + " WHERE gv.MAGIAOVIEN = ?";

    private static final String INSERT =
            "INSERT INTO GIAOVIEN (MAGIAOVIEN, MANHANVIEN, MABOMON, BANGCAP, TRANGTHAI) VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE =
            "UPDATE GIAOVIEN SET MANHANVIEN = ?, MABOMON = ?, BANGCAP = ?, TRANGTHAI = ? WHERE MAGIAOVIEN = ?";

    private static final String DELETE = "DELETE FROM GIAOVIEN WHERE MAGIAOVIEN = ?";

    public List<GiaoVien> findAll() throws SQLException {
        List<GiaoVien> result = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL)) {
            while (rs.next()) {
                result.add(mapRow(rs));
            }
        }
        return result;
    }

    public GiaoVien findById(int maGiaoVien) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) {
            ps.setInt(1, maGiaoVien);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public void insert(GiaoVien gv) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT)) {
            ps.setInt(1, gv.getMaGiaoVien());
            ps.setInt(2, gv.getMaNhanVien());
            ps.setString(3, gv.getMaBoMon());
            ps.setString(4, gv.getBangCap());
            ps.setString(5, gv.getTrangThai());
            ps.executeUpdate();
        }
    }

    public void update(GiaoVien gv) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE)) {
            ps.setInt(1, gv.getMaNhanVien());
            ps.setString(2, gv.getMaBoMon());
            ps.setString(3, gv.getBangCap());
            ps.setString(4, gv.getTrangThai());
            ps.setInt(5, gv.getMaGiaoVien());
            ps.executeUpdate();
        }
    }

    public void delete(int maGiaoVien) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE)) {
            ps.setInt(1, maGiaoVien);
            ps.executeUpdate();
        }
    }

    private GiaoVien mapRow(ResultSet rs) throws SQLException {
        GiaoVien gv = new GiaoVien();
        gv.setMaGiaoVien(rs.getInt("MAGIAOVIEN"));
        gv.setMaNhanVien(rs.getInt("MANHANVIEN"));
        gv.setMaBoMon(rs.getString("MABOMON"));
        gv.setBangCap(rs.getString("BANGCAP"));
        gv.setTrangThai(rs.getString("TRANGTHAI"));
        gv.setHoTen(rs.getString("HOTEN"));
        gv.setEmail(rs.getString("EMAIL"));
        gv.setSoDienThoai(rs.getString("SDT"));
        gv.setGioiTinh(rs.getString("GIOITINH"));
        return gv;
    }
}
