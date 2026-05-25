package com.trungtam.ui.hocvien;

import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Tra cứu khóa học/lớp học — dạng lưới thẻ (card grid).
 */
public class TraCuuKhoaHocPanel extends JPanel {

    private static final Color[] SUBJECT_COLORS = {
            new Color(0x1565C0), new Color(0x2E7D32), new Color(0x6A1B9A),
            new Color(0xBF360C), new Color(0x00838F), new Color(0xAD1457)
    };

    // {tên lớp, môn, giảng viên, buổi/tuần, cấp độ, màu index}
    private static final Object[][] KHOA_HOC = {
            { "Toán 12A", "Toán học", "Thầy Nguyễn Văn An", 3, "Nâng cao", 0 },
            { "Toán 11B", "Toán học", "Thầy Nguyễn Văn An", 2, "Trung cấp", 0 },
            { "Vật Lý 11B", "Vật lý", "Cô Trần Thị Bình", 2, "Trung cấp", 1 },
            { "Vật Lý 12A", "Vật lý", "Cô Trần Thị Bình", 3, "Nâng cao", 1 },
            { "Hóa Học 10C", "Hóa học", "Thầy Lê Hoàng Cường", 2, "Cơ bản", 2 },
            { "Hóa Học 11A", "Hóa học", "Thầy Lê Hoàng Cường", 2, "Trung cấp", 2 },
            { "Ngữ Văn 12A", "Ngữ văn", "Cô Phạm Thị Dung", 2, "Nâng cao", 3 },
            { "Tiếng Anh 11B", "Tiếng Anh", "Cô Phạm Thị Dung", 3, "Trung cấp", 4 },
            { "Tiếng Anh 12A", "Tiếng Anh", "Cô Phạm Thị Dung", 3, "Nâng cao", 4 },
    };

    private final JPanel cardGrid = new JPanel(new GridLayout(0, 3, 14, 14));
    private final JTextField txtSearch = new JTextField(18);
    private final JComboBox<String> cboMon = new JComboBox<>(
            new String[] { "Tất cả môn", "Toán học", "Vật lý", "Hóa học", "Ngữ văn", "Tiếng Anh" });
    private final JComboBox<String> cboCap = new JComboBox<>(
            new String[] { "Tất cả cấp độ", "Cơ bản", "Trung cấp", "Nâng cao" });

    public TraCuuKhoaHocPanel() {
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(UiTheme.PAD_L, UiTheme.PAD_L, UiTheme.PAD_L, UiTheme.PAD_L));
        setBackground(UiTheme.APP_BG);

