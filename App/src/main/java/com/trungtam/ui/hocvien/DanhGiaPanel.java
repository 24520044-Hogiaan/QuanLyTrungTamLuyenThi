package com.trungtam.ui.hocvien;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * Đánh giá chất lượng — học viên đánh giá khóa học và cơ sở vật chất theo từng mục.
 */
public class DanhGiaPanel extends JPanel {

    private static final String[] LOP_CUA_HV = {"Toán 12A", "Tiếng Anh 11B"};
    private final int[] starKhoaHoc    = {0};
    private final int[] starGiangVien  = {0};
    private final int[] starCoSo       = {0};
    private final int[] starDichVu     = {0};

    public DanhGiaPanel() {
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("ĐÁNH GIÁ & PHẢN HỒI");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(0x1B5E20));
        add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xC8E6C9), 1),
            new EmptyBorder(20, 20, 20, 20)));

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL; g.insets = new Insets(10, 6, 10, 8);
        g.weightx = 1;

        // Chọn lớp
        addSection(form, g, 0, "THÔNG TIN ĐÁNH GIÁ");
        JComboBox<String> cboLop = new JComboBox<>(LOP_CUA_HV);
        cboLop.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        addRow(form, g, 1, "Khóa học cần đánh giá:", cboLop);

        // Đánh giá khóa học
        addSection(form, g, 2, "CHẤT LƯỢNG GIẢNG DẠY");

        JPanel pnlKH = buildRadioStars(starKhoaHoc);
        addRow(form, g, 3, "Nội dung bài giảng:", pnlKH);

        JPanel pnlGV = buildRadioStars(starGiangVien);
        addRow(form, g, 4, "Phong cách giảng viên:", pnlGV);

        JTextArea txtNhanXetKH = new JTextArea(4, 0);
        txtNhanXetKH.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtNhanXetKH.setLineWrap(true); txtNhanXetKH.setWrapStyleWord(true);
        txtNhanXetKH.putClientProperty("JTextArea.placeholderText", "Nhận xét về nội dung và chất lượng giảng dạy...");
        addRow(form, g, 5, "Nhận xét:", new JScrollPane(txtNhanXetKH));

        // Đánh giá cơ sở vật chất
        addSection(form, g, 6, "CƠ SỞ VẬT CHẤT");

        JPanel pnlCS = buildRadioStars(starCoSo);
        addRow(form, g, 7, "Phòng học, bàn ghế, máy chiếu:", pnlCS);

        JPanel pnlDV = buildRadioStars(starDichVu);
        addRow(form, g, 8, "Dịch vụ hỗ trợ học viên:", pnlDV);

        JTextArea txtNhanXetCS = new JTextArea(4, 0);
        txtNhanXetCS.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtNhanXetCS.setLineWrap(true); txtNhanXetCS.setWrapStyleWord(true);
        txtNhanXetCS.putClientProperty("JTextArea.placeholderText", "Nhận xét về cơ sở vật chất và dịch vụ hỗ trợ...");
        addRow(form, g, 9, "Nhận xét:", new JScrollPane(txtNhanXetCS));

        // Nút gửi
        JButton btnGui = new JButton("Gửi Đánh Giá");
        btnGui.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnGui.setBackground(new Color(0x1B5E20));
        btnGui.setForeground(Color.WHITE);
        btnGui.setFocusPainted(false); btnGui.setBorderPainted(false);
        btnGui.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnGui.addActionListener(e -> {
            if (starKhoaHoc[0] == 0 || starGiangVien[0] == 0) {
                JOptionPane.showMessageDialog(this,
                    "Vui lòng chấm sao cho ít nhất mục Chất Lượng Giảng Dạy.", "Chưa đánh giá", JOptionPane.WARNING_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(this,
                "Cảm ơn bạn đã gửi đánh giá!\nPhản hồi của bạn giúp chúng tôi cải thiện chất lượng.",
                "Gửi Thành Công", JOptionPane.INFORMATION_MESSAGE);
        });

        g.gridx = 0; g.gridy = 10; g.gridwidth = 2; g.anchor = GridBagConstraints.EAST; g.fill = GridBagConstraints.NONE;
        g.weightx = 0;
        form.add(btnGui, g);

        add(new JScrollPane(form,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
    }

    /** Tạo nhóm Radio Button để đánh giá sao (1-5) */
    private JPanel buildRadioStars(int[] storage) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        p.setOpaque(false);
        ButtonGroup grp = new ButtonGroup();
        for (int i=1; i<=5; i++) {
            JRadioButton rb = new JRadioButton(i + " Sao");
            rb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            rb.setFocusPainted(false);
            int val = i;
            rb.addActionListener(e -> storage[0] = val);
            grp.add(rb);
            p.add(rb);
        }
        return p;
    }

    private void addSection(JPanel form, GridBagConstraints g, int row, String sectionTitle) {
        g.gridx = 0; g.gridy = row; g.gridwidth = 2; g.weightx = 1; g.fill = GridBagConstraints.HORIZONTAL;
        JLabel lbl = new JLabel(sectionTitle);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(new Color(0x1B5E20));
        lbl.setBorder(new MatteBorder(0, 0, 1, 0, new Color(0xC8E6C9)));
        form.add(lbl, g);
        g.gridwidth = 1;
    }

    private void addRow(JPanel form, GridBagConstraints g, int row, String label, JComponent field) {
        g.gridx = 0; g.gridy = row; g.weightx = 0; g.fill = GridBagConstraints.HORIZONTAL;
        JLabel lbl = new JLabel(label); lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        form.add(lbl, g);
        g.gridx = 1; g.weightx = 1;
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        form.add(field, g);
    }
}
