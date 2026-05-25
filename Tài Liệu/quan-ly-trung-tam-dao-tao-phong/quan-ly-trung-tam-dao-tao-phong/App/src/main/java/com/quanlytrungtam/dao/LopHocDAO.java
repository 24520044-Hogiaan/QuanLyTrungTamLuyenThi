package com.quanlytrungtam.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.quanlytrungtam.model.HocVien;
import com.quanlytrungtam.model.LopHoc;

public class LopHocDAO {

    private DBConnection dbConnection = new DBConnection();

    public List<LopHoc> getListLopHoc() {
        List<LopHoc> list = new ArrayList<>();
        Connection con = dbConnection.getConnection();

        if (con == null) {
            return list;
        }

        String sql = "SELECT * FROM LOPHOC";

        try {
            PreparedStatement statement = con.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                LopHoc lh = new LopHoc();

                lh.setMaLopHoc(rs.getInt("MALOPHOC"));
                lh.setTenLop(rs.getString("TENLOP"));
                lh.setNgayBd(rs.getDate("NGAYBD"));
                lh.setNgayKt(rs.getDate("NGAYKT"));
                lh.setMaKhoaHoc(rs.getInt("MAKHOAHOC"));
                lh.setMaGiaoVien(rs.getInt("MAGIAOVIEN"));
                lh.setSiso(rs.getInt("SISO"));
                lh.setTansuat(rs.getString("TANSUAT"));
                lh.setTrangThai(rs.getString("TRANGTHAI"));

                list.add(lh);
            }

            rs.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        dbConnection.closeConnection(con);

        return list;
    }

