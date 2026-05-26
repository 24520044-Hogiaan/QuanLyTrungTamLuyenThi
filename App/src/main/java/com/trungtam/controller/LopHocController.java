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
}
