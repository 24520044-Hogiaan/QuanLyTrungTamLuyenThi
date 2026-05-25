package com.trungtam.ui.admin;

import com.trungtam.ui.SidebarPanel;
import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AdminWindow extends JFrame {

    public AdminWindow() {
        setTitle("Quản Trị Hệ Thống – Trung Tâm Đào Tạo");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1300, 820);
        setMinimumSize(new Dimension(1024, 640));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UiTheme.APP_BG);

        root.add(buildTopbar(), BorderLayout.NORTH);

        SidebarPanel sidebar = new SidebarPanel(UiTheme.ADMIN);
        sidebar.addSection("Quản trị");
        sidebar.addItem("Quản Lý Tài Khoản");

        JPanel[] panels = { new QuanLyTaiKhoanPanel() };

        CardLayout cards = new CardLayout();
        JPanel contentArea = new JPanel(cards);
        contentArea.setBackground(UiTheme.APP_BG);
        for (int i = 0; i < panels.length; i++) {
            contentArea.add(UiComponents.pageScroll(panels[i]), String.valueOf(i));
        }
        cards.show(contentArea, "0");

        sidebar.setSelectionListener(idx -> cards.show(contentArea, String.valueOf(idx)));

        JScrollPane sidebarScroll = new JScrollPane(sidebar,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sidebarScroll.setBorder(null);
        sidebarScroll.getVerticalScrollBar().setUnitIncrement(16);

        root.add(sidebarScroll, BorderLayout.WEST);
        root.add(contentArea, BorderLayout.CENTER);
        setContentPane(root);
    }

    private JComponent buildTopbar() {
        JPanel topbar = new JPanel(new BorderLayout());
        topbar.setBackground(UiTheme.CARD_BG);
        topbar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UiTheme.CARD_BORDER),
                new EmptyBorder(14, 18, 14, 18)));

        JPanel left = new JPanel(new GridLayout(2, 1, 0, 2));
        left.setOpaque(false);

        JLabel title = new JLabel("Quản trị hệ thống");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.TEXT_PRIMARY);

        JLabel subtitle = new JLabel("Quản lý tài khoản, phân quyền người dùng");
        subtitle.setFont(UiTheme.CAPTION);
        subtitle.setForeground(UiTheme.TEXT_MUTED);

        left.add(title);
        left.add(subtitle);

        JLabel badge = new JLabel("Admin");
        badge.setFont(UiTheme.CAPTION);
        badge.setOpaque(true);
        badge.setBackground(new Color(UiTheme.ADMIN.getRed(), UiTheme.ADMIN.getGreen(), UiTheme.ADMIN.getBlue(), 24));
        badge.setForeground(UiTheme.ADMIN);
        badge.setBorder(new EmptyBorder(6, 12, 6, 12));

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        right.add(badge);

        topbar.add(left, BorderLayout.WEST);
        topbar.add(right, BorderLayout.EAST);
        return topbar;
    }
}