        JLabel title = new JLabel("TRA CỨU & ĐĂNG KÝ LỚP HỌC");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.SECONDARY);
        add(title, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(buildCardArea(),
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        JPanel center = new JPanel(new BorderLayout(0, 8));
        center.setOpaque(false);
        center.add(buildFilterBar(), BorderLayout.NORTH);
        center.add(scroll, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        renderCards(KHOA_HOC);
    }

    private JPanel buildFilterBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        bar.setBackground(UiTheme.CARD_BG);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.SECONDARY_FG),
                new EmptyBorder(6, 10, 6, 10)));

        txtSearch.putClientProperty("JTextField.placeholderText", "Tìm lớp, giảng viên...");
        txtSearch.setFont(UiTheme.BODY);
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                applyFilter();
            }
        });

        cboMon.setFont(UiTheme.BODY);
        cboCap.setFont(UiTheme.BODY);
        cboMon.addActionListener(e -> applyFilter());
        cboCap.addActionListener(e -> applyFilter());

        JLabel lblSearch = new JLabel("Tìm kiếm:");
        lblSearch.setFont(UiTheme.BODY);
        JLabel lblMon = new JLabel("Môn:");
        lblMon.setFont(UiTheme.BODY);
        JLabel lblCap = new JLabel("Cấp độ:");
        lblCap.setFont(UiTheme.BODY);

        bar.add(lblSearch);
        bar.add(txtSearch);
        bar.add(lblMon);
        bar.add(cboMon);
        bar.add(lblCap);
        bar.add(cboCap);
        return bar;
    }

    private JPanel buildCardArea() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(4, 0, 4, 0));
        cardGrid.setOpaque(false);
        cardGrid.setBorder(new EmptyBorder(4, 4, 4, 4));
        wrapper.add(cardGrid, BorderLayout.NORTH);
        return wrapper;
    }

    private void applyFilter() {
        String kw = txtSearch.getText().trim().toLowerCase();
        String mon = (String) cboMon.getSelectedItem();
        String cap = (String) cboCap.getSelectedItem();

        List<Object[]> filtered = new ArrayList<>();
        for (Object[] row : KHOA_HOC) {
            boolean okMon = "Tất cả môn".equals(mon) || row[1].equals(mon);
            boolean okCap = "Tất cả cấp độ".equals(cap) || row[4].equals(cap);
            boolean okKw = kw.isEmpty()
                    || ((String) row[0]).toLowerCase().contains(kw)
                    || ((String) row[2]).toLowerCase().contains(kw);
            if (okMon && okCap && okKw)
                filtered.add(row);
        }
        renderCards(filtered.toArray(new Object[0][]));
    }

    private void renderCards(Object[][] data) {
        cardGrid.removeAll();
        for (Object[] row : data)
            cardGrid.add(buildCard(row));
        cardGrid.revalidate();
        cardGrid.repaint();
    }

    private JPanel buildCard(Object[] row) {
        String tenLop = (String) row[0];
        String mon = (String) row[1];
        String gv = (String) row[2];
        int buoi = (int) row[3];
        String capDo = (String) row[4];
        Color color = SUBJECT_COLORS[(int) row[5]];

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(UiTheme.CARD_BG);
        card.setBorder(BorderFactory.createLineBorder(UiTheme.CARD_BORDER, 1));

        // Coloured header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(color);
        header.setPreferredSize(new Dimension(0, 64));
        header.setBorder(new EmptyBorder(0, UiTheme.PAD_M, 0, UiTheme.PAD_M));
        JLabel lblMon = new JLabel(mon);
        lblMon.setFont(UiTheme.BODY_B);
        lblMon.setForeground(new Color(255, 255, 255, 200));
        header.add(lblMon, BorderLayout.CENTER);
        card.add(header, BorderLayout.NORTH);

        // Body
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(UiTheme.CARD_BG);
        body.setBorder(new EmptyBorder(12, UiTheme.PAD_M, 12, UiTheme.PAD_M));

        JLabel lblTen = new JLabel(tenLop);
        lblTen.setFont(UiTheme.TITLE_S);
        lblTen.setForeground(UiTheme.TEXT_PRIMARY);
        lblTen.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lblGV = new JLabel(gv);
        lblGV.setFont(UiTheme.BODY);
        lblGV.setForeground(UiTheme.TEXT_SECONDARY);
        lblGV.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lblInfo = new JLabel(buoi + " buổi/tuần");
        lblInfo.setFont(UiTheme.CAPTION);
        lblInfo.setForeground(UiTheme.TEXT_MUTED);
        lblInfo.setAlignmentX(LEFT_ALIGNMENT);

        body.add(lblTen);
        body.add(Box.createVerticalStrut(4));
        body.add(lblGV);
        body.add(Box.createVerticalStrut(4));
        body.add(lblInfo);
        body.add(Box.createVerticalStrut(10));

        // Badge + button
        JPanel footer = new JPanel(new BorderLayout(8, 0));
        footer.setOpaque(false);
        JLabel badge = new JLabel("  " + capDo + "  ");
        badge.setFont(UiTheme.CAPTION);
        badge.setOpaque(true);
        badge.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
        badge.setForeground(color);
        badge.setBorder(BorderFactory.createLineBorder(color, 1));

        JButton btnDK = UiComponents.primaryButton("Đăng ký", color);
        btnDK.setFont(UiTheme.BODY_B);
        btnDK.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Đã gửi yêu cầu đăng ký lớp: " + tenLop,
                "Đăng Ký Thành Công", JOptionPane.INFORMATION_MESSAGE));
        footer.add(badge, BorderLayout.WEST);
        footer.add(btnDK, BorderLayout.EAST);
        footer.setAlignmentX(LEFT_ALIGNMENT);
        body.add(footer);

        card.add(body, BorderLayout.CENTER);

        // Hover border
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createLineBorder(color, 1));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createLineBorder(UiTheme.CARD_BORDER, 1));
            }
        });
        return card;
    }
}
