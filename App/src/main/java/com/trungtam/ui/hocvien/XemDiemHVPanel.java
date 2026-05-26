package com.trungtam.ui.hocvien;

import com.trungtam.controller.DangKyController;
import com.trungtam.controller.LopHocController;
import com.trungtam.model.DangKy;
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
 * Xem điểm học tập cá nhân — dữ liệu từ database (lớp đã đăng ký).
 */
public class XemDiemHVPanel extends JPanel {

    private final int maHocVien;
    private final DangKyController dangKyController = new DangKyController();
    private final LopHocController lopHocController = new LopHocController();

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
                new String[] { "Bài Kiểm Tra", "Ngày Thi", "Điểm", "Hệ Số", "Nhận Xét Giảng Viên" }, 0) {
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

        JLabel lblNote = new JLabel("  (Dữ liệu điểm được tải từ hệ thống)");
        lblNote.setFont(UiTheme.CAPTION_I);
        lblNote.setForeground(UiTheme.TEXT_MUTED);

        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        filterBar.setOpaque(false);
        JLabel lblLopLabel = new JLabel("Lớp học:");
        lblLopLabel.setFont(UiTheme.BODY);
        filterBar.add(lblLopLabel);
        filterBar.add(cboLop);
        filterBar.add(lblNote);

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
        // Currently no DIEM table query - show empty
        // When DIEM DAO is implemented, load from DB here
        lblSoBai.setText("0 bài");
        lblTB.setText("-");
        lblTong.setText("-");
        lblXepLoai.setText("Chưa có dữ liệu");
    }
}
