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

        if (!"Cho duyet".equals(trangThai)) {
            JOptionPane.showMessageDialog(this,
                    "Chỉ xử lý được yêu cầu ở trạng thái 'Cho duyet'.\nTrạng thái hiện tại: " + trangThai,
                    "Không hợp lệ", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] options = { "Chấp thuận (APPROVE)", "Từ chối (REJECT)" };
        int choice = JOptionPane.showOptionDialog(this,
                "Chọn hành động cho yêu cầu hoàn trả #" + maHoanTra,
                "Xử lý hoàn trả", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);

        if (choice < 0) return;

        String approveOrReject = choice == 0 ? "APPROVE" : "REJECT";
        String note = JOptionPane.showInputDialog(this, "Ghi chú (tùy chọn):", "");

        try {
            hoanTraController.processRefund(maHoanTra, 3, approveOrReject, note);
            String resultMsg = choice == 0
                    ? "Đã chấp thuận hoàn trả #" + maHoanTra + ". Hóa đơn điều chỉnh đã được tạo."
                    : "Đã từ chối yêu cầu hoàn trả #" + maHoanTra + ".";
            JOptionPane.showMessageDialog(this, resultMsg, "Thành công", JOptionPane.INFORMATION_MESSAGE);
            loadData();
        } catch (Exception ex) {
            String errorMsg = ex.getMessage();
            if (errorMsg != null && errorMsg.contains("ORA-")) {
                errorMsg = errorMsg.substring(errorMsg.indexOf("ORA-"));
            }
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
