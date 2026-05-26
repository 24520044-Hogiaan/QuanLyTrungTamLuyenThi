package com.trungtam.dao;

import com.trungtam.model.DangKy;
import com.trungtam.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DangKyDAO {

    public List<DangKy> getListDangKy() {
        List<DangKy> list = new ArrayList<>();
        String sql = "SELECT * FROM DANGKY";
        try (Connection con = DatabaseConnection.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                DangKy dk = new DangKy();
                dk.setMaHocVien(rs.getInt("MAHOCVIEN"));
                dk.setMaLopHoc(rs.getInt("MALOPHOC"));
                java.sql.Date ngayDK = rs.getDate("NGAYDANGKY");
                dk.setNgayDangKy(ngayDK != null ? ngayDK.toLocalDate() : null);
                dk.setTrangThaiDKY(rs.getString("TRANGTHAIDKY"));
                dk.setMaHoaDon(rs.getInt("MAHOADON"));
                dk.setHinhThucTT(rs.getString("HINHTHUCTT"));
                list.add(dk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<DangKy> getDangKyByHocVien(int maHocVien) {
        List<DangKy> list = new ArrayList<>();
        String sql = "SELECT * FROM DANGKY WHERE MAHOCVIEN = ?";
        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maHocVien);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DangKy dk = new DangKy();
                    dk.setMaHocVien(rs.getInt("MAHOCVIEN"));
                    dk.setMaLopHoc(rs.getInt("MALOPHOC"));
                    java.sql.Date ngayDK = rs.getDate("NGAYDANGKY");
                    dk.setNgayDangKy(ngayDK != null ? ngayDK.toLocalDate() : null);
                    dk.setTrangThaiDKY(rs.getString("TRANGTHAIDKY"));
                    dk.setMaHoaDon(rs.getInt("MAHOADON"));
                    dk.setHinhThucTT(rs.getString("HINHTHUCTT"));
                    list.add(dk);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int countByLop(int maLopHoc) {
        String sql = "SELECT COUNT(*) FROM DANGKY WHERE MALOPHOC = ? AND TRANGTHAIDKY IN ('Dang hoc', 'Cho thanh toan')";
        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maLopHoc);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean insertDangKy(DangKy dk) {
        String sql = "INSERT INTO DANGKY (MAHOCVIEN, MALOPHOC, NGAYDANGKY, TRANGTHAIDKY, HINHTHUCTT) " +
                     "VALUES (?, ?, SYSDATE, 'Cho thanh toan', ?)";
        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, dk.getMaHocVien());
            ps.setInt(2, dk.getMaLopHoc());
            ps.setString(3, dk.getHinhThucTT() != null ? dk.getHinhThucTT() : "1 thang");
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean existsDangKy(int maHocVien, int maLopHoc) {
        String sql = "SELECT COUNT(*) FROM DANGKY WHERE MAHOCVIEN = ? AND MALOPHOC = ?";
        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maHocVien);
            ps.setInt(2, maLopHoc);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<DangKy> getDangKyByLop(int maLopHoc) {
        List<DangKy> list = new ArrayList<>();
        String sql = "SELECT * FROM DANGKY WHERE MALOPHOC = ?";
        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maLopHoc);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DangKy dk = new DangKy();
                    dk.setMaHocVien(rs.getInt("MAHOCVIEN"));
                    dk.setMaLopHoc(rs.getInt("MALOPHOC"));
                    java.sql.Date ngayDK = rs.getDate("NGAYDANGKY");
                    dk.setNgayDangKy(ngayDK != null ? ngayDK.toLocalDate() : null);
                    dk.setTrangThaiDKY(rs.getString("TRANGTHAIDKY"));
                    dk.setMaHoaDon(rs.getInt("MAHOADON"));
                    dk.setHinhThucTT(rs.getString("HINHTHUCTT"));
                    list.add(dk);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
