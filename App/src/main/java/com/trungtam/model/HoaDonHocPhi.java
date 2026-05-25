package com.trungtam.model;

import java.time.LocalDate;

public class HoaDonHocPhi {
    private int maHoaDon;
    private int maHocVien;
    private Integer maHocBong;
    private int maLop;
    private LocalDate ngayLap;
    private double tongTien;
    private String trangThaiHD;
    private String loaiHD;
    private String hinhThuc;
    private String hinhThucTT;
    private Integer thangTT;
    private Integer namTT;
    private Integer maHoaDonGoc;
    private String ghiChu;

    public HoaDonHocPhi() {}

    public int getMaHoaDon() { return maHoaDon; }
    public void setMaHoaDon(int maHoaDon) { this.maHoaDon = maHoaDon; }

    public int getMaHocVien() { return maHocVien; }
    public void setMaHocVien(int maHocVien) { this.maHocVien = maHocVien; }

    public Integer getMaHocBong() { return maHocBong; }
    public void setMaHocBong(Integer maHocBong) { this.maHocBong = maHocBong; }

    public int getMaLop() { return maLop; }
    public void setMaLop(int maLop) { this.maLop = maLop; }

    public LocalDate getNgayLap() { return ngayLap; }
    public void setNgayLap(LocalDate ngayLap) { this.ngayLap = ngayLap; }

    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }

    public String getTrangThaiHD() { return trangThaiHD; }
    public void setTrangThaiHD(String trangThaiHD) { this.trangThaiHD = trangThaiHD; }

    public String getLoaiHD() { return loaiHD; }
    public void setLoaiHD(String loaiHD) { this.loaiHD = loaiHD; }

    public String getHinhThuc() { return hinhThuc; }
    public void setHinhThuc(String hinhThuc) { this.hinhThuc = hinhThuc; }

    public String getHinhThucTT() { return hinhThucTT; }
    public void setHinhThucTT(String hinhThucTT) { this.hinhThucTT = hinhThucTT; }

    public Integer getThangTT() { return thangTT; }
    public void setThangTT(Integer thangTT) { this.thangTT = thangTT; }

    public Integer getNamTT() { return namTT; }
    public void setNamTT(Integer namTT) { this.namTT = namTT; }

    public Integer getMaHoaDonGoc() { return maHoaDonGoc; }
    public void setMaHoaDonGoc(Integer maHoaDonGoc) { this.maHoaDonGoc = maHoaDonGoc; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}
