package com.trungtam.controller;

import com.trungtam.dao.KhoaHocDAO;
import com.trungtam.model.KhoaHoc;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class KhoaHocController {
    private final KhoaHocDAO khoaHocDAO = new KhoaHocDAO();

    public List<KhoaHoc> layDanhSach() {
        try {
            return khoaHocDAO.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public boolean themKhoaHoc(KhoaHoc kh) {
        try {
            khoaHocDAO.insert(kh);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatKhoaHoc(KhoaHoc kh) {
        try {
            khoaHocDAO.update(kh);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaKhoaHoc(int maKhoaHoc) {
        try {
            khoaHocDAO.delete(maKhoaHoc);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
