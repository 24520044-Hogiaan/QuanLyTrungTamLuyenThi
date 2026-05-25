package com.trungtam.ui.hocvien;

import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;

/**
 * Panel đăng ký lớp học — danh sách lớp bên trái, học viên đã đăng ký bên phải.
 */
public class DangKyLopPanel extends JPanel {

    private static final String[] LOP_LIST = { "Toán 12A", "Lý 11B", "Hóa 10C", "Văn 12A", "Anh 11B" };

    private static final Object[][] LOP_INFO = {
            { "Toán 12A", 25 }, { "Lý 11B", 20 }, { "Hóa 10C", 22 }, { "Văn 12A", 18 }, { "Anh 11B", 24 }
    };

    private static final Object[][] DANG_KY_DATA = {
            { "Toán 12A", 1, "Nguyễn Minh Anh", "01/09/2025", "Đã đóng" },
            { "Toán 12A", 4, "Phạm Thị Hoa", "01/09/2025", "Đã đóng" },
            { "Toán 12A", 9, "Hoàng Văn Phúc", "01/09/2025", "Chưa đóng" },
            { "Toán 12A", 2, "Trần Bảo Châu", "02/09/2025", "Đã đóng" },
            { "Lý 11B", 7, "Bùi Đức Mạnh", "01/09/2025", "Đã đóng" },
            { "Lý 11B", 6, "Võ Thị Lan", "01/09/2025", "Quá hạn" },
            { "Hóa 10C", 10, "Ngô Thị Quỳnh", "01/09/2025", "Đã đóng" },
            { "Hóa 10C", 4, "Phạm Thị Hoa", "01/09/2025", "Chưa đóng" },
            { "Văn 12A", 2, "Trần Bảo Châu", "01/09/2025", "Đã đóng" },
            { "Anh 11B", 6, "Võ Thị Lan", "01/09/2025", "Đã đóng" },
            { "Anh 11B", 7, "Bùi Đức Mạnh", "01/09/2025", "Chưa đóng" },
    };

    private final DefaultTableModel tableModel;
    private final JLabel lblLopInfo = new JLabel();
    private final JLabel lblSiSo = new JLabel();

    public DangKyLopPanel() {
        setLayout(new BorderLayout(0, 10));
        setBorder(new EmptyBorder(UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M));
        setBackground(UiTheme.APP_BG);

        JLabel title = new JLabel("ĐĂNG KÝ LỚP HỌC");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.PRIMARY);
        add(title, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new String[] { "Mã HV", "Họ Tên", "Ngày Đăng Ký", "Học Phí" }, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        UiComponents.styleTable(table);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        int[] widths = { 70, 200, 130 };
        for (int i = 0; i < widths.length; i++) {
            TableColumn col = table.getColumnModel().getColumn(i);
            col.setPreferredWidth(widths[i]);
            col.setMaxWidth(widths[i]);
            col.setResizable(false);
        }
        table.setDefaultRenderer(Object.class, new HocPhiRenderer());

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                buildLopList(), buildRightPanel(table));
        split.setDividerLocation(200);
        split.setDividerSize(5);
        split.setBorder(null);
        add(split, BorderLayout.CENTER);
    }

    private JPanel buildLopList() {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setBackground(UiTheme.CARD_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.CARD_BORDER),
                new EmptyBorder(6, 6, 6, 6)));

        JLabel lbl = new JLabel("Danh Sách Lớp");
        lbl.setFont(UiTheme.BODY_B);
        lbl.setForeground(UiTheme.TEXT_SECONDARY);
        lbl.setBorder(new EmptyBorder(0, 4, 4, 0));
        panel.add(lbl, BorderLayout.NORTH);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String lop : LOP_LIST)
            listModel.addElement(lop);
        JList<String> lopList = new JList<>(listModel);
        lopList.setFont(UiTheme.BODY);
        lopList.setFixedCellHeight(42);
        lopList.setSelectedIndex(0);
        lopList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting())
                loadLopData((String) lopList.getSelectedValue());
        });
        JScrollPane sp = UiComponents.scrollPane(lopList);
        sp.setBorder(BorderFactory.createLineBorder(UiTheme.CARD_BORDER, 1));
        panel.add(sp, BorderLayout.CENTER);
        loadLopData(LOP_LIST[0]);
        return panel;
    }

    private JPanel buildRightPanel(JTable table) {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setOpaque(false);

        lblLopInfo.setFont(UiTheme.BODY_B);
        lblLopInfo.setOpaque(true);
        lblLopInfo.setBackground(new Color(0xE8EAF6));
        lblLopInfo.setForeground(UiTheme.PRIMARY);
        lblLopInfo.setBorder(new EmptyBorder(6, 10, 6, 10));

        lblSiSo.setFont(UiTheme.BODY_B);
        lblSiSo.setForeground(new Color(0x1B5E20));

        JPanel infoBar = new JPanel(new BorderLayout(8, 0));
        infoBar.setOpaque(true);
        infoBar.setBackground(new Color(0xE8EAF6));
        infoBar.add(lblLopInfo, BorderLayout.CENTER);
        infoBar.add(lblSiSo, BorderLayout.EAST);

        panel.add(infoBar, BorderLayout.NORTH);
        panel.add(UiComponents.tableScroll(table), BorderLayout.CENTER);
        return panel;
    }

    private void loadLopData(String tenLop) {
        int siSoMax = 0;
        for (Object[] info : LOP_INFO) {
            if (info[0].equals(tenLop)) {
                siSoMax = (int) info[1];
                break;
            }
        }
        tableModel.setRowCount(0);
        int count = 0;
        for (Object[] row : DANG_KY_DATA) {
            if (row[0].equals(tenLop)) {
                tableModel.addRow(new Object[] { row[1], row[2], row[3], row[4] });
                count++;
            }
        }
        lblLopInfo.setText("  Lớp: " + tenLop);
        lblSiSo.setText("Sĩ số: " + count + "/" + siSoMax + " học viên  ");
    }

    private static class HocPhiRenderer extends UiComponents.AltRowRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable t, Object val, boolean sel, boolean foc, int r, int c) {
            super.getTableCellRendererComponent(t, val, sel, foc, r, c);
            if (!sel && c == 3) {
                String s = String.valueOf(val);
                setForeground("Đã đóng".equals(s) ? UiTheme.SUCCESS
                        : "Quá hạn".equals(s) ? UiTheme.DANGER : UiTheme.WARNING);
                setFont(UiTheme.BODY_B);
            } else if (!sel) {
                setFont(UiTheme.BODY);
            }
            return this;
        }
    }
}
