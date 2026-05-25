package com.quanlytrungtam.controller;

import java.io.File;

import org.jfree.data.category.DefaultCategoryDataset;

import com.quanlytrungtam.dao.DangKyDAO;

public class DangKyController {

    private final DangKyDAO dangKyDAO = new DangKyDAO();

    public DefaultCategoryDataset getDangKyDataset(int year) {
        return dangKyDAO.getDangKyDataset(year);
    }

    public double[] getThongKeTongQuan() {
        return dangKyDAO.getThongKeTongQuan();
    }

    public boolean exportExcel(int year, File file) {
        return dangKyDAO.exportExcel(year, file);
    }
}
