package com.quanlytrungtam.controller;

import java.util.List;

import com.quanlytrungtam.dao.YeuCauChuyenLopDAO;
import com.quanlytrungtam.model.YeuCauChuyenLop;

public class YeuCauChuyenLopController {
    private YeuCauChuyenLopDAO yeuCauChuyenLopDAO;

    public YeuCauChuyenLopController() {
        this.yeuCauChuyenLopDAO = new YeuCauChuyenLopDAO();
    }

    public List<YeuCauChuyenLop> getListYeuCau() {
        return yeuCauChuyenLopDAO.getListYeuCau();
    }

    public boolean add(YeuCauChuyenLop yc) {
        return yeuCauChuyenLopDAO.add(yc);
    }

    public boolean updateProcess(YeuCauChuyenLop yc) {
        return yeuCauChuyenLopDAO.updateProcess(yc);
    }

    public boolean delete(int maYeuCau) {
        return yeuCauChuyenLopDAO.delete(maYeuCau);
    }
}
