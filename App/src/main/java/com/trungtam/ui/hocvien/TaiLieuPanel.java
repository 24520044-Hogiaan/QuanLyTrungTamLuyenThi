package com.trungtam.ui.hocvien;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;

/**
 * Xem và tải tài liệu học tập — file do giảng viên/nhân viên upload.
 */
public class TaiLieuPanel extends JPanel {

    // {môn, tên tài liệu, giảng viên, ngày tải, loại, kích thước}
    private static final Object[][] TAI_LIEU = {
        {"Toán học",   "Bài tập tích phân chương 3",     "Thầy Nguyễn Văn An",   "10/09/2025", "PDF",  "2.3 MB"},
        {"Toán học",   "Đề thi thử giữa kỳ 2024",        "Thầy Nguyễn Văn An",   "15/10/2025", "PDF",  "1.1 MB"},
        {"Toán học",   "Slide bài giảng tuần 5-8",        "Thầy Nguyễn Văn An",   "20/10/2025", "PPTX", "5.7 MB"},
        {"Toán học",   "Video giải đề thi thử",           "Thầy Nguyễn Văn An",   "01/11/2025", "MP4",  "180 MB"},
        {"Tiếng Anh",  "Grammar Practice - Unit 1-5",     "Cô Phạm Thị Dung",     "12/09/2025", "PDF",  "3.4 MB"},
        {"Tiếng Anh",  "Từ vựng chủ đề Education",        "Cô Phạm Thị Dung",     "25/09/2025", "PDF",  "0.8 MB"},
        {"Tiếng Anh",  "Listening Practice - IELTS B2",   "Cô Phạm Thị Dung",     "05/10/2025", "MP3",  "45 MB"},
        {"Tiếng Anh",  "Reading Comprehension Exercises", "Cô Phạm Thị Dung",     "20/10/2025", "DOCX", "1.2 MB"},
    };

    private final DefaultTableModel tableModel;
    private final JComboBox<String> cboMon = new JComboBox<>(new String[]{
        "Tất cả môn", "Toán học", "Tiếng Anh"});

    public TaiLieuPanel() {
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("XEM TÀI LIỆU HỌC TẬP");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(0x1B5E20));
        add(title, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
            new String[]{"Môn", "Tên Tài Liệu", "Giảng Viên", "Ngày Tải", "Loại", "Kích Thước", "Thao Tác"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(tableModel);
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setShowGrid(true); table.setGridColor(new Color(0xE0E0E0));
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        int[] w = {100, 260, 170, 100, 60, 80};
        for (int i = 0; i < w.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(w[i]);
            table.getColumnModel().getColumn(i).setMaxWidth(w[i]);
            table.getColumnModel().getColumn(i).setResizable(false);
        }
        table.setDefaultRenderer(Object.class, new TaiLieuRenderer());

        // Nút tải xuống / xem theo dòng chọn
        JButton btnXem = buildActionBtn("Xem Trực Tuyến", new Color(0x1565C0));
        JButton btnTai = buildActionBtn("Tải Xuống",      new Color(0x2E7D32));

        btnXem.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Vui lòng chọn tài liệu.", "Chưa chọn", JOptionPane.WARNING_MESSAGE); return; }
            String loai = (String) tableModel.getValueAt(table.convertRowIndexToModel(row), 4);
            if ("MP4".equals(loai) || "MP3".equals(loai))
                JOptionPane.showMessageDialog(this, "File media sẽ mở trong trình phát media mặc định.", "Xem Trực Tuyến", JOptionPane.INFORMATION_MESSAGE);
            else
                JOptionPane.showMessageDialog(this, "Tài liệu sẽ mở trong trình duyệt / ứng dụng mặc định.", "Xem Trực Tuyến", JOptionPane.INFORMATION_MESSAGE);
        });
        btnTai.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Vui lòng chọn tài liệu.", "Chưa chọn", JOptionPane.WARNING_MESSAGE); return; }
            String ten = (String) tableModel.getValueAt(table.convertRowIndexToModel(row), 1);
            JOptionPane.showMessageDialog(this, "Đã tải xuống: " + ten + "\nLưu vào thư mục Tải Về.", "Tải Xuống Thành Công", JOptionPane.INFORMATION_MESSAGE);
        });

        cboMon.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cboMon.addActionListener(e -> applyFilter());

        JPanel filterBar = new JPanel(new BorderLayout(12, 0));
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        left.add(new JLabel("Lọc theo môn:"));
        left.add(cboMon);
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        right.add(btnXem); right.add(btnTai);
        filterBar.add(left, BorderLayout.WEST);
        filterBar.add(right, BorderLayout.EAST);

        JPanel center = new JPanel(new BorderLayout(0, 8));
        center.add(filterBar, BorderLayout.NORTH);
        center.add(new JScrollPane(table,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        loadData(TAI_LIEU);
    }

    private void applyFilter() {
        String mon = (String) cboMon.getSelectedItem();
        java.util.List<Object[]> filtered = new java.util.ArrayList<>();
        for (Object[] row : TAI_LIEU) {
            if ("Tất cả môn".equals(mon) || row[0].equals(mon)) filtered.add(row);
        }
        loadData(filtered.toArray(new Object[0][]));
    }

    private void loadData(Object[][] data) {
        tableModel.setRowCount(0);
        for (Object[] row : data)
            tableModel.addRow(new Object[]{row[0], row[1], row[2], row[3], row[4], row[5], ""});
    }

    private JButton buildActionBtn(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(color); btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false); btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // Màu theo loại file
    private static class TaiLieuRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int r, int c) {
            super.getTableCellRendererComponent(t, val, sel, foc, r, c);
            if (!sel) {
                setBackground(r % 2 == 0 ? Color.WHITE : new Color(0xF1F8E9));
                if (c == 4) {
                    String loai = String.valueOf(val);
                    setForeground("PDF".equals(loai)  ? new Color(0xC62828)
                                : "MP4".equals(loai) || "MP3".equals(loai) ? new Color(0x1565C0)
                                : "PPTX".equals(loai) ? new Color(0xE65100)
                                :                        new Color(0x2E7D32));
                    setFont(getFont().deriveFont(Font.BOLD));
                } else {
                    setForeground(new Color(0x212121));
                    setFont(getFont().deriveFont(Font.PLAIN));
                }
            }
            return this;
        }
    }
}
