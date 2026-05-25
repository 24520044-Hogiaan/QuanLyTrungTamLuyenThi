package com.quanlytrungtam.model;

import java.util.Date;

public class HoanTienModel {
    private int maHoanTra;
    private int maHoaDon;
    private int maHocVien;
    private double soTien;
    private String lyDo;
    private Date ngayYeuCau;
    private Date ngayHoanTra;
    private String trangThai;
    private int maNhanVien;
    private Date ngayTao;
    private String hinhThuc;
    private int nguoiTao;

    public HoanTienModel() {
    }

    public int getMaHoanTra() {
        return maHoanTra;
    }

    public void setMaHoanTra(int maHoanTra) {
        this.maHoanTra = maHoanTra;
    }

    public int getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(int maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public int getMaHocVien() {
        return maHocVien;
    }

    public void setMaHocVien(int maHocVien) {
        this.maHocVien = maHocVien;
    }

    public double getSoTien() {
        return soTien;
    }

    public void setSoTien(double soTien) {
        this.soTien = soTien;
    }

    public String getLyDo() {
        return lyDo;
    }

    public void setLyDo(String lyDo) {
        this.lyDo = lyDo;
    }

    public Date getNgayYeuCau() {
        return ngayYeuCau;
    }

    public void setNgayYeuCau(Date ngayYeuCau) {
        this.ngayYeuCau = ngayYeuCau;
    }

    public Date getNgayHoanTra() {
        return ngayHoanTra;
    }

    public void setNgayHoanTra(Date ngayHoanTra) {
        this.ngayHoanTra = ngayHoanTra;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public int getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(int maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public Date getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Date ngayTao) {
        this.ngayTao = ngayTao;
    }

    public String getHinhThuc() {
        return hinhThuc;
    }

    public void setHinhThuc(String hinhThuc) {
        this.hinhThuc = hinhThuc;
    }

    public int getNguoiTao() {
        return nguoiTao;
    }

    public void setNguoiTao(int nguoiTao) {
        this.nguoiTao = nguoiTao;
    }
}