    public boolean add(LopHoc lh) {
        Connection con = dbConnection.getConnection();

        if (con == null) {
            return false;
        }

        String sql = "INSERT INTO LOPHOC "
                + "(MALOPHOC, TENLOP, NGAYBD, NGAYKT, MAKHOAHOC, MAGIAOVIEN, SISO, TANSUAT, TRANGTHAI) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement statement = con.prepareStatement(sql);

            statement.setInt(1, lh.getMaLopHoc());
            statement.setString(2, lh.getTenLop());
            statement.setDate(3, new Date(lh.getNgayBd().getTime()));
            statement.setDate(4, new Date(lh.getNgayKt().getTime()));
            statement.setInt(5, lh.getMaKhoaHoc());
            statement.setInt(6, lh.getMaGiaoVien());
            statement.setInt(7, lh.getSiso());
            statement.setString(8, lh.getTansuat());
            statement.setString(9, lh.getTrangThai());

            int rs = statement.executeUpdate();

            statement.close();
            dbConnection.closeConnection(con);

            return rs > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            dbConnection.closeConnection(con);
            return false;
        }
    }

    public boolean edit(LopHoc lh) {
        Connection con = dbConnection.getConnection();

        if (con == null) {
            return false;
        }

        String sql = "UPDATE LOPHOC SET "
                + "TENLOP = ?, "
                + "NGAYBD = ?, "
                + "NGAYKT = ?, "
                + "MAKHOAHOC = ?, "
                + "MAGIAOVIEN = ?, "
                + "SISO = ?, "
                + "TANSUAT = ?, "
                + "TRANGTHAI = ? "
                + "WHERE MALOPHOC = ?";

        try {
            PreparedStatement statement = con.prepareStatement(sql);

            statement.setString(1, lh.getTenLop());
            statement.setDate(2, new Date(lh.getNgayBd().getTime()));
            statement.setDate(3, new Date(lh.getNgayKt().getTime()));
            statement.setInt(4, lh.getMaKhoaHoc());
            statement.setInt(5, lh.getMaGiaoVien());
            statement.setInt(6, lh.getSiso());
            statement.setString(7, lh.getTansuat());
            statement.setString(8, lh.getTrangThai());
            statement.setInt(9, lh.getMaLopHoc());

            int rs = statement.executeUpdate();

            statement.close();
            dbConnection.closeConnection(con);

            return rs > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            dbConnection.closeConnection(con);
            return false;
        }
    }

    public boolean delete(LopHoc lh) {
        Connection con = dbConnection.getConnection();

        if (con == null) {
            return false;
        }

        String sql = "DELETE FROM LOPHOC WHERE MALOPHOC = ?";

        try {
            PreparedStatement statement = con.prepareStatement(sql);

            statement.setInt(1, lh.getMaLopHoc());

            int rs = statement.executeUpdate();

            statement.close();
            dbConnection.closeConnection(con);

            return rs > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            dbConnection.closeConnection(con);
            return false;
        }
    }

    public List<LopHoc> search(String keyword) {
        List<LopHoc> list = new ArrayList<>();

        Connection con = dbConnection.getConnection();

        if (con == null) {
            return list;
        }

        String sql = "SELECT * FROM LOPHOC "
                + "WHERE TENLOP LIKE ? "
                + "OR TANSUAT LIKE ? "
                + "OR TRANGTHAI LIKE ?";

        try {
            PreparedStatement statement = con.prepareStatement(sql);

            String val = "%" + keyword + "%";

            statement.setString(1, val);
            statement.setString(2, val);
            statement.setString(3, val);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                LopHoc lh = new LopHoc();

                lh.setMaLopHoc(rs.getInt("MALOPHOC"));
                lh.setTenLop(rs.getString("TENLOP"));
                lh.setNgayBd(rs.getDate("NGAYBD"));
                lh.setNgayKt(rs.getDate("NGAYKT"));
                lh.setMaKhoaHoc(rs.getInt("MAKHOAHOC"));
                lh.setMaGiaoVien(rs.getInt("MAGIAOVIEN"));
                lh.setSiso(rs.getInt("SISO"));
                lh.setTansuat(rs.getString("TANSUAT"));
                lh.setTrangThai(rs.getString("TRANGTHAI"));

                list.add(lh);
            }

            rs.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        dbConnection.closeConnection(con);

        return list;
    }

    public List<HocVien> getListHocVien(int maLopHoc) {
        List<HocVien> list = new ArrayList<>();

        Connection con = dbConnection.getConnection();

        if (con == null) {
            return list;
        }

        String sql = "SELECT * FROM HOCVIEN INNER JOIN DANGKY ON HOCVIEN.MAHOCVIEN = DANGKY.MAHOCVIEN WHERE MALOPHOC = ? AND TRANGTHAIDKY = 'Dang hoc'";

        try {
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, maLopHoc);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                HocVien lh = new HocVien();

                lh.setMaHocVien(rs.getInt("MAHOCVIEN"));
                lh.setHoTen(rs.getString("HOTEN"));
                lh.setNgaySinh(rs.getDate("NGAYSINH"));
                lh.setGioiTinh(rs.getString("GIOITINH"));
                lh.setSDT(rs.getString("SDT"));
                lh.setEmail(rs.getString("EMAIL"));

                list.add(lh);
            }

            rs.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        dbConnection.closeConnection(con);

        return list;
    }

    public boolean addHocVienToLop(int maHocVien, int maLopHoc, String hinhThucTT) {
        Connection con = dbConnection.getConnection();
        if (con == null) {
            return false;
        }
        String sql = "INSERT INTO DANGKY (MAHOCVIEN, MALOPHOC, NGAYDANGKY, TRANGTHAIDKY, HINHTHUCTT) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, maHocVien);
            statement.setInt(2, maLopHoc);
            statement.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            statement.setString(4, "Dang hoc");
            statement.setString(5, hinhThucTT);
            int rows = statement.executeUpdate();
            statement.close();
            dbConnection.closeConnection(con);
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            dbConnection.closeConnection(con);
            return false;
        }
    }

    public boolean deleteHocVienFromLop(int maHocVien, int maLopHoc) {
        Connection con = dbConnection.getConnection();
        if (con == null) {
            return false;
        }
        String sql = "DELETE FROM DANGKY WHERE MAHOCVIEN = ? AND MALOPHOC = ?";
        try {
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, maHocVien);
            statement.setInt(2, maLopHoc);
            int rows = statement.executeUpdate();
            statement.close();
            dbConnection.closeConnection(con);
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            dbConnection.closeConnection(con);
            return false;
        }
    }

    public LopHoc getLopHocById(int maLopHoc) {
        Connection con = dbConnection.getConnection();
        if (con == null) {
            return null;
        }
        String sql = "SELECT * FROM LOPHOC WHERE MALOPHOC = ?";
        try {
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, maLopHoc);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                LopHoc lh = new LopHoc();
                lh.setMaLopHoc(rs.getInt("MALOPHOC"));
                lh.setTenLop(rs.getString("TENLOP"));
                lh.setNgayBd(rs.getDate("NGAYBD"));
                lh.setNgayKt(rs.getDate("NGAYKT"));
                lh.setMaKhoaHoc(rs.getInt("MAKHOAHOC"));
                lh.setMaGiaoVien(rs.getInt("MAGIAOVIEN"));
                lh.setSiso(rs.getInt("SISO"));
                lh.setTansuat(rs.getString("TANSUAT"));
                lh.setTrangThai(rs.getString("TRANGTHAI"));
                return lh;
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dbConnection.closeConnection(con);
        return null;
    }

    public int getCountStudentInLop(int maLopHoc) {
        Connection con = dbConnection.getConnection();
        if (con == null) {
            return 0;
        }
        String sql = "SELECT COUNT(*) FROM DANGKY WHERE MALOPHOC = ? AND TRANGTHAIDKY = 'Dang hoc'";
        try {
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, maLopHoc);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dbConnection.closeConnection(con);
        return 0;
    }

    public LopHoc getLopHocByMaLop(int maLopHoc) {
        Connection con = dbConnection.getConnection();
        if (con == null) {
            return null;
        }
        String sql = "SELECT * FROM LOPHOC WHERE MALOPHOC = ?";
        try {
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, maLopHoc);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                LopHoc lh = new LopHoc();
                lh.setMaLopHoc(rs.getInt("MALOPHOC"));
                lh.setTenLop(rs.getString("TENLOP"));
                lh.setNgayBd(rs.getDate("NGAYBD"));
                lh.setNgayKt(rs.getDate("NGAYKT"));
                lh.setMaKhoaHoc(rs.getInt("MAKHOAHOC"));
                lh.setMaGiaoVien(rs.getInt("MAGIAOVIEN"));
                lh.setSiso(rs.getInt("SISO"));
                lh.setTansuat(rs.getString("TANSUAT"));
                lh.setTrangThai(rs.getString("TRANGTHAI"));
                return lh;
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dbConnection.closeConnection(con);
        return null;
    }

    public List<LopHoc> getAssignedLopHoc(int maGiaoVien) {
        List<LopHoc> list = new ArrayList<>();
        Connection con = dbConnection.getConnection();
        if (con == null) {
            return list;
        }
        String sql = "SELECT * FROM LOPHOC WHERE MAGIAOVIEN = ? AND TRANGTHAI = 'Dang mo'";
        try {
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, maGiaoVien);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                LopHoc lh = new LopHoc();
                lh.setMaLopHoc(rs.getInt("MALOPHOC"));
                lh.setTenLop(rs.getString("TENLOP"));
                lh.setNgayBd(rs.getDate("NGAYBD"));
                lh.setNgayKt(rs.getDate("NGAYKT"));
                lh.setMaKhoaHoc(rs.getInt("MAKHOAHOC"));
                lh.setMaGiaoVien(rs.getInt("MAGIAOVIEN"));
                lh.setSiso(rs.getInt("SISO"));
                lh.setTansuat(rs.getString("TANSUAT"));
                lh.setTrangThai(rs.getString("TRANGTHAI"));
                list.add(lh);
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dbConnection.closeConnection(con);
        return list;
    }

    public List<LopHoc> getUnassignedLopHoc() {
        List<LopHoc> list = new ArrayList<>();
        Connection con = dbConnection.getConnection();
        if (con == null) {
            return list;
        }
        String sql = "SELECT * FROM LOPHOC WHERE (MAGIAOVIEN IS NULL OR MAGIAOVIEN = 0) AND TRANGTHAI = 'Dang mo'";
        try {
            PreparedStatement statement = con.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                LopHoc lh = new LopHoc();
                lh.setMaLopHoc(rs.getInt("MALOPHOC"));
                lh.setTenLop(rs.getString("TENLOP"));
                lh.setNgayBd(rs.getDate("NGAYBD"));
                lh.setNgayKt(rs.getDate("NGAYKT"));
                lh.setMaKhoaHoc(rs.getInt("MAKHOAHOC"));
                lh.setMaGiaoVien(rs.getInt("MAGIAOVIEN"));
                lh.setSiso(rs.getInt("SISO"));
                lh.setTansuat(rs.getString("TANSUAT"));
                lh.setTrangThai(rs.getString("TRANGTHAI"));
                list.add(lh);
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dbConnection.closeConnection(con);
        return list;
    }

    public boolean assignGiaoVien(int maLopHoc, int maGiaoVien) {
        Connection con = dbConnection.getConnection();
        if (con == null) {
            return false;
        }
        String sql = "UPDATE LOPHOC SET MAGIAOVIEN = ? WHERE MALOPHOC = ?";
        try {
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, maGiaoVien);
            statement.setInt(2, maLopHoc);
            int rs = statement.executeUpdate();
            statement.close();
            dbConnection.closeConnection(con);
            return rs > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            dbConnection.closeConnection(con);
            return false;
        }
    }

    public boolean unassignGiaoVien(int maLopHoc) {
        Connection con = dbConnection.getConnection();
        if (con == null) {
            return false;
        }
        String sql = "UPDATE LOPHOC SET MAGIAOVIEN = NULL WHERE MALOPHOC = ?";
        try {
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, maLopHoc);
            int rs = statement.executeUpdate();
            statement.close();
            dbConnection.closeConnection(con);
            return rs > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            dbConnection.closeConnection(con);
            return false;
        }
    }

    public java.util.Map<Integer, Integer> getMapSiSoHocVien() {
        java.util.Map<Integer, Integer> map = new java.util.HashMap<>();
        Connection con = dbConnection.getConnection();
        if (con == null) {
            return map;
        }
        String sql = "SELECT MALOPHOC, COUNT(*) FROM DANGKY WHERE TRANGTHAIDKY = 'Dang hoc' GROUP BY MALOPHOC";
        try {
            PreparedStatement statement = con.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                map.put(rs.getInt(1), rs.getInt(2));
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dbConnection.closeConnection(con);
        return map;
    }

}