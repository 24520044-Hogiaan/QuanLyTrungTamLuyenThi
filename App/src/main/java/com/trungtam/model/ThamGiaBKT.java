package com.trungtam.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ThamGiaBKT {
    private int maHocVien;
    private int maBaiKiemTra;
    private int lanThi;
    private double diemSo;
    private String nhanXet;
    private LocalDate ngayThi;
    private LocalDateTime thoiGianThi;

    public ThamGiaBKT() {}

    public int getMaHocVien() { return maHocVien; }
    public void setMaHocVien(int maHocVien) { this.maHocVien = maHocVien; }

    public int getMaBaiKiemTra() { return maBaiKiemTra; }
    public void setMaBaiKiemTra(int maBaiKiemTra) { this.maBaiKiemTra = maBaiKiemTra; }

    public int getLanThi() { return lanThi; }
    public void setLanThi(int lanThi) { this.lanThi = lanThi; }

    public double getDiemSo() { return diemSo; }
    public void setDiemSo(double diemSo) { this.diemSo = diemSo; }

    public String getNhanXet() { return nhanXet; }
    public void setNhanXet(String nhanXet) { this.nhanXet = nhanXet; }

    public LocalDate getNgayThi() { return ngayThi; }
    public void setNgayThi(LocalDate ngayThi) { this.ngayThi = ngayThi; }

    public LocalDateTime getThoiGianThi() { return thoiGianThi; }
    public void setThoiGianThi(LocalDateTime thoiGianThi) { this.thoiGianThi = thoiGianThi; }
}
