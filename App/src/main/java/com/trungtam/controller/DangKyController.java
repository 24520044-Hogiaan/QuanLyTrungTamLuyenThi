package com.trungtam.controller;

import com.trungtam.dao.DangKyDAO;
import com.trungtam.model.DangKy;

import java.util.List;

public class DangKyController {
    private final DangKyDAO dangKyDAO = new DangKyDAO();

    public List<DangKy> getListDangKy() { return dangKyDAO.getListDangKy(); }
    public List<DangKy> getDangKyByLop(int maLopHoc) { return dangKyDAO.getDangKyByLop(maLopHoc); }
    public List<DangKy> getDangKyByHocVien(int maHocVien) { return dangKyDAO.getDangKyByHocVien(maHocVien); }
    public int countByLop(int maLopHoc) { return dangKyDAO.countByLop(maLopHoc); }
    public boolean insertDangKy(DangKy dk) { return dangKyDAO.insertDangKy(dk); }
    public boolean existsDangKy(int maHocVien, int maLopHoc) { return dangKyDAO.existsDangKy(maHocVien, maLopHoc); }
    public boolean deleteDangKy(int maHocVien, int maLopHoc) { return dangKyDAO.deleteDangKy(maHocVien, maLopHoc); }
}
