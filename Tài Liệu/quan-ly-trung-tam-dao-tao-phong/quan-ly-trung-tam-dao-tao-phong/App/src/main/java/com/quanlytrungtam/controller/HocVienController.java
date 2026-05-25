package com.quanlytrungtam.controller;

import java.util.List;

import com.quanlytrungtam.dao.HocVienDAO;
import com.quanlytrungtam.model.HocVien;

public class HocVienController {
    private final HocVienDAO hocVienDAO = new HocVienDAO();

    public List<HocVien> getListHocVien() {
        return hocVienDAO.getListHocVien();
    }

    public boolean addHocVien(HocVien hv) {
        return hocVienDAO.add(hv);
    }

    public boolean editHocVien(HocVien hv) {
        return hocVienDAO.edit(hv);
    }

    public boolean deleteHocVien(int maHocVien) {
        return hocVienDAO.delete(maHocVien);
    }

    public List<HocVien> searchHocVien(String keyword) {
        return hocVienDAO.search(keyword);
    }
}
