package com.trungtam.ui.quanly;

import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class QuanLyLopHocPanel extends JPanel {

    private static final Color[] CARD_COLORS = {
            new Color(0x1A237E), new Color(0x1B5E20), new Color(0x6A1B9A),
            new Color(0xE65100), new Color(0x00695C)
    };

    public QuanLyLopHocPanel() {
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M));
        setBackground(UiTheme.APP_BG);

        add(buildTopBar(), BorderLayout.NORTH);
        add(buildCardGrid(), BorderLayout.CENTER);
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout(12, 0));
        bar.setOpaque(false);

        JLabel title = new JLabel("TRA CỨU LỚP HỌC");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.QUANLY);

        JButton addBtn = UiComponents.ghostButton("Thêm lớp học");

        JPanel filterRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 4));
        filterRow.setOpaque(false);

        filterRow.add(new JLabel("Tìm kiếm:"));
        JTextField searchField = new JTextField(18);
        searchField.putClientProperty("JTextField.placeholderText", "Tìm lớp học...");
        searchField.setFont(UiTheme.BODY);
        filterRow.add(searchField);

        filterRow.add(new JLabel("Môn:"));
        JComboBox<String> monCombo = new JComboBox<>(new String[]{"Tất cả môn", "Toán", "Văn", "Anh", "Lý", "Hóa"});
        monCombo.setFont(UiTheme.BODY);
        filterRow.add(monCombo);

        filterRow.add(new JLabel("Cấp độ:"));
        JComboBox<String> capDoCombo = new JComboBox<>(new String[]{"Tất cả cấp độ", "Cơ bản", "Nâng cao", "Trung cấp"});
        capDoCombo.setFont(UiTheme.BODY);
        filterRow.add(capDoCombo);

        JPanel top = new JPanel(new BorderLayout(0, 8));
        top.setOpaque(false);

        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setOpaque(false);
        titleRow.add(title, BorderLayout.WEST);
        titleRow.add(addBtn, BorderLayout.EAST);

        top.add(titleRow, BorderLayout.NORTH);
        top.add(filterRow, BorderLayout.SOUTH);
        return top;
    }

    private JScrollPane buildCardGrid() {
        Object[][] lopData = {
                {"Lớp Toán 12A1", "GV: GV 1", "3 buổi/tuần", "Sĩ số: 0/20", "01/06/2026 - 01/09/2026", "Trung cấp"},
                {"Lớp Văn 12B1", "GV: GV 2", "2 buổi/tuần", "Sĩ số: 2/15", "01/06/2026 - 01/09/2026", "Trung cấp"},
                {"Lớp Anh 12C1", "GV: GV 3", "3 buổi/tuần", "Sĩ số: 1/12", "01/06/2026 - 01/09/2026", "Trung cấp"},
                {"Lớp Lý 12D1", "GV: GV 4", "3 buổi/tuần", "Sĩ số: 1/22", "01/06/2026 - 01/09/2026", "Trung cấp"},
                {"Lớp Hóa 12E1", "GV: GV 1", "2 buổi/tuần", "Sĩ số: 0/10", "01/06/2026 - 01/09/2026", "Trung cấp"},
        };

        JPanel grid = new JPanel(new GridLayout(0, 3, 16, 16));
        grid.setOpaque(false);

        for (int i = 0; i < lopData.length; i++) {
            grid.add(createClassCard(i + 1, lopData[i], CARD_COLORS[i % CARD_COLORS.length]));
        }

        // Fill empty cells for grid alignment
        int remainder = lopData.length % 3;
        if (remainder > 0) {
            for (int i = 0; i < 3 - remainder; i++) {
                JPanel empty = new JPanel();
                empty.setOpaque(false);
                grid.add(empty);
            }
        }

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(grid, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(wrapper,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        return scroll;
    }

    private JPanel createClassCard(int index, Object[] data, Color headerColor) {
        JPanel card = new JPanel(new BorderLayout(0, 0));
        card.setBackground(UiTheme.CARD_BG);
        card.setBorder(BorderFactory.createLineBorder(UiTheme.CARD_BORDER, 1));

        // Colored header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(headerColor);
        header.setBorder(new EmptyBorder(14, 16, 14, 16));
        JLabel headerLabel = new JLabel("Lớp học " + index);
        headerLabel.setFont(UiTheme.BODY_B);
        headerLabel.setForeground(Color.WHITE);
        header.add(headerLabel, BorderLayout.WEST);

        // Body
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(UiTheme.CARD_BG);
        body.setBorder(new EmptyBorder(12, 16, 8, 16));

        JLabel nameLabel = new JLabel((String) data[0]);
        nameLabel.setFont(UiTheme.TITLE_S);
        nameLabel.setForeground(UiTheme.TEXT_PRIMARY);

        JLabel gvLabel = new JLabel((String) data[1]);
        gvLabel.setFont(UiTheme.CAPTION);
        gvLabel.setForeground(UiTheme.TEXT_MUTED);

        JLabel infoLabel = new JLabel((String) data[2] + "  •  " + (String) data[3]);
        infoLabel.setFont(UiTheme.CAPTION);
        infoLabel.setForeground(UiTheme.TEXT_SECONDARY);

        JLabel dateLabel = new JLabel((String) data[4]);
        dateLabel.setFont(UiTheme.CAPTION);
        dateLabel.setForeground(UiTheme.TEXT_MUTED);

        body.add(nameLabel);
        body.add(Box.createVerticalStrut(4));
        body.add(gvLabel);
        body.add(Box.createVerticalStrut(4));
        body.add(infoLabel);
        body.add(Box.createVerticalStrut(4));
        body.add(dateLabel);

        // Footer with buttons
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 8));
        footer.setBackground(UiTheme.CARD_BG);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UiTheme.CARD_BORDER));

        JLabel capDoLabel = new JLabel((String) data[5]);
        capDoLabel.setFont(UiTheme.CAPTION);
        capDoLabel.setForeground(headerColor);
        capDoLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(headerColor, 1),
                new EmptyBorder(2, 8, 2, 8)));

        footer.add(capDoLabel);
        footer.add(Box.createHorizontalStrut(16));
        footer.add(UiComponents.ghostButton("Chi tiết"));
        footer.add(UiComponents.ghostButton("Sửa"));

        JButton delBtn = UiComponents.ghostButton("Xóa");
        delBtn.setForeground(UiTheme.DANGER);
        footer.add(delBtn);

        card.add(header, BorderLayout.NORTH);
        card.add(body, BorderLayout.CENTER);
        card.add(footer, BorderLayout.SOUTH);
        return card;
    }
}
