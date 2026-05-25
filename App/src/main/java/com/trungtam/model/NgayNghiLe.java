package com.trungtam.model;

import java.time.LocalDate;

public class NgayNghiLe {
    private int maNgayNghi;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;

    public NgayNghiLe() {}

    public int getMaNgayNghi() { return maNgayNghi; }
    public void setMaNgayNghi(int maNgayNghi) { this.maNgayNghi = maNgayNghi; }

    public LocalDate getNgayBatDau() { return ngayBatDau; }
    public void setNgayBatDau(LocalDate ngayBatDau) { this.ngayBatDau = ngayBatDau; }

    public LocalDate getNgayKetThuc() { return ngayKetThuc; }
    public void setNgayKetThuc(LocalDate ngayKetThuc) { this.ngayKetThuc = ngayKetThuc; }
}
