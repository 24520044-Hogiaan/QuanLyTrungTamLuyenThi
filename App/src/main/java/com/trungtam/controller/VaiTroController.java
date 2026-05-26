package com.trungtam.controller;

import com.trungtam.dao.VaiTroDAO;
import com.trungtam.model.VaiTro;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class VaiTroController {
    private final VaiTroDAO vaiTroDAO = new VaiTroDAO();

    public List<VaiTro> layDanhSach() {
        try { return vaiTroDAO.findAll(); }
        catch (SQLException e) { e.printStackTrace(); return Collections.emptyList(); }
    }

    public String getTenVaiTro(int maVaiTro) {
        try { return vaiTroDAO.getTenVaiTro(maVaiTro); }
        catch (SQLException e) { e.printStackTrace(); return null; }
    }
}
