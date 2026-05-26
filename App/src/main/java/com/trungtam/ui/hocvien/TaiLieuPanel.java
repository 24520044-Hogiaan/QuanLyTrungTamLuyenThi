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
 * Xem tài liệu học tập — dữ liệu từ database (hiển thị lớp đã đăng ký).
 */
public class TaiLieuPanel extends JPanel {

    private final int maHocVien;
    private final DangKyController dangKyController = new DangKyController();
    private final LopHocController lopHocController = new LopHocController();

    private final DefaultTableModel tableModel;

    public TaiLieuPanel(int maHocVien) {
        this.maHocVien = maHocVien;
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(UiTheme.PAD_L, UiTheme.PAD_L, UiTheme.PAD_L, UiTheme.PAD_L));
        setBackground(UiTheme.APP_BG);

        JLabel title = new JLabel("XEM TÀI LIỆU HỌC TẬP");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.SECONDARY);
        add(title, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new String[] { "Lớp Học", "Tên Tài Liệu", "Giảng Viên", "Ngày Tải", "Loại", "Kích Thước" },
                0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        UiComponents.styleTable(table);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        // Filter bar
        List<String> lopNames = getRegisteredClasses();
        JComboBox<String> cboMon = new JComboBox<>();
        cboMon.addItem("Tất cả lớp");
        for (String name : lopNames) cboMon.addItem(name);
        cboMon.setFont(UiTheme.BODY);

        JPanel filterBar = new JPanel(new BorderLayout(12, 0));
        filterBar.setOpaque(false);
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        left.setOpaque(false);
        JLabel lblMon = new JLabel("Lọc theo lớp:");
        lblMon.setFont(UiTheme.BODY);
        left.add(lblMon);
        left.add(cboMon);
        filterBar.add(left, BorderLayout.WEST);

        JPanel center = new JPanel(new BorderLayout(0, 8));
        center.setOpaque(false);
        center.add(filterBar, BorderLayout.NORTH);
        center.add(UiComponents.tableScroll(table), BorderLayout.CENTER);

        // Info message
        if (lopNames.isEmpty()) {
            JLabel lbl = new JLabel("Bạn chưa đăng ký lớp nào. Tài liệu sẽ hiển thị khi bạn đăng ký lớp.");
            lbl.setFont(UiTheme.BODY);
            lbl.setForeground(UiTheme.TEXT_MUTED);
            lbl.setBorder(new EmptyBorder(20, 10, 20, 10));
            center.add(lbl, BorderLayout.SOUTH);
        } else {
            JLabel lbl = new JLabel("Tài liệu sẽ được giảng viên tải lên khi có bài giảng mới.");
            lbl.setFont(UiTheme.CAPTION_I);
            lbl.setForeground(UiTheme.TEXT_MUTED);
            lbl.setBorder(new EmptyBorder(8, 4, 0, 0));
            center.add(lbl, BorderLayout.SOUTH);
        }

        add(center, BorderLayout.CENTER);
    }

    private List<String> getRegisteredClasses() {
        List<String> names = new ArrayList<>();
        List<DangKy> myDK = dangKyController.getDangKyByHocVien(maHocVien);
        List<LopHoc> allLop = lopHocController.layDanhSach();
        Map<Integer, LopHoc> lopMap = new HashMap<>();
        for (LopHoc lop : allLop) lopMap.put(lop.getMaLopHoc(), lop);

        for (DangKy dk : myDK) {
            LopHoc lop = lopMap.get(dk.getMaLopHoc());
            if (lop != null) names.add(lop.getTenLop());
        }
        return names;
    }
}
