package com.trungtam.dao;

import com.trungtam.model.NgayNghiLe;
import com.trungtam.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NgayNghiLeDAO {

    public List<NgayNghiLe> getListNgayNghi() {
        List<NgayNghiLe> list = new ArrayList<>();
        String sql = "SELECT * FROM NGAYNGHILE";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                NgayNghiLe nn = new NgayNghiLe();
                nn.setMaNgayNghi(rs.getInt("MANGAYNGHI"));
                Date bd = rs.getDate("NGAYBATDAU");
                if (bd != null) nn.setNgayBatDau(bd.toLocalDate());
                Date kt = rs.getDate("NGAYKETTHUC");
                if (kt != null) nn.setNgayKetThuc(kt.toLocalDate());
                list.add(nn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addNgayNghi(NgayNghiLe nn) {
        String sql = "INSERT INTO NGAYNGHILE(MANGAYNGHI, NGAYBATDAU, NGAYKETTHUC) VALUES (?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, nn.getMaNgayNghi());
            ps.setDate(2, java.sql.Date.valueOf(nn.getNgayBatDau()));
            ps.setDate(3, java.sql.Date.valueOf(nn.getNgayKetThuc()));
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteNgayNghi(int maNgayNghi) {
        String sql = "DELETE FROM NGAYNGHILE WHERE MANGAYNGHI = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maNgayNghi);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
