package com.quanlytrungtam.controller;

import java.util.List;

import com.quanlytrungtam.dao.KhoaHocDAO;
import com.quanlytrungtam.model.KhoaHoc;

public class KhoaHocController {
    private KhoaHocDAO khoaHocDAO = new KhoaHocDAO();

    public List<KhoaHoc> getListKhoaHoc() {
        return khoaHocDAO.getListKhoaHoc();
    }

    public boolean addKhoaHoc(KhoaHoc kh) {
        return khoaHocDAO.add(kh);
    }

    public boolean editKhoaHoc(KhoaHoc kh) {
        return khoaHocDAO.edit(kh);
    }

    public boolean deleteKhoaHoc(KhoaHoc kh) {
        return khoaHocDAO.delete(kh);
    }

    public List<KhoaHoc> search(String keyword) {
        return khoaHocDAO.search(keyword);
    }
}
