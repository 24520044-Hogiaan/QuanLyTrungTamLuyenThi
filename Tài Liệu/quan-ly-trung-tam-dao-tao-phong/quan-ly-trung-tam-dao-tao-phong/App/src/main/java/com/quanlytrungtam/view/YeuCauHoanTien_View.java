package com.quanlytrungtam.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import com.quanlytrungtam.controller.HoanTienController;
import com.quanlytrungtam.model.HoanTienModel;
import com.quanlytrungtam.ui.UiComponents;
import com.quanlytrungtam.ui.UiTheme;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class YeuCauHoanTien_View extends JPanel implements Refreshable {

    private final DefaultTableModel tableModel;
    private final JTable table;
    private final TableRowSorter<DefaultTableModel> rowSorter;

    private List<HoanTienModel> list = new ArrayList<>();
    private JTextField searchField;
    private JComboBox<String> cboTrangThai;

    private final HoanTienController controller = new HoanTienController();
    private final DecimalFormat moneyFormat = new DecimalFormat("#,### VNĐ");

    private static final String[] COLUMNS = {
            "Mã Hoàn Trả", "Mã Hóa Đơn", "Mã Học Viên", "Số Tiền",
            "Lý Do", "Ngày Yêu Cầu", "Trạng Thái",
            "Mã Nhân Viên", "Ngày Hoàn Trả"
    };

    public YeuCauHoanTien_View() {
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M));
        setBackground(UiTheme.APP_BG);

        add(buildTopBar(), BorderLayout.NORTH);

        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(tableModel);
        UiComponents.styleTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        rowSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(rowSorter);

        add(UiComponents.tableScroll(table), BorderLayout.CENTER);
        add(buildBottomBar(), BorderLayout.SOUTH);

        refreshTable();
    }

    private JPanel buildTopBar() {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setOpaque(false);

        JLabel title = new JLabel("Yêu Cầu Hoàn Tiền");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.TEXT_PRIMARY);

        searchField = new JTextField(18);
        searchField.putClientProperty("JTextField.placeholderText", "Tìm kiếm...");
        searchField.setFont(UiTheme.BODY);

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                applyFilters();
            }
        });

        cboTrangThai = new JComboBox<>(new String[] {
                "Tất cả", "Chap thuan", "Cho duyet", "Da hoan", "Tu choi"
        });
        cboTrangThai.setFont(UiTheme.BODY);
        cboTrangThai.addActionListener(e -> applyFilters());

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightPanel.setOpaque(false);
        rightPanel.add(new JLabel("Trạng thái:"));
        rightPanel.add(cboTrangThai);
        rightPanel.add(searchField);

        panel.add(title, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }

    private void applyFilters() {
        List<RowFilter<DefaultTableModel, Object>> filters = new ArrayList<>();

        String kw = searchField.getText().trim();
        if (!kw.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i)" + kw, 0));
        }

        String selectedStatus = (String) cboTrangThai.getSelectedItem();
        if (selectedStatus != null && !selectedStatus.equals("Tất cả")) {
            filters.add(RowFilter.regexFilter("(?i)^" + selectedStatus + "$", 6));
        }

        if (filters.isEmpty()) {
            rowSorter.setRowFilter(null);
        } else {
            rowSorter.setRowFilter(RowFilter.andFilter(filters));
        }
    }

    private JPanel buildBottomBar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panel.setOpaque(false);

        JButton btnDuyet = UiComponents.primaryButton("Duyệt", UiTheme.SUCCESS);
        JButton btnTuChoi = UiComponents.primaryButton("Từ chối", UiTheme.DANGER);
        JButton btnLamMoi = UiComponents.primaryButton("Làm Mới", UiTheme.PRIMARY);

        btnDuyet.addActionListener(e -> processRequest("Da hoan"));
        btnTuChoi.addActionListener(e -> processRequest("Tu choi"));
        btnLamMoi.addActionListener(e -> refreshAll());

        panel.add(btnDuyet);
        panel.add(btnTuChoi);
        panel.add(btnLamMoi);

        return panel;
    }

    private void processRequest(String status) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một yêu cầu!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = table.convertRowIndexToModel(row);

        int maHT = (Integer) tableModel.getValueAt(modelRow, 0);
        String trangThaiHienTai = (String) tableModel.getValueAt(modelRow, 6);
        if (trangThaiHienTai != null
                && !trangThaiHienTai.trim().equalsIgnoreCase("Cho duyet")) {
            JOptionPane.showMessageDialog(this,
                    "Yêu cầu đã được xử lý!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        HoanTienModel ht = null;
        for (HoanTienModel item : list) {
            if (item.getMaHoanTra() == maHT) {
                ht = item;
                break;
            }
        }

        if (ht == null) {
            JOptionPane.showMessageDialog(this,
                    "Không tìm thấy yêu cầu!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        ht.setTrangThai(status);
        ht.setMaNhanVien(1);
        ht.setNgayHoanTra(new Date());

        boolean success = controller.updateProcess(ht);

        if (success) {
            refreshTable();
            JOptionPane.showMessageDialog(this,
                    "Cập nhật trạng thái thành công!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Cập nhật thất bại!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void refresh() {
        if (searchField != null) {
            searchField.setText("");
        }
        if (cboTrangThai != null) {
            cboTrangThai.setSelectedIndex(0);
        }
        rowSorter.setRowFilter(null);
        refreshTable();
    }

    private void refreshAll() {
        refresh();

        JOptionPane.showMessageDialog(this,
                "Làm mới thành công!",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        list = controller.getListYeuCau();

        for (HoanTienModel ht : list) {
            tableModel.addRow(new Object[] {
                    ht.getMaHoanTra(),
                    ht.getMaHoaDon(),
                    ht.getMaHocVien(),
                    moneyFormat.format(ht.getSoTien()),
                    ht.getLyDo(),
                    ht.getNgayYeuCau(),
                    ht.getTrangThai(),
                    ht.getMaNhanVien(),
                    ht.getNgayHoanTra()
            });
        }
    }
}
