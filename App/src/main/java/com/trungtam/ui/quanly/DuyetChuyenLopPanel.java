package com.trungtam.ui.quanly;

import com.trungtam.controller.YeuCauChuyenLopController;
import com.trungtam.model.YeuCauChuyenLop;
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

public class DuyetChuyenLopPanel extends JPanel {

    private final DefaultTableModel tableModel;
    private final TableRowSorter<DefaultTableModel> rowSorter;
    private final YeuCauChuyenLopController ycController = new YeuCauChuyenLopController();
    private JTable table;

    private static final String[] COT = {
            "Mã YC", "Mã HV", "Lớp Cũ", "Lớp Mới", "Lý Do",
            "Ngày YC", "Trạng Thái", "Mã NV", "Ngày Xử Lý", "Ghi Chú"
    };

    public DuyetChuyenLopPanel() {
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M));
        setBackground(UiTheme.APP_BG);
        add(buildTopBar(), BorderLayout.NORTH);
        tableModel = new DefaultTableModel(COT, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        UiComponents.styleTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        rowSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(rowSorter);
        add(UiComponents.tableScroll(table), BorderLayout.CENTER);
        add(buildBottomBar(), BorderLayout.SOUTH);
        loadData();
    }

    private JPanel buildTopBar() {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setOpaque(false);

        JLabel title = new JLabel("Yêu Cầu Chuyển Lớp");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.TEXT_PRIMARY);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightPanel.setOpaque(false);

        JLabel lblTrangThai = new JLabel("Trạng thái:");
        lblTrangThai.setFont(UiTheme.BODY);
        JComboBox<String> cboTrangThai = new JComboBox<>(new String[]{"Tất cả", "Cho duyet", "Chap thuan", "Tu choi"});
        cboTrangThai.setFont(UiTheme.BODY);
        cboTrangThai.addActionListener(e -> {
            String selected = (String) cboTrangThai.getSelectedItem();
            if ("Tất cả".equals(selected)) {
                rowSorter.setRowFilter(null);
            } else {
                rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + selected, 6));
            }
        });

        JTextField searchField = new JTextField(18);
        searchField.putClientProperty("JTextField.placeholderText", "Tìm kiếm...");
        searchField.setFont(UiTheme.BODY);
        searchField.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) {
                String kw = searchField.getText().trim();
                rowSorter.setRowFilter(kw.isEmpty() ? null : RowFilter.regexFilter("(?i)" + kw));
            }
        });

        rightPanel.add(lblTrangThai);
        rightPanel.add(cboTrangThai);
        rightPanel.add(searchField);

        panel.add(title, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.EAST);
        return panel;
    }

    private JPanel buildBottomBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 6));
        bar.setOpaque(false);
        
        JButton btnDuyet = UiComponents.primaryButton("Duyệt", UiTheme.SUCCESS);
        btnDuyet.addActionListener(e -> xuLyYeuCau("Chap thuan"));
        
        JButton btnTuChoi = UiComponents.primaryButton("Từ chối", UiTheme.DANGER);
        btnTuChoi.addActionListener(e -> xuLyYeuCau("Tu choi"));
        
        bar.add(btnDuyet);
        bar.add(btnTuChoi);
        
        JButton refreshBtn = UiComponents.ghostButton("Làm Mới");
        refreshBtn.addActionListener(e -> loadData());
        bar.add(refreshBtn);
        return bar;
    }

    private void xuLyYeuCau(String trangThaiMoi) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một yêu cầu để xử lý!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = table.convertRowIndexToModel(row);
        int maYC = (int) tableModel.getValueAt(modelRow, 0);
        String currentStatus = (String) tableModel.getValueAt(modelRow, 6);

        if (!"Cho duyet".equalsIgnoreCase(currentStatus)) {
            JOptionPane.showMessageDialog(this, "Chỉ có thể xử lý yêu cầu đang 'Cho duyet'!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String input = JOptionPane.showInputDialog(this, "Nhập Mã Nhân Viên của bạn để xác nhận:", "Xác nhận", JOptionPane.QUESTION_MESSAGE);
        if (input == null || input.trim().isEmpty()) return;

        try {
            int maNV = Integer.parseInt(input.trim());
            if (ycController.updateTrangThai(maYC, trangThaiMoi, maNV)) {
                JOptionPane.showMessageDialog(this, "Xử lý thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Xử lý thất bại! Vui lòng kiểm tra lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Mã Nhân Viên không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<YeuCauChuyenLop> list = ycController.getListYeuCau();
        for (YeuCauChuyenLop yc : list) {
            tableModel.addRow(new Object[]{
                    yc.getMaYeuCau(), yc.getMaHocVien(), yc.getMaLopCu(), yc.getMaLopMoi(),
                    yc.getLyDo(), yc.getNgayYeuCau(), yc.getTrangThai(),
                    yc.getMaNhanVien(), yc.getNgayXuLy(), yc.getGhiChu()
            });
        }
    }
}
