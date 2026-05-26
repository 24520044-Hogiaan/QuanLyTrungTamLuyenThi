package com.quanlytrungtam.model;

public class PhongHoc {
    private Integer maPhongHoc;
    private String tenPhong;
    private Integer sucChua;
    private String trangThai;

    public PhongHoc() {
    }

    public PhongHoc(Integer maPhongHoc, String tenPhong, Integer sucChua, String trangThai) {
        this.maPhongHoc = maPhongHoc;
        this.tenPhong = tenPhong;
        this.sucChua = sucChua;
        this.trangThai = trangThai;
    }

    public Integer getMaPhongHoc() {
        return maPhongHoc;
    }

    public void setMaPhongHoc(Integer maPhongHoc) {
        this.maPhongHoc = maPhongHoc;
    }

    public String getTenPhong() {
        return tenPhong;
    }

    public void setTenPhong(String tenPhong) {
        this.tenPhong = tenPhong;
    }

    public Integer getSucChua() {
        return sucChua;
    }

    public void setSucChua(Integer sucChua) {
        this.sucChua = sucChua;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}
