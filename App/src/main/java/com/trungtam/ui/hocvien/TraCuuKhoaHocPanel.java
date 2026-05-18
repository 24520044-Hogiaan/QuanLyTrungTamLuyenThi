package com.trungtam.ui.hocvien;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Tra cứu khóa học/lớp học — dạng lưới thẻ (card grid) tương tự Coursera.
 * Mỗi thẻ: tiêu đề lớn, giảng viên, số buổi/tuần, cấp độ.
 */
public class TraCuuKhoaHocPanel extends JPanel {

    private static final Color[] SUBJECT_COLORS = {
        new Color(0x1565C0), new Color(0x2E7D32), new Color(0x6A1B9A),
        new Color(0xBF360C), new Color(0x00838F), new Color(0xAD1457)
    };

    // {tên lớp, môn, giảng viên, buổi/tuần, cấp độ, mã màu index}
    private static final Object[][] KHOA_HOC = {
        {"Toán 12A",      "Toán học",    "Thầy Nguyễn Văn An",   3, "Nâng cao",  0},
        {"Toán 11B",      "Toán học",    "Thầy Nguyễn Văn An",   2, "Trung cấp", 0},
        {"Vật Lý 11B",   "Vật lý",      "Cô Trần Thị Bình",      2, "Trung cấp", 1},
        {"Vật Lý 12A",   "Vật lý",      "Cô Trần Thị Bình",      3, "Nâng cao",  1},
        {"Hóa Học 10C",  "Hóa học",     "Thầy Lê Hoàng Cường",   2, "Cơ bản",   2},
        {"Hóa Học 11A",  "Hóa học",     "Thầy Lê Hoàng Cường",   2, "Trung cấp", 2},
        {"Ngữ Văn 12A",  "Ngữ văn",     "Cô Phạm Thị Dung",      2, "Nâng cao",  3},
        {"Tiếng Anh 11B","Tiếng Anh",   "Cô Phạm Thị Dung",      3, "Trung cấp", 4},
        {"Tiếng Anh 12A","Tiếng Anh",   "Cô Phạm Thị Dung",      3, "Nâng cao",  4},
    };

    private final JPanel cardGrid = new JPanel(new GridLayout(0, 3, 14, 14));
    private final JTextField txtSearch  = new JTextField(18);
    private final JComboBox<String> cboMon   = new JComboBox<>(new String[]{
        "Tất cả môn", "Toán học", "Vật lý", "Hóa học", "Ngữ văn", "Tiếng Anh"});
    private final JComboBox<String> cboCap   = new JComboBox<>(new String[]{
        "Tất cả cấp độ", "Cơ bản", "Trung cấp", "Nâng cao"});

    public TraCuuKhoaHocPanel() {
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("TRA CỨU KHÓA HỌC / LỚP HỌC");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(0x1B5E20));
        add(title, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(buildCardArea(),
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        JPanel center = new JPanel(new BorderLayout(0, 8));
        center.add(buildFilterBar(), BorderLayout.NORTH);
        center.add(scroll, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        renderCards(KHOA_HOC);
    }

    private JPanel buildFilterBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        bar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xC8E6C9)),
            new EmptyBorder(6, 10, 6, 10)));

        txtSearch.putClientProperty("JTextField.placeholderText", "Tìm lớp, giảng viên...");
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) { applyFilter(); }
        });

        cboMon.addActionListener(e -> applyFilter());
        cboCap.addActionListener(e -> applyFilter());

        bar.add(new JLabel("Tìm kiếm:"));
        bar.add(txtSearch);
        bar.add(new JLabel("Môn:"));
        bar.add(cboMon);
        bar.add(new JLabel("Cấp độ:"));
        bar.add(cboCap);
        return bar;
    }

    private JPanel buildCardArea() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBorder(new EmptyBorder(4, 0, 4, 0));
        cardGrid.setOpaque(false);
        cardGrid.setBorder(new EmptyBorder(4, 4, 4, 4));
        wrapper.add(cardGrid, BorderLayout.NORTH); // NORTH để không giãn chiều dọc
        return wrapper;
    }

    private void applyFilter() {
        String kw  = txtSearch.getText().trim().toLowerCase();
        String mon = (String) cboMon.getSelectedItem();
        String cap = (String) cboCap.getSelectedItem();

        List<Object[]> filtered = new ArrayList<>();
        for (Object[] row : KHOA_HOC) {
            boolean okMon = "Tất cả môn".equals(mon)     || row[1].equals(mon);
            boolean okCap = "Tất cả cấp độ".equals(cap)  || row[4].equals(cap);
            boolean okKw  = kw.isEmpty()
                || ((String)row[0]).toLowerCase().contains(kw)
                || ((String)row[2]).toLowerCase().contains(kw);
            if (okMon && okCap && okKw) filtered.add(row);
        }
        renderCards(filtered.toArray(new Object[0][]));
    }

    private void renderCards(Object[][] data) {
        cardGrid.removeAll();
        for (Object[] row : data) cardGrid.add(buildCard(row));
        cardGrid.revalidate();
        cardGrid.repaint();
    }

    private JPanel buildCard(Object[] row) {
        String tenLop  = (String) row[0];
        String mon     = (String) row[1];
        String gv      = (String) row[2];
        int buoi       = (int)    row[3];
        String capDo   = (String) row[4];
        Color  color   = SUBJECT_COLORS[(int) row[5]];

        JPanel card = new JPanel(new BorderLayout(0, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(0xE0E0E0), 1));

        // Header màu theo môn
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(color);
        header.setPreferredSize(new Dimension(0, 64));
        header.setBorder(new EmptyBorder(0, 14, 0, 14));
        JLabel lblMon = new JLabel(mon);
        lblMon.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblMon.setForeground(new Color(255, 255, 255, 200));
        header.add(lblMon, BorderLayout.CENTER);
        card.add(header, BorderLayout.NORTH);

        // Body
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(Color.WHITE);
        body.setBorder(new EmptyBorder(12, 14, 12, 14));

        JLabel lblTen = new JLabel(tenLop);
        lblTen.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTen.setForeground(new Color(0x212121));
        lblTen.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lblGV = new JLabel(gv);
        lblGV.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblGV.setForeground(new Color(0x616161));
        lblGV.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lblInfo = new JLabel(buoi + " buổi/tuần");
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblInfo.setForeground(new Color(0x757575));
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
        badge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        badge.setOpaque(true);
        badge.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
        badge.setForeground(color);
        badge.setBorder(BorderFactory.createLineBorder(color, 1));
        JButton btnDK = new JButton("Đăng ký");
        btnDK.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnDK.setBackground(color);
        btnDK.setForeground(Color.WHITE);
        btnDK.setFocusPainted(false);
        btnDK.setBorderPainted(false);
        btnDK.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnDK.addActionListener(e ->
            JOptionPane.showMessageDialog(this,
                "Đã gửi yêu cầu đăng ký lớp: " + tenLop,
                "Đăng Ký Thành Công", JOptionPane.INFORMATION_MESSAGE));
        footer.add(badge, BorderLayout.WEST);
        footer.add(btnDK,  BorderLayout.EAST);
        footer.setAlignmentX(LEFT_ALIGNMENT);
        body.add(footer);

        card.add(body, BorderLayout.CENTER);

        // Hover
        card.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createLineBorder(color, 1));
            }
            @Override public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createLineBorder(new Color(0xE0E0E0), 1));
            }
        });
        return card;
    }
}
