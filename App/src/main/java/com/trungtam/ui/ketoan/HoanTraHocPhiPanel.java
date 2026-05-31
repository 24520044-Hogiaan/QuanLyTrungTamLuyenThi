package com.trungtam.ui.ketoan;

import com.trungtam.controller.HoanTraController;
import com.trungtam.model.HoanTra;
import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HoanTraHocPhiPanel extends JPanel {

    private final DefaultTableModel tableModel;
    private final HoanTraController hoanTraController = new HoanTraController();
    private JTable table;

    private static final String[] COT = {
            "Mã HT", "Mã HV", "Mã Hóa Đơn", "Số Tiền", "Lý Do", "Hình Thức", "Trạng Thái"
    };

    public HoanTraHocPhiPanel() {
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
        add(UiComponents.tableScroll(table), BorderLayout.CENTER);
        loadData();
    }

    private JPanel buildTopBar() {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setOpaque(false);
        JLabel title = new JLabel("Hoàn Trả Học Phí");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.TEXT_PRIMARY);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.setOpaque(false);
        JButton processBtn = UiComponents.primaryButton("Xử lý hoàn trả", UiTheme.KETOAN);
        processBtn.addActionListener(e -> handleRefund());
        buttons.add(processBtn);
        JButton refreshBtn = UiComponents.ghostButton("Làm mới");
        refreshBtn.addActionListener(e -> loadData());
        buttons.add(refreshBtn);
        panel.add(title, BorderLayout.WEST);
        panel.add(buttons, BorderLayout.EAST);
        return panel;
    }

    private void handleRefund() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn yêu cầu hoàn trả.", "Chưa chọn", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = table.convertRowIndexToModel(viewRow);
        int maHoanTra = (int) tableModel.getValueAt(modelRow, 0);
        String trangThai = (String) tableModel.getValueAt(modelRow, 6);

        if (!"Chap thuan".equals(trangThai)) {
            JOptionPane.showMessageDialog(this,
                    "Kế toán chỉ thực hiện hoàn tiền cho yêu cầu đã được 'Chap thuan'.\nTrạng thái hiện tại: " + trangThai,
                    "Không hợp lệ", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int conf = JOptionPane.showConfirmDialog(this, 
            "Xác nhận hoàn tiền cho yêu cầu #" + maHoanTra + "?", 
            "Xác nhận", JOptionPane.YES_NO_OPTION);
            
        if (conf != JOptionPane.YES_OPTION) return;

        String note = JOptionPane.showInputDialog(this, "Ghi chú hoàn tiền (tùy chọn):", "");

        try {
            hoanTraController.completeRefund(maHoanTra, 3, note); // 3 is mock Ke Toan id
            JOptionPane.showMessageDialog(this, "Hoàn tiền thành công! Hóa đơn điều chỉnh âm đã được tạo.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            loadData();
        } catch (Exception ex) {
            String errorMsg = ex.getMessage();
            JOptionPane.showMessageDialog(this, "Lỗi: " + errorMsg, "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<HoanTra> list = hoanTraController.getListHoanTra();
        for (HoanTra ht : list) {
            tableModel.addRow(new Object[]{
                    ht.getMaHoanTra(), ht.getMaHocVien(), ht.getMaHoaDon(),
                    String.format("%,.0f VNĐ", ht.getSoTien()),
                    ht.getLyDo(), ht.getHinhThuc(), ht.getTrangThai()
            });
        }
    }
}
