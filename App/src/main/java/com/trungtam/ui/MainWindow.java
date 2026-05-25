package com.trungtam.ui;

import com.trungtam.ui.giaovien.*;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    public MainWindow() {
        setTitle("Quản Lý Giáo Viên – Trung Tâm Đào Tạo");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1300, 820);
        setMinimumSize(new Dimension(1024, 640));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UiTheme.APP_BG);

        // ── Sidebar ───────────────────────────────────────────────────────────
        SidebarPanel sidebar = new SidebarPanel(UiTheme.PRIMARY);
        sidebar.addSection("Chức Năng Chính");
        String[] titles = {
                "Danh Sách GV", "Điểm Danh", "Nhập Điểm",
                "Lịch Giảng Dạy", "Thống Kê Lớp", "Gửi Thông Báo"
        };
        for (String t : titles)
            sidebar.addItem(t);

        // ── Content area (CardLayout) ─────────────────────────────────────────
        JPanel[] panels = {
                new GiaoVienPanel(), new DiemDanhPanel(), new NhapDiemPanel(),
                new LichGiangDayPanel(), new ThongKeLopPanel(), new GuiThongBaoPanel()
        };

        CardLayout cards = new CardLayout();
        JPanel contentArea = new JPanel(cards);
        contentArea.setBackground(UiTheme.APP_BG);
        for (int i = 0; i < panels.length; i++) {
            contentArea.add(panels[i], String.valueOf(i));
        }
        cards.show(contentArea, "0");

        sidebar.setSelectionListener(idx -> cards.show(contentArea, String.valueOf(idx)));

        // Wrap sidebar in a scroll pane so it works on small screens
        JScrollPane sidebarScroll = new JScrollPane(sidebar,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sidebarScroll.setBorder(null);
        sidebarScroll.getVerticalScrollBar().setUnitIncrement(16);

        root.add(sidebarScroll, BorderLayout.WEST);
        root.add(contentArea, BorderLayout.CENTER);
        setContentPane(root);
    }
}
