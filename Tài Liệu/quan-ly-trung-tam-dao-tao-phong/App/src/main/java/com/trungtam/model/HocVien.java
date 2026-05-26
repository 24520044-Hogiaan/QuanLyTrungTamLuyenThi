package com.trungtam.model;

import java.time.LocalDate;

public class HocVien {
    private final int maHocVien;
    private final String hoTen, email, soDienThoai, khoiThi, trangThai;
    private final LocalDate ngaySinh, ngayDangKy;

    public HocVien(int maHocVien, String hoTen, String email, String soDienThoai,
                   String khoiThi, String trangThai, LocalDate ngaySinh, LocalDate ngayDangKy) {
        this.maHocVien = maHocVien;
        this.hoTen      = hoTen;
        this.email      = email;
        this.soDienThoai = soDienThoai;
        this.khoiThi    = khoiThi;
        this.trangThai  = trangThai;
        this.ngaySinh   = ngaySinh;
        this.ngayDangKy = ngayDangKy;
    }

    public int getMaHocVien()      { return maHocVien; }
    public String getHoTen()       { return hoTen; }
    public String getEmail()       { return email; }
    public String getSoDienThoai() { return soDienThoai; }
    public String getKhoiThi()     { return khoiThi; }
    public String getTrangThai()   { return trangThai; }
    public LocalDate getNgaySinh()   { return ngaySinh; }
    public LocalDate getNgayDangKy() { return ngayDangKy; }
}
