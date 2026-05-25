package com.trungtam.ui.auth;

import com.trungtam.controller.AuthController;
import com.trungtam.model.TaiKhoan;
import com.trungtam.ui.MainWindow;
import com.trungtam.ui.UiTheme;
import com.trungtam.ui.hocvien.HocVienWindow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginWindow extends JFrame {
    private final AuthController authController = new AuthController();

    public LoginWindow(int roleId) {
        setTitle("Dang nhap");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(460, 280);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new GridLayout(0, 1, 8, 8));
        root.setBorder(new EmptyBorder(20, 20, 20, 20));
        root.setBackground(UiTheme.APP_BG);

        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JButton loginButton = new JButton("Dang nhap");

        root.add(new JLabel("Ten tai khoan"));
        root.add(userField);
        root.add(new JLabel("Mat khau"));
        root.add(passField);
        root.add(loginButton);

        loginButton.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword());
            TaiKhoan tk = authController.dangNhap(username, password, roleId);
            if (tk == null) {
                JOptionPane.showMessageDialog(this, "Dang nhap that bai hoac sai vai tro.");
                return;
            }
            dispose();
            if (roleId == RoleSelectionWindow.ROLE_GIAOVIEN) {
                new MainWindow().setVisible(true);
            } else {
                new HocVienWindow().setVisible(true);
            }
        });

        setContentPane(root);
    }
}
