package com.quanlytrungtam.controller;

import java.util.List;

import com.quanlytrungtam.dao.GiaoVienDAO;
import com.quanlytrungtam.model.GiaoVien;

public class GiaoVienController {
    private GiaoVienDAO giaoVienDAO;

    public GiaoVienController() {
        giaoVienDAO = new GiaoVienDAO();
    }

    public List<GiaoVien> getListGiaoVien() {
        return giaoVienDAO.getListGiaoVien();
    }

    public boolean addGiaoVien(GiaoVien gv) {
        return giaoVienDAO.add(gv);
    }

    public boolean editGiaoVien(GiaoVien gv) {
        return giaoVienDAO.edit(gv);
    }

    public boolean deleteGiaoVien(GiaoVien gv) {
        return giaoVienDAO.delete(gv);
    }

}
