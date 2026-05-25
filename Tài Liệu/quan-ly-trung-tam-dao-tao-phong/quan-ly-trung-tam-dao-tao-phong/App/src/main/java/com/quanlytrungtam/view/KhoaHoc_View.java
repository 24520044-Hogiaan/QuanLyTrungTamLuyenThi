package com.quanlytrungtam.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;

import com.quanlytrungtam.controller.KhoaHocController;
import com.quanlytrungtam.model.KhoaHoc;
import com.quanlytrungtam.ui.UiComponents;
import com.quanlytrungtam.ui.UiTheme;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class KhoaHoc_View extends JPanel implements Refreshable {

    private final DefaultTableModel tableModel;
    private final JTable table;
    private final TableRowSorter<DefaultTableModel> rowSorter;
    private List<KhoaHoc> khoaHocList = new ArrayList<>();
    private JTextField searchField;
    private KhoaHocController khoaHocController = new KhoaHocController();
    private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("#,##0 VNĐ");

    private static final String[] COT = {
            "Mã KH", "Tên Khóa Học", "Mô Tả", "Hình thức học phí", "Học Phí", "Mã Bộ Môn", "Cấp Độ"
    };

    public KhoaHoc_View() {
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M));
        setBackground(UiTheme.APP_BG);

        add(buildTopBar(), BorderLayout.NORTH);

        tableModel = new DefaultTableModel(COT, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table = new JTable(tableModel);
        UiComponents.styleTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultRenderer(Object.class, new KhoaHocCellRenderer());

        rowSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(rowSorter);

        int[] widths = { 80, 180, 220, 130, 130, 100, 110 };
        for (int i = 0; i < widths.length && i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        UiComponents.setColumnAlignments(table,
                SwingConstants.CENTER,
                SwingConstants.LEFT,
                SwingConstants.LEFT,
                SwingConstants.RIGHT,
                SwingConstants.RIGHT,
                SwingConstants.CENTER,
                SwingConstants.CENTER);

        add(UiComponents.tableScroll(table), BorderLayout.CENTER);
        add(buildBottomBar(), BorderLayout.SOUTH);

        refreshTable();
    }

    private JPanel buildTopBar() {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setOpaque(false);

        JLabel title = new JLabel("Danh Sách Khóa Học");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.TEXT_PRIMARY);

        searchField = new JTextField(25);
        searchField.putClientProperty("JTextField.placeholderText", "Tìm kiếm...");
        searchField.setFont(UiTheme.BODY);
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String kw = searchField.getText().trim();
                rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + kw, 0));
            }
        });

        panel.add(title, BorderLayout.WEST);
        panel.add(searchField, BorderLayout.EAST);
        return panel;
    }

    private JPanel buildBottomBar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panel.setOpaque(false);

        JButton btnThem = UiComponents.primaryButton("Thêm", UiTheme.SUCCESS);
        JButton btnSua = UiComponents.primaryButton("Sửa", UiTheme.INFO);
        JButton btnXoa = UiComponents.primaryButton("Xóa", UiTheme.DANGER);
        JButton btnLamMoi = UiComponents.primaryButton("Làm Mới", UiTheme.PRIMARY);

        btnThem.addActionListener(e -> addKhoaHoc());
        btnSua.addActionListener(e -> editKhoaHoc());
        btnXoa.addActionListener(e -> deleteKhoaHoc());
        btnLamMoi.addActionListener(e -> refreshKhoaHocList());

        panel.add(btnThem);
        panel.add(btnSua);
        panel.add(btnXoa);
        panel.add(btnLamMoi);

        return panel;
    }

    private void addKhoaHoc() {
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        KhoaHocDialog dialog = new KhoaHocDialog(parentFrame, null);
        dialog.setVisible(true);
        KhoaHoc result = dialog.getResult();
        if (result != null) {
            int newId = 1;
            for (KhoaHoc kh : khoaHocList) {
                if (kh.getMakhoahoc() >= newId) {
                    newId = kh.getMakhoahoc() + 1;
                }
            }
            result.setMakhoahoc(newId);
            khoaHocController.addKhoaHoc(result);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Thêm khóa học mới thành công!", "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void editKhoaHoc() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một khóa học từ bảng để sửa!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = table.convertRowIndexToModel(selectedRow);
        int maKhoaHoc = (Integer) tableModel.getValueAt(modelRow, 0);

        KhoaHoc existing = null;
        for (KhoaHoc kh : khoaHocList) {
            if (kh.getMakhoahoc() == maKhoaHoc) {
                existing = kh;
                break;
            }
        }

        if (existing == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin khóa học!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        KhoaHocDialog dialog = new KhoaHocDialog(parentFrame, existing);
        dialog.setVisible(true);
        KhoaHoc result = dialog.getResult();
        if (result != null) {
            khoaHocController.editKhoaHoc(result);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Cập nhật thông tin khóa học thành công!", "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void deleteKhoaHoc() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một khóa học từ bảng để xóa!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = table.convertRowIndexToModel(selectedRow);
        int maKhoaHoc = (int) tableModel.getValueAt(modelRow, 0);

        KhoaHoc existing = null;
        for (KhoaHoc kh : khoaHocList) {
            if (kh.getMakhoahoc() == maKhoaHoc) {
                existing = kh;
                break;
            }
        }

        if (existing == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin khóa học!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xóa khóa học \"" + existing.getTenkh() + "\" không?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            khoaHocController.deleteKhoaHoc(existing);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Đã xóa khóa học thành công!", "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    public void refresh() {
        if (searchField != null) {
            searchField.setText("");
        }
        rowSorter.setRowFilter(null);
        refreshTable();
    }

    private void refreshKhoaHocList() {
        refresh();
        JOptionPane.showMessageDialog(this, "Danh sách khóa học đã được cập nhật!", "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        khoaHocList = khoaHocController.getListKhoaHoc();
        for (KhoaHoc kh : khoaHocList) {
            double hpThang = kh.getHocphithang() != null ? kh.getHocphithang() : 0.0;
            double hpToanKh = kh.getHocphitoankh() != null ? kh.getHocphitoankh() : 0.0;

            boolean isToanKhoa = hpToanKh > 0 && hpThang <= 0;
            String hinhThuc = isToanKhoa ? "Đóng toàn khóa" : "Đóng theo tháng";
            double hocPhi = isToanKhoa ? hpToanKh : hpThang;

            tableModel.addRow(new Object[] {
                    kh.getMakhoahoc(),
                    kh.getTenkh(),
                    kh.getMota(),
                    hinhThuc,
                    CURRENCY_FORMAT.format(hocPhi),
                    kh.getMabomon(),
                    kh.getCapdo()
            });
        }
    }

    private static class KhoaHocCellRenderer extends UiComponents.AltRowRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable t, Object val, boolean sel, boolean foc, int r, int c) {

            Object formattedVal = val;
            if (val instanceof Double && (c == 3 || c == 4)) {
                formattedVal = CURRENCY_FORMAT.format((Double) val);
            }

            super.getTableCellRendererComponent(t, formattedVal, sel, foc, r, c);

            if (!sel && c == 6) {
                String capdo = String.valueOf(val).toLowerCase();
                if (capdo.contains("cao") || capdo.contains("advanced")) {
                    setForeground(UiTheme.DANGER);
                    setFont(UiTheme.BODY_B);
                } else if (capdo.contains("trung") || capdo.contains("intermediate")) {
                    setForeground(UiTheme.INFO);
                    setFont(UiTheme.BODY_B);
                } else {
                    setForeground(UiTheme.SUCCESS);
                    setFont(UiTheme.BODY_B);
                }
            } else if (!sel) {
                setFont(UiTheme.BODY);
            }
            return this;
        }
    }
}