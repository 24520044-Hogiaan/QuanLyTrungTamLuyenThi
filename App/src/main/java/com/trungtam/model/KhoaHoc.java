package com.trungtam.model;

public class KhoaHoc {
    private int maKhoaHoc;
    private String tenKhoaHoc;
    private String moTa;
    private double hocPhiThang;
    private double hocPhiToanKhoa;
    private int maBoMon;
    private String capDo;

    public KhoaHoc() {}

    public int getMaKhoaHoc() { return maKhoaHoc; }
    public void setMaKhoaHoc(int maKhoaHoc) { this.maKhoaHoc = maKhoaHoc; }

    public String getTenKhoaHoc() { return tenKhoaHoc; }
    public void setTenKhoaHoc(String tenKhoaHoc) { this.tenKhoaHoc = tenKhoaHoc; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public double getHocPhiThang() { return hocPhiThang; }
    public void setHocPhiThang(double hocPhiThang) { this.hocPhiThang = hocPhiThang; }

    public double getHocPhiToanKhoa() { return hocPhiToanKhoa; }
    public void setHocPhiToanKhoa(double hocPhiToanKhoa) { this.hocPhiToanKhoa = hocPhiToanKhoa; }

    public int getMaBoMon() { return maBoMon; }
    public void setMaBoMon(int maBoMon) { this.maBoMon = maBoMon; }

    public String getCapDo() { return capDo; }
    public void setCapDo(String capDo) { this.capDo = capDo; }

    @Override
    public String toString() { return tenKhoaHoc; }
}
