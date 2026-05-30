package com.trungtam.ui.giaovien;

import com.trungtam.controller.DangKyController;
import com.trungtam.controller.HocVienController;
import com.trungtam.controller.LopHocController;
import com.trungtam.dao.BuoiHocDAO;
import com.trungtam.dao.DiemDanhDAO;
import com.trungtam.model.DangKy;
import com.trungtam.model.DiemDanh;
import com.trungtam.model.HocVien;
import com.trungtam.model.LopHoc;
import com.trungtam.model.BuoiHoc;
import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class DiemDanhPanel extends JPanel {

    private final LopHocController lopHocController = new LopHocController();
    private final DangKyController dangKyController = new DangKyController();
    private final HocVienController hocVienController = new HocVienController();
    private final BuoiHocDAO buoiHocDAO = new BuoiHocDAO();
    private final DiemDanhDAO diemDanhDAO = new DiemDanhDAO();

    private static final String[] TRANG_THAI = { "Co mat", "Vang mat", "Tre", "Nghi phep" };
    private static final Color[] STATUS_BG = {
            new Color(0xE8F5E9), new Color(0xFFEBEE), new Color(0xFFF8E1), new Color(0xE3F2FD)
    };
    private static final Color[] STATUS_FG = {
            UiTheme.SUCCESS, UiTheme.DANGER, UiTheme.WARNING, UiTheme.INFO
    };

    private final JComboBox<String> cboLop = new JComboBox<>();
    private final JComboBox<String> cboBuoi = new JComboBox<>();
    private DefaultTableModel tableModel;
    private JTable table;
    private final JButton btnLuu = UiComponents.primaryButton("Lưu Điểm Danh", new Color(0x43A047));
    private final JLabel lblSoLuong = new JLabel();
    private final JLabel lblBuoiInfo = new JLabel("← Chọn lớp và buổi học để điểm danh");

    private final Map<String, Integer> lopNameToMa = new HashMap<>();
    private final Map<String, Integer> buoiNameToMa = new HashMap<>();
    private final int maGiaoVien;
    
    private boolean isUpdatingCombo = false;

    public DiemDanhPanel(int maGiaoVien) {
        this.maGiaoVien = maGiaoVien;
        setLayout(new BorderLayout(0, 10));
        setBorder(new EmptyBorder(UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M));
        setBackground(UiTheme.APP_BG);

        add(buildTopBar(), BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);

        cboLop.addActionListener(e -> onLopSelected());
        cboBuoi.addActionListener(e -> onBuoiSelected());
        
        loadLopData();
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
        if (cboLop.getItemCount() > 0) onLopSelected();
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout(12, 0));
        bar.setOpaque(false);
        JLabel title = new JLabel("Điểm Danh Học Viên");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.TEXT_PRIMARY);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);
        
        JLabel lblLop = new JLabel("Lớp:");
        lblLop.setFont(UiTheme.BODY);
        cboLop.setFont(UiTheme.BODY);
        
        JLabel lblBuoi = new JLabel("Buổi:");
        lblBuoi.setFont(UiTheme.BODY);
        cboBuoi.setFont(UiTheme.BODY);
        cboBuoi.setPreferredSize(new Dimension(150, 32));
        
        JButton btnThemBuoi = new JButton("+ Tạo Buổi Hôm Nay");
        btnThemBuoi.setFont(UiTheme.BODY);
        btnThemBuoi.addActionListener(e -> taoBuoiHocMoi());
        
        right.add(lblLop);
        right.add(cboLop);
        right.add(lblBuoi);
        right.add(cboBuoi);
        right.add(btnThemBuoi);

        bar.add(title, BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    private JPanel buildContent() {
        String[] cols = { "#", "Mã HV", "Họ Tên Học Viên", "Trạng Thái", "Ghi Chú" };
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return c == 3 || c == 4;
            }
        };
        table = new JTable(tableModel);
        UiComponents.styleTable(table);
        table.getColumnModel().getColumn(0).setMaxWidth(40);
        table.getColumnModel().getColumn(1).setMaxWidth(70);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(new JComboBox<>(TRANG_THAI)));
        table.getColumnModel().getColumn(3).setCellRenderer(new StatusCellRenderer());
        UiComponents.setColumnAlignments(table,
                SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.LEFT,
                SwingConstants.CENTER, SwingConstants.LEFT);

        lblBuoiInfo.setFont(UiTheme.CAPTION_I);
        lblBuoiInfo.setForeground(UiTheme.TEXT_MUTED);

        lblSoLuong.setFont(UiTheme.BODY_B);
        lblSoLuong.setForeground(new Color(0x2E7D32));

        JPanel infoRow = new JPanel(new BorderLayout());
        infoRow.setOpaque(false);
        infoRow.setBorder(new EmptyBorder(0, 4, 6, 0));
        infoRow.add(lblBuoiInfo, BorderLayout.WEST);

        btnLuu.setEnabled(false);
        btnLuu.addActionListener(e -> saveDiemDanh());

        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setOpaque(false);
        panel.add(infoRow, BorderLayout.NORTH);
        panel.add(UiComponents.tableScroll(table), BorderLayout.CENTER);

        JPanel saveBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 4));
        saveBar.setOpaque(false);
        saveBar.add(lblSoLuong);
        saveBar.add(btnLuu);
        panel.add(saveBar, BorderLayout.SOUTH);
        return panel;
    }

    private void onLopSelected() {
        if (isUpdatingCombo) return;
        String tenLop = (String) cboLop.getSelectedItem();
        if (tenLop == null) return;
        Integer maLop = lopNameToMa.get(tenLop);
        if (maLop == null) return;

        isUpdatingCombo = true;
        cboBuoi.removeAllItems();
        buoiNameToMa.clear();
        try {
            List<BuoiHoc> dsBuoi = buoiHocDAO.findByLop(maLop);
            for (BuoiHoc bh : dsBuoi) {
                String tenBuoi = (bh.getNgayHoc() != null ? bh.getNgayHoc().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "") + 
                                 " (" + (bh.getGioHoc() != null ? bh.getGioHoc() : "") + ")";
                cboBuoi.addItem(tenBuoi);
                buoiNameToMa.put(tenBuoi, bh.getMaBuoiHoc());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        isUpdatingCombo = false;
        
        if (cboBuoi.getItemCount() > 0) {
            cboBuoi.setSelectedIndex(cboBuoi.getItemCount() - 1);
            onBuoiSelected();
        } else {
            tableModel.setRowCount(0);
            lblSoLuong.setText("0 học viên  ");
            btnLuu.setEnabled(false);
            lblBuoiInfo.setText("← Chưa có buổi học nào cho lớp này. Hãy bấm 'Tạo Buổi Hôm Nay'.");
        }
    }
    
    private void taoBuoiHocMoi() {
        String tenLop = (String) cboLop.getSelectedItem();
        if (tenLop == null) return;
        Integer maLop = lopNameToMa.get(tenLop);
        if (maLop == null) return;
        
        BuoiHoc bh = new BuoiHoc();
        bh.setMaLopHoc(maLop);
        bh.setNgayHoc(LocalDate.now());
        bh.setGioHoc("TBD");
        bh.setTrangThai("Hoan thanh");
        bh.setLoaiBuoi("Thuong");
        try {
            buoiHocDAO.insert(bh);
            JOptionPane.showMessageDialog(this, "Tạo buổi học mới thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            onLopSelected();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi tạo buổi học: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onBuoiSelected() {
        if (isUpdatingCombo) return;
        String tenLop = (String) cboLop.getSelectedItem();
        String tenBuoi = (String) cboBuoi.getSelectedItem();
        if (tenLop == null || tenBuoi == null) return;
        Integer maLop = lopNameToMa.get(tenLop);
        Integer maBuoi = buoiNameToMa.get(tenBuoi);
        if (maLop == null || maBuoi == null) return;

        lblBuoiInfo.setFont(UiTheme.BODY_B);
        lblBuoiInfo.setForeground(UiTheme.INFO);
        lblBuoiInfo.setText("Lớp: " + tenLop + "  |  Buổi: " + tenBuoi);

        List<DangKy> dkList = dangKyController.getDangKyByLop(maLop);
        List<HocVien> allHV = hocVienController.layDanhSach();
        Map<Integer, HocVien> hvMap = new HashMap<>();
        for (HocVien hv : allHV) hvMap.put(hv.getMaHocVien(), hv);
        
        Map<Integer, DiemDanh> diemDanhMap = new HashMap<>();
        try {
            List<DiemDanh> ddList = diemDanhDAO.findByBuoiHoc(maBuoi);
            for (DiemDanh dd : ddList) diemDanhMap.put(dd.getMaHocVien(), dd);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tableModel.setRowCount(0);
        int idx = 1;
        for (DangKy dk : dkList) {
            HocVien hv = hvMap.get(dk.getMaHocVien());
            String hoTen = hv != null ? hv.getHoTen() : "HV #" + dk.getMaHocVien();
            DiemDanh dd = diemDanhMap.get(dk.getMaHocVien());
            String status = dd != null ? dd.getTrangThai() : "Co mat";
            
            tableModel.addRow(new Object[] { idx++, dk.getMaHocVien(), hoTen, status, "" });
        }
        lblSoLuong.setText(dkList.size() + " học viên  ");
        btnLuu.setEnabled(!dkList.isEmpty());
    }

    private void saveDiemDanh() {
        String tenBuoi = (String) cboBuoi.getSelectedItem();
        if (tenBuoi == null) return;
        Integer maBuoi = buoiNameToMa.get(tenBuoi);
        if (maBuoi == null) return;

        long vang = countStatus("Vang mat"), tre = countStatus("Tre");
        
        try {
            for (int r = 0; r < tableModel.getRowCount(); r++) {
                int maHV = (Integer) tableModel.getValueAt(r, 1);
                String status = (String) tableModel.getValueAt(r, 3);
                
                DiemDanh dd = new DiemDanh();
                dd.setMaBuoiHoc(maBuoi);
                dd.setMaHocVien(maHV);
                dd.setTrangThai(status);
                diemDanhDAO.upsert(dd);
            }
            JOptionPane.showMessageDialog(this,
                    String.format("Điểm danh đã lưu thành công!\n─────────────────\nVắng mặt: %d  |  Đi trễ: %d", vang, tre),
                    "Lưu thành công", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi lưu điểm danh: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private long countStatus(String status) {
        long count = 0;
        for (int r = 0; r < tableModel.getRowCount(); r++) {
            if (status.equals(tableModel.getValueAt(r, 3))) count++;
        }
        return count;
    }

    private static class StatusCellRenderer extends JLabel implements TableCellRenderer {
        StatusCellRenderer() { setOpaque(true); setFont(UiTheme.BODY_B); }

        @Override
        public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int r, int c) {
            String s = String.valueOf(val);
            setText("  " + s);
            int idx = Arrays.asList(TRANG_THAI).indexOf(s);
            if (!sel) {
                setBackground(idx >= 0 ? STATUS_BG[idx] : UiTheme.CARD_BG);
                setForeground(idx >= 0 ? STATUS_FG[idx] : UiTheme.TEXT_PRIMARY);
            } else {
                setBackground(t.getSelectionBackground());
                setForeground(t.getSelectionForeground());
            }
            return this;
        }
    }
}
