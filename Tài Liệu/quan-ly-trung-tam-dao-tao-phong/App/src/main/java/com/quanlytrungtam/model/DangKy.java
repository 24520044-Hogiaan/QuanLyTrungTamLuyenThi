package com.quanlytrungtam.model;

import java.util.Date;

public class DangKy {
    private int maHocVien;
    private int maLopHoc;
    private Date ngayDangKy;
    private String trangThaiDky;
    private int maHoaDon;
    private String hinhThucTT;

    public DangKy() {
    }

    public DangKy(int maHocVien, int maLopHoc, Date ngayDangKy, String trangThaiDky, int maHoaDon,
            String hinhThucTT) {
        this.maHocVien = maHocVien;
        this.maLopHoc = maLopHoc;
        this.ngayDangKy = ngayDangKy;
        this.trangThaiDky = trangThaiDky;
        this.maHoaDon = maHoaDon;
        this.hinhThucTT = hinhThucTT;
    }

    public int getMaHocVien() {
        return maHocVien;
    }

    public void setMaHocVien(int maHocVien) {
        this.maHocVien = maHocVien;
    }

    public int getMaLopHoc() {
        return maLopHoc;
    }

    public void setMaLopHoc(int maLopHoc) {
        this.maLopHoc = maLopHoc;
    }

    public Date getNgayDangKy() {
        return ngayDangKy;
    }

    public void setNgayDangKy(Date ngayDangKy) {
        this.ngayDangKy = ngayDangKy;
    }

    public String getTrangThaiDky() {
        return trangThaiDky;
    }

    public void setTrangThaiDky(String trangThaiDky) {
        this.trangThaiDky = trangThaiDky;
    }

    public int getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(int maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public String getHinhThucTT() {
        return hinhThucTT;
    }

    public void setHinhThucTT(String hinhThucTT) {
        this.hinhThucTT = hinhThucTT;
    }

}
