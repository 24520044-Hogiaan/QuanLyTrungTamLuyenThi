package com.trungtam.dao;

import com.trungtam.model.KhoaHoc;
import com.trungtam.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KhoaHocDAO {

    private static final String SELECT_ALL =
            "SELECT MAKHOAHOC, TENKH, MOTA, HOCPHITHANG, HOCPHITOANKH, MABOMON, CAPDO FROM KHOAHOC";

    private static final String SELECT_BY_ID = SELECT_ALL + " WHERE MAKHOAHOC = ?";

    public List<KhoaHoc> findAll() throws SQLException {
        List<KhoaHoc> result = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL)) {
            while (rs.next()) {
                result.add(mapRow(rs));
            }
        }
        return result;
    }

    public KhoaHoc findById(int maKhoaHoc) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) {
            ps.setInt(1, maKhoaHoc);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    private KhoaHoc mapRow(ResultSet rs) throws SQLException {
        KhoaHoc kh = new KhoaHoc();
        kh.setMaKhoaHoc(rs.getInt("MAKHOAHOC"));
        kh.setTenKhoaHoc(rs.getString("TENKH"));
        kh.setMoTa(rs.getString("MOTA"));
        kh.setHocPhiThang(rs.getDouble("HOCPHITHANG"));
        kh.setHocPhiToanKhoa(rs.getDouble("HOCPHITOANKH"));
        kh.setMaBoMon(rs.getInt("MABOMON"));
        kh.setCapDo(rs.getString("CAPDO"));
        return kh;
    }

    public void insert(KhoaHoc kh) throws SQLException {
        String getIdSql = "SELECT NVL(MAX(MAKHOAHOC), 0) + 1 FROM KHOAHOC";
        String insertSql = "INSERT INTO KHOAHOC (MAKHOAHOC, TENKH, MOTA, HOCPHITHANG, HOCPHITOANKH, MABOMON, CAPDO) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(getIdSql)) {
            if (rs.next()) {
                kh.setMaKhoaHoc(rs.getInt(1));
            }
            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setInt(1, kh.getMaKhoaHoc());
                ps.setString(2, kh.getTenKhoaHoc());
                ps.setString(3, kh.getMoTa());
                ps.setDouble(4, kh.getHocPhiThang());
                ps.setDouble(5, kh.getHocPhiToanKhoa());
                ps.setInt(6, kh.getMaBoMon());
                ps.setString(7, kh.getCapDo());
                ps.executeUpdate();
            }
        }
    }

    public void update(KhoaHoc kh) throws SQLException {
        String sql = "UPDATE KHOAHOC SET TENKH = ?, MOTA = ?, HOCPHITHANG = ?, HOCPHITOANKH = ?, MABOMON = ?, CAPDO = ? WHERE MAKHOAHOC = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kh.getTenKhoaHoc());
            ps.setString(2, kh.getMoTa());
            ps.setDouble(3, kh.getHocPhiThang());
            ps.setDouble(4, kh.getHocPhiToanKhoa());
            ps.setInt(5, kh.getMaBoMon());
            ps.setString(6, kh.getCapDo());
            ps.setInt(7, kh.getMaKhoaHoc());
            ps.executeUpdate();
        }
    }

    public void delete(int maKhoaHoc) throws SQLException {
        String sql = "DELETE FROM KHOAHOC WHERE MAKHOAHOC = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maKhoaHoc);
            ps.executeUpdate();
        }
    }
}
