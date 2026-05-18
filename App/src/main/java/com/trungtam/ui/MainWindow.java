package com.trungtam.ui;

import com.formdev.flatlaf.FlatLightLaf;
import com.trungtam.ui.giaovien.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private static final Color SIDEBAR_BG  = new Color(0x1A237E); // xanh navy đậm
    private static final Color SIDEBAR_SEL = new Color(0x1565C0); // xanh sáng hơn khi chọn
    private static final Color SIDEBAR_FG  = new Color(0xC5CAE9); // trắng ngà
    private static final Color ACCENT_BAR  = new Color(0x90CAF9); // thanh accent xanh nhạt

    public MainWindow() {
        setTitle("Quan Ly Giao Vien - Trung Tam Dao Tao");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 800);
        setLocationRelativeTo(null);
        setJMenuBar(buildMenuBar());
        add(buildTabbedPane());
    }

    private JMenuBar buildMenuBar() {
        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("Module");
        menu.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JMenuItem miGV = new JMenuItem("Quan Ly Giao Vien (dang mo)");
        miGV.setEnabled(false);

        JMenuItem miHV = new JMenuItem("Quan Ly Hoc Vien");
        miHV.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        miHV.addActionListener(e ->
            new com.trungtam.ui.hocvien.HocVienWindow().setVisible(true));

        menu.add(miGV);
        menu.addSeparator();
        menu.add(miHV);
        bar.add(menu);
        return bar;
    }

    private JTabbedPane buildTabbedPane() {
        JTabbedPane tabs = new JTabbedPane(JTabbedPane.LEFT);
        tabs.setBackground(SIDEBAR_BG);

        String[] titles = {
            "DANH SÁCH GV", "ĐIỂM DANH", "NHẬP ĐIỂM",
            "LỊCH GIẢNG DẠY", "THỐNG KÊ LỚP", "GỬI THÔNG BÁO"
        };
        JPanel[] panels = {
            new GiaoVienPanel(), new DiemDanhPanel(), new NhapDiemPanel(),
            new LichGiangDayPanel(), new ThongKeLopPanel(), new GuiThongBaoPanel()
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
        panel.setPreferredSize(new Dimension(158, 46));

        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
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
                // Tab chọn: xanh sáng hơn + thanh accent + chữ trắng sáng
                panel.setBackground(SIDEBAR_SEL);
                panel.setBorder(new MatteBorder(0, 4, 0, 0, ACCENT_BAR));
                lbl.setForeground(Color.WHITE);
                lbl.setBorder(new EmptyBorder(0, 10, 0, 0));
            } else {
                // Tab thường: xanh đậm + chữ trắng ngà
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
            UIManager.put("TabbedPane.tabHeight", 48);
            // Xóa focus ring trắng mặc định của FlatLaf quanh tab đang chọn
            UIManager.put("TabbedPane.focusColor", new Color(0, 0, 0, 0));
            UIManager.put("TabbedPane.selectedBackground", SIDEBAR_BG);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new MainWindow().setVisible(true));
    }
}
