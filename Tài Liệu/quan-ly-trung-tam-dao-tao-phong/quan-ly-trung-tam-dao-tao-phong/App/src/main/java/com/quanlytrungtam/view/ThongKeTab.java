package com.quanlytrungtam.view;

import javax.swing.*;

import com.quanlytrungtam.ui.UiTheme;

import java.awt.*;

public class ThongKeTab extends JPanel implements Refreshable {

        private JTabbedPane tabbedPane;
        private DoanhThuPanel doanhThuPanel;
        private BaoCaoDangKyPanel baoCaoDangKyPanel;

        public ThongKeTab() {

                setLayout(new BorderLayout());
                setBackground(UiTheme.APP_BG);

                initTabs();
        }

        private void initTabs() {

                tabbedPane = new JTabbedPane();

                tabbedPane.setFont(UiTheme.BODY_B);

                tabbedPane.setFocusable(false);

                doanhThuPanel = new DoanhThuPanel();
                tabbedPane.addTab("Doanh Thu", doanhThuPanel);

                baoCaoDangKyPanel = new BaoCaoDangKyPanel();
                tabbedPane.addTab("Đăng Ký", baoCaoDangKyPanel);

                add(tabbedPane, BorderLayout.CENTER);
        }

        @Override
        public void refresh() {
                if (doanhThuPanel != null) {
                        doanhThuPanel.refresh();
                }
                if (baoCaoDangKyPanel != null) {
                        baoCaoDangKyPanel.refresh();
                }
        }

}