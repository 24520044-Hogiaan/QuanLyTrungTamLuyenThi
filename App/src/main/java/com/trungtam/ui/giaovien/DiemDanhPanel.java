package com.trungtam.ui.giaovien;

import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

/**
 * Panel điểm danh học viên.
 * Luồng: Chọn lớp → Chọn / Tạo buổi → Tích trạng thái → Lưu.
 */
public class DiemDanhPanel extends JPanel {

    record BuoiHoc(int so, LocalDate ngay, String ghiChu) {
        private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        @Override
        public String toString() {
            return "Buổi " + so + "  –  " + ngay.format(FMT);
        }
    }

    private static final String[] LOP_LIST = { "Toán 12A", "Lý 11B", "Hóa 10C" };
    private static final String[][] HV_PER_LOP = {
            { "Nguyễn Minh Anh", "Trần Bảo Châu", "Lê Văn Dũng", "Phạm Thị Hoa", "Đỗ Quốc Hùng" },
            { "Võ Thị Lan", "Bùi Đức Mạnh", "Đinh Thị Nga" },
            { "Hoàng Văn Phúc", "Ngô Thị Quỳnh", "Phan Minh Tâm", "Lý Thị Uyên" }
    };
    private static final String[] TRANG_THAI = { "Có mặt", "Vắng mặt", "Đi trễ", "Nghỉ phép" };
    private static final Color[] STATUS_BG = {
            new Color(0xE8F5E9), new Color(0xFFEBEE), new Color(0xFFF8E1), new Color(0xE3F2FD)
    };
    private static final Color[] STATUS_FG = {
            UiTheme.SUCCESS, UiTheme.DANGER, UiTheme.WARNING, UiTheme.INFO
    };

    private final JComboBox<String> cboLop = new JComboBox<>(LOP_LIST);
    private final DefaultListModel<BuoiHoc> listModel = new DefaultListModel<>();
    private final JList<BuoiHoc> buoiList = new JList<>(listModel);
    private final JLabel lblBuoiInfo = new JLabel("← Chọn buổi học để bắt đầu điểm danh");
    private DefaultTableModel tableModel;
    private JTable table;
    private final JButton btnLuu = UiComponents.primaryButton("Lưu Điểm Danh", new Color(0x43A047));
    private final JLabel lblSoLuong = new JLabel();

    private final Map<String, List<BuoiHoc>> lopBuoiMap = new HashMap<>();

