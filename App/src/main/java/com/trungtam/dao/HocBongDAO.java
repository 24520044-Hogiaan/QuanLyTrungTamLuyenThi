package com.trungtam.dao;

import com.trungtam.model.HocBong;
import com.trungtam.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HocBongDAO {

    public List<HocBong> findAll() throws SQLException {
        List<HocBong> list = new ArrayList<>();
        String sql = "SELECT * FROM HOCBONG";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                HocBong hb = new HocBong();
                hb.setMaHocBong(rs.getInt("MAHOCBONG"));
                hb.setMaHocVien(rs.getInt("MAHOCVIEN"));
                hb.setGiaTri(rs.getDouble("GIATRI"));
                Date ngay = rs.getDate("NGAYCAP");
                if (ngay != null) hb.setNgayCap(ngay.toLocalDate());
                hb.setTrangThaiHB(rs.getString("TRANGTHAIHB"));
                list.add(hb);
            }
        }
        return list;
    }

    public List<HocBong> findByHocVien(int maHocVien) throws SQLException {
        List<HocBong> list = new ArrayList<>();
        String sql = "SELECT * FROM HOCBONG WHERE MAHOCVIEN = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maHocVien);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    HocBong hb = new HocBong();
                    hb.setMaHocBong(rs.getInt("MAHOCBONG"));
                    hb.setMaHocVien(rs.getInt("MAHOCVIEN"));
                    hb.setGiaTri(rs.getDouble("GIATRI"));
                    Date ngay = rs.getDate("NGAYCAP");
                    if (ngay != null) hb.setNgayCap(ngay.toLocalDate());
                    hb.setTrangThaiHB(rs.getString("TRANGTHAIHB"));
                    list.add(hb);
                }
            }
        }
        return list;
    }
}
