package com.trungtam;

import com.formdev.flatlaf.FlatLightLaf;
import com.trungtam.ui.MainWindow;
import com.trungtam.ui.hocvien.HocVienWindow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Cửa sổ khởi động — chọn module trước khi vào hệ thống.
 */
public class AppLauncher extends JFrame {

    public AppLauncher() {
        setTitle("Hệ Thống Quản Lý Trung Tâm Đào Tạo");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(520, 340);
        setLocationRelativeTo(null);
        setResizable(false);
        setContentPane(buildContent());
    }

    private JPanel buildContent() {
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(new Color(0xF5F5F5));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(0x1A237E));
        header.setBorder(new EmptyBorder(24, 32, 24, 32));
        JLabel lblTitle = new JLabel("TRUNG TÂM ĐÀO TẠO");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        JLabel lblSub = new JLabel("Chọn module để bắt đầu làm việc");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(new Color(0xC5CAE9));
        JPanel headerText = new JPanel(new GridLayout(2, 1, 0, 4));
        headerText.setOpaque(false);
        headerText.add(lblTitle);
        headerText.add(lblSub);
        header.add(headerText, BorderLayout.CENTER);
        root.add(header, BorderLayout.NORTH);

        // Buttons
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(new EmptyBorder(36, 40, 36, 40));
        btnPanel.add(buildModuleButton(
            "QUẢN LÝ GIÁO VIÊN",
            "Điểm danh, nhập điểm,\nlịch giảng dạy, thông báo",
            new Color(0x1A237E),
            e -> { dispose(); new MainWindow().setVisible(true); }
        ));
        btnPanel.add(buildModuleButton(
            "QUẢN LÝ HỌC VIÊN",
            "Danh sách, đăng ký lớp,\nxem điểm, học phí",
            new Color(0x1B5E20),
            e -> { dispose(); new HocVienWindow().setVisible(true); }
        ));
        root.add(btnPanel, BorderLayout.CENTER);

        // Footer
        JLabel footer = new JLabel("Version 1.0  |  FlatLaf UI", SwingConstants.CENTER);
        footer.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        footer.setForeground(Color.GRAY);
        footer.setBorder(new EmptyBorder(0, 0, 12, 0));
        root.add(footer, BorderLayout.SOUTH);
        return root;
    }

    private JButton buildModuleButton(String title, String desc, Color color,
                                       java.awt.event.ActionListener action) {
        JButton btn = new JButton();
        btn.setLayout(new BorderLayout(0, 8));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(20, 16, 20, 16));

        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblDesc = new JLabel(
            "<html><center>" + desc.replace("\n", "<br>") + "</center></html>",
            SwingConstants.CENTER);
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblDesc.setForeground(new Color(255, 255, 255, 180));

        btn.add(lblTitle, BorderLayout.CENTER);
        btn.add(lblDesc, BorderLayout.SOUTH);
        btn.addActionListener(action);

        // Hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(color.brighter());
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(color);
            }
        });
        return btn;
    }

    public static void main(String[] args) {
        try {
            FlatLightLaf.setup();
            UIManager.put("TabbedPane.focusColor", new Color(0, 0, 0, 0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new AppLauncher().setVisible(true));
    }
}
