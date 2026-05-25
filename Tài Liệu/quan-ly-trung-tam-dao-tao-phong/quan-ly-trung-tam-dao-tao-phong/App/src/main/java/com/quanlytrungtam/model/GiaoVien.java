package com.quanlytrungtam.model;

public class GiaoVien {
    private int maGiaoVien;
    private int maNhanVien;
    private String chuyenMon;
    private String bangCap;
    private String trangThai;

    public GiaoVien() {
    }

    public GiaoVien(int maGiaoVien, int maNhanVien, String chuyenMon,
            String bangCap, String trangThai) {
        this.maGiaoVien = maGiaoVien;
        this.maNhanVien = maNhanVien;
        this.chuyenMon = chuyenMon;
        this.bangCap = bangCap;
        this.trangThai = trangThai;
    }

    public int getMaGiaoVien() {
        return maGiaoVien;
    }

    public void setMaGiaoVien(int maGiaoVien) {
        this.maGiaoVien = maGiaoVien;
    }

    public int getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(int maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getChuyenMon() {
        return chuyenMon;
    }

    public void setChuyenMon(String chuyenMon) {
        this.chuyenMon = chuyenMon;
    }

    public String getBangCap() {
        return bangCap;
    }

    public void setBangCap(String bangCap) {
        this.bangCap = bangCap;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

}
