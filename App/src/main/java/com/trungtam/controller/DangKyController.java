package com.trungtam.controller;

import com.trungtam.dao.DangKyDAO;
import com.trungtam.model.DangKy;

import java.util.List;

public class DangKyController {
    private final DangKyDAO dangKyDAO = new DangKyDAO();

    public List<DangKy> getListDangKy() { return dangKyDAO.getListDangKy(); }
    public List<DangKy> getDangKyByLop(int maLopHoc) { return dangKyDAO.getDangKyByLop(maLopHoc); }
}
