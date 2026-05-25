package com.trungtam.ui.hocvien;

import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;

/**
 * Xem điểm học tập cá nhân — học viên chỉ thấy điểm của mình.
 */
public class XemDiemHVPanel extends JPanel {

    private static final String[] LOP_CUA_HV = { "Toán 12A", "Tiếng Anh 11B" };

    // {lớp, bài KT, ngày, điểm, hệ số, nhận xét GV}
    private static final Object[][] DIEM_DATA = {
            { "Toán 12A", "Kiểm tra 1", "10/10/2025", 8.5, 1, "Làm bài tốt, cần chú ý phần đạo hàm" },
            { "Toán 12A", "Kiểm tra 2", "05/11/2025", 7.0, 1, "Cần luyện thêm bài tập tích phân" },
            { "Toán 12A", "Kiểm tra giữa kỳ", "20/11/2025", 8.0, 2, "Tốt, giữ vững phong độ" },
            { "Toán 12A", "Kiểm tra cuối kỳ", "20/12/2025", 9.0, 2, "Xuất sắc! Tiếp tục phát huy" },
            { "Tiếng Anh 11B", "Kiểm tra 1", "12/10/2025", 7.5, 1, "Ngữ pháp ổn, cần cải thiện từ vựng" },
            { "Tiếng Anh 11B", "Kiểm tra 2", "10/11/2025", 8.0, 1, "Tiến bộ rõ rệt" },
            { "Tiếng Anh 11B", "Kiểm tra giữa kỳ", "22/11/2025", 7.0, 2, "Cần ôn lại phần reading" },
    };

    private final DefaultTableModel tableModel;
    private final JLabel lblTB = new JLabel("-");
    private final JLabel lblTong = new JLabel("-");
    private final JLabel lblXepLoai = new JLabel("-");
    private final JLabel lblSoBai = new JLabel("-");

    public XemDiemHVPanel() {
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(UiTheme.PAD_L, UiTheme.PAD_L, UiTheme.PAD_L, UiTheme.PAD_L));
        setBackground(UiTheme.APP_BG);

        JLabel title = new JLabel("XEM ĐIỂM & KẾT QUẢ HỌC TẬP");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.SECONDARY);
        add(title, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new String[] { "Bài Kiểm Tra", "Ngày Thi", "Điểm", "Hệ Số", "Nhận Xét Giảng Viên" }, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        UiComponents.styleTable(table);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        int[] w = { 180, 110, 60, 70 };
        for (int i = 0; i < w.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(w[i]);
            table.getColumnModel().getColumn(i).setMaxWidth(w[i]);
            table.getColumnModel().getColumn(i).setResizable(false);
        }
        table.setDefaultRenderer(Object.class, new DiemRenderer());
        // Bài KT: left; Ngày: center; Điểm: center; Hệ Số: center; Nhận Xét: left
        UiComponents.setColumnAlignments(table,
                SwingConstants.LEFT, SwingConstants.CENTER,
                SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.LEFT);

        JComboBox<String> cboLop = new JComboBox<>(LOP_CUA_HV);
        cboLop.setFont(UiTheme.BODY);
        cboLop.addActionListener(e -> loadDiem((String) cboLop.getSelectedItem()));

        JLabel lblNote = new JLabel("  (Chỉ hiển thị điểm của bản thân bạn)");
        lblNote.setFont(UiTheme.CAPTION_I);
        lblNote.setForeground(UiTheme.TEXT_MUTED);

        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        filterBar.setOpaque(false);
        JLabel lblLop = new JLabel("Lớp học:");
        lblLop.setFont(UiTheme.BODY);
        filterBar.add(lblLop);
        filterBar.add(cboLop);
        filterBar.add(lblNote);

        JPanel center = new JPanel(new BorderLayout(0, 8));
        center.setOpaque(false);
        center.add(filterBar, BorderLayout.NORTH);
        center.add(UiComponents.tableScroll(table), BorderLayout.CENTER);
        center.add(buildSummaryBar(), BorderLayout.SOUTH);
        add(center, BorderLayout.CENTER);

        loadDiem(LOP_CUA_HV[0]);
    }

    private JPanel buildSummaryBar() {
        JPanel bar = new JPanel(new GridLayout(1, 4, 12, 0));
        bar.setOpaque(false);
        bar.setBorder(new EmptyBorder(8, 0, 0, 0));
        bar.add(UiComponents.statCard("Số Bài Thi", lblSoBai, UiTheme.INFO));
        bar.add(UiComponents.statCard("Điểm Trung Bình", lblTB, UiTheme.SUCCESS));
        bar.add(UiComponents.statCard("Điểm Tổng (có hệ số)", lblTong, new Color(0x6A1B9A)));
        bar.add(UiComponents.statCard("Xếp Loại", lblXepLoai, UiTheme.WARNING));
        return bar;
    }

    private void loadDiem(String lop) {
        tableModel.setRowCount(0);
        double tongCoDHS = 0, tongHS = 0;
        int count = 0;
        for (Object[] row : DIEM_DATA) {
            if (row[0].equals(lop)) {
                tableModel.addRow(new Object[] { row[1], row[2], row[3], row[4], row[5] });
                tongCoDHS += (double) row[3] * (int) row[4];
                tongHS += (int) row[4];
                count++;
            }
        }
        // KPI: count unit for "số bài", numeric for scores
        lblSoBai.setText(count + " bài");
        if (tongHS > 0) {
            double tb = tongCoDHS / tongHS;
            lblTB.setText(String.format("%.1f", tb));
            lblTong.setText(String.format("%.1f", tongCoDHS));
            lblXepLoai.setText(tb >= 9 ? "Xuất Sắc" : tb >= 8 ? "Giỏi" : tb >= 6.5 ? "Khá" : "TB");
        } else {
            lblTB.setText("-");
            lblTong.setText("-");
            lblXepLoai.setText("-");
        }
    }

    private static class DiemRenderer extends UiComponents.AltRowRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable t, Object val, boolean sel, boolean foc, int r, int c) {
            super.getTableCellRendererComponent(t, val, sel, foc, r, c);
            if (!sel && c == 2 && val instanceof Double d) {
                setForeground(d >= 8 ? UiTheme.SUCCESS : d >= 6.5 ? UiTheme.WARNING : UiTheme.DANGER);
                setFont(UiTheme.BODY_B);
            } else if (!sel) {
                setFont(UiTheme.BODY);
            }
            return this;
        }
    }
}
