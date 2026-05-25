package com.trungtam.model;

import java.time.LocalDateTime;

public class TaiKhoan {
    private int maTaiKhoan;
    private String tenTaiKhoan;
    private String matKhau;
    private int maVaiTro;
    private String trangThaiTK;
    private LocalDateTime ngayTao;
    private LocalDateTime lanDangNhapCuoi;

    public TaiKhoan() {}

    public int getMaTaiKhoan() { return maTaiKhoan; }
    public void setMaTaiKhoan(int maTaiKhoan) { this.maTaiKhoan = maTaiKhoan; }

    public String getTenTaiKhoan() { return tenTaiKhoan; }
    public void setTenTaiKhoan(String tenTaiKhoan) { this.tenTaiKhoan = tenTaiKhoan; }

    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }

    public int getMaVaiTro() { return maVaiTro; }
    public void setMaVaiTro(int maVaiTro) { this.maVaiTro = maVaiTro; }

    public String getTrangThaiTK() { return trangThaiTK; }
    public void setTrangThaiTK(String trangThaiTK) { this.trangThaiTK = trangThaiTK; }

    public LocalDateTime getNgayTao() { return ngayTao; }
    public void setNgayTao(LocalDateTime ngayTao) { this.ngayTao = ngayTao; }

    public LocalDateTime getLanDangNhapCuoi() { return lanDangNhapCuoi; }
    public void setLanDangNhapCuoi(LocalDateTime lanDangNhapCuoi) { this.lanDangNhapCuoi = lanDangNhapCuoi; }
}
