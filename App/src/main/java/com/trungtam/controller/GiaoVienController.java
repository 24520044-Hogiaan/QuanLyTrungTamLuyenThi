package com.trungtam.controller;

import com.trungtam.dao.GiaoVienDAO;
import com.trungtam.model.GiaoVien;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class GiaoVienController {

    private final GiaoVienDAO giaoVienDAO = new GiaoVienDAO();

    public List<GiaoVien> layDanhSach() {
        try {
            return giaoVienDAO.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public GiaoVien timTheoMa(int maGiaoVien) {
        try {
            return giaoVienDAO.findById(maGiaoVien);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean themGiaoVien(GiaoVien gv) {
        try {
            giaoVienDAO.insert(gv);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatGiaoVien(GiaoVien gv) {
        try {
            giaoVienDAO.update(gv);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaGiaoVien(int maGiaoVien) {
        try {
            giaoVienDAO.delete(maGiaoVien);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
