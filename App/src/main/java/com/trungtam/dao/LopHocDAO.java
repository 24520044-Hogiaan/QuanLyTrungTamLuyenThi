package com.trungtam.dao;

import com.trungtam.model.LopHoc;
import com.trungtam.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LopHocDAO {

    private static final String SELECT_ALL =
            "SELECT MALOPHOC, TENLOP, NGAYBD, NGAYKT, MAKHOAHOC, MAGIAOVIEN, SISO, TANSUAT, TRANGTHAI FROM LOPHOC";

    private static final String SELECT_BY_ID = SELECT_ALL + " WHERE MALOPHOC = ?";

    public List<LopHoc> findAll() throws SQLException {
        List<LopHoc> result = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL)) {
            while (rs.next()) {
                result.add(mapRow(rs));
            }
        }
        return result;
    }

    public LopHoc findById(int maLopHoc) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) {
            ps.setInt(1, maLopHoc);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    private LopHoc mapRow(ResultSet rs) throws SQLException {
        LopHoc lh = new LopHoc();
        lh.setMaLopHoc(rs.getInt("MALOPHOC"));
        lh.setTenLop(rs.getString("TENLOP"));
        Date ngayBD = rs.getDate("NGAYBD");
        if (ngayBD != null) lh.setNgayBatDau(ngayBD.toLocalDate());
        Date ngayKT = rs.getDate("NGAYKT");
        if (ngayKT != null) lh.setNgayKetThuc(ngayKT.toLocalDate());
        lh.setMaKhoaHoc(rs.getInt("MAKHOAHOC"));
        lh.setMaGiaoVien(rs.getInt("MAGIAOVIEN"));
        lh.setSiSo(rs.getInt("SISO"));
        lh.setTanSuat(rs.getString("TANSUAT"));
        lh.setTrangThai(rs.getString("TRANGTHAI"));
        return lh;
    }

    public boolean addLopHoc(LopHoc lh) throws SQLException {
        String sqlMaxId = "SELECT NVL(MAX(MALOPHOC), 0) + 1 FROM LOPHOC";
        String sql = "INSERT INTO LOPHOC (MALOPHOC, TENLOP, NGAYBD, NGAYKT, MAKHOAHOC, MAGIAOVIEN, SISO, TANSUAT, TRANGTHAI) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection()) {
            int newId = 1;
            try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sqlMaxId)) {
                if (rs.next()) newId = rs.getInt(1);
            }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, newId);
                ps.setString(2, lh.getTenLop());
                ps.setDate(3, lh.getNgayBatDau() != null ? Date.valueOf(lh.getNgayBatDau()) : null);
                ps.setDate(4, lh.getNgayKetThuc() != null ? Date.valueOf(lh.getNgayKetThuc()) : null);
                ps.setInt(5, lh.getMaKhoaHoc());
                ps.setInt(6, lh.getMaGiaoVien());
                ps.setInt(7, lh.getSiSo());
                ps.setString(8, lh.getTanSuat());
                ps.setString(9, lh.getTrangThai());
                return ps.executeUpdate() > 0;
            }
        }
    }

    public boolean updateLopHoc(LopHoc lh) throws SQLException {
        String sql = "UPDATE LOPHOC SET TENLOP = ?, NGAYBD = ?, NGAYKT = ?, MAKHOAHOC = ?, MAGIAOVIEN = ?, SISO = ?, TANSUAT = ?, TRANGTHAI = ? WHERE MALOPHOC = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, lh.getTenLop());
            ps.setDate(2, lh.getNgayBatDau() != null ? Date.valueOf(lh.getNgayBatDau()) : null);
            ps.setDate(3, lh.getNgayKetThuc() != null ? Date.valueOf(lh.getNgayKetThuc()) : null);
            ps.setInt(4, lh.getMaKhoaHoc());
            ps.setInt(5, lh.getMaGiaoVien());
            ps.setInt(6, lh.getSiSo());
            ps.setString(7, lh.getTanSuat());
            ps.setString(8, lh.getTrangThai());
            ps.setInt(9, lh.getMaLopHoc());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteLopHoc(int maLopHoc) throws SQLException {
        String sql = "DELETE FROM LOPHOC WHERE MALOPHOC = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maLopHoc);
            return ps.executeUpdate() > 0;
        }
    }
}
