package com.trungtam.model;

import java.time.LocalDate;

public class DangKy {
    private int maHocVien;
    private int maLopHoc;
    private LocalDate ngayDangKy;
    private String trangThaiDKY;
    private Integer maHoaDon;
    private String hinhThucTT;

    public DangKy() {}

    public int getMaHocVien() { return maHocVien; }
    public void setMaHocVien(int maHocVien) { this.maHocVien = maHocVien; }

    public int getMaLopHoc() { return maLopHoc; }
    public void setMaLopHoc(int maLopHoc) { this.maLopHoc = maLopHoc; }

    public LocalDate getNgayDangKy() { return ngayDangKy; }
    public void setNgayDangKy(LocalDate ngayDangKy) { this.ngayDangKy = ngayDangKy; }

    public String getTrangThaiDKY() { return trangThaiDKY; }
    public void setTrangThaiDKY(String trangThaiDKY) { this.trangThaiDKY = trangThaiDKY; }

    public Integer getMaHoaDon() { return maHoaDon; }
    public void setMaHoaDon(Integer maHoaDon) { this.maHoaDon = maHoaDon; }

    public String getHinhThucTT() { return hinhThucTT; }
    public void setHinhThucTT(String hinhThucTT) { this.hinhThucTT = hinhThucTT; }
}
