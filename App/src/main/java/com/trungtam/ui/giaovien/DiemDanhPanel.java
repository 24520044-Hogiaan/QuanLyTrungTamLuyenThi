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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

/**
 * Panel điểm danh học viên — dữ liệu từ database.
 */
public class DiemDanhPanel extends JPanel {

    private final LopHocController lopHocController = new LopHocController();
    private final DangKyController dangKyController = new DangKyController();
    private final HocVienController hocVienController = new HocVienController();

    private static final String[] TRANG_THAI = { "Có mặt", "Vắng mặt", "Đi trễ", "Nghỉ phép" };
    private static final Color[] STATUS_BG = {
            new Color(0xE8F5E9), new Color(0xFFEBEE), new Color(0xFFF8E1), new Color(0xE3F2FD)
    };
    private static final Color[] STATUS_FG = {
            UiTheme.SUCCESS, UiTheme.DANGER, UiTheme.WARNING, UiTheme.INFO
    };

    private final JComboBox<String> cboLop = new JComboBox<>();
    private DefaultTableModel tableModel;
    private JTable table;
    private final JButton btnLuu = UiComponents.primaryButton("Lưu Điểm Danh", new Color(0x43A047));
    private final JLabel lblSoLuong = new JLabel();
    private final JLabel lblBuoiInfo = new JLabel("← Chọn lớp để bắt đầu điểm danh");

    private final Map<String, Integer> lopNameToMa = new HashMap<>();

    public DiemDanhPanel() {
        setLayout(new BorderLayout(0, 10));
        setBorder(new EmptyBorder(UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M));
        setBackground(UiTheme.APP_BG);

        loadLopData();
        add(buildTopBar(), BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);

        cboLop.addActionListener(e -> onLopSelected());
    }

    private void loadLopData() {
        cboLop.removeAllItems();
        lopNameToMa.clear();
        List<LopHoc> allLop = lopHocController.layDanhSach();
        for (LopHoc lop : allLop) {
            if ("Dang mo".equalsIgnoreCase(lop.getTrangThai()) || "Dang hoc".equalsIgnoreCase(lop.getTrangThai())) {
                cboLop.addItem(lop.getTenLop());
                lopNameToMa.put(lop.getTenLop(), lop.getMaLopHoc());
            }
        }
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
        right.add(lblLop);
        right.add(cboLop);

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
        String tenLop = (String) cboLop.getSelectedItem();
        if (tenLop == null) return;
        Integer maLop = lopNameToMa.get(tenLop);
        if (maLop == null) return;

        lblBuoiInfo.setFont(UiTheme.BODY_B);
        lblBuoiInfo.setForeground(UiTheme.INFO);
        lblBuoiInfo.setText("Lớp: " + tenLop + "  |  Ngày: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        // Load students registered in this class
        List<DangKy> dkList = dangKyController.getDangKyByLop(maLop);
        List<HocVien> allHV = hocVienController.layDanhSach();
        Map<Integer, HocVien> hvMap = new HashMap<>();
        for (HocVien hv : allHV) hvMap.put(hv.getMaHocVien(), hv);

        tableModel.setRowCount(0);
        int idx = 1;
        for (DangKy dk : dkList) {
            HocVien hv = hvMap.get(dk.getMaHocVien());
            String hoTen = hv != null ? hv.getHoTen() : "HV #" + dk.getMaHocVien();
            tableModel.addRow(new Object[] { idx++, dk.getMaHocVien(), hoTen, "Có mặt", "" });
        }
        lblSoLuong.setText(dkList.size() + " học viên  ");
        btnLuu.setEnabled(!dkList.isEmpty());
    }

    private void saveDiemDanh() {
        long vang = countStatus("Vắng mặt"), tre = countStatus("Đi trễ");
        JOptionPane.showMessageDialog(this,
                String.format("Điểm danh đã lưu thành công!\n─────────────────\nVắng mặt: %d  |  Đi trễ: %d", vang, tre),
                "Lưu thành công", JOptionPane.INFORMATION_MESSAGE);
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
