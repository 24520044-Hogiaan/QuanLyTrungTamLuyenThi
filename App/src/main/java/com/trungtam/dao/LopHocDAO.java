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
}
