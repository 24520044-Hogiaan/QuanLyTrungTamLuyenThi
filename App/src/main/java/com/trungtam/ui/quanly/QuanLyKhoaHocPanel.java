package com.trungtam.ui.quanly;

import com.trungtam.controller.KhoaHocController;
import com.trungtam.model.KhoaHoc;
import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class QuanLyKhoaHocPanel extends JPanel {

    private final DefaultTableModel tableModel;
    private final TableRowSorter<DefaultTableModel> rowSorter;
    private final KhoaHocController khoaHocController = new KhoaHocController();

    private static final String[] COT = {
            "Mã KH", "Tên Khóa Học", "Mô Tả", "Hình thức học phí", "Học Phí", "Mã Bộ Môn", "Cấp Độ"
    };

    public QuanLyKhoaHocPanel() {
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M));
        setBackground(UiTheme.APP_BG);
        add(buildTopBar(), BorderLayout.NORTH);
        tableModel = new DefaultTableModel(COT, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        UiComponents.styleTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        rowSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(rowSorter);
        UiComponents.setColumnAlignments(table,
                SwingConstants.CENTER, SwingConstants.LEFT, SwingConstants.LEFT,
                SwingConstants.CENTER, SwingConstants.RIGHT, SwingConstants.CENTER,
                SwingConstants.CENTER);
        add(UiComponents.tableScroll(table), BorderLayout.CENTER);
        add(buildBottomBar(), BorderLayout.SOUTH);
        loadData();
    }

    private JPanel buildTopBar() {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setOpaque(false);
        JLabel title = new JLabel("Danh Sách Khóa Học");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.TEXT_PRIMARY);
        JTextField searchField = new JTextField(22);
        searchField.putClientProperty("JTextField.placeholderText", "Tìm kiếm...");
        searchField.setFont(UiTheme.BODY);
        searchField.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) {
                String kw = searchField.getText().trim();
                rowSorter.setRowFilter(kw.isEmpty() ? null : RowFilter.regexFilter("(?i)" + kw));
            }
        });
        panel.add(title, BorderLayout.WEST);
        panel.add(searchField, BorderLayout.EAST);
        return panel;
    }

    private JPanel buildBottomBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 6));
        bar.setOpaque(false);

        JButton btnThem = UiComponents.primaryButton("Thêm", UiTheme.SUCCESS);
        btnThem.addActionListener(e -> {
            com.trungtam.dao.BoMonDAO bmDao = new com.trungtam.dao.BoMonDAO();
            KhoaHocDialog dialog = new KhoaHocDialog(SwingUtilities.getWindowAncestor(this), null, bmDao.getListBoMon());
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                if (khoaHocController.themKhoaHoc(dialog.getKhoaHoc())) {
                    JOptionPane.showMessageDialog(this, "Thêm khóa học thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi thêm khóa học!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton btnSua = UiComponents.primaryButton("Sửa", UiTheme.PRIMARY);
        btnSua.addActionListener(e -> {
            JTable table = (JTable) ((JViewport) ((JScrollPane) getComponent(1)).getViewport()).getView();
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khóa học để sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int maKhoaHoc = (int) table.getValueAt(row, 0);
            KhoaHoc kh = null;
            for (KhoaHoc k : khoaHocController.layDanhSach()) {
                if (k.getMaKhoaHoc() == maKhoaHoc) { kh = k; break; }
            }
            if (kh != null) {
                com.trungtam.dao.BoMonDAO bmDao = new com.trungtam.dao.BoMonDAO();
                KhoaHocDialog dialog = new KhoaHocDialog(SwingUtilities.getWindowAncestor(this), kh, bmDao.getListBoMon());
                dialog.setVisible(true);
                if (dialog.isSaved()) {
                    if (khoaHocController.capNhatKhoaHoc(dialog.getKhoaHoc())) {
                        JOptionPane.showMessageDialog(this, "Cập nhật khóa học thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        loadData();
                    } else {
                        JOptionPane.showMessageDialog(this, "Lỗi cập nhật khóa học!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        JButton btnXoa = UiComponents.primaryButton("Xóa", UiTheme.DANGER);
        btnXoa.addActionListener(e -> {
            JTable table = (JTable) ((JViewport) ((JScrollPane) getComponent(1)).getViewport()).getView();
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khóa học để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int maKhoaHoc = (int) table.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa khóa học #" + maKhoaHoc + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (khoaHocController.xoaKhoaHoc(maKhoaHoc)) {
                    JOptionPane.showMessageDialog(this, "Xóa khóa học thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi xóa khóa học! Có thể khóa học đang được sử dụng ở nơi khác.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton refreshBtn = UiComponents.ghostButton("Làm Mới");
        refreshBtn.addActionListener(e -> loadData());

        bar.add(btnThem);
        bar.add(btnSua);
        bar.add(btnXoa);
        bar.add(refreshBtn);
        return bar;
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<KhoaHoc> list = khoaHocController.layDanhSach();
        for (KhoaHoc kh : list) {
            String hinhThuc = "Đóng theo tháng";
            double hocPhi = kh.getHocPhiThang();
            if (kh.getHocPhiThang() <= 0 && kh.getHocPhiToanKhoa() > 0) {
                hinhThuc = "Toàn khóa";
                hocPhi = kh.getHocPhiToanKhoa();
            }
            
            tableModel.addRow(new Object[]{
                    kh.getMaKhoaHoc(), kh.getTenKhoaHoc(), kh.getMoTa(),
                    hinhThuc,
                    String.format("%,.0f VNĐ", hocPhi),
                    kh.getMaBoMon(), kh.getCapDo()
            });
        }
    }
}
