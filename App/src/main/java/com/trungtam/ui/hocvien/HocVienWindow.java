package com.trungtam.ui.hocvien;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * Cửa sổ cổng học viên — sidebar xanh lá để phân biệt với module Giáo Viên (xanh navy).
 */
public class HocVienWindow extends JFrame {

    private static final Color SIDEBAR_BG  = new Color(0x1B5E20);
    private static final Color SIDEBAR_SEL = new Color(0x2E7D32);
    private static final Color SIDEBAR_FG  = new Color(0xC8E6C9);
    private static final Color ACCENT_BAR  = new Color(0xA5D6A7);

    public HocVienWindow() {
        setTitle("Cong Hoc Vien - Trung Tam Dao Tao");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1280, 800);
        setLocationRelativeTo(null);
        add(buildTabbedPane());
    }

    private JTabbedPane buildTabbedPane() {
        JTabbedPane tabs = new JTabbedPane(JTabbedPane.LEFT);
        tabs.setBackground(SIDEBAR_BG);

        String[] titles = {
            "HỒ SƠ CÁ NHÂN",
            "TRA CỨU KHÓA HỌC",
            "ĐĂNG KÝ LỚP",
            "THỜI KHÓA BIỂU",
            "XEM ĐIỂM",
            "BẢNG XẾP HẠNG",
            "TÀI LIỆU",
            "THANH TOÁN",
            "ĐÁNH GIÁ"
        };
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

        for (int i = 0; i < titles.length; i++) {
            tabs.addTab(null, panels[i]);
            tabs.setTabComponentAt(i, buildTabLabel(titles[i]));
        }

        tabs.addChangeListener(e -> updateTabHighlight(tabs, titles.length));
        updateTabHighlight(tabs, titles.length);
        return tabs;
    }

    private JPanel buildTabLabel(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(true);
        panel.setPreferredSize(new Dimension(158, 44));
        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setBorder(new EmptyBorder(0, 14, 0, 0));
        panel.add(lbl, BorderLayout.CENTER);
        return panel;
    }

    private void updateTabHighlight(JTabbedPane tabs, int count) {
        int selected = tabs.getSelectedIndex();
        for (int i = 0; i < count; i++) {
            if (!(tabs.getTabComponentAt(i) instanceof JPanel panel)) continue;
            JLabel lbl = (JLabel) panel.getComponent(0);
            if (i == selected) {
                panel.setBackground(SIDEBAR_SEL);
                panel.setBorder(new MatteBorder(0, 4, 0, 0, ACCENT_BAR));
                lbl.setForeground(Color.WHITE);
                lbl.setBorder(new EmptyBorder(0, 10, 0, 0));
            } else {
                panel.setBackground(SIDEBAR_BG);
                panel.setBorder(null);
                lbl.setForeground(SIDEBAR_FG);
                lbl.setBorder(new EmptyBorder(0, 14, 0, 0));
            }
        }
    }

    public static void main(String[] args) {
        try {
            FlatLightLaf.setup();
            UIManager.put("TabbedPane.tabHeight", 46);
            UIManager.put("TabbedPane.focusColor", new Color(0, 0, 0, 0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new HocVienWindow().setVisible(true));
    }
}
