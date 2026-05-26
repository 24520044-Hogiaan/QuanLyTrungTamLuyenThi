package com.quanlytrungtam;

import com.formdev.flatlaf.FlatLightLaf;
import com.quanlytrungtam.ui.UiTheme;
import com.quanlytrungtam.view.BoMon_View;
import com.quanlytrungtam.view.GiaoVien_View;
import com.quanlytrungtam.view.HocVien_View;
import com.quanlytrungtam.view.KhoaHoc_View;
import com.quanlytrungtam.view.NgayNghiLe_View;
import com.quanlytrungtam.view.PhongHoc_View;
import com.quanlytrungtam.view.Refreshable;
import com.quanlytrungtam.view.SidebarPanel;
import com.quanlytrungtam.view.ThongKeTab;
import com.quanlytrungtam.view.YeuCauChuyenLop_View;
import com.quanlytrungtam.view.YeuCauHoanTien_View;
import com.quanlytrungtam.view.LopHoc.LopHocMainPanel;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    public Main() {
        setTitle("Quản Lý ");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1300, 820);
        setMinimumSize(new Dimension(1024, 640));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UiTheme.APP_BG);

        SidebarPanel sidebar = new SidebarPanel(UiTheme.PRIMARY);
        sidebar.addSection("Nhân viên quản lý");
        String[] titles = {
                "Giáo Viên", "Bộ Môn", "Học Viên", "Khóa Học", "Lớp Học",
                "Thống Kê", "Yêu Cầu Chuyển Lớp", "Yêu Cầu Hoàn Tiền", "Phòng Học",
                "Ngày Nghỉ"
        };
        for (String t : titles)
            sidebar.addItem(t);

        JPanel[] panels = {
                new GiaoVien_View(),
                new BoMon_View(),
                new HocVien_View(),
                new KhoaHoc_View(),
                new LopHocMainPanel(),
                new ThongKeTab(),
                new YeuCauChuyenLop_View(),
                new YeuCauHoanTien_View(),
                new PhongHoc_View(),
                new NgayNghiLe_View()
        };

        CardLayout cards = new CardLayout();
        JPanel contentArea = new JPanel(cards);
        contentArea.setBackground(UiTheme.APP_BG);
        for (int i = 0; i < panels.length; i++) {
            contentArea.add(panels[i], String.valueOf(i));
        }
        cards.show(contentArea, "0");

        sidebar.setSelectionListener(idx -> {
            cards.show(contentArea, String.valueOf(idx));
            if (idx >= 0 && idx < panels.length) {
                JPanel selectedPanel = panels[idx];
                if (selectedPanel instanceof Refreshable) {
                    ((Refreshable) selectedPanel).refresh();
                }
            }
        });

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
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
