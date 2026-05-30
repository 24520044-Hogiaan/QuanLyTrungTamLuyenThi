package com.trungtam.ui.giaovien;

import com.trungtam.controller.DangKyController;
import com.trungtam.controller.HocVienController;
import com.trungtam.controller.LopHocController;
import com.trungtam.model.DangKy;
import com.trungtam.model.HocVien;
import com.trungtam.model.LopHoc;
import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Thống kê lớp học — dữ liệu từ database.
 */
public class ThongKeLopPanel extends JPanel {

    private final LopHocController lopHocController = new LopHocController();
    private final DangKyController dangKyController = new DangKyController();
    private final HocVienController hocVienController = new HocVienController();

    private final JComboBox<String> cboLop = new JComboBox<>();
    private DefaultTableModel tableModel;
    private final JLabel lblTongHV = new JLabel("-");
    private final JLabel lblSiSoMax = new JLabel("-");
    private final JLabel lblTrangThai = new JLabel("-");
    private final Map<String, Integer> lopNameToMa = new HashMap<>();
    private final int maGiaoVien;

    public ThongKeLopPanel(int maGiaoVien) {
        this.maGiaoVien = maGiaoVien;
        setLayout(new BorderLayout(0, 10));
        setBorder(new EmptyBorder(UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M));
        setBackground(UiTheme.APP_BG);

        loadLopData();
        add(buildTopBar(), BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);

        cboLop.addActionListener(e -> onLopSelected());
        if (cboLop.getItemCount() > 0) onLopSelected();
    }

    private void loadLopData() {
        cboLop.removeAllItems();
        lopNameToMa.clear();
        List<LopHoc> allLop = lopHocController.layDanhSach();
        for (LopHoc lop : allLop) {
            if (lop.getMaGiaoVien() == maGiaoVien &&
                ("Dang mo".equalsIgnoreCase(lop.getTrangThai()) || "Dang hoc".equalsIgnoreCase(lop.getTrangThai()))) {
                cboLop.addItem(lop.getTenLop());
                lopNameToMa.put(lop.getTenLop(), lop.getMaLopHoc());
            }
        }
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout(12, 0));
        bar.setOpaque(false);
        JLabel title = new JLabel("Thống Kê Lớp Học");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.TEXT_PRIMARY);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);
        JLabel lblLop = new JLabel("Lớp:");
        lblLop.setFont(UiTheme.BODY);
        cboLop.setFont(UiTheme.BODY);
        right.add(lblLop);
        right.add(cboLop);

        bar.add(title, BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    private JPanel buildContent() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setOpaque(false);

        // Summary cards
        JPanel cards = new JPanel(new GridLayout(1, 3, 12, 0));
        cards.setOpaque(false);
        cards.setBorder(new EmptyBorder(0, 0, 4, 0));
        cards.add(UiComponents.statCard("Tổng Học Viên", lblTongHV, UiTheme.INFO));
        cards.add(UiComponents.statCard("Sĩ Số Tối Đa", lblSiSoMax, UiTheme.WARNING));
        cards.add(UiComponents.statCard("Trạng Thái Lớp", lblTrangThai, UiTheme.SUCCESS));
        panel.add(cards, BorderLayout.NORTH);

        // Student table
        String[] cols = { "#", "Mã HV", "Họ Tên", "Email", "SĐT", "Trạng Thái ĐK" };
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        UiComponents.styleTable(table);
        table.getColumnModel().getColumn(0).setMaxWidth(40);
        table.getColumnModel().getColumn(1).setMaxWidth(70);
        UiComponents.setColumnAlignments(table,
                SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.LEFT,
                SwingConstants.LEFT, SwingConstants.CENTER, SwingConstants.CENTER);

        panel.add(UiComponents.tableScroll(table), BorderLayout.CENTER);
        return panel;
    }

    private void onLopSelected() {
        String tenLop = (String) cboLop.getSelectedItem();
        if (tenLop == null) return;
        Integer maLop = lopNameToMa.get(tenLop);
        if (maLop == null) return;

        // Find the LopHoc
        List<LopHoc> allLop = lopHocController.layDanhSach();
        LopHoc selectedLop = null;
        for (LopHoc lop : allLop) {
            if (lop.getMaLopHoc() == maLop) { selectedLop = lop; break; }
        }

        // Load students
        List<DangKy> dkList = dangKyController.getDangKyByLop(maLop);
        List<HocVien> allHV = hocVienController.layDanhSach();
        Map<Integer, HocVien> hvMap = new HashMap<>();
        for (HocVien hv : allHV) hvMap.put(hv.getMaHocVien(), hv);

        // Update summary
        lblTongHV.setText(String.valueOf(dkList.size()));
        lblSiSoMax.setText(selectedLop != null ? String.valueOf(selectedLop.getSiSo()) : "-");
        lblTrangThai.setText(selectedLop != null ? selectedLop.getTrangThai() : "-");

        // Fill table
        tableModel.setRowCount(0);
        int idx = 1;
        for (DangKy dk : dkList) {
            HocVien hv = hvMap.get(dk.getMaHocVien());
            tableModel.addRow(new Object[] {
                idx++,
                dk.getMaHocVien(),
                hv != null ? hv.getHoTen() : "HV #" + dk.getMaHocVien(),
                hv != null ? (hv.getEmail() != null ? hv.getEmail() : "") : "",
                hv != null ? (hv.getSoDienThoai() != null ? hv.getSoDienThoai() : "") : "",
                dk.getTrangThaiDKY() != null ? dk.getTrangThaiDKY() : ""
            });
        }
    }
}
