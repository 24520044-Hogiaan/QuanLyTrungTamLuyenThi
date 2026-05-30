package com.trungtam.ui.hocvien;

import com.trungtam.controller.DangKyController;
import com.trungtam.controller.LopHocController;
import com.trungtam.model.DangKy;
import com.trungtam.model.LopHoc;
import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;
import com.trungtam.util.DatabaseConnection;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;
import java.util.*;
import java.util.List;

public class BangXepHangPanel extends JPanel {

    private final int maHocVien;
    private final DangKyController dangKyController = new DangKyController();
    private final LopHocController lopHocController = new LopHocController();

    private final DefaultTableModel tableModel;
    private final JLabel vRank = new JLabel("-");
    private final JLabel vTop = new JLabel("-");
    private final JLabel vDiem = new JLabel("-");
    private final JComboBox<String> cboLop = new JComboBox<>();
    private final Map<String, Integer> tenLopToMa = new HashMap<>();

    public BangXepHangPanel(int maHocVien) {
        this.maHocVien = maHocVien;
        setLayout(new BorderLayout(0, 14));
        setBorder(new EmptyBorder(UiTheme.PAD_L, UiTheme.PAD_L, UiTheme.PAD_L, UiTheme.PAD_L));
        setBackground(UiTheme.APP_BG);

        JLabel title = new JLabel("BẢNG XẾP HẠNG LỚP HỌC");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.SECONDARY);
        add(title, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new String[] { "Hạng", "Họ Tên", "Điểm TB", "Xếp Loại" }, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        add(buildContent(), BorderLayout.CENTER);
        loadLopData();
    }

    private JPanel buildContent() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setOpaque(false);

        cboLop.setFont(UiTheme.BODY);
        cboLop.addActionListener(e -> loadRanking());

        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        filterBar.setOpaque(false);
        JLabel lblLop = new JLabel("Chọn lớp:");
        lblLop.setFont(UiTheme.BODY);
        filterBar.add(lblLop);
        filterBar.add(cboLop);

        panel.add(filterBar, BorderLayout.NORTH);

        JPanel card = new JPanel(new GridLayout(1, 3, 12, 0));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(8, 0, 8, 0));
        card.add(UiComponents.statCard("Hạng của bạn", vRank, UiTheme.INFO));
        card.add(UiComponents.statCard("Top phần trăm", vTop, UiTheme.SUCCESS));
        card.add(UiComponents.statCard("Điểm TB của bạn", vDiem, new Color(0x6A1B9A)));

        JTable table = new JTable(tableModel);
        UiComponents.styleTable(table);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 8));
        centerPanel.setOpaque(false);
        centerPanel.add(card, BorderLayout.NORTH);
        centerPanel.add(UiComponents.tableScroll(table), BorderLayout.CENTER);

        panel.add(centerPanel, BorderLayout.CENTER);
        return panel;
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
            vRank.setText("-");
            vTop.setText("-");
            vDiem.setText("-");
        }
    }

    private void loadRanking() {
        tableModel.setRowCount(0);
        String selectedLop = (String) cboLop.getSelectedItem();
        if (selectedLop == null || !tenLopToMa.containsKey(selectedLop)) return;

        int maLop = tenLopToMa.get(selectedLop);

        String sql = "SELECT MAHOCVIEN, TEN_HOC_VIEN, DIEM_TRUNG_BINH " +
                     "FROM V_DIEM_TRUNG_BINH_KHOA_HOC WHERE MALOP = ? " +
                     "ORDER BY DIEM_TRUNG_BINH DESC";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maLop);
            try (ResultSet rs = ps.executeQuery()) {
                int rank = 0;
                int totalStudents = 0;
                int myRank = -1;
                double myDiemTB = 0;

                List<Object[]> rows = new ArrayList<>();
                while (rs.next()) {
                    rank++;
                    totalStudents++;
                    int hvId = rs.getInt("MAHOCVIEN");
                    String tenHV = rs.getString("TEN_HOC_VIEN");
                    double diemTB = rs.getDouble("DIEM_TRUNG_BINH");
                    String xepLoai = xepLoai(diemTB);

                    rows.add(new Object[]{ rank, tenHV, String.format("%.2f", diemTB), xepLoai });

                    if (hvId == maHocVien) {
                        myRank = rank;
                        myDiemTB = diemTB;
                    }
                }

                for (Object[] row : rows) {
                    tableModel.addRow(row);
                }

                if (myRank > 0) {
                    vRank.setText(myRank + "/" + totalStudents);
                    double topPercent = ((double) myRank / totalStudents) * 100;
                    vTop.setText(String.format("Top %.0f%%", topPercent));
                    vDiem.setText(String.format("%.2f", myDiemTB));
                } else {
                    vRank.setText("-");
                    vTop.setText("-");
                    vDiem.setText("Chưa có điểm");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            vRank.setText("-");
            vTop.setText("-");
            vDiem.setText("Lỗi tải dữ liệu");
        }
    }

    private String xepLoai(double diemTB) {
        if (diemTB >= 8.5) return "Giỏi";
        if (diemTB >= 7.0) return "Khá";
        if (diemTB >= 5.0) return "Trung Bình";
        return "Yếu";
    }
}
