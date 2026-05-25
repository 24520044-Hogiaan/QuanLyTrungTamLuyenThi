package com.trungtam.ui.auth;

import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.function.Consumer;

public class RoleSelectionWindow extends JFrame {
    public static final int ROLE_GIAOVIEN = 2;
    public static final int ROLE_HOCVIEN = 3;

    public RoleSelectionWindow(Consumer<Integer> onRoleSelected) {
        setTitle("Chon vai tro dang nhap");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(520, 320);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UiTheme.APP_BG);
        root.setBorder(new EmptyBorder(20, 24, 20, 24));

        JLabel title = new JLabel("Ban dang nhap voi vai tro nao?", SwingConstants.CENTER);
        title.setFont(UiTheme.TITLE_M);
        root.add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(1, 2, 12, 0));
        center.setOpaque(false);

        JButton gvButton = new JButton("Giao vien");
        gvButton.setBackground(UiTheme.PRIMARY);
        gvButton.setForeground(Color.WHITE);
        gvButton.addActionListener(e -> onRoleSelected.accept(ROLE_GIAOVIEN));

        JButton hvButton = new JButton("Hoc vien");
        hvButton.setBackground(UiTheme.SECONDARY);
        hvButton.setForeground(Color.WHITE);
        hvButton.addActionListener(e -> onRoleSelected.accept(ROLE_HOCVIEN));

        center.add(gvButton);
        center.add(hvButton);
        root.add(center, BorderLayout.CENTER);
        setContentPane(root);
    }
}
