package com.trungtam.controller;

import com.trungtam.model.TaiKhoan;
import com.trungtam.process.AuthService;

public class AuthController {
    private final AuthService authService = new AuthService();

    public TaiKhoan dangNhap(String username, String password, int roleId) {
        return authService.loginByRole(username, password, roleId);
    }
}
