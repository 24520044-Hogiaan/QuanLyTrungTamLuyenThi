package com.quanlytrungtam.controller;

import java.util.List;

import com.quanlytrungtam.dao.PhongHocDAO;
import com.quanlytrungtam.model.PhongHoc;

public class PhongHocController {

    private final PhongHocDAO phongHocDAO;

    public PhongHocController() {
        phongHocDAO = new PhongHocDAO();
    }

    public List<PhongHoc> getListPhongHoc() {
        return phongHocDAO.getListPhongHoc();
    }

    public boolean addPhongHoc(PhongHoc ph) {
        return phongHocDAO.addPhongHoc(ph);
    }

    public boolean editPhongHoc(PhongHoc ph) {
        return phongHocDAO.editPhongHoc(ph);
    }

    public boolean deletePhongHoc(PhongHoc ph) {
        return phongHocDAO.deletePhongHoc(ph);
    }

}
