package com.trungtam.controller;

import com.trungtam.dao.HocVienDAO;
import com.trungtam.model.HocVien;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class HocVienController {

    private final HocVienDAO hocVienDAO = new HocVienDAO();

    public List<HocVien> layDanhSach() {
        try {
            return hocVienDAO.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<HocVien> layDanhSachTheoLop(int maLop) {
        try {
            return hocVienDAO.findByLop(maLop);
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public HocVien timTheoMa(int maHocVien) {
        try {
            return hocVienDAO.findById(maHocVien);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public HocVien timTheoTaiKhoan(int maTaiKhoan) {
        try {
            return hocVienDAO.findByTaiKhoan(maTaiKhoan);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean themHocVien(HocVien hv) {
        try {
            hocVienDAO.insert(hv);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatHocVien(HocVien hv) {
        try {
            hocVienDAO.update(hv);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaHocVien(int maHocVien) {
        try {
            hocVienDAO.delete(maHocVien);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
