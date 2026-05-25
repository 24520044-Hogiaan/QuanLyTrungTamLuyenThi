package com.trungtam.model;

import java.time.LocalDate;

public class BaiKiemTra {
    private int maBaiKiemTra;
    private String tenBKT;
    private LocalDate ngayKiemTra;
    private int thoiGian;
    private String loaiBKT;
    private int maLop;
    private int diemToiDa;
    private int diemDat;

    public BaiKiemTra() {}

    public int getMaBaiKiemTra() { return maBaiKiemTra; }
    public void setMaBaiKiemTra(int maBaiKiemTra) { this.maBaiKiemTra = maBaiKiemTra; }

    public String getTenBKT() { return tenBKT; }
    public void setTenBKT(String tenBKT) { this.tenBKT = tenBKT; }

    public LocalDate getNgayKiemTra() { return ngayKiemTra; }
    public void setNgayKiemTra(LocalDate ngayKiemTra) { this.ngayKiemTra = ngayKiemTra; }

    public int getThoiGian() { return thoiGian; }
    public void setThoiGian(int thoiGian) { this.thoiGian = thoiGian; }

    public String getLoaiBKT() { return loaiBKT; }
    public void setLoaiBKT(String loaiBKT) { this.loaiBKT = loaiBKT; }

    public int getMaLop() { return maLop; }
    public void setMaLop(int maLop) { this.maLop = maLop; }

    public int getDiemToiDa() { return diemToiDa; }
    public void setDiemToiDa(int diemToiDa) { this.diemToiDa = diemToiDa; }

    public int getDiemDat() { return diemDat; }
    public void setDiemDat(int diemDat) { this.diemDat = diemDat; }
}
