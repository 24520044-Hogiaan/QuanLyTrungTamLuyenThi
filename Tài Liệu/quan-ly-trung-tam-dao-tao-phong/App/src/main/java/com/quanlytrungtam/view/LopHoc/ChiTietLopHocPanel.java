package com.quanlytrungtam.view.LopHoc;

import javax.swing.*;
import javax.swing.border.*;

import com.quanlytrungtam.ui.UiTheme;
import com.quanlytrungtam.view.Refreshable;
import com.quanlytrungtam.view.SidebarPanel;

import java.awt.*;
import java.awt.event.*;

public class ChiTietLopHocPanel extends JPanel {

    public ChiTietLopHocPanel(LopHocMainPanel parent, int maLopHoc, String tenLop, String tenKhoaHoc,
            String giangVien, String tanSuat, String capDo, String ngayBD, String ngayKT, int siSo,
            String trangThai) {

        setLayout(new BorderLayout());
        setBackground(UiTheme.APP_BG);

        // ── Sidebar ──
        SidebarPanel sidebar = new SidebarPanel(UiTheme.PRIMARY);
        sidebar.addSection("Lớp: " + tenLop);
        sidebar.addItem("Thông Tin Chung");
        sidebar.addItem("Học Viên");
        sidebar.addItem("Lịch Học");
        sidebar.addItem("Kiểm Tra");

        sidebar.add(Box.createVerticalGlue());

        JButton btnBack = new JButton("\u2190 Quay lại");
        btnBack.setFont(UiTheme.BODY_B);
        btnBack.setForeground(UiTheme.TEXT_SECONDARY);
        btnBack.setBackground(Color.WHITE);
        btnBack.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(0xE0E0E0)),
                new EmptyBorder(16, 20, 16, 20)));
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBack.setFocusPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.setOpaque(true);
        btnBack.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnBack.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btnBack.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnBack.setBackground(new Color(0xF5F5F5));
            }

            public void mouseExited(MouseEvent e) {
                btnBack.setBackground(Color.WHITE);
            }
        });
        btnBack.addActionListener(e -> parent.backToList());
        sidebar.add(btnBack);

        // ── Right panel ──
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(UiTheme.APP_BG);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UiTheme.CARD_BG);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 1, 0, UiTheme.CARD_BORDER),
                new EmptyBorder(18, 24, 18, 24)));

        JLabel lblTitle = new JLabel("CHI TIẾT LỚP HỌC");
        lblTitle.setFont(UiTheme.TITLE_L);
        lblTitle.setForeground(UiTheme.TEXT_PRIMARY);

        JLabel lblSub = new JLabel("Lớp: " + tenLop
                + "  \u2022  Khóa học: " + tenKhoaHoc
                + "  \u2022  Giảng viên: " + giangVien);
        lblSub.setFont(UiTheme.BODY);
        lblSub.setForeground(UiTheme.TEXT_SECONDARY);

        headerPanel.add(lblTitle, BorderLayout.NORTH);
        headerPanel.add(lblSub, BorderLayout.SOUTH);
        rightPanel.add(headerPanel, BorderLayout.NORTH);

        // ── Content cards ──
        CardLayout cardLayout = new CardLayout();
        JPanel contentCards = new JPanel(cardLayout);
        contentCards.setOpaque(false);
        contentCards.setBorder(new EmptyBorder(20, 24, 20, 24));

        JPanel[] panels = { new ThongTinChungPanel(tenLop, tenKhoaHoc, giangVien,
                tanSuat, capDo, ngayBD, ngayKT, siSo, trangThai),
                new DanhSachHocVienPanel(maLopHoc),
                new LichHocPanel(),
                new KiemTraPanel()
        };
        contentCards.add(panels[0], "0");
        contentCards.add(panels[1], "1");
        contentCards.add(panels[2], "2");
        contentCards.add(panels[3], "3");

        rightPanel.add(contentCards, BorderLayout.CENTER);
        sidebar.setSelectionListener(idx -> {
            cardLayout.show(contentCards, String.valueOf(idx));
            if (idx >= 0 && idx < panels.length) {
                JPanel selectedPanel = panels[idx];
                if (selectedPanel instanceof Refreshable) {
                    ((Refreshable) selectedPanel).refresh();
                }
            }
        });

        add(sidebar, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }
}