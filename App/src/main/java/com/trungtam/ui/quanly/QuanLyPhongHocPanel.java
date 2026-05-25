package com.trungtam.ui.quanly;

import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class QuanLyPhongHocPanel extends JPanel {

    private final DefaultTableModel tableModel;

    private static final String[] COT = {
            "Mã Phòng", "Tên Phòng", "Sức Chứa", "Trạng Thái"
    };

    public QuanLyPhongHocPanel() {
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M));
        setBackground(UiTheme.APP_BG);

        JLabel title = new JLabel("Quản Lý Phòng Học");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.TEXT_PRIMARY);
        add(title, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(COT, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        UiComponents.styleTable(table);
        UiComponents.setColumnAlignments(table,
                SwingConstants.CENTER, SwingConstants.LEFT, SwingConstants.CENTER, SwingConstants.CENTER);

        add(UiComponents.tableScroll(table), BorderLayout.CENTER);
        loadSampleData();
    }

    private void loadSampleData() {
        tableModel.addRow(new Object[]{201, "Phòng Toán 101", 40, "Sẵn sàng"});
        tableModel.addRow(new Object[]{202, "Phòng Văn 102", 40, "Sẵn sàng"});
        tableModel.addRow(new Object[]{203, "Phòng Anh 103", 40, "Sẵn sàng"});
    }
}
