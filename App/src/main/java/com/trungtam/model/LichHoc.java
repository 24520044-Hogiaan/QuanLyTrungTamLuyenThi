package com.trungtam.model;

public class LichHoc {
    private int maLich;
    private int maLop;
    private String gioBatDau;
    private String gioKetThuc;
    private String thu;

    public LichHoc() {}

    public int getMaLich() { return maLich; }
    public void setMaLich(int maLich) { this.maLich = maLich; }

    public int getMaLop() { return maLop; }
    public void setMaLop(int maLop) { this.maLop = maLop; }

    public String getGioBatDau() { return gioBatDau; }
    public void setGioBatDau(String gioBatDau) { this.gioBatDau = gioBatDau; }

    public String getGioKetThuc() { return gioKetThuc; }
    public void setGioKetThuc(String gioKetThuc) { this.gioKetThuc = gioKetThuc; }

    public String getThu() { return thu; }
    public void setThu(String thu) { this.thu = thu; }
}
