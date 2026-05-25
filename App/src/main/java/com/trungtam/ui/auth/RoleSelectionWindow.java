package com.trungtam.ui.auth;

import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

/**
 * Cửa sổ chọn vai trò đăng nhập — thiết kế theo GUI Playbook.
 */
public class RoleSelectionWindow extends JFrame {
    public static final int ROLE_GIAOVIEN = 2;
    public static final int ROLE_HOCVIEN = 3;

    public RoleSelectionWindow(Consumer<Integer> onRoleSelected) {
        setTitle("Chọn vai trò đăng nhập");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(520, 380);
        setMinimumSize(new Dimension(420, 320));
        setLocationRelativeTo(null);
        setResizable(true);

        // Group header + cards + footer in one vertical block, centered in window
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);

        JLabel lblTitle = new JLabel("Chọn vai trò đăng nhập");
        lblTitle.setFont(UiTheme.TITLE_L);
        lblTitle.setForeground(UiTheme.TEXT_PRIMARY);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("Vui lòng chọn vai trò để tiếp tục vào hệ thống");
        lblSub.setFont(UiTheme.BODY);
        lblSub.setForeground(UiTheme.TEXT_MUTED);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        content.add(lblTitle);
        content.add(Box.createVerticalStrut(6));
        content.add(lblSub);
        content.add(Box.createVerticalStrut(24));

        JPanel cardPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        cardPanel.setOpaque(false);
        cardPanel.setPreferredSize(new Dimension(440, 180));
        cardPanel.setMaximumSize(new Dimension(520, 200));
        cardPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        cardPanel.add(buildRoleCard(
                "Giáo Viên",
                "Điểm danh, nhập điểm, lịch giảng dạy",
                UiTheme.PRIMARY,
                () -> onRoleSelected.accept(ROLE_GIAOVIEN)));

        cardPanel.add(buildRoleCard(
                "Học Viên",
                "Xem điểm, đăng ký lớp, thanh toán học phí",
                UiTheme.SECONDARY,
                () -> onRoleSelected.accept(ROLE_HOCVIEN)));

        content.add(cardPanel);
        content.add(Box.createVerticalStrut(16));

        JLabel footer = new JLabel("Trung Tâm Đào Tạo  •  Phiên bản 1.0");
        footer.setFont(UiTheme.CAPTION_I);
        footer.setForeground(UiTheme.TEXT_MUTED);
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(footer);

        // Center the whole block in the window
        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(UiTheme.APP_BG);
        root.setBorder(new EmptyBorder(24, 32, 24, 32));
        root.add(content);

        setContentPane(root);
    }

    /**
     * Blend a color with white at a given ratio to produce a fully opaque tint.
     * ratio=0.0 -> white, ratio=1.0 -> original color
     */
    private static Color tint(Color c, double ratio) {
        int r = (int) (255 + (c.getRed()   - 255) * ratio);
        int g = (int) (255 + (c.getGreen() - 255) * ratio);
        int b = (int) (255 + (c.getBlue()  - 255) * ratio);
        return new Color(r, g, b);
    }

    private JPanel buildRoleCard(String title, String desc, Color color, Runnable action) {
        final Color hoverBg = tint(color, 0.05);

        // Border luôn cố định 2px — chỉ đổi màu khi hover, không đổi kích thước
        final Border normalBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.CARD_BORDER, 2),
                new EmptyBorder(19, 17, 19, 17));
        final Border hoverBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2),
                new EmptyBorder(19, 17, 19, 17));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UiTheme.CARD_BG);
        card.setBorder(normalBorder);
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Icon circle
        JLabel iconCircle = new JLabel() {
            @Override
            protected void paintComponent(Graphics g0) {
                Graphics2D g = (Graphics2D) g0.create();
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setColor(tint(color, 0.15));
                g.fillOval(0, 0, 48, 48);
                g.setColor(color);
                g.setFont(new Font("Segoe UI", Font.BOLD, 20));
                FontMetrics fm = g.getFontMetrics();
                String initial = title.substring(0, 1);
                int tx = (48 - fm.stringWidth(initial)) / 2;
                int ty = (48 - fm.getHeight()) / 2 + fm.getAscent();
                g.drawString(initial, tx, ty);
                g.dispose();
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(48, 48);
            }

            @Override
            public Dimension getMaximumSize() {
                return new Dimension(48, 48);
            }

            @Override
            public Dimension getMinimumSize() {
                return new Dimension(48, 48);
            }
        };
        iconCircle.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(Box.createVerticalGlue());
        card.add(iconCircle);
        card.add(Box.createVerticalStrut(10));

        // Title
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(UiTheme.TITLE_S);
        lblTitle.setForeground(UiTheme.TEXT_PRIMARY);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblTitle);
        card.add(Box.createVerticalStrut(4));

        // Description
        JLabel lblDesc = new JLabel(
                "<html><center>" + desc + "</center></html>");
        lblDesc.setFont(UiTheme.CAPTION);
        lblDesc.setForeground(UiTheme.TEXT_MUTED);
        lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblDesc.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(lblDesc);
        card.add(Box.createVerticalGlue());

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(hoverBorder);
                card.setBackground(hoverBg);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(normalBorder);
                card.setBackground(UiTheme.CARD_BG);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                action.run();
            }
        });

        return card;
    }
}
