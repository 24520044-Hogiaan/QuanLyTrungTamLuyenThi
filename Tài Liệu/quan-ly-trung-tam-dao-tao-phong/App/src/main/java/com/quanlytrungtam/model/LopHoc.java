package com.quanlytrungtam.model;

import java.util.Date;

public class LopHoc {
    private Integer maLopHoc;
    private String tenLop;
    private Date ngayBd;
    private Date ngayKt;
    private Integer maKhoaHoc;
    private Integer maGiaoVien;
    private Integer siso;
    private String tansuat;
    private String trangThai;

    public LopHoc() {
    }

    public LopHoc(Integer maLopHoc, String tenLop, Date ngayBd, Date ngayKt,
            Integer maKhoaHoc, Integer maGiaoVien, Integer siso,
            String tansuat, String trangThai) {
        this.maLopHoc = maLopHoc;
        this.tenLop = tenLop;
        this.ngayBd = ngayBd;
        this.ngayKt = ngayKt;
        this.maKhoaHoc = maKhoaHoc;
        this.maGiaoVien = maGiaoVien;
        this.siso = siso;
        this.tansuat = tansuat;
        this.trangThai = trangThai;
    }

    public Integer getMaLopHoc() {
        return maLopHoc;
    }

    public void setMaLopHoc(Integer maLopHoc) {
        this.maLopHoc = maLopHoc;
    }

    public String getTenLop() {
        return tenLop;
    }

    public void setTenLop(String tenLop) {
        this.tenLop = tenLop;
    }

    public Date getNgayBd() {
        return ngayBd;
    }

    public void setNgayBd(Date ngayBd) {
        this.ngayBd = ngayBd;
    }

    public Date getNgayKt() {
        return ngayKt;
    }

    public void setNgayKt(Date ngayKt) {
        this.ngayKt = ngayKt;
    }

    public Integer getMaKhoaHoc() {
        return maKhoaHoc;
    }

    public void setMaKhoaHoc(Integer maKhoaHoc) {
        this.maKhoaHoc = maKhoaHoc;
    }

    public Integer getMaGiaoVien() {
        return maGiaoVien;
    }

    public void setMaGiaoVien(Integer maGiaoVien) {
        this.maGiaoVien = maGiaoVien;
    }

    public Integer getSiso() {
        return siso;
    }

    public void setSiso(Integer siso) {
        this.siso = siso;
    }

    public String getTansuat() {
        return tansuat;
    }

    public void setTansuat(String tansuat) {
        this.tansuat = tansuat;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}
