package com.trungtam.model;

public class GiaoVien {
    private int maGiaoVien;
    private int maNhanVien;
    private String maBoMon;
    private String bangCap;
    private String trangThai;

    // Denormalized fields from NHANVIEN (for display convenience)
    private String hoTen;
    private String email;
    private String soDienThoai;
    private String gioiTinh;

    public GiaoVien() {}

    public int getMaGiaoVien() { return maGiaoVien; }
    public void setMaGiaoVien(int maGiaoVien) { this.maGiaoVien = maGiaoVien; }

    public int getMaNhanVien() { return maNhanVien; }
    public void setMaNhanVien(int maNhanVien) { this.maNhanVien = maNhanVien; }

    public String getMaBoMon() { return maBoMon; }
    public void setMaBoMon(String maBoMon) { this.maBoMon = maBoMon; }

    public String getBangCap() { return bangCap; }
    public void setBangCap(String bangCap) { this.bangCap = bangCap; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }

    @Override
    public String toString() { return hoTen; }
}
