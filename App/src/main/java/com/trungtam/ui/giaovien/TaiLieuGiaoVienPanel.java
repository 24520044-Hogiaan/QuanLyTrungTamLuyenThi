package com.trungtam.ui.giaovien;

import com.trungtam.controller.LopHocController;
import com.trungtam.controller.TaiLieuController;
import com.trungtam.model.LopHoc;
import com.trungtam.model.TaiLieu;
import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaiLieuGiaoVienPanel extends JPanel {
    private final int maGiaoVien;
    private final LopHocController lopHocController = new LopHocController();
    private final TaiLieuController taiLieuController = new TaiLieuController();

    private final JComboBox<String> cboLop = new JComboBox<>();
    private final Map<String, Integer> lopNameToMa = new HashMap<>();
    
    private DefaultTableModel tableModel;
    private JTable table;

    public TaiLieuGiaoVienPanel(int maGiaoVien) {
        this.maGiaoVien = maGiaoVien;
        setLayout(new BorderLayout(0, 16));
        setBorder(new EmptyBorder(UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M));
        setBackground(UiTheme.APP_BG);

        add(buildTopBar(), BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);

        cboLop.addActionListener(e -> onLopSelected());
        
        loadLopData();
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);
        
        JLabel title = new JLabel("Quản Lý Tài Liệu Học Tập");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.TEXT_PRIMARY);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        right.setOpaque(false);
        
        JLabel lblLop = new JLabel("Lớp:");
        lblLop.setFont(UiTheme.BODY);
        cboLop.setFont(UiTheme.BODY);
        
        JButton btnAdd = UiComponents.primaryButton("Thêm Tài Liệu", UiTheme.PRIMARY);
        btnAdd.addActionListener(e -> themTaiLieuMoi());
        
        right.add(lblLop);
        right.add(cboLop);
        right.add(btnAdd);

        bar.add(title, BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    private JPanel buildContent() {
        String[] cols = {"Mã TL", "Tên Tài Liệu", "Đường Dẫn (Link)"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        UiComponents.styleTable(table);
        table.getColumnModel().getColumn(0).setMaxWidth(80);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.add(UiComponents.tableScroll(table), BorderLayout.CENTER);
        return panel;
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
        if (cboLop.getItemCount() > 0) {
            onLopSelected();
        }
    }

    private void onLopSelected() {
        String tenLop = (String) cboLop.getSelectedItem();
        if (tenLop == null) return;
        Integer maLop = lopNameToMa.get(tenLop);
        if (maLop == null) return;

        tableModel.setRowCount(0);
        List<TaiLieu> list = taiLieuController.findByLop(maLop);
        for (TaiLieu tl : list) {
            tableModel.addRow(new Object[]{
                    tl.getMaTaiLieu(),
                    tl.getTenTaiLieu(),
                    tl.getLink()
            });
        }
    }

    private void themTaiLieuMoi() {
        String tenLop = (String) cboLop.getSelectedItem();
        if (tenLop == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn lớp học trước!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Integer maLop = lopNameToMa.get(tenLop);
        if (maLop == null) return;

        JPanel pnl = new JPanel(new GridLayout(2, 2, 5, 5));
        JTextField txtTen = new JTextField();
        JTextField txtLink = new JTextField();
        
        pnl.add(new JLabel("Tên tài liệu:")); pnl.add(txtTen);
        pnl.add(new JLabel("Đường dẫn (Link):")); pnl.add(txtLink);

        int res = JOptionPane.showConfirmDialog(this, pnl, "Thêm tài liệu cho lớp " + tenLop, JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            String tenTL = txtTen.getText().trim();
            String link = txtLink.getText().trim();
            
            if (tenTL.isEmpty() || link.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin Tên tài liệu và Đường dẫn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            TaiLieu tl = new TaiLieu();
            tl.setMaLopHoc(maLop);
            tl.setTenTaiLieu(tenTL);
            tl.setLink(link);

            if (taiLieuController.insert(tl)) {
                JOptionPane.showMessageDialog(this, "Thêm tài liệu thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                onLopSelected();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi lưu tài liệu vào CSDL.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
