package com.trungtam.model;

import java.time.LocalDate;

public class BuoiHoc {
    private int maBuoiHoc;
    private int maLopHoc;
    private int maLich;
    private LocalDate ngayHoc;
    private String gioHoc;
    private int maPhongHoc;
    private String trangThai;
    private String loaiBuoi;

    public BuoiHoc() {}

    public int getMaBuoiHoc() { return maBuoiHoc; }
    public void setMaBuoiHoc(int maBuoiHoc) { this.maBuoiHoc = maBuoiHoc; }

    public int getMaLopHoc() { return maLopHoc; }
    public void setMaLopHoc(int maLopHoc) { this.maLopHoc = maLopHoc; }

    public int getMaLich() { return maLich; }
    public void setMaLich(int maLich) { this.maLich = maLich; }

    public LocalDate getNgayHoc() { return ngayHoc; }
    public void setNgayHoc(LocalDate ngayHoc) { this.ngayHoc = ngayHoc; }

    public String getGioHoc() { return gioHoc; }
    public void setGioHoc(String gioHoc) { this.gioHoc = gioHoc; }

    public int getMaPhongHoc() { return maPhongHoc; }
    public void setMaPhongHoc(int maPhongHoc) { this.maPhongHoc = maPhongHoc; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getLoaiBuoi() { return loaiBuoi; }
    public void setLoaiBuoi(String loaiBuoi) { this.loaiBuoi = loaiBuoi; }
}
