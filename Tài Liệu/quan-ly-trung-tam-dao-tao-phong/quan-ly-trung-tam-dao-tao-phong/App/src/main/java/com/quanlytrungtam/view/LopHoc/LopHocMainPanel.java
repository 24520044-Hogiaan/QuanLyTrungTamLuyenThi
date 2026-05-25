package com.quanlytrungtam.view.LopHoc;

import javax.swing.*;

import com.quanlytrungtam.view.Refreshable;

import java.awt.*;

public class LopHocMainPanel extends JPanel implements Refreshable {

    private TraCuuLopHocPanel dsPanel;

    private CardLayout cardLayout;
    private static int count = 0;

    public LopHocMainPanel() {
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        dsPanel = new TraCuuLopHocPanel(this);
        add(dsPanel, "LIST");
        cardLayout.show(this, "LIST");
        System.out.println(count);
        count++;
    }

    @Override
    public void refresh() {
        if (dsPanel != null) {
            dsPanel.refresh();
        }
    }

    public void showDetailPanel(int maLopHoc, String tenLop, String tenKhoaHoc,
            String giangVien, String tanSuat, String capDo,
            String ngayBD, String ngayKT, int siSo, String trangThai) {
        ChiTietLopHocPanel detail = new ChiTietLopHocPanel(
                this, maLopHoc, tenLop, tenKhoaHoc, giangVien,
                tanSuat, capDo, ngayBD, ngayKT, siSo, trangThai);
        add(detail, "DETAIL");
        cardLayout.show(this, "DETAIL");
    }

    public void backToList() {
        cardLayout.show(this, "LIST");
    }

}