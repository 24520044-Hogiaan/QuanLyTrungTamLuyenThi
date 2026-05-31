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

    public void completeRefund(int refundId, int staffId, String note) throws java.sql.SQLException {
        hoanTraDAO.completeRefund(refundId, staffId, note);
    }

    public boolean guiYeuCauHoanTra(HoanTra ht) {
        return hoanTraDAO.insert(ht);
    }
}
