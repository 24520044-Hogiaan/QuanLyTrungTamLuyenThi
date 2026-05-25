package com.quanlytrungtam.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class HoaDonHocPhi {
    private Long maHoaDon;
    private Long maHocVien;
    private Long maLop;
    private LocalDate ngayLap;
    private BigDecimal tongTien;
    private String trangThaiHd;
    private String loaiHd;
    private String hinhThuc;
    private String hinhThucTt;
    private Integer thangTt;
    private Integer namTt;
    private HoaDonHocPhi hoaDonGoc;
    private Long maThongTin;
    private String ghiChu;

    public Long getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(Long maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public Long getMaHocVien() {
        return maHocVien;
    }

    public void setMaHocVien(Long maHocVien) {
        this.maHocVien = maHocVien;
    }

    public Long getMaLop() {
        return maLop;
    }

    public void setMaLop(Long maLop) {
        this.maLop = maLop;
    }

    public LocalDate getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(LocalDate ngayLap) {
        this.ngayLap = ngayLap;
    }

    public BigDecimal getTongTien() {
        return tongTien;
    }

    public void setTongTien(BigDecimal tongTien) {
        this.tongTien = tongTien;
    }

    public String getTrangThaiHd() {
        return trangThaiHd;
    }

    public void setTrangThaiHd(String trangThaiHd) {
        this.trangThaiHd = trangThaiHd;
    }

    public String getLoaiHd() {
        return loaiHd;
    }

    public void setLoaiHd(String loaiHd) {
        this.loaiHd = loaiHd;
    }

    public String getHinhThuc() {
        return hinhThuc;
    }

    public void setHinhThuc(String hinhThuc) {
        this.hinhThuc = hinhThuc;
    }

    public String getHinhThucTt() {
        return hinhThucTt;
    }

    public void setHinhThucTt(String hinhThucTt) {
        this.hinhThucTt = hinhThucTt;
    }

    public Integer getThangTt() {
        return thangTt;
    }

    public void setThangTt(Integer thangTt) {
        this.thangTt = thangTt;
    }

    public Integer getNamTt() {
        return namTt;
    }

    public void setNamTt(Integer namTt) {
        this.namTt = namTt;
    }

    public HoaDonHocPhi getHoaDonGoc() {
        return hoaDonGoc;
    }

    public void setHoaDonGoc(HoaDonHocPhi hoaDonGoc) {
        this.hoaDonGoc = hoaDonGoc;
    }

    public Long getMaThongTin() {
        return maThongTin;
    }

    public void setMaThongTin(Long maThongTin) {
        this.maThongTin = maThongTin;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}
