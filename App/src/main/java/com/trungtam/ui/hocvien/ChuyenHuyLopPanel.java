package com.trungtam.ui.hocvien;

import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * Chuyển lớp / Hủy lớp — 2 tab riêng biệt.
 */
public class ChuyenHuyLopPanel extends JPanel {

    private static final String[] LOP_DA_DK = { "Toán 12A", "Tiếng Anh 11B" };
    private static final String[] LOP_MO = {
            "Vật Lý 11B", "Hóa Học 10C", "Ngữ Văn 12A", "Tiếng Anh 12A", "Toán 11B", "Hóa Học 11A"
    };

    public ChuyenHuyLopPanel() {
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(UiTheme.PAD_L, UiTheme.PAD_L, UiTheme.PAD_L, UiTheme.PAD_L));
        setBackground(UiTheme.APP_BG);

        JLabel title = new JLabel("CHUYỂN / HỦY LỚP HỌC");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.SECONDARY);
        add(title, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(UiTheme.BODY_B);
        tabs.addTab("  Chuyển Lớp  ", buildChuyenLopTab());
        tabs.addTab("  Hủy Lớp  ", buildHuyLopTab());
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel buildChuyenLopTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBorder(new EmptyBorder(UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M));
        panel.setBackground(UiTheme.APP_BG);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(8, 4, 8, 8);

        JComboBox<String> cboHien = new JComboBox<>(LOP_DA_DK);
        JComboBox<String> cboMoi = new JComboBox<>(LOP_MO);
        JTextArea txtLyDo = new JTextArea(3, 0);
        txtLyDo.setLineWrap(true);
        txtLyDo.setWrapStyleWord(true);
        txtLyDo.setFont(UiTheme.BODY);
        cboHien.setFont(UiTheme.BODY);
        cboMoi.setFont(UiTheme.BODY);

        addFormRow(form, g, 0, "Lớp hiện tại:", cboHien);
        addFormRow(form, g, 1, "Lớp muốn chuyển sang:", cboMoi);
        g.gridx = 0;
        g.gridy = 2;
        g.weightx = 0;
        g.anchor = GridBagConstraints.NORTHWEST;
        JLabel lblLyDo = new JLabel("Lý do chuyển:");
        lblLyDo.setFont(UiTheme.BODY);
        form.add(lblLyDo, g);
        g.gridx = 1;
        g.weightx = 1;
        g.anchor = GridBagConstraints.CENTER;
        form.add(new JScrollPane(txtLyDo), g);

        panel.add(infoLabel("Lưu ý: Chỉ được chuyển lớp trong 7 ngày đầu sau khi đăng ký."),
                BorderLayout.NORTH);
        panel.add(form, BorderLayout.CENTER);

        JButton btn = UiComponents.primaryButton("Gửi Yêu Cầu Chuyển Lớp", UiTheme.WARNING);
        btn.addActionListener(e -> {
            if (cboHien.getSelectedItem().equals(cboMoi.getSelectedItem())) {
                JOptionPane.showMessageDialog(panel, "Lớp cũ và lớp mới không được trùng nhau.", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(panel,
                    "Đã gửi yêu cầu chuyển từ [" + cboHien.getSelectedItem() + "] sang ["
                            + cboMoi.getSelectedItem() + "].",
                    "Thành Công", JOptionPane.INFORMATION_MESSAGE);
        });
        panel.add(buildBtnBar(btn), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildHuyLopTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBorder(new EmptyBorder(UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M));
        panel.setBackground(UiTheme.APP_BG);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(8, 4, 8, 8);

        JComboBox<String> cboLop = new JComboBox<>(LOP_DA_DK);
        JTextArea txtLyDo = new JTextArea(3, 0);
        txtLyDo.setLineWrap(true);
        txtLyDo.setWrapStyleWord(true);
        txtLyDo.setFont(UiTheme.BODY);
        cboLop.setFont(UiTheme.BODY);

        addFormRow(form, g, 0, "Lớp muốn hủy:", cboLop);
        g.gridx = 0;
        g.gridy = 1;
        g.weightx = 0;
        g.anchor = GridBagConstraints.NORTHWEST;
        JLabel lblLyDo = new JLabel("Lý do hủy:");
        lblLyDo.setFont(UiTheme.BODY);
        form.add(lblLyDo, g);
        g.gridx = 1;
        g.weightx = 1;
        g.anchor = GridBagConstraints.CENTER;
        form.add(new JScrollPane(txtLyDo), g);

        JLabel warning = new JLabel("<html><b>Lưu ý chính sách hoàn phí:</b><br>"
                + "- Hủy trong 7 ngày đầu: hoàn 100% học phí<br>"
                + "- Sau 7 ngày đến 1 tháng: hoàn 50%<br>"
                + "- Sau 1 tháng: không hoàn phí</html>");
        warning.setFont(UiTheme.BODY);
        warning.setForeground(UiTheme.DANGER);
        warning.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xEF9A9A)),
                new EmptyBorder(10, 12, 10, 12)));
        warning.setOpaque(true);
        warning.setBackground(new Color(0xFFEBEE));

        panel.add(warning, BorderLayout.NORTH);
        panel.add(form, BorderLayout.CENTER);

        JButton btn = UiComponents.primaryButton("Xác Nhận Hủy Lớp", UiTheme.DANGER);
        btn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(panel,
                    "Bạn chắc chắn muốn hủy lớp: " + cboLop.getSelectedItem() + "?",
                    "Xác Nhận Hủy Lớp", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION)
                JOptionPane.showMessageDialog(panel,
                        "Đã gửi yêu cầu hủy lớp. Hệ thống sẽ xử lý trong 24h.",
                        "Thành Công", JOptionPane.INFORMATION_MESSAGE);
        });
        panel.add(buildBtnBar(btn), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildBtnBar(JButton btn) {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 6));
        bar.setOpaque(false);
        bar.add(btn);
        return bar;
    }

    private void addFormRow(JPanel form, GridBagConstraints g, int row, String label, JComponent field) {
        g.gridx = 0;
        g.gridy = row;
        g.weightx = 0.35;
        g.anchor = GridBagConstraints.WEST;
        JLabel lbl = new JLabel(label);
        lbl.setFont(UiTheme.BODY);
        form.add(lbl, g);
        g.gridx = 1;
        g.weightx = 0.65;
        g.anchor = GridBagConstraints.CENTER;
        form.add(field, g);
    }

    private JLabel infoLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UiTheme.CAPTION_I);
        l.setForeground(UiTheme.TEXT_MUTED);
        return l;
    }
}
