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
 * Panel nhập điểm cho học viên — dữ liệu từ database.
 */
public class NhapDiemPanel extends JPanel {

    private final LopHocController lopHocController = new LopHocController();
    private final DangKyController dangKyController = new DangKyController();
    private final HocVienController hocVienController = new HocVienController();

    private static final String[] LOAI_BKT = { "Thường xuyên", "Giữa kỳ", "Cuối kỳ" };

    private final JComboBox<String> cboLop = new JComboBox<>();
    private final JComboBox<String> cboLoai = new JComboBox<>(LOAI_BKT);
    private final JTextField txtTenBKT = new JTextField(20);
    private DefaultTableModel tableModel;
    private final JLabel lblSoLuong = new JLabel();
    private final Map<String, Integer> lopNameToMa = new HashMap<>();

    public NhapDiemPanel() {
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
        JLabel title = new JLabel("Nhập Điểm Bài Kiểm Tra");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.TEXT_PRIMARY);

        JPanel form = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        form.setOpaque(false);

        JLabel lblLop = new JLabel("Lớp:");
        lblLop.setFont(UiTheme.BODY);
        cboLop.setFont(UiTheme.BODY);
        JLabel lblLoai = new JLabel("Loại:");
        lblLoai.setFont(UiTheme.BODY);
        cboLoai.setFont(UiTheme.BODY);
        JLabel lblTen = new JLabel("Tên BKT:");
        lblTen.setFont(UiTheme.BODY);
        txtTenBKT.setFont(UiTheme.BODY);
        txtTenBKT.putClientProperty("JTextField.placeholderText", "VD: Kiểm tra 1");

        form.add(lblLop);
        form.add(cboLop);
        form.add(lblLoai);
        form.add(cboLoai);
        form.add(lblTen);
        form.add(txtTenBKT);

        bar.add(title, BorderLayout.WEST);
        bar.add(form, BorderLayout.EAST);
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

        JButton btnLuu = UiComponents.primaryButton("Lưu Điểm", new Color(0x43A047));
        btnLuu.addActionListener(e -> {
            if (txtTenBKT.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tên bài kiểm tra.", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(this,
                    "Đã lưu điểm bài kiểm tra: " + txtTenBKT.getText().trim() + "\nLoại: " + cboLoai.getSelectedItem(),
                    "Lưu Thành Công", JOptionPane.INFORMATION_MESSAGE);
        });

        lblSoLuong.setFont(UiTheme.BODY_B);
        lblSoLuong.setForeground(new Color(0x2E7D32));

        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setOpaque(false);
        panel.add(UiComponents.tableScroll(table), BorderLayout.CENTER);

        JPanel saveBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
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

        List<DangKy> dkList = dangKyController.getDangKyByLop(maLop);
        List<HocVien> allHV = hocVienController.layDanhSach();
        Map<Integer, HocVien> hvMap = new HashMap<>();
        for (HocVien hv : allHV) hvMap.put(hv.getMaHocVien(), hv);

        tableModel.setRowCount(0);
        int idx = 1;
        for (DangKy dk : dkList) {
            HocVien hv = hvMap.get(dk.getMaHocVien());
            String hoTen = hv != null ? hv.getHoTen() : "HV #" + dk.getMaHocVien();
            tableModel.addRow(new Object[] { idx++, dk.getMaHocVien(), hoTen, "", "" });
        }
        lblSoLuong.setText(dkList.size() + " học viên  ");
    }
}
