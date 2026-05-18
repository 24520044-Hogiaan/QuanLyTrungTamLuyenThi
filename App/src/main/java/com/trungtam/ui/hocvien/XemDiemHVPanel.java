package com.trungtam.ui.hocvien;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;

/**
 * Xem điểm học tập cá nhân — học viên chỉ thấy điểm của mình.
 * Thống kê (TB, tổng, xếp loại) hiển thị phía dưới bảng.
 */
public class XemDiemHVPanel extends JPanel {

    private static final String[] LOP_CUA_HV = {"Toán 12A", "Tiếng Anh 11B"};

    // {lớp, bài KT, ngày, điểm, hệ số, nhận xét GV}
    private static final Object[][] DIEM_DATA = {
        {"Toán 12A",      "Kiểm tra 1",       "10/10/2025", 8.5, 1, "Làm bài tốt, cần chú ý phần đạo hàm"},
        {"Toán 12A",      "Kiểm tra 2",       "05/11/2025", 7.0, 1, "Cần luyện thêm bài tập tích phân"},
        {"Toán 12A",      "Kiểm tra giữa kỳ", "20/11/2025", 8.0, 2, "Tốt, giữ vững phong độ"},
        {"Toán 12A",      "Kiểm tra cuối kỳ", "20/12/2025", 9.0, 2, "Xuất sắc! Tiếp tục phát huy"},
        {"Tiếng Anh 11B", "Kiểm tra 1",       "12/10/2025", 7.5, 1, "Ngữ pháp ổn, cần cải thiện từ vựng"},
        {"Tiếng Anh 11B", "Kiểm tra 2",       "10/11/2025", 8.0, 1, "Tiến bộ rõ rệt"},
        {"Tiếng Anh 11B", "Kiểm tra giữa kỳ", "22/11/2025", 7.0, 2, "Cần ôn lại phần reading"},
    };

    private final DefaultTableModel tableModel;
    private final JLabel lblTB       = new JLabel("-");
    private final JLabel lblTong     = new JLabel("-");
    private final JLabel lblXepLoai  = new JLabel("-");
    private final JLabel lblSoBai    = new JLabel("-");

    public XemDiemHVPanel() {
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("XEM ĐIỂM & KẾT QUẢ HỌC TẬP");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(0x1B5E20));
        add(title, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
            new String[]{"Bài Kiểm Tra", "Ngày Thi", "Điểm", "Hệ Số", "Nhận Xét Giảng Viên"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(tableModel);
        table.setRowHeight(36);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setShowGrid(true); table.setGridColor(new Color(0xE0E0E0));
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        int[] w = {180, 110, 60, 70};
        for (int i = 0; i < w.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(w[i]);
            table.getColumnModel().getColumn(i).setMaxWidth(w[i]);
            table.getColumnModel().getColumn(i).setResizable(false);
        }
        table.setDefaultRenderer(Object.class, new DiemRenderer());

        // Filter
        JComboBox<String> cboLop = new JComboBox<>(LOP_CUA_HV);
        cboLop.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cboLop.addActionListener(e -> loadDiem((String) cboLop.getSelectedItem()));

        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        filterBar.add(new JLabel("Lớp học:"));
        filterBar.add(cboLop);
        filterBar.add(new JLabel("  (Chỉ hiển thị điểm của bản thân bạn)"));

        JPanel center = new JPanel(new BorderLayout(0, 8));
        center.add(filterBar, BorderLayout.NORTH);
        center.add(new JScrollPane(table,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
        center.add(buildSummaryBar(), BorderLayout.SOUTH);
        add(center, BorderLayout.CENTER);

        loadDiem(LOP_CUA_HV[0]);
    }

    private JPanel buildSummaryBar() {
        JPanel bar = new JPanel(new GridLayout(1, 4, 12, 0));
        bar.setBorder(new EmptyBorder(8, 0, 0, 0));
        bar.add(buildStatCard("Số Bài Thi",     lblSoBai,   new Color(0x1565C0)));
        bar.add(buildStatCard("Điểm Trung Bình",lblTB,      new Color(0x2E7D32)));
        bar.add(buildStatCard("Điểm Tổng (có hệ số)", lblTong, new Color(0x6A1B9A)));
        bar.add(buildStatCard("Xếp Loại",       lblXepLoai, new Color(0xBF360C)));
        return bar;
    }

    private JPanel buildStatCard(String title, JLabel valLabel, Color color) {
        JPanel card = new JPanel(new BorderLayout(0, 4));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(color.getRed(), color.getGreen(), color.getBlue(), 80), 1),
            new EmptyBorder(10, 14, 10, 14)));
        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(new Color(0x757575));
        valLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        valLabel.setForeground(color);
        card.add(lbl, BorderLayout.NORTH);
        card.add(valLabel, BorderLayout.CENTER);
        return card;
    }

    private void loadDiem(String lop) {
        tableModel.setRowCount(0);
        double tongCoDHS = 0, tongHS = 0;
        int count = 0;
        for (Object[] row : DIEM_DATA) {
            if (row[0].equals(lop)) {
                tableModel.addRow(new Object[]{row[1], row[2], row[3], row[4], row[5]});
                tongCoDHS += (double) row[3] * (int) row[4];
                tongHS    += (int) row[4];
                count++;
            }
        }
        lblSoBai.setText(count + " bài");
        if (tongHS > 0) {
            double tb = tongCoDHS / tongHS;
            lblTB.setText(String.format("%.1f", tb));
            lblTong.setText(String.format("%.1f", tongCoDHS));
            lblXepLoai.setText(tb >= 9 ? "Xuất Sắc" : tb >= 8 ? "Giỏi" : tb >= 6.5 ? "Khá" : "TB");
        } else {
            lblTB.setText("-"); lblTong.setText("-"); lblXepLoai.setText("-");
        }
    }

    private static class DiemRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int r, int c) {
            super.getTableCellRendererComponent(t, val, sel, foc, r, c);
            if (!sel) {
                setBackground(r % 2 == 0 ? Color.WHITE : new Color(0xF1F8E9));
                if (c == 2 && val instanceof Double d) {
                    setForeground(d >= 8 ? new Color(0x2E7D32) : d >= 6.5 ? new Color(0xE65100) : new Color(0xC62828));
                } else {
                    setForeground(new Color(0x212121));
                }
            }
            return this;
        }
    }
}
