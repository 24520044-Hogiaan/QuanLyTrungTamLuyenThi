package com.trungtam.model;

import java.time.LocalDate;

public class LopHoc {
    private int maLopHoc;
    private String tenLop;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
    private int maKhoaHoc;
    private int maGiaoVien;
    private int siSo;
    private String tanSuat;
    private String trangThai;

    public LopHoc() {}

    public int getMaLopHoc() { return maLopHoc; }
    public void setMaLopHoc(int maLopHoc) { this.maLopHoc = maLopHoc; }

    public String getTenLop() { return tenLop; }
    public void setTenLop(String tenLop) { this.tenLop = tenLop; }

    public LocalDate getNgayBatDau() { return ngayBatDau; }
    public void setNgayBatDau(LocalDate ngayBatDau) { this.ngayBatDau = ngayBatDau; }

    public LocalDate getNgayKetThuc() { return ngayKetThuc; }
    public void setNgayKetThuc(LocalDate ngayKetThuc) { this.ngayKetThuc = ngayKetThuc; }

    public int getMaKhoaHoc() { return maKhoaHoc; }
    public void setMaKhoaHoc(int maKhoaHoc) { this.maKhoaHoc = maKhoaHoc; }

    public int getMaGiaoVien() { return maGiaoVien; }
    public void setMaGiaoVien(int maGiaoVien) { this.maGiaoVien = maGiaoVien; }

    public int getSiSo() { return siSo; }
    public void setSiSo(int siSo) { this.siSo = siSo; }

    public String getTanSuat() { return tanSuat; }
    public void setTanSuat(String tanSuat) { this.tanSuat = tanSuat; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    @Override
    public String toString() { return tenLop; }
}
