package com.trungtam.process;

import com.trungtam.dao.TaiKhoanDAO;
import com.trungtam.model.TaiKhoan;

public class AuthService {
    private final TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();

    public TaiKhoan loginByRole(String username, String password, int roleId) {
        try {
            TaiKhoan tk = taiKhoanDAO.findByUsername(username);
            if (tk == null) return null;
            if (!password.equals(tk.getMatKhau())) return null;
            if (tk.getMaVaiTro() != roleId) return null;
            taiKhoanDAO.updateLastLogin(tk.getMaTaiKhoan());
            return tk;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
