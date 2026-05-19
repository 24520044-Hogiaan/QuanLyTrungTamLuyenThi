package com.trungtam.ui.hocvien;

import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * Thanh toán học phí: bên trái = thông tin CK + mã QR, bên phải = danh sách cần
 * đóng.
 */
public class ThanhToanHocPhiPanel extends JPanel {

    private static final Object[][] HP_DATA = {
            { "Toán 12A", "Toán học", 3_500_000, "30/09/2025", "Đã đóng" },
            { "Tiếng Anh 11B", "Tiếng Anh", 2_800_000, "30/09/2025", "Chưa đóng" },
    };

    public ThanhToanHocPhiPanel() {
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(UiTheme.PAD_L, UiTheme.PAD_L, UiTheme.PAD_L, UiTheme.PAD_L));
        setBackground(UiTheme.APP_BG);

        JLabel title = new JLabel("THANH TOÁN HỌC PHÍ");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.SECONDARY);
        add(title, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                buildBankInfoPanel(), buildPaymentListPanel());
        split.setDividerLocation(340);
        split.setDividerSize(6);
        split.setBorder(null);
        add(split, BorderLayout.CENTER);
    }

    private JPanel buildBankInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 0, 16));

        JPanel qrPanel = buildQRPanel();
        JLabel lblQR = new JLabel("Quét mã QR để thanh toán", SwingConstants.CENTER);
        lblQR.setFont(UiTheme.BODY_B);
        lblQR.setForeground(UiTheme.SECONDARY);

        JPanel qrWrapper = new JPanel(new BorderLayout(0, 8));
        qrWrapper.setOpaque(false);
        qrWrapper.add(qrPanel, BorderLayout.CENTER);
        qrWrapper.add(lblQR, BorderLayout.SOUTH);
        qrWrapper.setMaximumSize(new Dimension(240, 280));
        qrWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel bankCard = new JPanel(new GridBagLayout());
        bankCard.setBackground(UiTheme.CARD_BG);
        bankCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.SECONDARY_FG, 1),
                new EmptyBorder(12, 12, 12, 12)));
        bankCard.setMaximumSize(new Dimension(320, 280));
        bankCard.setAlignmentX(Component.CENTER_ALIGNMENT);

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(6, 4, 6, 4);

        addInfoRow(bankCard, g, 0, "Ngân hàng:", "Vietcombank (VCB)");
        addInfoRow(bankCard, g, 1, "Số tài khoản:", "1234 5678 9012 3456");
        addInfoRow(bankCard, g, 2, "Chủ tài khoản:", "TRUNG TÂM ĐÀO TẠO ABC");
        addInfoRow(bankCard, g, 3, "Chi nhánh:", "TP. Hồ Chí Minh");

        g.gridx = 0;
        g.gridy = 4;
        g.gridwidth = 2;
        g.weightx = 1;
        bankCard.add(new JSeparator(), g);

        g.gridy = 5;
        JLabel lblCK = new JLabel("Cú pháp chuyển khoản:");
        lblCK.setFont(UiTheme.BODY_B);
        lblCK.setForeground(UiTheme.SECONDARY);
        bankCard.add(lblCK, g);

        g.gridy = 6;
        JLabel lblMa = new JLabel(
                "<html><code style='font-size:13px'><b>HVTX_[MaHV]_[TenLop]</b></code></html>");
        lblMa.setOpaque(true);
        lblMa.setBackground(new Color(0xF1F8E9));
        lblMa.setBorder(new EmptyBorder(10, 10, 10, 10));
        bankCard.add(lblMa, g);

        panel.add(qrWrapper);
        panel.add(Box.createVerticalStrut(16));
        panel.add(bankCard);
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private JPanel buildQRPanel() {
        JPanel qr = new JPanel() {
            @Override
            protected void paintComponent(Graphics g0) {
                super.paintComponent(g0);
                Graphics2D g2 = (Graphics2D) g0;
                int size = Math.min(getWidth(), getHeight()) - 20;
                int xOffset = (getWidth() - size) / 2;
                int yOffset = (getHeight() - size) / 2;
                int cellSize = size / 20;
                if (cellSize < 1)
                    return;

                g2.setColor(Color.WHITE);
                g2.fillRect(0, 0, getWidth(), getHeight());

                java.util.Random rand = new java.util.Random(42L);
                g2.setColor(UiTheme.PRIMARY);
                for (int row = 0; row < 20; row++) {
                    for (int col = 0; col < 20; col++) {
                        boolean isCorner = (row < 7 && col < 7) || (row < 7 && col >= 13)
                                || (row >= 13 && col < 7);
                        boolean fill = isCorner
                                ? (row == 0 || row == 6 || col == 0 || col == 6
                                        || (row >= 2 && row <= 4 && col >= 2 && col <= 4))
                                : rand.nextBoolean();
                        if (fill)
                            g2.fillRect(xOffset + col * cellSize, yOffset + row * cellSize, cellSize, cellSize);
                    }
                }
            }
        };
        qr.setPreferredSize(new Dimension(180, 180));
        qr.setMinimumSize(new Dimension(180, 180));
        qr.setBackground(Color.WHITE);
        qr.setBorder(BorderFactory.createLineBorder(UiTheme.SECONDARY, 2));
        return qr;
    }

    private JPanel buildPaymentListPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 8, 0, 0));

        JLabel lbl = UiComponents.divider("CÁC KHÓA HỌC CỦA BẠN", UiTheme.SECONDARY);
        lbl.setFont(UiTheme.TITLE_S);
        panel.add(lbl, BorderLayout.NORTH);

        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBackground(UiTheme.APP_BG);

        for (Object[] row : HP_DATA) {
            list.add(buildCoursePaymentCard(row));
            list.add(Box.createVerticalStrut(10));
        }

        panel.add(UiComponents.scrollPane(list), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildCoursePaymentCard(Object[] row) {
        String tenLop = (String) row[0];
        String mon = (String) row[1];
        int hocPhi = (int) row[2];
        String hanDong = (String) row[3];
        String trangThai = (String) row[4];
        boolean daDong = "Đã đóng".equals(trangThai);

        JPanel card = new JPanel(new BorderLayout(0, 6));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(daDong ? UiTheme.SECONDARY_RAIL : new Color(0xFFCC80), 1),
                new EmptyBorder(12, 14, 12, 14)));
        card.setBackground(daDong ? new Color(0xF1F8E9) : new Color(0xFFF8E1));

        JLabel lblTen = new JLabel(tenLop + "  •  " + mon);
        lblTen.setFont(UiTheme.BODY_B);
        lblTen.setForeground(UiTheme.TEXT_PRIMARY);

        JLabel lblGia = new JLabel(String.format("%,d đ  |  Hạn: %s", hocPhi, hanDong));
        lblGia.setFont(UiTheme.CAPTION);
        lblGia.setForeground(UiTheme.TEXT_MUTED);

        JPanel info = new JPanel(new GridLayout(2, 1, 0, 2));
        info.setOpaque(false);
        info.add(lblTen);
        info.add(lblGia);
        card.add(info, BorderLayout.CENTER);

        if (daDong) {
            JLabel lblDone = new JLabel("Đã thanh toán");
            lblDone.setFont(UiTheme.BODY_B);
            lblDone.setForeground(UiTheme.SUCCESS);
            card.add(lblDone, BorderLayout.EAST);
        } else {
            JButton btnPay = UiComponents.primaryButton("Xác Nhận Đã CK", UiTheme.WARNING);
            btnPay.addActionListener(e -> JOptionPane.showMessageDialog(this,
                    "Đã ghi nhận xác nhận chuyển khoản cho lớp: " + tenLop
                            + "\nVui lòng đợi kiểm tra từ phía trung tâm (1-2 ngày làm việc).",
                    "Xác Nhận Thành Công", JOptionPane.INFORMATION_MESSAGE));
            card.add(btnPay, BorderLayout.EAST);
        }
        return card;
    }

    private void addInfoRow(JPanel panel, GridBagConstraints g, int row, String label, String value) {
        g.gridx = 0;
        g.gridy = row;
        g.weightx = 0;
        g.gridwidth = 1;
        JLabel lbl = new JLabel(label);
        lbl.setFont(UiTheme.CAPTION);
        lbl.setForeground(UiTheme.TEXT_MUTED);
        panel.add(lbl, g);
        g.gridx = 1;
        g.weightx = 1;
        JLabel val = new JLabel(value);
        val.setFont(UiTheme.BODY_B);
        val.setForeground(UiTheme.TEXT_PRIMARY);
        panel.add(val, g);
    }
}
