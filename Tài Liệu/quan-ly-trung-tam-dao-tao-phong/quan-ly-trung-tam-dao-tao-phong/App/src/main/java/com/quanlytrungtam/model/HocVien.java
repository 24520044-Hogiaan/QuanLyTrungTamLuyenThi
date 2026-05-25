package com.quanlytrungtam.model;

import java.util.Date;

public class HocVien {
    private int MaHocVien;
    private String TaiKhoanNganHang;
    private String HoTen;
    private Date NgaySinh;
    private String GioiTinh;
    private String DiaChi;
    private String SDT;
    private String Email;

    public HocVien() {
    }

    public HocVien(int maHocVien, String taiKhoan, String hoTen, Date ngaySinh, String gioiTinh, String diaChi,
            String SDT, String Email) {
        this.MaHocVien = maHocVien;
        this.TaiKhoanNganHang = taiKhoan;
        this.HoTen = hoTen;
        this.NgaySinh = ngaySinh;
        this.GioiTinh = gioiTinh;
        this.DiaChi = diaChi;
        this.SDT = SDT;
        this.Email = Email;
    }

    public int getMaHocVien() {
        return MaHocVien;
    }

    public void setMaHocVien(int maHocVien) {
        MaHocVien = maHocVien;
    }

    public String getTaiKhoanNganHang() {
        return TaiKhoanNganHang;
    }

    public void setTaiKhoanNganHang(String taiKhoanNganHang) {
        TaiKhoanNganHang = taiKhoanNganHang;
    }

    public String getHoTen() {
        return HoTen;
    }

    public void setHoTen(String hoTen) {
        HoTen = hoTen;
    }

    public Date getNgaySinh() {
        return NgaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        NgaySinh = ngaySinh;
    }

    public String getGioiTinh() {
        return GioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        GioiTinh = gioiTinh;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String diaChi) {
        DiaChi = diaChi;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

}
