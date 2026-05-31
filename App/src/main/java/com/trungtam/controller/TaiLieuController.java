package com.trungtam.controller;

import com.trungtam.dao.TaiLieuDAO;
import com.trungtam.model.TaiLieu;

import java.sql.SQLException;
import java.util.List;

public class TaiLieuController {
    private final TaiLieuDAO taiLieuDAO = new TaiLieuDAO();

    public List<TaiLieu> findByLop(int maLopHoc) {
        try {
            return taiLieuDAO.findByLop(maLopHoc);
        } catch (SQLException e) {
            e.printStackTrace();
            return java.util.Collections.emptyList();
        }
    }

    public boolean insert(TaiLieu tl) {
        try {
            return taiLieuDAO.insert(tl);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
