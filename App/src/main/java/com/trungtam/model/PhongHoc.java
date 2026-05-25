package com.trungtam.model;

public class PhongHoc {
    private int maPhongHoc;
    private String tenPhong;
    private int sucChua;
    private String trangThai;

    public PhongHoc() {}

    public int getMaPhongHoc() { return maPhongHoc; }
    public void setMaPhongHoc(int maPhongHoc) { this.maPhongHoc = maPhongHoc; }

    public String getTenPhong() { return tenPhong; }
    public void setTenPhong(String tenPhong) { this.tenPhong = tenPhong; }

    public int getSucChua() { return sucChua; }
    public void setSucChua(int sucChua) { this.sucChua = sucChua; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}
