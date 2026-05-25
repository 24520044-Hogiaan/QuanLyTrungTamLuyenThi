package com.quanlytrungtam.controller;

import java.util.List;

import com.quanlytrungtam.dao.NgayNghiLeDAO;
import com.quanlytrungtam.model.NgayNghiLe;

public class NgayNghiLeController {
    private NgayNghiLeDAO ngayNghiLeDAO;

    public NgayNghiLeController() {
        this.ngayNghiLeDAO = new NgayNghiLeDAO();
    }

    public List<NgayNghiLe> getListNgayNghiLe() {
        return ngayNghiLeDAO.getListNgayNghiLe();
    }

    public boolean addNgayNghiLe(NgayNghiLe nnl) {
        return ngayNghiLeDAO.addNgayNghiLe(nnl);
    }

    public boolean editNgayNghiLe(NgayNghiLe nnl) {
        return ngayNghiLeDAO.editNgayNghiLe(nnl);
    }

    public boolean deleteNgayNghiLe(NgayNghiLe nnl) {
        return ngayNghiLeDAO.deleteNgayNghiLe(nnl);
    }
}
