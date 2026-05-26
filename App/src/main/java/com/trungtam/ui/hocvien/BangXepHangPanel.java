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
 * Bảng xếp hạng học viên trong lớp — dữ liệu từ database.
 */
public class BangXepHangPanel extends JPanel {

    private final int maHocVien;
    private final DangKyController dangKyController = new DangKyController();
    private final LopHocController lopHocController = new LopHocController();

    public BangXepHangPanel(int maHocVien) {
        this.maHocVien = maHocVien;
        setLayout(new BorderLayout(0, 14));
        setBorder(new EmptyBorder(UiTheme.PAD_L, UiTheme.PAD_L, UiTheme.PAD_L, UiTheme.PAD_L));
        setBackground(UiTheme.APP_BG);

        JLabel title = new JLabel("BẢNG XẾP HẠNG LỚP HỌC");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.SECONDARY);
        add(title, BorderLayout.NORTH);

        add(buildContent(), BorderLayout.CENTER);
    }

    private JPanel buildContent() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setOpaque(false);

        // Load registered classes
        List<DangKy> myDK = dangKyController.getDangKyByHocVien(maHocVien);
        List<LopHoc> allLop = lopHocController.layDanhSach();
        Map<Integer, LopHoc> lopMap = new HashMap<>();
        for (LopHoc lop : allLop) lopMap.put(lop.getMaLopHoc(), lop);

        List<String> lopNames = new ArrayList<>();
        for (DangKy dk : myDK) {
            LopHoc lop = lopMap.get(dk.getMaLopHoc());
            if (lop != null) lopNames.add(lop.getTenLop());
        }

        if (lopNames.isEmpty()) {
            JLabel lbl = new JLabel("Bạn chưa đăng ký lớp nào. Hãy đăng ký lớp để xem bảng xếp hạng.");
            lbl.setFont(UiTheme.BODY);
            lbl.setForeground(UiTheme.TEXT_MUTED);
            lbl.setBorder(new EmptyBorder(20, 10, 20, 10));
            panel.add(lbl, BorderLayout.CENTER);
            return panel;
        }

        JComboBox<String> cboLop = new JComboBox<>(lopNames.toArray(new String[0]));
        cboLop.setFont(UiTheme.BODY);

        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        filterBar.setOpaque(false);
        JLabel lblLop = new JLabel("Chọn lớp:");
        lblLop.setFont(UiTheme.BODY);
        filterBar.add(lblLop);
        filterBar.add(cboLop);

        JLabel lblNote = new JLabel("  (Bảng xếp hạng sẽ hiển thị khi có dữ liệu điểm)");
        lblNote.setFont(UiTheme.CAPTION_I);
        lblNote.setForeground(UiTheme.TEXT_MUTED);
        filterBar.add(lblNote);

        panel.add(filterBar, BorderLayout.NORTH);

        // Summary cards
        JPanel card = new JPanel(new GridLayout(1, 3, 12, 0));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(8, 0, 8, 0));

        JLabel vRank = new JLabel("-");
        JLabel vTop = new JLabel("-");
        JLabel vDiem = new JLabel("-");
        card.add(UiComponents.statCard("Hạng của bạn", vRank, UiTheme.INFO));
        card.add(UiComponents.statCard("Top phần trăm", vTop, UiTheme.SUCCESS));
        card.add(UiComponents.statCard("Điểm TB của bạn", vDiem, new Color(0x6A1B9A)));

        // Table placeholder
        DefaultTableModel model = new DefaultTableModel(
                new String[] { "Hạng", "Học Viên", "Điểm TB", "Xếp Loại" }, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable table = new JTable(model);
        UiComponents.styleTable(table);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 8));
        centerPanel.setOpaque(false);
        centerPanel.add(card, BorderLayout.NORTH);
        centerPanel.add(UiComponents.tableScroll(table), BorderLayout.CENTER);

        panel.add(centerPanel, BorderLayout.CENTER);
        return panel;
    }
}
