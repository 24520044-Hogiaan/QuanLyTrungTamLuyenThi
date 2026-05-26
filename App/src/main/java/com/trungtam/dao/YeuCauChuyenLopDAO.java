package com.trungtam.dao;

import com.trungtam.model.YeuCauChuyenLop;
import com.trungtam.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class YeuCauChuyenLopDAO {

    public List<YeuCauChuyenLop> getListYeuCau() {
        List<YeuCauChuyenLop> list = new ArrayList<>();
        String sql = "SELECT * FROM YEUCAUCHUYENLOP";
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

    public boolean updateTrangThai(int maYeuCau, String trangThai, int maNhanVien) {
        String sql = "UPDATE YEUCAUCHUYENLOP SET TRANGTHAI = ?, MANHANVIEN = ?, NGAYXULY = SYSDATE WHERE MAYEUCAU = ?";
        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, trangThai);
            ps.setInt(2, maNhanVien);
            ps.setInt(3, maYeuCau);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private YeuCauChuyenLop mapResultSet(ResultSet rs) throws SQLException {
        YeuCauChuyenLop yc = new YeuCauChuyenLop();
        yc.setMaYeuCau(rs.getInt("MAYEUCAU"));
        yc.setMaHocVien(rs.getInt("MAHOCVIEN"));
        yc.setMaLopCu(rs.getInt("MALOPCU"));
        yc.setMaLopMoi(rs.getInt("MALOPMOI"));
        yc.setLyDo(rs.getString("LYDO"));
        yc.setNgayYeuCau(rs.getDate("NGAYYEUCAU"));
        yc.setTrangThai(rs.getString("TRANGTHAI"));
        yc.setMaNhanVien(rs.getInt("MANHANVIEN"));
        yc.setNgayXuLy(rs.getDate("NGAYXULY"));
        yc.setGhiChu(rs.getString("GHICHU"));
        return yc;
    }
}
