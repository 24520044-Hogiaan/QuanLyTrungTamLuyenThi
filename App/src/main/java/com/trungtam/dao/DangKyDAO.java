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
        String sqlHocPhi = "SELECT k.HOCPHITHANG, k.HOCPHITOANKH FROM LOPHOC l JOIN KHOAHOC k ON l.MAKHOAHOC = k.MAKHOAHOC WHERE l.MALOPHOC = ?";
        String sqlMaxHD = "SELECT COALESCE(MAX(MAHOADON), 0) + 1 FROM HOADONHOCPHI";
        String sqlHD = "INSERT INTO HOADONHOCPHI (MAHOADON, MAHOCVIEN, MALOP, TONGTIEN, TRANGTHAIHD, LOAIHD, HINHTHUCTT, THANGTT, NAMTT) " +
                       "VALUES (?, ?, ?, ?, 'Cho thanh toan', 'Dong tien hoc phi', ?, ?, ?)";
        String sqlDK = "INSERT INTO DANGKY (MAHOCVIEN, MALOPHOC, NGAYDANGKY, TRANGTHAIDKY, HINHTHUCTT, MAHOADON) " +
                       "VALUES (?, ?, SYSDATE, 'Cho thanh toan', ?, ?)";
        
        try (Connection con = DatabaseConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                String hinhThucTT = dk.getHinhThucTT() != null ? dk.getHinhThucTT() : "Toan khoa";
                
                // 1. Get HOCPHI
                double hocPhi = 0;
                try (PreparedStatement ps = con.prepareStatement(sqlHocPhi)) {
                    ps.setInt(1, dk.getMaLopHoc());
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            if ("1 thang".equals(hinhThucTT)) {
                                hocPhi = rs.getDouble("HOCPHITHANG");
                            } else {
                                hocPhi = rs.getDouble("HOCPHITOANKH");
                            }
                        }
                    }
                }
                
                // 2. Get new MAHOADON
                int maHoaDon = 1;
                try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sqlMaxHD)) {
                    if (rs.next()) maHoaDon = rs.getInt(1);
                }
                
                // 3. Insert HOADONHOCPHI
                Integer thangTT = null;
                Integer namTT = null;
                if ("1 thang".equals(hinhThucTT)) {
                    java.time.LocalDate now = java.time.LocalDate.now();
                    thangTT = now.getMonthValue();
                    namTT = now.getYear();
                }
                
                try (PreparedStatement ps = con.prepareStatement(sqlHD)) {
                    ps.setInt(1, maHoaDon);
                    ps.setInt(2, dk.getMaHocVien());
                    ps.setInt(3, dk.getMaLopHoc());
                    ps.setDouble(4, hocPhi);
                    ps.setString(5, hinhThucTT);
                    if (thangTT != null) ps.setInt(6, thangTT); else ps.setNull(6, java.sql.Types.INTEGER);
                    if (namTT != null) ps.setInt(7, namTT); else ps.setNull(7, java.sql.Types.INTEGER);
                    ps.executeUpdate();
                }
                
                // 4. Insert DANGKY
                try (PreparedStatement ps = con.prepareStatement(sqlDK)) {
                    ps.setInt(1, dk.getMaHocVien());
                    ps.setInt(2, dk.getMaLopHoc());
                    ps.setString(3, hinhThucTT);
                    ps.setInt(4, maHoaDon);
                    ps.executeUpdate();
                }
                
                con.commit();
                return true;
            } catch (SQLException ex) {
                con.rollback();
                try (java.io.PrintWriter pw = new java.io.PrintWriter(new java.io.FileWriter("C:\\Users\\Tien\\quan-ly-trung-tam-dao-tao\\App\\error.txt"))) {
                    ex.printStackTrace(pw);
                } catch (Exception e2) {}
                ex.printStackTrace();
                return false;
            }
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

    public boolean deleteDangKy(int maHocVien, int maLopHoc) {
        String sql = "DELETE FROM DANGKY WHERE MAHOCVIEN = ? AND MALOPHOC = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maHocVien);
            ps.setInt(2, maLopHoc);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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
