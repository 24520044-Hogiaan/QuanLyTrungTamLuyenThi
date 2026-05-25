package com.quanlytrungtam.controller;

import java.io.File;

import org.jfree.data.category.DefaultCategoryDataset;

import com.quanlytrungtam.dao.HoaDonHocPhiDAO;

public class HoaDonController {

    public DefaultCategoryDataset getDoanhThuDataset(int year) {
        return new HoaDonHocPhiDAO().getDoanhThuDataset(year);
    }

    public double[] getThongKeTongQuan() {
        return new HoaDonHocPhiDAO().getThongKeTongQuan();
    }

    public boolean exportExcel(int year, File file) {
        return new HoaDonHocPhiDAO().exportExcel(year, file);
    }

}
