package com.trungtam.controller;

import com.trungtam.dao.HocBongDAO;
import com.trungtam.model.HocBong;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class HocBongController {
    private final HocBongDAO hocBongDAO = new HocBongDAO();

    public List<HocBong> layDanhSach() {
        try { return hocBongDAO.findAll(); }
        catch (SQLException e) { e.printStackTrace(); return Collections.emptyList(); }
    }

    public List<HocBong> timTheoHocVien(int maHocVien) {
        try { return hocBongDAO.findByHocVien(maHocVien); }
        catch (SQLException e) { e.printStackTrace(); return Collections.emptyList(); }
    }
}
