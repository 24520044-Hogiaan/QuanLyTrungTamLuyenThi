package com.trungtam.ui.hocvien;

import com.trungtam.controller.DangKyController;
import com.trungtam.controller.LopHocController;
import com.trungtam.dao.BaiKiemTraDAO;
import com.trungtam.dao.ThamGiaBKTDAO;
import com.trungtam.model.*;
import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class XemDiemHVPanel extends JPanel {

    private final int maHocVien;
    private final DangKyController dangKyController = new DangKyController();
    private final LopHocController lopHocController = new LopHocController();
    private final BaiKiemTraDAO baiKiemTraDAO = new BaiKiemTraDAO();
    private final ThamGiaBKTDAO thamGiaBKTDAO = new ThamGiaBKTDAO();

    private final DefaultTableModel tableModel;
    private final JLabel lblTB = new JLabel("-");
    private final JLabel lblTong = new JLabel("-");
    private final JLabel lblXepLoai = new JLabel("-");
    private final JLabel lblSoBai = new JLabel("-");
    private final JComboBox<String> cboLop = new JComboBox<>();
    private final Map<String, Integer> tenLopToMa = new HashMap<>();

    public XemDiemHVPanel(int maHocVien) {
        this.maHocVien = maHocVien;
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(UiTheme.PAD_L, UiTheme.PAD_L, UiTheme.PAD_L, UiTheme.PAD_L));
        setBackground(UiTheme.APP_BG);

        JLabel title = new JLabel("XEM ĐIỂM & KẾT QUẢ HỌC TẬP");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.SECONDARY);
        add(title, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new String[] { "Bài Kiểm Tra", "Loại", "Ngày Thi", "Điểm", "Điểm Tối Đa", "Nhận Xét" }, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        UiComponents.styleTable(table);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        cboLop.setFont(UiTheme.BODY);
        cboLop.addActionListener(e -> loadDiem());

        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        filterBar.setOpaque(false);
        JLabel lblLopLabel = new JLabel("Lớp học:");
        lblLopLabel.setFont(UiTheme.BODY);
        filterBar.add(lblLopLabel);
        filterBar.add(cboLop);

        JPanel center = new JPanel(new BorderLayout(0, 8));
        center.setOpaque(false);
        center.add(filterBar, BorderLayout.NORTH);
        center.add(UiComponents.tableScroll(table), BorderLayout.CENTER);
        center.add(buildSummaryBar(), BorderLayout.SOUTH);
        add(center, BorderLayout.CENTER);

        loadLopData();
    }

    private void loadLopData() {
        cboLop.removeAllItems();
        tenLopToMa.clear();

        List<DangKy> myDK = dangKyController.getDangKyByHocVien(maHocVien);
        List<LopHoc> allLop = lopHocController.layDanhSach();
        Map<Integer, LopHoc> lopMap = new HashMap<>();
        for (LopHoc lop : allLop) lopMap.put(lop.getMaLopHoc(), lop);

        for (DangKy dk : myDK) {
            LopHoc lop = lopMap.get(dk.getMaLopHoc());
            if (lop != null) {
                cboLop.addItem(lop.getTenLop());
                tenLopToMa.put(lop.getTenLop(), lop.getMaLopHoc());
            }
        }

        if (cboLop.getItemCount() == 0) {
            lblSoBai.setText("0 bài");
            lblTB.setText("-");
            lblTong.setText("-");
            lblXepLoai.setText("-");
        }
    }

    private JPanel buildSummaryBar() {
        JPanel bar = new JPanel(new GridLayout(1, 4, 12, 0));
        bar.setOpaque(false);
        bar.setBorder(new EmptyBorder(8, 0, 0, 0));
        bar.add(UiComponents.statCard("Số Bài Thi", lblSoBai, UiTheme.INFO));
        bar.add(UiComponents.statCard("Điểm Trung Bình", lblTB, UiTheme.SUCCESS));
        bar.add(UiComponents.statCard("Điểm Tổng (có hệ số)", lblTong, new Color(0x6A1B9A)));
        bar.add(UiComponents.statCard("Xếp Loại", lblXepLoai, UiTheme.WARNING));
        return bar;
    }

    private void loadDiem() {
        tableModel.setRowCount(0);
        String selectedLop = (String) cboLop.getSelectedItem();
        if (selectedLop == null || !tenLopToMa.containsKey(selectedLop)) {
            resetSummary();
            return;
        }

        int maLop = tenLopToMa.get(selectedLop);

        try {
            List<BaiKiemTra> bktList = baiKiemTraDAO.findByLop(maLop);
            List<ThamGiaBKT> tgList = thamGiaBKTDAO.findByHocVien(maHocVien);

            Map<Integer, ThamGiaBKT> bestScoreMap = new HashMap<>();
            for (ThamGiaBKT tg : tgList) {
                ThamGiaBKT existing = bestScoreMap.get(tg.getMaBaiKiemTra());
                if (existing == null || tg.getDiemSo() > existing.getDiemSo()) {
                    bestScoreMap.put(tg.getMaBaiKiemTra(), tg);
                }
            }

            int soBai = 0;
            double tongDiemTX = 0, diemGK = 0, diemCK = 0;

            for (BaiKiemTra bkt : bktList) {
                ThamGiaBKT tg = bestScoreMap.get(bkt.getMaBaiKiemTra());
                String diem = tg != null ? String.format("%.1f", tg.getDiemSo()) : "Chưa thi";
                String nhanXet = tg != null ? (tg.getNhanXet() != null ? tg.getNhanXet() : "") : "";
                String ngayThi = tg != null && tg.getNgayThi() != null ? tg.getNgayThi().toString() : "-";

                tableModel.addRow(new Object[]{
                        bkt.getTenBKT(), bkt.getLoaiBKT(), ngayThi,
                        diem, bkt.getDiemToiDa(), nhanXet
                });

                if (tg != null) {
                    soBai++;
                    switch (bkt.getLoaiBKT()) {
                        case "Thuong xuyen": tongDiemTX += tg.getDiemSo(); break;
                        case "Giua ky": diemGK = tg.getDiemSo(); break;
                        case "Cuoi ky": diemCK = tg.getDiemSo(); break;
                    }
                }
            }

            lblSoBai.setText(soBai + " bài");

            if (soBai > 0) {
                // Công thức: (Tổng TX + GK*2 + CK*3) / 9
                double diemTB = (tongDiemTX + diemGK * 2 + diemCK * 3) / 9.0;
                double diemTong = tongDiemTX + diemGK * 2 + diemCK * 3;

                lblTB.setText(String.format("%.2f", diemTB));
                lblTong.setText(String.format("%.1f", diemTong));
                lblXepLoai.setText(xepLoai(diemTB));
            } else {
                resetSummary();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            resetSummary();
        }
    }

    private void resetSummary() {
        lblSoBai.setText("0 bài");
        lblTB.setText("-");
        lblTong.setText("-");
        lblXepLoai.setText("Chưa có dữ liệu");
    }

    private String xepLoai(double diemTB) {
        if (diemTB >= 8.5) return "Giỏi";
        if (diemTB >= 7.0) return "Khá";
        if (diemTB >= 5.0) return "Trung Bình";
        return "Yếu";
    }
}
