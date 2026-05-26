package com.trungtam.controller;

import com.trungtam.dao.BoMonDAO;
import com.trungtam.model.BoMon;

import java.util.List;

public class BoMonController {
    private final BoMonDAO boMonDAO = new BoMonDAO();

    public List<BoMon> getListBoMon() { return boMonDAO.getListBoMon(); }
    public boolean addBoMon(BoMon bm) { return boMonDAO.addBoMon(bm); }
    public boolean editBoMon(BoMon bm) { return boMonDAO.editBoMon(bm); }
    public boolean deleteBoMon(BoMon bm) { return boMonDAO.deleteBoMon(bm); }
}
