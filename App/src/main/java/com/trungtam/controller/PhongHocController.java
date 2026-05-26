package com.trungtam.controller;

import com.trungtam.dao.PhongHocDAO;
import com.trungtam.model.PhongHoc;

import java.util.List;

public class PhongHocController {
    private final PhongHocDAO phongHocDAO = new PhongHocDAO();

    public List<PhongHoc> getListPhongHoc() { return phongHocDAO.getListPhongHoc(); }
    public boolean addPhongHoc(PhongHoc ph) { return phongHocDAO.addPhongHoc(ph); }
    public boolean editPhongHoc(PhongHoc ph) { return phongHocDAO.editPhongHoc(ph); }
    public boolean deletePhongHoc(int maPhongHoc) { return phongHocDAO.deletePhongHoc(maPhongHoc); }
}
