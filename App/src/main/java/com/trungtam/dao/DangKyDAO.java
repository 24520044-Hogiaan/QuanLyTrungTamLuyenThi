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
                dk.setNgayDangKy(rs.getDate("NGAYDANGKY"));
                dk.setTrangThaiDky(rs.getString("TRANGTHAIDKY"));
                dk.setMaHoaDon(rs.getInt("MAHOADON"));
                dk.setHinhThucTT(rs.getString("HINHTHUCTT"));
                list.add(dk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
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
                    dk.setNgayDangKy(rs.getDate("NGAYDANGKY"));
                    dk.setTrangThaiDky(rs.getString("TRANGTHAIDKY"));
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
