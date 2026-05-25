package com.quanlytrungtam.controller;

import java.util.List;

import com.quanlytrungtam.dao.LopHocDAO;
import com.quanlytrungtam.model.HocVien;
import com.quanlytrungtam.model.LopHoc;

public class LopHocController {
    private LopHocDAO lopHocDAO = new LopHocDAO();

    public List<LopHoc> getListLopHoc() {
        return lopHocDAO.getListLopHoc();
    }

    public boolean edit(LopHoc lh) {
        return lopHocDAO.edit(lh);
    }

    public boolean add(LopHoc lh) {
        return lopHocDAO.add(lh);
    }

    public boolean delete(LopHoc lh) {
        return lopHocDAO.delete(lh);
    }

    public boolean addHocVienToLop(int maHocVien, int maLopHoc, String hinhThucThanhToan) {
        return lopHocDAO.addHocVienToLop(maHocVien, maLopHoc, hinhThucThanhToan);
    }

    public LopHoc getLopHocById(int maLopHoc) {
        return lopHocDAO.getLopHocById(maLopHoc);
    }

    public List<HocVien> getListHocVien(int maLopHoc) {
        return lopHocDAO.getListHocVien(maLopHoc);
    }

    public int getCountStudentInLop(int maLopHoc) {
        return lopHocDAO.getCountStudentInLop(maLopHoc);
    }

    public java.util.Map<Integer, Integer> getMapSiSoHocVien() {
        return lopHocDAO.getMapSiSoHocVien();
    }

    public LopHoc getLopHocByMaLop(int maLopHoc) {
        return lopHocDAO.getLopHocByMaLop(maLopHoc);
    }

    public List<LopHoc> getAssignedLopHoc(int maGiaoVien) {
        return lopHocDAO.getAssignedLopHoc(maGiaoVien);
    }

    public List<LopHoc> getUnassignedLopHoc() {
        return lopHocDAO.getUnassignedLopHoc();
    }

    public boolean assignGiaoVien(int maLopHoc, int maGiaoVien) {
        return lopHocDAO.assignGiaoVien(maLopHoc, maGiaoVien);
    }

    public boolean unassignGiaoVien(int maLopHoc) {
        return lopHocDAO.unassignGiaoVien(maLopHoc);
    }

    public boolean deleteHocVienFromLop(int maHocVien, int maLopHoc) {
        return lopHocDAO.deleteHocVienFromLop(maHocVien, maLopHoc);
    }

}