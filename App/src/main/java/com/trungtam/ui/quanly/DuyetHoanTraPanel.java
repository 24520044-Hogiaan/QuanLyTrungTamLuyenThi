package com.trungtam.ui.quanly;

import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DuyetHoanTraPanel extends JPanel {

    private final DefaultTableModel tableModel;

    private static final String[] COT = {
            "Mã HT", "Học Viên", "Mã Hóa Đơn", "Số Tiền", "Lý Do", "Ngày YC", "Trạng Thái"
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

        add(UiComponents.tableScroll(table), BorderLayout.CENTER);
        loadSampleData();
    }

    private JPanel buildTopBar() {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setOpaque(false);

        JLabel title = new JLabel("Duyệt Yêu Cầu Hoàn Trả");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.TEXT_PRIMARY);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.setOpaque(false);

        JButton approveBtn = UiComponents.primaryButton("Phê duyệt", UiTheme.SUCCESS);
        approveBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Chức năng đang phát triển."));
        buttons.add(approveBtn);

        JButton rejectBtn = UiComponents.ghostButton("Từ chối");
        rejectBtn.setForeground(UiTheme.DANGER);
        rejectBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Chức năng đang phát triển."));
        buttons.add(rejectBtn);

        panel.add(title, BorderLayout.WEST);
        panel.add(buttons, BorderLayout.EAST);
        return panel;
    }

    private void loadSampleData() {
        tableModel.addRow(new Object[]{1, "Nguyễn An", 1, "100,000 VND",
                "Hoàn trả tiền thừa quỹ lớp", "25/05/2026", "Chờ duyệt"});
    }
}
