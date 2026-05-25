package com.trungtam.process;

import com.trungtam.dao.TaiKhoanDAO;
import com.trungtam.model.TaiKhoan;

public class AuthService {
    private final TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();

    public TaiKhoan loginByRole(String username, String password, int roleId) {
        TaiKhoan tk;
        try {
            tk = taiKhoanDAO.findByUsername(username);
        } catch (Exception e) {
            throw new RuntimeException("Không thể kết nối CSDL: " + e.getMessage(), e);
        }
        if (tk == null) return null;
        if (!password.equals(tk.getMatKhau())) return null;
        if (tk.getMaVaiTro() != roleId) return null;
        try {
            taiKhoanDAO.updateLastLogin(tk.getMaTaiKhoan());
        } catch (Exception ignored) {}
        return tk;
    }
}