    public DiemDanhPanel() {
        setLayout(new BorderLayout(0, 10));
        setBorder(new EmptyBorder(UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M));
        setBackground(UiTheme.APP_BG);
        initSampleData();

        add(buildTopBar(), BorderLayout.NORTH);
        add(buildSplitPane(), BorderLayout.CENTER);

        cboLop.addActionListener(e -> refreshBuoiList());
        refreshBuoiList();
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout(12, 0));
        bar.setOpaque(false);
        JLabel title = new JLabel("Điểm Danh Học Viên");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.TEXT_PRIMARY);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);
        JLabel lblLop = new JLabel("Lớp:");
        lblLop.setFont(UiTheme.BODY);
        cboLop.setFont(UiTheme.BODY);
        right.add(lblLop);
        right.add(cboLop);

        bar.add(title, BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    private JSplitPane buildSplitPane() {
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buildLeftPanel(), buildRightPanel());
        split.setDividerLocation(240);
        split.setDividerSize(5);
        split.setBorder(null);
        return split;
    }

    private JPanel buildLeftPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setPreferredSize(new Dimension(240, 0));
        panel.setBackground(UiTheme.CARD_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.CARD_BORDER, 1),
                new EmptyBorder(10, 10, 10, 10)));

        JLabel lbl = new JLabel("DANH SÁCH BUỔI HỌC");
        lbl.setFont(UiTheme.CAPTION);
        lbl.setForeground(UiTheme.INFO);
        panel.add(lbl, BorderLayout.NORTH);

        buoiList.setFont(UiTheme.BODY);
        buoiList.setFixedCellHeight(38);
        buoiList.setCellRenderer(new BuoiCellRenderer());
        buoiList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting())
                onBuoiSelected();
        });
        JScrollPane listScroll = UiComponents.scrollPane(buoiList);
        listScroll.setBorder(BorderFactory.createLineBorder(UiTheme.CARD_BORDER, 1));
        panel.add(listScroll, BorderLayout.CENTER);

        JButton btnTao = UiComponents.primaryButton("+  Tạo Buổi Điểm Danh", UiTheme.INFO);
        btnTao.addActionListener(e -> showTaoBuoiDialog());
        panel.add(btnTao, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildRightPanel() {
        String[] cols = { "#", "Họ Tên Học Viên", "Trạng Thái", "Ghi Chú" };
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return c == 2 || c == 3;
            }
        };
        table = new JTable(tableModel);
        UiComponents.styleTable(table);
        table.getColumnModel().getColumn(0).setMaxWidth(40);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(new JComboBox<>(TRANG_THAI)));
        table.getColumnModel().getColumn(2).setCellRenderer(new StatusCellRenderer());
        // #: center; Họ Tên: left; Trạng Thái: center; Ghi Chú: left
        UiComponents.setColumnAlignments(table,
                SwingConstants.CENTER, SwingConstants.LEFT,
                SwingConstants.CENTER, SwingConstants.LEFT);

        lblBuoiInfo.setFont(UiTheme.CAPTION_I);
        lblBuoiInfo.setForeground(UiTheme.TEXT_MUTED);

        lblSoLuong.setFont(UiTheme.BODY_B);
        lblSoLuong.setForeground(new Color(0x2E7D32));

        JPanel infoRow = new JPanel(new BorderLayout());
        infoRow.setOpaque(false);
        infoRow.setBorder(new EmptyBorder(0, 4, 6, 0));
        infoRow.add(lblBuoiInfo, BorderLayout.WEST);

        btnLuu.setEnabled(false);
        btnLuu.addActionListener(e -> saveDiemDanh());

        JPanel right = new JPanel(new BorderLayout(0, 8));
        right.setOpaque(false);
        right.setBorder(new EmptyBorder(0, 10, 0, 0));
        right.add(infoRow, BorderLayout.NORTH);
        right.add(UiComponents.tableScroll(table), BorderLayout.CENTER);

        JPanel saveBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 4));
        saveBar.setOpaque(false);
        saveBar.add(lblSoLuong);
        saveBar.add(btnLuu);
        right.add(saveBar, BorderLayout.SOUTH);
        return right;
    }

    private void refreshBuoiList() {
        listModel.clear();
        lopBuoiMap.getOrDefault(getSelectedLop(), List.of()).forEach(listModel::addElement);
        tableModel.setRowCount(0);
        lblBuoiInfo.setText("← Chọn buổi học để bắt đầu điểm danh");
        lblBuoiInfo.setFont(UiTheme.CAPTION_I);
        lblBuoiInfo.setForeground(UiTheme.TEXT_MUTED);
        lblSoLuong.setText("");
        btnLuu.setEnabled(false);
    }

    private void onBuoiSelected() {
        BuoiHoc b = buoiList.getSelectedValue();
        if (b == null)
            return;

        String lop = getSelectedLop();
        int lopIdx = Arrays.asList(LOP_LIST).indexOf(lop);
        DateTimeFormatter viFmt = DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy", Locale.of("vi"));

        lblBuoiInfo.setFont(UiTheme.BODY_B);
        lblBuoiInfo.setForeground(UiTheme.INFO);
        lblBuoiInfo.setText("Buổi " + b.so() + "  |  " + b.ngay().format(viFmt)
                + (b.ghiChu().isEmpty() ? "" : "  –  " + b.ghiChu()));

        tableModel.setRowCount(0);
        String[] hocVien = HV_PER_LOP[lopIdx];
        for (int i = 0; i < hocVien.length; i++) {
            tableModel.addRow(new Object[] { i + 1, hocVien[i], "Có mặt", "" });
        }
        lblSoLuong.setText(hocVien.length + " học viên  ");
        btnLuu.setEnabled(true);
    }

    private void saveDiemDanh() {
        long vang = countStatus("Vắng mặt"), tre = countStatus("Đi trễ");
        JOptionPane.showMessageDialog(this,
                String.format("Điểm danh đã lưu thành công!\n─────────────────\nVắng mặt: %d  |  Đi trễ: %d", vang,
                        tre),
                "Lưu thành công", JOptionPane.INFORMATION_MESSAGE);
    }

    private long countStatus(String status) {
        long count = 0;
        for (int r = 0; r < tableModel.getRowCount(); r++) {
            if (status.equals(tableModel.getValueAt(r, 2)))
                count++;
        }
        return count;
    }

    private void showTaoBuoiDialog() {
        JTextField txtNgay = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), 14);
        JTextField txtGhiChu = new JTextField(14);
        txtGhiChu.putClientProperty("JTextField.placeholderText", "VD: Học bù, bài mới...");
        txtNgay.setFont(UiTheme.BODY);
        txtGhiChu.setFont(UiTheme.BODY);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 4, 6, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        form.add(new JLabel("Ngày buổi học (dd/MM/yyyy) *"), gbc);
        gbc.gridx = 1;
        form.add(txtNgay, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        form.add(new JLabel("Ghi chú"), gbc);
        gbc.gridx = 1;
        form.add(txtGhiChu, gbc);

        String lop = getSelectedLop();
        int opt = JOptionPane.showConfirmDialog(this, form,
                "Tạo buổi điểm danh – " + lop, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opt != JOptionPane.OK_OPTION)
            return;

        try {
            LocalDate ngay = LocalDate.parse(txtNgay.getText().trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            List<BuoiHoc> list = lopBuoiMap.computeIfAbsent(lop, k -> new ArrayList<>());
            list.add(new BuoiHoc(list.size() + 1, ngay, txtGhiChu.getText().trim()));
            refreshBuoiList();
            buoiList.setSelectedIndex(listModel.size() - 1);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ngày không đúng định dạng dd/MM/yyyy.", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getSelectedLop() {
        return (String) cboLop.getSelectedItem();
    }

    private void initSampleData() {
        lopBuoiMap.put("Toán 12A", new ArrayList<>(List.of(
                new BuoiHoc(1, LocalDate.of(2026, 5, 5), ""),
                new BuoiHoc(2, LocalDate.of(2026, 5, 8), ""),
                new BuoiHoc(3, LocalDate.of(2026, 5, 12), "Học bù"),
                new BuoiHoc(4, LocalDate.of(2026, 5, 15), "Ôn thi chương 5"))));
        lopBuoiMap.put("Lý 11B", new ArrayList<>(List.of(
                new BuoiHoc(1, LocalDate.of(2026, 5, 6), ""),
                new BuoiHoc(2, LocalDate.of(2026, 5, 13), ""))));
        lopBuoiMap.put("Hóa 10C", new ArrayList<>(List.of(
                new BuoiHoc(1, LocalDate.of(2026, 5, 7), ""),
                new BuoiHoc(2, LocalDate.of(2026, 5, 14), ""),
                new BuoiHoc(3, LocalDate.of(2026, 5, 18), "Buổi hôm nay"))));
    }

    // ── Cell renderers ────────────────────────────────────────────────────────

    private static class StatusCellRenderer extends JLabel implements TableCellRenderer {
        StatusCellRenderer() {
            setOpaque(true);
            setFont(UiTheme.BODY_B);
        }

        @Override
        public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int r, int c) {
            String s = String.valueOf(val);
            setText("  " + s);
            int idx = Arrays.asList(TRANG_THAI).indexOf(s);
            if (!sel) {
                setBackground(idx >= 0 ? STATUS_BG[idx] : UiTheme.CARD_BG);
                setForeground(idx >= 0 ? STATUS_FG[idx] : UiTheme.TEXT_PRIMARY);
            } else {
                setBackground(t.getSelectionBackground());
                setForeground(t.getSelectionForeground());
            }
            return this;
        }
    }

    private static class BuoiCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object val, int idx, boolean sel, boolean foc) {
            super.getListCellRendererComponent(list, val, idx, sel, foc);
            setFont(UiTheme.BODY);
            setBorder(new EmptyBorder(6, 10, 6, 8));
            if (!sel) {
                setBackground(idx % 2 == 0 ? UiTheme.CARD_BG : UiTheme.ROW_ALT);
                setForeground(UiTheme.TEXT_PRIMARY);
            }
            return this;
        }
    }
}
