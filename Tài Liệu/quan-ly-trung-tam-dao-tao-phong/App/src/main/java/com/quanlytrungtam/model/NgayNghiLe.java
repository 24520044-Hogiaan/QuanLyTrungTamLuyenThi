package com.quanlytrungtam.model;

import java.util.Date;

public class NgayNghiLe {

    private int maNgayNghi;
    private Date ngayBatDau;
    private Date ngayKetThuc;

    public NgayNghiLe() {
    }

    public NgayNghiLe(int maNgayNghi, Date ngayBatDau, Date ngayKetThuc) {
        this.maNgayNghi = maNgayNghi;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
    }

    public int getMaNgayNghi() {
        return maNgayNghi;
    }

    public void setMaNgayNghi(int maNgayNghi) {
        this.maNgayNghi = maNgayNghi;
    }

    public Date getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(Date ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public Date getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(Date ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }
}