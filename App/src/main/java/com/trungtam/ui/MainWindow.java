package com.trungtam.ui;

import com.trungtam.ui.giaovien.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainWindow extends JFrame {

    private final int maGiaoVien;

    public MainWindow(com.trungtam.model.TaiKhoan loggedInAccount) {
        int maGV = -1;
        if (loggedInAccount != null) {
            com.trungtam.controller.NhanVienController nvCtrl = new com.trungtam.controller.NhanVienController();
            com.trungtam.model.NhanVien nv = null;
            for (com.trungtam.model.NhanVien n : nvCtrl.layDanhSach()) {
                if (n.getMaTaiKhoan() == loggedInAccount.getMaTaiKhoan()) {
                    nv = n;
                    break;
                }
            }
            if (nv != null) {
                com.trungtam.controller.GiaoVienController gvCtrl = new com.trungtam.controller.GiaoVienController();
                for (com.trungtam.model.GiaoVien g : gvCtrl.layDanhSach()) {
                    if (g.getMaNhanVien() == nv.getMaNhanVien()) {
                        maGV = g.getMaGiaoVien();
                        break;
                    }
                }
            }
        }
        this.maGiaoVien = maGV;

        setTitle("Quản Lý Giáo Viên – Trung Tâm Đào Tạo");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1300, 820);
        setMinimumSize(new Dimension(1024, 640));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UiTheme.APP_BG);

        root.add(buildTopbar(), BorderLayout.NORTH);

        SidebarPanel sidebar = new SidebarPanel(UiTheme.PRIMARY);
        sidebar.addSection("Chức năng chính");
        String[] titles = {
                "Danh Sách GV", "Điểm Danh", "Nhập Điểm",
                "Lịch Giảng Dạy", "Thống Kê Lớp"
        };
        for (String t : titles) {
            sidebar.addItem(t);
        }

        JPanel[] panels = {
                new GiaoVienPanel(), new DiemDanhPanel(maGiaoVien), new NhapDiemPanel(maGiaoVien),
                new LichGiangDayPanel(maGiaoVien), new ThongKeLopPanel(maGiaoVien)
        };

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

        JLabel title = new JLabel("Cổng giáo viên");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.TEXT_PRIMARY);

        JLabel subtitle = new JLabel("Điểm danh, quản lý lớp và nhập điểm");
        subtitle.setFont(UiTheme.CAPTION);
        subtitle.setForeground(UiTheme.TEXT_MUTED);

        left.add(title);
        left.add(subtitle);

        JLabel badge = new JLabel("Giáo viên");
        badge.setFont(UiTheme.CAPTION);
        badge.setOpaque(true);
        badge.setBackground(new Color(UiTheme.PRIMARY.getRed(), UiTheme.PRIMARY.getGreen(), UiTheme.PRIMARY.getBlue(), 24));
        badge.setForeground(UiTheme.PRIMARY);
        badge.setBorder(new EmptyBorder(6, 12, 6, 12));

        JButton btnLogout = new JButton("Đăng xuất");
        btnLogout.setFont(UiTheme.CAPTION);
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc muốn đăng xuất?", "Đăng Xuất",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new com.trungtam.ui.auth.LoginWindow().setVisible(true);
            }
        });

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);
        right.add(badge);
        right.add(btnLogout);

        topbar.add(left, BorderLayout.WEST);
        topbar.add(right, BorderLayout.EAST);
        return topbar;
    }
}
