package com.trungtam.controller;

import com.trungtam.dao.NhanVienDAO;
import com.trungtam.model.NhanVien;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class NhanVienController {
    private final NhanVienDAO nhanVienDAO = new NhanVienDAO();

    public List<NhanVien> layDanhSach() {
        try { return nhanVienDAO.findAll(); }
        catch (SQLException e) { e.printStackTrace(); return Collections.emptyList(); }
    }

    public NhanVien timTheoMa(int maNhanVien) {
        try { return nhanVienDAO.findById(maNhanVien); }
        catch (SQLException e) { e.printStackTrace(); return null; }
    }
}
