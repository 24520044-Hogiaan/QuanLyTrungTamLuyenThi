package com.quanlytrungtam.model;

public class KhoaHoc {

    private int makhoahoc;
    private String tenkh;
    private String mota;
    private Double hocphithang;
    private Double hocphitoankh;
    private int mabomon;
    private String capdo;

    public KhoaHoc() {
    }

    public int getMakhoahoc() {
        return makhoahoc;
    }

    public KhoaHoc(int makhoahoc, String tenkh, String mota, Double hocphithang, Double hocphitoankh,
            int mabomon, String capdo) {
        this.makhoahoc = makhoahoc;
        this.tenkh = tenkh;
        this.mota = mota;
        this.hocphithang = hocphithang;
        this.hocphitoankh = hocphitoankh;
        this.mabomon = mabomon;
        this.capdo = capdo;
    }

    public void setMakhoahoc(int makhoahoc) {
        this.makhoahoc = makhoahoc;
    }

    public String getTenkh() {
        return tenkh;
    }

    public void setTenkh(String tenkh) {
        this.tenkh = tenkh;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public Double getHocphithang() {
        return hocphithang;
    }

    public void setHocphithang(Double hocphithang) {
        this.hocphithang = hocphithang;
    }

    public Double getHocphitoankh() {
        return hocphitoankh;
    }

    public void setHocphitoankh(Double hocphitoankh) {
        this.hocphitoankh = hocphitoankh;
    }

    public int getMabomon() {
        return mabomon;
    }

    public void setMabomon(int mabomon) {
        this.mabomon = mabomon;
    }

    public String getCapdo() {
        return capdo;
    }

    public void setCapdo(String capdo) {
        this.capdo = capdo;
    }
}