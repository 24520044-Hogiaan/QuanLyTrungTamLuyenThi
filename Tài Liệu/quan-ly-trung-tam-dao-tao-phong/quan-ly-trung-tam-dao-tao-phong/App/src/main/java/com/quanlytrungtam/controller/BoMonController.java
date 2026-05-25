package com.quanlytrungtam.controller;

import java.util.List;

import com.quanlytrungtam.dao.BoMonDAO;
import com.quanlytrungtam.model.BoMon;

public class BoMonController {
    private final BoMonDAO bomonDao;

    public BoMonController() {
        bomonDao = new BoMonDAO();
    }

    public List<BoMon> getListBoMon() {
        return bomonDao.getListBoMon();
    }

    public boolean addBoMon(BoMon bm) {
        return bomonDao.addBoMon(bm);
    }

    public boolean editBoMon(BoMon bm) {
        return bomonDao.editBoMon(bm);
    }

    public boolean deleteBoMon(BoMon bm) {
        return bomonDao.deleteBoMon(bm);
    }
}
