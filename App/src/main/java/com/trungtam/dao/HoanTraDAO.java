package com.trungtam.dao;

import com.trungtam.model.HoanTra;
import com.trungtam.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HoanTraDAO {

    public List<HoanTra> getListHoanTra() {
        List<HoanTra> list = new ArrayList<>();
        String sql = "SELECT * FROM HOANTRA";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateTrangThai(int maHoanTra, String trangThai, int maNhanVien) {
        String sql = "UPDATE HOANTRA SET TRANGTHAI = ?, MANHANVIEN = ?, NGAYHOANTRA = SYSDATE WHERE MAHOANTRA = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, trangThai);
            ps.setInt(2, maNhanVien);
            ps.setInt(3, maHoanTra);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private HoanTra mapResultSet(ResultSet rs) throws SQLException {
        HoanTra ht = new HoanTra();
        ht.setMaHoanTra(rs.getInt("MAHOANTRA"));
        ht.setMaHoaDon(rs.getInt("MAHOADON"));
        ht.setMaHocVien(rs.getInt("MAHOCVIEN"));
        ht.setSoTien(rs.getDouble("SOTIEN"));
        ht.setLyDo(rs.getString("LYDO"));
        Date ngayYC = rs.getDate("NGAYYEUCAU");
        if (ngayYC != null) ht.setNgayYeuCau(ngayYC.toLocalDate());
        Date ngayHT = rs.getDate("NGAYHOANTRA");
        if (ngayHT != null) ht.setNgayHoanTra(ngayHT.toLocalDate());
        ht.setTrangThai(rs.getString("TRANGTHAI"));
        ht.setMaNhanVien(rs.getInt("MANHANVIEN"));
        ht.setHinhThuc(rs.getString("HINHTHUC"));
        return ht;
    }

    public void completeRefund(int refundId, int staffId, String note) throws SQLException {
        String queryHoanTra = "SELECT SOTIEN, MAHOADON, MAHOCVIEN, HINHTHUC, TRANGTHAI FROM HOANTRA WHERE MAHOANTRA = ?";
        String queryHoaDon = "SELECT TONGTIEN, MALOP FROM HOADONHOCPHI WHERE MAHOADON = ?";
        String updateHoanTra = "UPDATE HOANTRA SET TRANGTHAI = 'Da hoan', MANHANVIEN = ?, NGAYHOANTRA = SYSDATE WHERE MAHOANTRA = ?";
        String insertHoaDon = "INSERT INTO HOADONHOCPHI (MAHOADON, MAHOCVIEN, MALOP, NGAYLAP, TONGTIEN, TRANGTHAIHD, LOAIHD, HINHTHUC, MAHOADON_GOC, GHICHU) " +
                              "VALUES ((SELECT NVL(MAX(MAHOADON), 0) + 1 FROM HOADONHOCPHI), ?, ?, SYSDATE, ?, 'Da thanh toan', 'Dieu chinh', ?, ?, ?)";

        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            con.setAutoCommit(false);

            double refundAmount = 0;
            int invoiceId = 0;
            int studentId = 0;
            String hinhThuc = "";
            String trangThai = "";

            try (PreparedStatement ps = con.prepareStatement(queryHoanTra)) {
                ps.setInt(1, refundId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        refundAmount = rs.getDouble("SOTIEN");
                        invoiceId = rs.getInt("MAHOADON");
                        studentId = rs.getInt("MAHOCVIEN");
                        hinhThuc = rs.getString("HINHTHUC");
                        trangThai = rs.getString("TRANGTHAI");
                    } else {
                        throw new SQLException("Không tìm thấy yêu cầu hoàn tiền ID: " + refundId);
                    }
                }
            }

            if (!"Chap thuan".equals(trangThai)) {
                throw new SQLException("Chỉ có thể hoàn tiền cho các yêu cầu đã được 'Chấp thuận'.");
            }

            double originalTotal = 0;
            int classId = 0;
            try (PreparedStatement ps = con.prepareStatement(queryHoaDon)) {
                ps.setInt(1, invoiceId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        originalTotal = rs.getDouble("TONGTIEN");
                        classId = rs.getInt("MALOP");
                    }
                }
            }

            if (refundAmount > originalTotal) {
                throw new SQLException("Số tiền hoàn trả vượt quá giá trị hóa đơn gốc.");
            }

            try (PreparedStatement ps = con.prepareStatement(updateHoanTra)) {
                ps.setInt(1, staffId);
                ps.setInt(2, refundId);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = con.prepareStatement(insertHoaDon)) {
                ps.setInt(1, studentId);
                ps.setInt(2, classId);
                ps.setDouble(3, -refundAmount);
                ps.setString(4, hinhThuc);
                ps.setInt(5, invoiceId);
                ps.setString(6, note != null && !note.isEmpty() ? note : "Hoan tra theo yeu cau ID: " + refundId);
                ps.executeUpdate();
            }

            con.commit();
        } catch (SQLException e) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            throw e;
        } finally {
            if (con != null) {
                try { con.setAutoCommit(true); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        }
    }

    public boolean insert(HoanTra ht) {
        String sqlMax = "SELECT COALESCE(MAX(MAHOANTRA), 0) + 1 FROM HOANTRA";
        String sqlIns = "INSERT INTO HOANTRA(MAHOANTRA, MAHOADON, MAHOCVIEN, SOTIEN, LYDO, NGAYYEUCAU, TRANGTHAI, HINHTHUC) " +
                        "VALUES (?, ?, ?, ?, ?, SYSDATE, 'Cho duyet', 'Chuyen Khoan')";
        try {
            try (Connection con = DatabaseConnection.getConnection();
                 Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(sqlMax)) {
                if (rs.next()) ht.setMaHoanTra(rs.getInt(1));
            }
            try (Connection con = DatabaseConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sqlIns)) {
                ps.setInt(1, ht.getMaHoanTra());
                ps.setInt(2, ht.getMaHoaDon());
                ps.setInt(3, ht.getMaHocVien());
                ps.setDouble(4, ht.getSoTien());
                ps.setString(5, ht.getLyDo());
                ps.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
