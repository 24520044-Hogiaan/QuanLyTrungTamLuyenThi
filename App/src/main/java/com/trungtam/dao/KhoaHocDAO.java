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
}
