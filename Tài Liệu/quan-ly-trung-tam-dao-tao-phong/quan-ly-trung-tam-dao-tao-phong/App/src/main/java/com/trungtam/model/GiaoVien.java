package com.trungtam.model;

import java.time.LocalDate;

public class GiaoVien {
    private int maGiaoVien;
    private String hoTen;
    private String email;
    private String soDienThoai;
    private String chuyenMon;
    private String bangCap;
    private String trangThai; // 'Dang lam', 'Nghi viec'
    private LocalDate ngaySinh;

    public GiaoVien() {}

    public GiaoVien(int maGiaoVien, String hoTen, String email,
                    String soDienThoai, String chuyenMon, String bangCap,
                    String trangThai, LocalDate ngaySinh) {
        this.maGiaoVien = maGiaoVien;
        this.hoTen = hoTen;
        this.email = email;
        this.soDienThoai = soDienThoai;
        this.chuyenMon = chuyenMon;
        this.bangCap = bangCap;
        this.trangThai = trangThai;
        this.ngaySinh = ngaySinh;
    }

    public int getMaGiaoVien() { return maGiaoVien; }
    public void setMaGiaoVien(int maGiaoVien) { this.maGiaoVien = maGiaoVien; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getChuyenMon() { return chuyenMon; }
    public void setChuyenMon(String chuyenMon) { this.chuyenMon = chuyenMon; }

    public String getBangCap() { return bangCap; }
    public void setBangCap(String bangCap) { this.bangCap = bangCap; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public LocalDate getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(LocalDate ngaySinh) { this.ngaySinh = ngaySinh; }

    @Override
    public String toString() { return hoTen; }
}
