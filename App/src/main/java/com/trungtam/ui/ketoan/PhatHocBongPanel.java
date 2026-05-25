package com.trungtam.ui.ketoan;

import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PhatHocBongPanel extends JPanel {

    private final DefaultTableModel tableModel;

    private static final String[] COT = {
            "Mã HB", "Học Viên", "Khóa Học", "Điểm TB", "Xếp Hạng", "Giá Trị HB", "Trạng Thái"
    };

    public PhatHocBongPanel() {
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
        loadSampleData();
    }

    private JPanel buildTopBar() {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setOpaque(false);

        JLabel title = new JLabel("Phát Học Bổng");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.TEXT_PRIMARY);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.setOpaque(false);

        JButton payBtn = UiComponents.primaryButton("Chi trả", UiTheme.KETOAN);
        payBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Chức năng chi trả học bổng — đang phát triển."));
        buttons.add(payBtn);

        JButton printBtn = UiComponents.ghostButton("In phiếu chi");
        printBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Chức năng in phiếu chi — đang phát triển."));
        buttons.add(printBtn);

        panel.add(title, BorderLayout.WEST);
        panel.add(buttons, BorderLayout.EAST);
        return panel;
    }

    private void loadSampleData() {
        tableModel.addRow(new Object[]{1, "Nguyễn An", "Toán Đại Số Lớp 10", 8.5, 1, "500,000 VND", "Đã nhận"});
    }
}
