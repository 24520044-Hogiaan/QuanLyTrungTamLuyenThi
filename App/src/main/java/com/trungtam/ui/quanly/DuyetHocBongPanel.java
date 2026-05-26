package com.trungtam.ui.quanly;

import com.trungtam.controller.HocBongController;
import com.trungtam.model.HocBong;
import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DuyetHocBongPanel extends JPanel {

    private final DefaultTableModel tableModel;
    private final HocBongController hocBongController = new HocBongController();

    private static final String[] COT = { "Mã HB", "Mã Học Viên", "Giá Trị", "Ngày Cấp", "Trạng Thái" };

    public DuyetHocBongPanel() {
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
        add(UiComponents.tableScroll(table), BorderLayout.CENTER);
        loadData();
    }

    private JPanel buildTopBar() {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setOpaque(false);
        JLabel title = new JLabel("Duyệt Học Bổng");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.TEXT_PRIMARY);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.setOpaque(false);
        buttons.add(UiComponents.primaryButton("Xét duyệt", UiTheme.SUCCESS));
        JButton refreshBtn = UiComponents.ghostButton("Làm mới");
        refreshBtn.addActionListener(e -> loadData());
        buttons.add(refreshBtn);
        panel.add(title, BorderLayout.WEST);
        panel.add(buttons, BorderLayout.EAST);
        return panel;
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<HocBong> list = hocBongController.layDanhSach();
        for (HocBong hb : list) {
            tableModel.addRow(new Object[]{
                    hb.getMaHocBong(), hb.getMaHocVien(),
                    String.format("%,.0f VNĐ", hb.getGiaTri()),
                    hb.getNgayCap(), hb.getTrangThaiHB()
            });
        }
    }
}
