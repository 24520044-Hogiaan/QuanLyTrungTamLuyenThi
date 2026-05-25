package com.trungtam;

import com.trungtam.ui.MainWindow;
import com.trungtam.ui.UiTheme;
import com.trungtam.ui.auth.LoginWindow;
import com.trungtam.ui.auth.RoleSelectionWindow;
import com.trungtam.ui.hocvien.HocVienWindow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Cửa sổ khởi động — chọn module trước khi vào hệ thống.
 */
public class AppLauncher extends JFrame {

    public AppLauncher() {
        setTitle("Hệ Thống Quản Lý Trung Tâm Đào Tạo");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(560, 360);
        setMinimumSize(new Dimension(480, 320));
        setLocationRelativeTo(null);
        setResizable(true);
        setContentPane(buildContent());
    }

    private JPanel buildContent() {
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(UiTheme.APP_BG);

        // ── Header ────────────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UiTheme.PRIMARY);
        header.setBorder(new EmptyBorder(22, 32, 22, 32));

        JLabel lblTitle = new JLabel("TRUNG TÂM ĐÀO TẠO");
        lblTitle.setFont(UiTheme.TITLE_L);
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Chọn module để bắt đầu làm việc");
        lblSub.setFont(UiTheme.BODY);
        lblSub.setForeground(UiTheme.SIDEBAR_FG);

        JPanel headerText = new JPanel(new GridLayout(2, 1, 0, 4));
        headerText.setOpaque(false);
        headerText.add(lblTitle);
        headerText.add(lblSub);
        header.add(headerText, BorderLayout.CENTER);
        root.add(header, BorderLayout.NORTH);

        // ── Module buttons ────────────────────────────────────────────────────
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 16, 0));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(new EmptyBorder(32, 36, 32, 36));

        btnPanel.add(buildModuleButton(
                "QUẢN LÝ GIÁO VIÊN",
                "Điểm danh, nhập điểm,\nlịch giảng dạy, thông báo",
                UiTheme.PRIMARY,
                e -> {
                    dispose();
                    new MainWindow().setVisible(true);
                }));

        btnPanel.add(buildModuleButton(
                "QUẢN LÝ HỌC VIÊN",
                "Danh sách, đăng ký lớp,\nxem điểm, học phí",
                UiTheme.SECONDARY,
                e -> {
                    dispose();
                    new HocVienWindow().setVisible(true);
                }));

        root.add(btnPanel, BorderLayout.CENTER);

        // ── Footer ────────────────────────────────────────────────────────────
        JLabel footer = new JLabel("Phiên bản 1.0  |  FlatLaf UI", SwingConstants.CENTER);
        footer.setFont(UiTheme.CAPTION_I);
        footer.setForeground(UiTheme.TEXT_MUTED);
        footer.setBorder(new EmptyBorder(0, 0, 12, 0));
        root.add(footer, BorderLayout.SOUTH);

        return root;
    }

    private JButton buildModuleButton(String title, String desc, Color color,
            ActionListener action) {
        JButton btn = new JButton();
        btn.setLayout(new BorderLayout(0, 8));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(20, 18, 20, 18));
        btn.setOpaque(true);

        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(UiTheme.TITLE_S);
        lblTitle.setForeground(Color.WHITE);

        JLabel lblDesc = new JLabel(
                "<html><center>" + desc.replace("\n", "<br>") + "</center></html>",
                SwingConstants.CENTER);
        lblDesc.setFont(UiTheme.CAPTION);
        lblDesc.setForeground(new Color(255, 255, 255, 190));

        btn.add(lblTitle, BorderLayout.CENTER);
        btn.add(lblDesc, BorderLayout.SOUTH);
        btn.addActionListener(action);

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(color.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(color);
            }
        });
        return btn;
    }

    public static void main(String[] args) {
        try {
            Class<?> lafClass = Class.forName("com.formdev.flatlaf.FlatLightLaf");
            lafClass.getMethod("setup").invoke(null);
        } catch (Exception ignored) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        try {
            UiTheme.applyGlobalTokens();
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            final RoleSelectionWindow[] roleSelectionWindowRef = new RoleSelectionWindow[1];
            roleSelectionWindowRef[0] = new RoleSelectionWindow(roleId -> {
                roleSelectionWindowRef[0].dispose();
                LoginWindow loginWindow = new LoginWindow(roleId);
                loginWindow.setVisible(true);
            });
            roleSelectionWindowRef[0].setVisible(true);
        });
    }
}
