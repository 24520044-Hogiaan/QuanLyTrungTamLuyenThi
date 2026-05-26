package com.trungtam.controller;

import com.trungtam.dao.NgayNghiLeDAO;
import com.trungtam.model.NgayNghiLe;

import java.util.List;

public class NgayNghiLeController {
    private final NgayNghiLeDAO ngayNghiLeDAO = new NgayNghiLeDAO();

    public List<NgayNghiLe> getListNgayNghi() { return ngayNghiLeDAO.getListNgayNghi(); }
    public boolean addNgayNghi(NgayNghiLe nn) { return ngayNghiLeDAO.addNgayNghi(nn); }
    public boolean deleteNgayNghi(int maNgayNghi) { return ngayNghiLeDAO.deleteNgayNghi(maNgayNghi); }
}
