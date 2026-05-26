package com.trungtam.controller;

import com.trungtam.dao.YeuCauChuyenLopDAO;
import com.trungtam.model.YeuCauChuyenLop;

import java.util.List;

public class YeuCauChuyenLopController {
    private final YeuCauChuyenLopDAO yeuCauDAO = new YeuCauChuyenLopDAO();

    public List<YeuCauChuyenLop> getListYeuCau() { return yeuCauDAO.getListYeuCau(); }
    public boolean updateTrangThai(int maYeuCau, String trangThai, int maNhanVien) {
        return yeuCauDAO.updateTrangThai(maYeuCau, trangThai, maNhanVien);
    }

    public boolean guiYeuCau(YeuCauChuyenLop yc) {
        return yeuCauDAO.insertYeuCau(yc);
    }
}
