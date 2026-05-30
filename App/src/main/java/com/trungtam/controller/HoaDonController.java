package com.trungtam.controller;

import com.trungtam.dao.HoaDonHocPhiDAO;
import com.trungtam.model.HoaDonHocPhi;

import java.util.List;

public class HoaDonController {
    private final HoaDonHocPhiDAO hoaDonDAO = new HoaDonHocPhiDAO();

    public List<HoaDonHocPhi> getListHoaDon() { return hoaDonDAO.getListHoaDon(); }
    public List<HoaDonHocPhi> getHoaDonByHocVien(int maHocVien) { return hoaDonDAO.getHoaDonByHocVien(maHocVien); }

    public void processPayment(int maHoaDon, String paymentMethod, int staffId) throws java.sql.SQLException {
        hoaDonDAO.processPayment(maHoaDon, paymentMethod, staffId);
    }
}
