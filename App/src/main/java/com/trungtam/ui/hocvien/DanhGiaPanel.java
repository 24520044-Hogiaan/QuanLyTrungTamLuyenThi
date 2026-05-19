package com.trungtam.ui.hocvien;

import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * Đánh giá chất lượng — học viên đánh giá khóa học và cơ sở vật chất.
 */
public class DanhGiaPanel extends JPanel {

    private static final String[] LOP_CUA_HV = { "Toán 12A", "Tiếng Anh 11B" };
    private final int[] starKhoaHoc = { 0 };
    private final int[] starGiangVien = { 0 };
    private final int[] starCoSo = { 0 };
    private final int[] starDichVu = { 0 };

    public DanhGiaPanel() {
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(UiTheme.PAD_L, UiTheme.PAD_L, UiTheme.PAD_L, UiTheme.PAD_L));
        setBackground(UiTheme.APP_BG);

        JLabel title = new JLabel("ĐÁNH GIÁ & PHẢN HỒI");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.SECONDARY);
        add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UiTheme.CARD_BG);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.SECONDARY_FG, 1),
                new EmptyBorder(UiTheme.PAD_L, UiTheme.PAD_L, UiTheme.PAD_L, UiTheme.PAD_L)));

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(10, 6, 10, 8);
        g.weightx = 1;

        addSection(form, g, 0, "THÔNG TIN ĐÁNH GIÁ");
        JComboBox<String> cboLop = new JComboBox<>(LOP_CUA_HV);
        cboLop.setFont(UiTheme.BODY);
        addRow(form, g, 1, "Khóa học cần đánh giá:", cboLop);

        addSection(form, g, 2, "CHẤT LƯỢNG GIẢNG DẠY");
        addRow(form, g, 3, "Nội dung bài giảng:", buildRadioStars(starKhoaHoc));
        addRow(form, g, 4, "Phong cách giảng viên:", buildRadioStars(starGiangVien));

        JTextArea txtNhanXetKH = new JTextArea(4, 0);
        txtNhanXetKH.setFont(UiTheme.BODY);
        txtNhanXetKH.setLineWrap(true);
        txtNhanXetKH.setWrapStyleWord(true);
        txtNhanXetKH.putClientProperty("JTextArea.placeholderText",
                "Nhận xét về nội dung và chất lượng giảng dạy...");
        JScrollPane spKH = new JScrollPane(txtNhanXetKH);
        spKH.getVerticalScrollBar().setUnitIncrement(16);
        addRow(form, g, 5, "Nhận xét:", spKH);

        addSection(form, g, 6, "CƠ SỞ VẬT CHẤT");
        addRow(form, g, 7, "Phòng học, bàn ghế, máy chiếu:", buildRadioStars(starCoSo));
        addRow(form, g, 8, "Dịch vụ hỗ trợ học viên:", buildRadioStars(starDichVu));

        JTextArea txtNhanXetCS = new JTextArea(4, 0);
        txtNhanXetCS.setFont(UiTheme.BODY);
        txtNhanXetCS.setLineWrap(true);
        txtNhanXetCS.setWrapStyleWord(true);
        txtNhanXetCS.putClientProperty("JTextArea.placeholderText",
                "Nhận xét về cơ sở vật chất và dịch vụ hỗ trợ...");
        JScrollPane spCS = new JScrollPane(txtNhanXetCS);
        spCS.getVerticalScrollBar().setUnitIncrement(16);
        addRow(form, g, 9, "Nhận xét:", spCS);

        JButton btnGui = UiComponents.primaryButton("Gửi Đánh Giá", UiTheme.SECONDARY);
        btnGui.addActionListener(e -> {
            if (starKhoaHoc[0] == 0 || starGiangVien[0] == 0) {
                JOptionPane.showMessageDialog(this,
                        "Vui lòng chấm sao cho ít nhất mục Chất Lượng Giảng Dạy.",
                        "Chưa đánh giá", JOptionPane.WARNING_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(this,
                    "Cảm ơn bạn đã gửi đánh giá!\nPhản hồi của bạn giúp chúng tôi cải thiện chất lượng.",
                    "Gửi Thành Công", JOptionPane.INFORMATION_MESSAGE);
        });

        g.gridx = 0;
        g.gridy = 10;
        g.gridwidth = 2;
        g.anchor = GridBagConstraints.EAST;
        g.fill = GridBagConstraints.NONE;
        g.weightx = 0;
        form.add(btnGui, g);

        add(UiComponents.scrollPane(form), BorderLayout.CENTER);
    }

    private JPanel buildRadioStars(int[] storage) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        p.setOpaque(false);
        ButtonGroup grp = new ButtonGroup();
        for (int i = 1; i <= 5; i++) {
            JRadioButton rb = new JRadioButton(i + " Sao");
            rb.setFont(UiTheme.BODY);
            rb.setFocusPainted(false);
            rb.setOpaque(false);
            int val = i;
            rb.addActionListener(e -> storage[0] = val);
            grp.add(rb);
            p.add(rb);
        }
        return p;
    }

    private void addSection(JPanel form, GridBagConstraints g, int row, String sectionTitle) {
        g.gridx = 0;
        g.gridy = row;
        g.gridwidth = 2;
        g.weightx = 1;
        g.fill = GridBagConstraints.HORIZONTAL;
        JLabel lbl = UiComponents.divider(sectionTitle, UiTheme.SECONDARY);
        form.add(lbl, g);
        g.gridwidth = 1;
    }

    private void addRow(JPanel form, GridBagConstraints g, int row, String label, JComponent field) {
        g.gridx = 0;
        g.gridy = row;
        g.weightx = 0.35;
        g.fill = GridBagConstraints.HORIZONTAL;
        JLabel lbl = new JLabel(label);
        lbl.setFont(UiTheme.BODY);
        lbl.setForeground(UiTheme.TEXT_SECONDARY);
        form.add(lbl, g);
        g.gridx = 1;
        g.weightx = 0.65;
        field.setFont(UiTheme.BODY);
        form.add(field, g);
    }
}
