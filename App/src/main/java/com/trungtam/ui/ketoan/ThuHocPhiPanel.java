package com.trungtam.ui.ketoan;

import com.trungtam.controller.HoaDonController;
import com.trungtam.model.HoaDonHocPhi;
import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class ThuHocPhiPanel extends JPanel {

    private final DefaultTableModel tableModel;
    private final TableRowSorter<DefaultTableModel> rowSorter;
    private final HoaDonController hoaDonController = new HoaDonController();
    private JTable table;

    private static final String[] COT = {
            "Mã HĐ", "Mã HV", "Mã Lớp", "Tổng Tiền", "Hình Thức TT", "Loại HĐ", "Trạng Thái"
    };

    public ThuHocPhiPanel() {
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M));
        setBackground(UiTheme.APP_BG);
        add(buildTopBar(), BorderLayout.NORTH);
        tableModel = new DefaultTableModel(COT, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        UiComponents.styleTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        rowSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(rowSorter);
        add(UiComponents.tableScroll(table), BorderLayout.CENTER);
        loadData();
    }

    private JPanel buildTopBar() {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setOpaque(false);
        JLabel title = new JLabel("Thu Học Phí & Lập Hóa Đơn");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.TEXT_PRIMARY);
        JTextField searchField = new JTextField(22);
        searchField.putClientProperty("JTextField.placeholderText", "Tìm theo tên học viên, lớp...");
        searchField.setFont(UiTheme.BODY);
        searchField.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) {
                String kw = searchField.getText().trim();
                rowSorter.setRowFilter(kw.isEmpty() ? null : RowFilter.regexFilter("(?i)" + kw));
            }
        });
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.setOpaque(false);
        JButton collectBtn = UiComponents.primaryButton("Thu phí", UiTheme.KETOAN);
        collectBtn.addActionListener(e -> handlePayment());
        buttons.add(collectBtn);
        JButton refreshBtn = UiComponents.ghostButton("Làm mới");
        refreshBtn.addActionListener(e -> loadData());
        buttons.add(refreshBtn);
        JPanel rightPanel = new JPanel(new BorderLayout(0, 6));
        rightPanel.setOpaque(false);
        rightPanel.add(searchField, BorderLayout.NORTH);
        rightPanel.add(buttons, BorderLayout.SOUTH);
        panel.add(title, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.EAST);
        return panel;
    }

    private void handlePayment() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn cần thu phí.", "Chưa chọn", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = table.convertRowIndexToModel(viewRow);
        int maHoaDon = (int) tableModel.getValueAt(modelRow, 0);
        String trangThai = (String) tableModel.getValueAt(modelRow, 6);

        if (!"Cho thanh toan".equals(trangThai)) {
            JOptionPane.showMessageDialog(this,
                    "Chỉ thu phí được hóa đơn ở trạng thái 'Cho thanh toan'.\nTrạng thái hiện tại: " + trangThai,
                    "Không hợp lệ", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] paymentMethods = { "Tien mat", "Chuyen khoan truc tiep" };
        String selectedMethod = (String) JOptionPane.showInputDialog(this,
                "Chọn hình thức thanh toán:", "Thu Phí - Hóa đơn #" + maHoaDon,
                JOptionPane.QUESTION_MESSAGE, null, paymentMethods, paymentMethods[0]);

        if (selectedMethod == null) return;

        try {
            hoaDonController.processPayment(maHoaDon, selectedMethod, 3);
            JOptionPane.showMessageDialog(this,
                    "Thu phí thành công!\nHóa đơn #" + maHoaDon + " đã được ghi nhận thanh toán.",
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
            loadData();
        } catch (Exception ex) {
            String errorMsg = ex.getMessage();
            if (errorMsg != null && errorMsg.contains("ORA-")) {
                errorMsg = errorMsg.substring(errorMsg.indexOf("ORA-"));
            }
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi thu phí: " + errorMsg,
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<HoaDonHocPhi> list = hoaDonController.getListHoaDon();
        for (HoaDonHocPhi hd : list) {
            tableModel.addRow(new Object[]{
                    hd.getMaHoaDon(), hd.getMaHocVien(), hd.getMaLop(),
                    String.format("%,.0f", hd.getTongTien()),
                    hd.getHinhThucTT(), hd.getLoaiHD(), hd.getTrangThaiHD()
            });
        }
    }
}
