package com.trungtam.ui.hocvien;

import com.trungtam.controller.DangKyController;
import com.trungtam.controller.LopHocController;
import com.trungtam.dao.TaiLieuDAO;
import com.trungtam.model.*;
import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class TaiLieuPanel extends JPanel {

    private final int maHocVien;
    private final DangKyController dangKyController = new DangKyController();
    private final LopHocController lopHocController = new LopHocController();
    private final TaiLieuDAO taiLieuDAO = new TaiLieuDAO();

    private final DefaultTableModel tableModel;
    private final JComboBox<String> cboLop = new JComboBox<>();
    private final Map<String, Integer> tenLopToMa = new HashMap<>();

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
                new String[] { "Mã TL", "Tên Tài Liệu", "Liên Kết" }, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(tableModel);
        UiComponents.styleTable(table);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row >= 0) {
                        String link = (String) tableModel.getValueAt(table.convertRowIndexToModel(row), 2);
                        if (link != null && !link.isEmpty()) {
                            try {
                                java.awt.Desktop.getDesktop().browse(new java.net.URI(link));
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(TaiLieuPanel.this,
                                        "Không thể mở link: " + link, "Lỗi", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            }
        });

        cboLop.setFont(UiTheme.BODY);
        cboLop.addItem("Tất cả lớp");
        cboLop.addActionListener(e -> loadTaiLieu());

        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        filterBar.setOpaque(false);
        JLabel lblMon = new JLabel("Lọc theo lớp:");
        lblMon.setFont(UiTheme.BODY);
        filterBar.add(lblMon);
        filterBar.add(cboLop);

        JLabel lblHint = new JLabel("  (Nhấp đôi vào dòng để mở tài liệu)");
        lblHint.setFont(UiTheme.CAPTION_I);
        lblHint.setForeground(UiTheme.TEXT_MUTED);
        filterBar.add(lblHint);
        
        filterBar.add(Box.createHorizontalStrut(20));
        JButton btnRefresh = UiComponents.ghostButton("Làm mới");
        btnRefresh.addActionListener(e -> loadLopData());
        filterBar.add(btnRefresh);

        JPanel center = new JPanel(new BorderLayout(0, 8));
        center.setOpaque(false);
        center.add(filterBar, BorderLayout.NORTH);
        center.add(UiComponents.tableScroll(table), BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        loadLopData();
    }

    private void loadLopData() {
        tenLopToMa.clear();

        List<DangKy> myDK = dangKyController.getDangKyByHocVien(maHocVien);
        List<LopHoc> allLop = lopHocController.layDanhSach();
        Map<Integer, LopHoc> lopMap = new HashMap<>();
        for (LopHoc lop : allLop) lopMap.put(lop.getMaLopHoc(), lop);

        for (DangKy dk : myDK) {
            if (!"Dang hoc".equalsIgnoreCase(dk.getTrangThaiDKY())) continue;
            LopHoc lop = lopMap.get(dk.getMaLopHoc());
            if (lop != null) {
                cboLop.addItem(lop.getTenLop());
                tenLopToMa.put(lop.getTenLop(), lop.getMaLopHoc());
            }
        }

        loadTaiLieu();
    }

    private void loadTaiLieu() {
        tableModel.setRowCount(0);
        String selectedLop = (String) cboLop.getSelectedItem();

        try {
            if ("Tất cả lớp".equals(selectedLop)) {
                for (Map.Entry<String, Integer> entry : tenLopToMa.entrySet()) {
                    List<TaiLieu> list = taiLieuDAO.findByLop(entry.getValue());
                    for (TaiLieu tl : list) {
                        tableModel.addRow(new Object[]{
                                tl.getMaTaiLieu(), tl.getTenTaiLieu(), tl.getLink()
                        });
                    }
                }
            } else if (tenLopToMa.containsKey(selectedLop)) {
                List<TaiLieu> list = taiLieuDAO.findByLop(tenLopToMa.get(selectedLop));
                for (TaiLieu tl : list) {
                    tableModel.addRow(new Object[]{
                            tl.getMaTaiLieu(), tl.getTenTaiLieu(), tl.getLink()
                    });
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (tableModel.getRowCount() == 0 && tenLopToMa.isEmpty()) {
            tableModel.addRow(new Object[]{ "", "Bạn chưa đăng ký lớp nào.", "" });
        }
    }
}
