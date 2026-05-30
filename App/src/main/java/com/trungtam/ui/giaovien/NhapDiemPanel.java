package com.trungtam.ui.giaovien;

import com.trungtam.controller.DangKyController;
import com.trungtam.controller.HocVienController;
import com.trungtam.controller.LopHocController;
import com.trungtam.dao.BaiKiemTraDAO;
import com.trungtam.dao.ThamGiaBKTDAO;
import com.trungtam.model.BaiKiemTra;
import com.trungtam.model.DangKy;
import com.trungtam.model.HocVien;
import com.trungtam.model.LopHoc;
import com.trungtam.model.ThamGiaBKT;
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

public class NhapDiemPanel extends JPanel {

    private final LopHocController lopHocController = new LopHocController();
    private final DangKyController dangKyController = new DangKyController();
    private final HocVienController hocVienController = new HocVienController();
    private final BaiKiemTraDAO bktDAO = new BaiKiemTraDAO();
    private final ThamGiaBKTDAO tgDAO = new ThamGiaBKTDAO();

    private final JComboBox<String> cboLop = new JComboBox<>();
    private final JComboBox<String> cboBKT = new JComboBox<>();
    private DefaultTableModel tableModel;
    private final JLabel lblSoLuong = new JLabel();
    private final Map<String, Integer> lopNameToMa = new HashMap<>();
    private final Map<String, Integer> bktNameToMa = new HashMap<>();
    private final int maGiaoVien;
    private final JButton btnLuu = UiComponents.primaryButton("Lưu Điểm", new Color(0x43A047));
    private final JLabel lblInfo = new JLabel("← Chọn lớp và bài kiểm tra để nhập điểm");
    
    private boolean isUpdatingCombo = false;

