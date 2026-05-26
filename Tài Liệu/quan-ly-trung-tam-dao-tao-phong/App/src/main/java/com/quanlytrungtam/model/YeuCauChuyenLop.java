package com.quanlytrungtam.model;

import java.util.Date;

public class YeuCauChuyenLop {

    private int maYeuCau;
    private int maHocVien;
    private int maLopCu;
    private int maLopMoi;
    private String lyDo;
    private Date ngayYeuCau;
    private String trangThai;
    private int maNhanVien;
    private Date ngayXuLy;
    private String ghiChu;

    public YeuCauChuyenLop() {
    }

    public int getMaYeuCau() {
        return maYeuCau;
    }

    public void setMaYeuCau(int maYeuCau) {
        this.maYeuCau = maYeuCau;
    }

    public int getMaHocVien() {
        return maHocVien;
    }

    public void setMaHocVien(int maHocVien) {
        this.maHocVien = maHocVien;
    }

    public int getMaLopCu() {
        return maLopCu;
    }

    public void setMaLopCu(int maLopCu) {
        this.maLopCu = maLopCu;
    }

    public int getMaLopMoi() {
        return maLopMoi;
    }

    public void setMaLopMoi(int maLopMoi) {
        this.maLopMoi = maLopMoi;
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

    public Date getNgayXuLy() {
        return ngayXuLy;
    }

    public void setNgayXuLy(Date ngayXuLy) {
        this.ngayXuLy = ngayXuLy;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}