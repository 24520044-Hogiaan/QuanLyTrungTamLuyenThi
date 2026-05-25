package com.trungtam.ui.auth;

import com.trungtam.controller.AuthController;
import com.trungtam.model.TaiKhoan;
import com.trungtam.ui.MainWindow;
import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;
import com.trungtam.ui.hocvien.HocVienWindow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginWindow extends JFrame {
    private final AuthController authController = new AuthController();

    public LoginWindow(int roleId) {
        setTitle("Đăng nhập");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(480, 420);
        setMinimumSize(new Dimension(400, 380));
        setLocationRelativeTo(null);
        setResizable(true);
        setContentPane(buildContent(roleId));
    }

    private JPanel buildContent(int roleId) {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UiTheme.APP_BG);

        JPanel card = new JPanel(new BorderLayout(0, 16)) {
            @Override
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                return new Dimension(350, d.height);
            }
        };
        card.setBackground(UiTheme.CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.CARD_BORDER, 1),
                new EmptyBorder(28, 36, 24, 36)));

        // Header
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);

        JLabel title = new JLabel("Đăng nhập");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.TEXT_PRIMARY);

        String roleName = roleId == RoleSelectionWindow.ROLE_GIAOVIEN ? "Giáo viên" : "Học viên";
        JLabel subtitle = new JLabel("Vai trò: " + roleName);
        subtitle.setFont(UiTheme.CAPTION);
        subtitle.setForeground(UiTheme.TEXT_MUTED);

        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);
        card.add(header, BorderLayout.NORTH);

        // Form fields
        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        Color btnColor = roleId == RoleSelectionWindow.ROLE_GIAOVIEN ? UiTheme.PRIMARY : UiTheme.SECONDARY;
        JButton loginButton = UiComponents.primaryButton("Đăng nhập", btnColor);

        userField.setFont(UiTheme.BODY);
        passField.setFont(UiTheme.BODY);
        userField.setPreferredSize(new Dimension(150, 32));
        passField.setPreferredSize(new Dimension(150, 32));
        userField.putClientProperty("JTextField.placeholderText", "Nhập tên tài khoản");
        passField.putClientProperty("JTextField.placeholderText", "Nhập mật khẩu");

        // Password toggle
        JCheckBox showPassToggle = new JCheckBox("Hiện mật khẩu");
        showPassToggle.setFont(UiTheme.CAPTION);
        showPassToggle.setForeground(UiTheme.TEXT_MUTED);
        showPassToggle.setOpaque(false);
        showPassToggle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        showPassToggle.addActionListener(e -> {
            if (showPassToggle.isSelected()) {
                passField.setEchoChar((char) 0);
            } else {
                passField.setEchoChar('\u2022');
            }
        });

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 0, 4, 0);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;
        g.gridx = 0;

        g.gridy = 0;
        form.add(createFieldLabel("Tên tài khoản"), g);
        g.gridy = 1;
        form.add(userField, g);
        g.gridy = 2;
        g.insets = new Insets(8, 0, 4, 0);
        form.add(createFieldLabel("Mật khẩu"), g);
        g.gridy = 3;
        g.insets = new Insets(4, 0, 2, 0);
        form.add(passField, g);
        g.gridy = 4;
        g.insets = new Insets(0, 0, 4, 0);
        form.add(showPassToggle, g);

        // Action bar
        JPanel actionBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 8));
        actionBar.setOpaque(false);
        actionBar.add(loginButton);

        JPanel center = new JPanel(new BorderLayout(0, 8));
        center.setOpaque(false);
        center.add(form, BorderLayout.CENTER);
        center.add(actionBar, BorderLayout.SOUTH);
        card.add(center, BorderLayout.CENTER);

        loginButton.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword());

            TaiKhoan tk;
            try {
                tk = authController.dangNhap(username, password, roleId);
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Lỗi kết nối",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (tk == null) {
                JOptionPane.showMessageDialog(this,
                        "Sai tên tài khoản, mật khẩu hoặc vai trò.",
                        "Đăng nhập thất bại",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            dispose();
            if (roleId == RoleSelectionWindow.ROLE_GIAOVIEN) {
                new MainWindow().setVisible(true);
            } else {
                new HocVienWindow().setVisible(true);
            }
        });

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(16, 24, 16, 24));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1;
        gbc.weighty = 1;
        wrapper.add(card, gbc);

        root.add(wrapper, BorderLayout.CENTER);
        return root;
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UiTheme.BODY);
        label.setForeground(UiTheme.TEXT_SECONDARY);
        return label;
    }
}
