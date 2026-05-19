package com.trungtam.ui.hocvien;

import com.formdev.flatlaf.FlatLightLaf;
import com.trungtam.ui.SidebarPanel;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import java.awt.*;

/**
 * Cổng học viên — sidebar xanh lá, content area dùng CardLayout.
 */
public class HocVienWindow extends JFrame {

    public HocVienWindow() {
        setTitle("Cổng Học Viên – Trung Tâm Đào Tạo");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1300, 820);
        setMinimumSize(new Dimension(1024, 640));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UiTheme.APP_BG);

        // ── Sidebar ───────────────────────────────────────────────────────────
        SidebarPanel sidebar = new SidebarPanel(UiTheme.SECONDARY);
        sidebar.addSection("Cổng Học Viên");
        String[] titles = {
                "Hồ Sơ Cá Nhân",
                "Tra Cứu Khóa Học",
                "Đăng Ký Lớp",
                "Thời Khóa Biểu",
                "Xem Điểm",
                "Bảng Xếp Hạng",
                "Tài Liệu",
                "Thanh Toán",
                "Đánh Giá"
        };
        for (String t : titles)
            sidebar.addItem(t);

        // ── Content area (CardLayout) ─────────────────────────────────────────
        JPanel[] panels = {
                new HoSoCaNhanPanel(),
                new TraCuuKhoaHocPanel(),
                new DangKyChuyenHuyPanel(),
                new ThoiKhoaBieuHVPanel(),
                new XemDiemHVPanel(),
                new BangXepHangPanel(),
                new TaiLieuPanel(),
                new ThanhToanHocPhiPanel(),
                new DanhGiaPanel()
        };

        CardLayout cards = new CardLayout();
        JPanel contentArea = new JPanel(cards);
        contentArea.setBackground(UiTheme.APP_BG);
        for (int i = 0; i < panels.length; i++) {
            contentArea.add(panels[i], String.valueOf(i));
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

    public static void main(String[] args) {
        try {
            FlatLightLaf.setup();
            UiTheme.applyGlobalTokens();
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new HocVienWindow().setVisible(true));
    }
}
