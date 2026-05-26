package com.trungtam.controller;

import com.trungtam.dao.HoanTraDAO;
import com.trungtam.model.HoanTra;

import java.util.List;

public class HoanTraController {
    private final HoanTraDAO hoanTraDAO = new HoanTraDAO();

    public List<HoanTra> getListHoanTra() { return hoanTraDAO.getListHoanTra(); }
    public boolean updateTrangThai(int maHoanTra, String trangThai, int maNhanVien) {
        return hoanTraDAO.updateTrangThai(maHoanTra, trangThai, maNhanVien);
    }
}
