package com.trungtam.model;

public class TaiLieu {
    private int maTaiLieu;
    private String tenTaiLieu;
    private String link;
    private int maLopHoc;

    public TaiLieu() {}

    public int getMaTaiLieu() { return maTaiLieu; }
    public void setMaTaiLieu(int maTaiLieu) { this.maTaiLieu = maTaiLieu; }

    public String getTenTaiLieu() { return tenTaiLieu; }
    public void setTenTaiLieu(String tenTaiLieu) { this.tenTaiLieu = tenTaiLieu; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public int getMaLopHoc() { return maLopHoc; }
    public void setMaLopHoc(int maLopHoc) { this.maLopHoc = maLopHoc; }
}
