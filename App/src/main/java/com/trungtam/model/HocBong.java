package com.trungtam.model;

import java.time.LocalDate;

public class HocBong {
    private int maHocBong;
    private int maHocVien;
    private double giaTri;
    private LocalDate ngayCap;
    private String trangThaiHB;

    public HocBong() {}

    public int getMaHocBong() { return maHocBong; }
    public void setMaHocBong(int maHocBong) { this.maHocBong = maHocBong; }

    public int getMaHocVien() { return maHocVien; }
    public void setMaHocVien(int maHocVien) { this.maHocVien = maHocVien; }

    public double getGiaTri() { return giaTri; }
    public void setGiaTri(double giaTri) { this.giaTri = giaTri; }

    public LocalDate getNgayCap() { return ngayCap; }
    public void setNgayCap(LocalDate ngayCap) { this.ngayCap = ngayCap; }

    public String getTrangThaiHB() { return trangThaiHB; }
    public void setTrangThaiHB(String trangThaiHB) { this.trangThaiHB = trangThaiHB; }
}
