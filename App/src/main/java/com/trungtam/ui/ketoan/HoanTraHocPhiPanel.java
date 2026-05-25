package com.trungtam.ui.ketoan;

import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class HoanTraHocPhiPanel extends JPanel {

    private final DefaultTableModel tableModel;

    private static final String[] COT = {
            "Mã HT", "Học Viên", "Mã Hóa Đơn", "Số Tiền", "Lý Do", "Phương Thức", "Trạng Thái"
    };

    public HoanTraHocPhiPanel() {
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

        JLabel title = new JLabel("Hoàn Trả Học Phí");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.TEXT_PRIMARY);

        JLabel note = new JLabel("Chỉ hiển thị yêu cầu đã được phê duyệt bởi nhân viên quản lý");
        note.setFont(UiTheme.CAPTION_I);
        note.setForeground(UiTheme.TEXT_MUTED);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.setOpaque(false);

        JButton processBtn = UiComponents.primaryButton("Xử lý hoàn trả", UiTheme.KETOAN);
        processBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Chức năng hoàn trả — đang phát triển."));
        buttons.add(processBtn);

        JButton printBtn = UiComponents.ghostButton("In phiếu hoàn trả");
        printBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Chức năng in phiếu — đang phát triển."));
        buttons.add(printBtn);

        JPanel rightPanel = new JPanel(new BorderLayout(0, 6));
        rightPanel.setOpaque(false);
        rightPanel.add(note, BorderLayout.NORTH);
        rightPanel.add(buttons, BorderLayout.SOUTH);

        panel.add(title, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.EAST);
        return panel;
    }

    private void loadSampleData() {
        tableModel.addRow(new Object[]{1, "Nguyễn An", 1, "100,000 VND",
                "Hoàn trả tiền thừa quỹ lớp", "Tiền mặt", "Chấp thuận"});
    }
}
