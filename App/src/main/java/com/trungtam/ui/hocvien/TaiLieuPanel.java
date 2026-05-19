package com.trungtam.ui.hocvien;

import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;

/**
 * Xem và tải tài liệu học tập.
 */
public class TaiLieuPanel extends JPanel {

    private static final Object[][] TAI_LIEU = {
            { "Toán học", "Bài tập tích phân chương 3", "Thầy Nguyễn Văn An", "10/09/2025", "PDF", "2.3 MB" },
            { "Toán học", "Đề thi thử giữa kỳ 2024", "Thầy Nguyễn Văn An", "15/10/2025", "PDF", "1.1 MB" },
            { "Toán học", "Slide bài giảng tuần 5-8", "Thầy Nguyễn Văn An", "20/10/2025", "PPTX", "5.7 MB" },
            { "Toán học", "Video giải đề thi thử", "Thầy Nguyễn Văn An", "01/11/2025", "MP4", "180 MB" },
            { "Tiếng Anh", "Grammar Practice - Unit 1-5", "Cô Phạm Thị Dung", "12/09/2025", "PDF", "3.4 MB" },
            { "Tiếng Anh", "Từ vựng chủ đề Education", "Cô Phạm Thị Dung", "25/09/2025", "PDF", "0.8 MB" },
            { "Tiếng Anh", "Listening Practice - IELTS B2", "Cô Phạm Thị Dung", "05/10/2025", "MP3", "45 MB" },
            { "Tiếng Anh", "Reading Comprehension Exercises", "Cô Phạm Thị Dung", "20/10/2025", "DOCX", "1.2 MB" },
    };

    private final DefaultTableModel tableModel;
    private final JComboBox<String> cboMon = new JComboBox<>(
            new String[] { "Tất cả môn", "Toán học", "Tiếng Anh" });

    public TaiLieuPanel() {
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(UiTheme.PAD_L, UiTheme.PAD_L, UiTheme.PAD_L, UiTheme.PAD_L));
        setBackground(UiTheme.APP_BG);

        JLabel title = new JLabel("XEM TÀI LIỆU HỌC TẬP");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.SECONDARY);
        add(title, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new String[] { "Môn", "Tên Tài Liệu", "Giảng Viên", "Ngày Tải", "Loại", "Kích Thước", "Thao Tác" },
                0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        UiComponents.styleTable(table);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        int[] w = { 100, 260, 170, 100, 60, 80 };
        for (int i = 0; i < w.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(w[i]);
            table.getColumnModel().getColumn(i).setMaxWidth(w[i]);
            table.getColumnModel().getColumn(i).setResizable(false);
        }
        table.setDefaultRenderer(Object.class, new TaiLieuRenderer());

        JButton btnXem = UiComponents.primaryButton("Xem Trực Tuyến", UiTheme.INFO);
        JButton btnTai = UiComponents.primaryButton("Tải Xuống", UiTheme.SUCCESS);

        btnXem.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn tài liệu.", "Chưa chọn",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            String loai = (String) tableModel.getValueAt(table.convertRowIndexToModel(row), 4);
            if ("MP4".equals(loai) || "MP3".equals(loai))
                JOptionPane.showMessageDialog(this, "File media sẽ mở trong trình phát media mặc định.",
                        "Xem Trực Tuyến", JOptionPane.INFORMATION_MESSAGE);
            else
                JOptionPane.showMessageDialog(this, "Tài liệu sẽ mở trong trình duyệt / ứng dụng mặc định.",
                        "Xem Trực Tuyến", JOptionPane.INFORMATION_MESSAGE);
        });
        btnTai.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn tài liệu.", "Chưa chọn",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            String ten = (String) tableModel.getValueAt(table.convertRowIndexToModel(row), 1);
            JOptionPane.showMessageDialog(this, "Đã tải xuống: " + ten + "\nLưu vào thư mục Tải Về.",
                    "Tải Xuống Thành Công", JOptionPane.INFORMATION_MESSAGE);
        });

        cboMon.setFont(UiTheme.BODY);
        cboMon.addActionListener(e -> applyFilter());

        JPanel filterBar = new JPanel(new BorderLayout(12, 0));
        filterBar.setOpaque(false);
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        left.setOpaque(false);
        JLabel lblMon = new JLabel("Lọc theo môn:");
        lblMon.setFont(UiTheme.BODY);
        left.add(lblMon);
        left.add(cboMon);
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        right.setOpaque(false);
        right.add(btnXem);
        right.add(btnTai);
        filterBar.add(left, BorderLayout.WEST);
        filterBar.add(right, BorderLayout.EAST);

        JPanel center = new JPanel(new BorderLayout(0, 8));
        center.setOpaque(false);
        center.add(filterBar, BorderLayout.NORTH);
        center.add(UiComponents.tableScroll(table), BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        loadData(TAI_LIEU);
    }

    private void applyFilter() {
        String mon = (String) cboMon.getSelectedItem();
        java.util.List<Object[]> filtered = new java.util.ArrayList<>();
        for (Object[] row : TAI_LIEU) {
            if ("Tất cả môn".equals(mon) || row[0].equals(mon))
                filtered.add(row);
        }
        loadData(filtered.toArray(new Object[0][]));
    }

    private void loadData(Object[][] data) {
        tableModel.setRowCount(0);
        for (Object[] row : data)
            tableModel.addRow(new Object[] { row[0], row[1], row[2], row[3], row[4], row[5], "" });
    }

    private static class TaiLieuRenderer extends UiComponents.AltRowRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable t, Object val, boolean sel, boolean foc, int r, int c) {
            super.getTableCellRendererComponent(t, val, sel, foc, r, c);
            if (!sel && c == 4) {
                String loai = String.valueOf(val);
                setForeground("PDF".equals(loai) ? UiTheme.DANGER
                        : "MP4".equals(loai) || "MP3".equals(loai) ? UiTheme.INFO
                                : "PPTX".equals(loai) ? UiTheme.WARNING
                                        : UiTheme.SUCCESS);
                setFont(UiTheme.BODY_B);
            } else if (!sel) {
                setFont(UiTheme.BODY);
            }
            return this;
        }
    }
}
