package com.trungtam.controller;

import com.trungtam.dao.LopHocDAO;
import com.trungtam.model.LopHoc;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class LopHocController {
    private final LopHocDAO lopHocDAO = new LopHocDAO();

    public List<LopHoc> layDanhSach() {
        try {
            return lopHocDAO.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public boolean themLopHoc(LopHoc lh) {
        try { return lopHocDAO.addLopHoc(lh); }
        catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean capNhatLopHoc(LopHoc lh) {
        try { return lopHocDAO.updateLopHoc(lh); }
        catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean xoaLopHoc(int maLopHoc) {
        try { return lopHocDAO.deleteLopHoc(maLopHoc); }
        catch (SQLException e) { e.printStackTrace(); return false; }
    }
}
