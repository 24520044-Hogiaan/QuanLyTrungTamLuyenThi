package com.trungtam.ui.quanly;

import com.trungtam.controller.HoanTraController;
import com.trungtam.model.HoanTra;
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

public class DuyetHoanTraPanel extends JPanel {

    private final DefaultTableModel tableModel;
    private final TableRowSorter<DefaultTableModel> rowSorter;
    private final HoanTraController hoanTraController = new HoanTraController();

    private static final String[] COT = {
            "Mã Hoàn Trả", "Mã Hóa Đơn", "Mã Học Viên", "Số Tiền", "Lý Do",
            "Ngày Yêu Cầu", "Trạng Thái", "Mã Nhân Viên", "Ngày Hoàn Trả"
    };

    public DuyetHoanTraPanel() {
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
        add(UiComponents.tableScroll(table), BorderLayout.CENTER);
        add(buildBottomBar(), BorderLayout.SOUTH);
        loadData();
    }

    private JPanel buildTopBar() {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setOpaque(false);
        JLabel title = new JLabel("Yêu Cầu Hoàn Tiền");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.TEXT_PRIMARY);
        JTextField searchField = new JTextField(18);
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
        bar.add(UiComponents.primaryButton("Duyệt", UiTheme.SUCCESS));
        bar.add(UiComponents.primaryButton("Từ chối", UiTheme.DANGER));
        JButton refreshBtn = UiComponents.ghostButton("Làm Mới");
        refreshBtn.addActionListener(e -> loadData());
        bar.add(refreshBtn);
        return bar;
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<HoanTra> list = hoanTraController.getListHoanTra();
        for (HoanTra ht : list) {
            tableModel.addRow(new Object[]{
                    ht.getMaHoanTra(), ht.getMaHoaDon(), ht.getMaHocVien(),
                    String.format("%,.0f VNĐ", ht.getSoTien()), ht.getLyDo(),
                    ht.getNgayYeuCau(), ht.getTrangThai(), ht.getMaNhanVien(), ht.getNgayHoanTra()
            });
        }
    }
}