    public NhapDiemPanel(int maGiaoVien) {
        this.maGiaoVien = maGiaoVien;
        setLayout(new BorderLayout(0, 10));
        setBorder(new EmptyBorder(UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M));
        setBackground(UiTheme.APP_BG);

        add(buildTopBar(), BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);

        cboLop.addActionListener(e -> onLopSelected());
        cboBKT.addActionListener(e -> onBKTSelected());
        
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
        JLabel title = new JLabel("Nhập Điểm Bài Kiểm Tra");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.TEXT_PRIMARY);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);

        JLabel lblLop = new JLabel("Lớp:");
        lblLop.setFont(UiTheme.BODY);
        cboLop.setFont(UiTheme.BODY);
        
        JLabel lblBkt = new JLabel("Bài kiểm tra:");
        lblBkt.setFont(UiTheme.BODY);
        cboBKT.setFont(UiTheme.BODY);
        cboBKT.setPreferredSize(new Dimension(200, 32));

        JButton btnTao = new JButton("+ Tạo BKT");
        btnTao.setFont(UiTheme.BODY);
        btnTao.addActionListener(e -> taoBKTMoi());

        right.add(lblLop);
        right.add(cboLop);
        right.add(lblBkt);
        right.add(cboBKT);
        right.add(btnTao);

        bar.add(title, BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    private JPanel buildContent() {
        String[] cols = { "#", "Mã HV", "Họ Tên Học Viên", "Điểm (0-10)", "Nhận Xét" };
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return c == 3 || c == 4;
            }
        };
        JTable table = new JTable(tableModel);
        UiComponents.styleTable(table);
        table.getColumnModel().getColumn(0).setMaxWidth(40);
        table.getColumnModel().getColumn(1).setMaxWidth(70);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        UiComponents.setColumnAlignments(table,
                SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.LEFT,
                SwingConstants.CENTER, SwingConstants.LEFT);

        lblInfo.setFont(UiTheme.CAPTION_I);
        lblInfo.setForeground(UiTheme.TEXT_MUTED);

        lblSoLuong.setFont(UiTheme.BODY_B);
        lblSoLuong.setForeground(new Color(0x2E7D32));

        btnLuu.setEnabled(false);
        btnLuu.addActionListener(e -> saveDiem());
        
        JPanel infoRow = new JPanel(new BorderLayout());
        infoRow.setOpaque(false);
        infoRow.setBorder(new EmptyBorder(0, 4, 6, 0));
        infoRow.add(lblInfo, BorderLayout.WEST);

        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setOpaque(false);
        panel.add(infoRow, BorderLayout.NORTH);
        panel.add(UiComponents.tableScroll(table), BorderLayout.CENTER);

        JPanel saveBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
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
        cboBKT.removeAllItems();
        bktNameToMa.clear();
        try {
            List<BaiKiemTra> bktList = bktDAO.findByLop(maLop);
            for (BaiKiemTra bkt : bktList) {
                String ten = bkt.getTenBKT() + " (" + (bkt.getNgayKiemTra() != null ? bkt.getNgayKiemTra().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "") + ")";
                cboBKT.addItem(ten);
                bktNameToMa.put(ten, bkt.getMaBaiKiemTra());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        isUpdatingCombo = false;
        
        if (cboBKT.getItemCount() > 0) {
            cboBKT.setSelectedIndex(cboBKT.getItemCount() - 1);
            onBKTSelected();
        } else {
            tableModel.setRowCount(0);
            lblSoLuong.setText("0 học viên  ");
            btnLuu.setEnabled(false);
            lblInfo.setText("← Chưa có bài kiểm tra nào. Hãy bấm '+ Tạo BKT'.");
        }
    }
    
    private void taoBKTMoi() {
        String tenLop = (String) cboLop.getSelectedItem();
        if (tenLop == null) return;
        Integer maLop = lopNameToMa.get(tenLop);
        if (maLop == null) return;
        
        JPanel pnl = new JPanel(new GridLayout(4, 2, 5, 5));
        JTextField txtTen = new JTextField();
        JComboBox<String> cboLoai = new JComboBox<>(new String[]{"Thường xuyên", "Giữa kỳ", "Cuối kỳ"});
        JTextField txtTgian = new JTextField("45");
        JTextField txtDiemMax = new JTextField("10");
        
        pnl.add(new JLabel("Tên bài KT:")); pnl.add(txtTen);
        pnl.add(new JLabel("Loại:")); pnl.add(cboLoai);
        pnl.add(new JLabel("Thời gian (phút):")); pnl.add(txtTgian);
        pnl.add(new JLabel("Điểm tối đa:")); pnl.add(txtDiemMax);
        
        int res = JOptionPane.showConfirmDialog(this, pnl, "Tạo Bài Kiểm Tra Mới", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION && !txtTen.getText().trim().isEmpty()) {
            BaiKiemTra bkt = new BaiKiemTra();
            bkt.setMaLop(maLop);
            bkt.setTenBKT(txtTen.getText().trim());
            bkt.setLoaiBKT((String)cboLoai.getSelectedItem());
            bkt.setNgayKiemTra(LocalDate.now());
            try {
                bkt.setThoiGian(Integer.parseInt(txtTgian.getText().trim()));
                bkt.setDiemToiDa(Integer.parseInt(txtDiemMax.getText().trim()));
                bkt.setDiemDat(0);
                
                bktDAO.insert(bkt);
                JOptionPane.showMessageDialog(this, "Tạo BKT mới thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                onLopSelected();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi tạo BKT: Kiểm tra lại số liệu", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onBKTSelected() {
        if (isUpdatingCombo) return;
        String tenLop = (String) cboLop.getSelectedItem();
        String tenBKT = (String) cboBKT.getSelectedItem();
        if (tenLop == null || tenBKT == null) return;
        Integer maLop = lopNameToMa.get(tenLop);
        Integer maBKT = bktNameToMa.get(tenBKT);
        if (maLop == null || maBKT == null) return;

        lblInfo.setFont(UiTheme.BODY_B);
        lblInfo.setForeground(UiTheme.INFO);
        lblInfo.setText("Lớp: " + tenLop + "  |  BKT: " + tenBKT);

        List<DangKy> dkList = dangKyController.getDangKyByLop(maLop);
        List<HocVien> allHV = hocVienController.layDanhSach();
        Map<Integer, HocVien> hvMap = new HashMap<>();
        for (HocVien hv : allHV) hvMap.put(hv.getMaHocVien(), hv);
        
        Map<Integer, ThamGiaBKT> tgMap = new HashMap<>();
        try {
            List<ThamGiaBKT> tgList = tgDAO.findByBaiKiemTra(maBKT);
            for (ThamGiaBKT tg : tgList) tgMap.put(tg.getMaHocVien(), tg);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tableModel.setRowCount(0);
        int idx = 1;
        for (DangKy dk : dkList) {
            HocVien hv = hvMap.get(dk.getMaHocVien());
            String hoTen = hv != null ? hv.getHoTen() : "HV #" + dk.getMaHocVien();
            ThamGiaBKT tg = tgMap.get(dk.getMaHocVien());
            
            String diem = tg != null ? String.valueOf(tg.getDiemSo()) : "";
            String nhanXet = tg != null && tg.getNhanXet() != null ? tg.getNhanXet() : "";
            
            tableModel.addRow(new Object[] { idx++, dk.getMaHocVien(), hoTen, diem, nhanXet });
        }
        lblSoLuong.setText(dkList.size() + " học viên  ");
        btnLuu.setEnabled(!dkList.isEmpty());
    }
    
    private void saveDiem() {
        String tenBKT = (String) cboBKT.getSelectedItem();
        if (tenBKT == null) return;
        Integer maBKT = bktNameToMa.get(tenBKT);
        if (maBKT == null) return;
        
        try {
            for (int r = 0; r < tableModel.getRowCount(); r++) {
                int maHV = (Integer) tableModel.getValueAt(r, 1);
                String diemStr = String.valueOf(tableModel.getValueAt(r, 3)).trim();
                String nxStr = String.valueOf(tableModel.getValueAt(r, 4)).trim();
                
                if (diemStr.isEmpty()) continue; // Skip if no grade
                
                double diem = Double.parseDouble(diemStr);
                if (diem < 0 || diem > 10) throw new NumberFormatException("Điểm phải từ 0 đến 10");
                
                ThamGiaBKT tg = new ThamGiaBKT();
                tg.setMaBaiKiemTra(maBKT);
                tg.setMaHocVien(maHV);
                tg.setLanThi(1);
                tg.setDiemSo(diem);
                tg.setNhanXet(nxStr.isEmpty() ? null : nxStr);
                tg.setNgayThi(LocalDate.now());
                
                tgDAO.upsert(tg);
            }
            JOptionPane.showMessageDialog(this, "Lưu điểm thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi nhập điểm: Vui lòng nhập số hợp lệ (0-10)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi lưu điểm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
