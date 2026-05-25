package com.trungtam.model;

public class BoMon {
    private int maBoMon;
    private String tenBoMon;

    public BoMon() {}

    public int getMaBoMon() { return maBoMon; }
    public void setMaBoMon(int maBoMon) { this.maBoMon = maBoMon; }

    public String getTenBoMon() { return tenBoMon; }
    public void setTenBoMon(String tenBoMon) { this.tenBoMon = tenBoMon; }

    @Override
    public String toString() { return tenBoMon; }
}
