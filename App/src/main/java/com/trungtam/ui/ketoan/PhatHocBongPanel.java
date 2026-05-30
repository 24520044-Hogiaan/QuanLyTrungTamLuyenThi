package com.trungtam.ui.ketoan;

import com.trungtam.controller.HocBongController;
import com.trungtam.model.HocBong;
import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;
import com.trungtam.util.DatabaseConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.List;

public class PhatHocBongPanel extends JPanel {

    private final DefaultTableModel tableModel;
    private final HocBongController hocBongController = new HocBongController();
    private JTable table;

    private static final String[] COT = { "Mã HB", "Mã HV", "Giá Trị", "Ngày Cấp", "Trạng Thái" };

    public PhatHocBongPanel() {
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
        JLabel title = new JLabel("Phát Học Bổng");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.TEXT_PRIMARY);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.setOpaque(false);
        JButton payBtn = UiComponents.primaryButton("Chi trả", UiTheme.KETOAN);
        payBtn.addActionListener(e -> handleDisburse());
        buttons.add(payBtn);
        JButton refreshBtn = UiComponents.ghostButton("Làm mới");
        refreshBtn.addActionListener(e -> loadData());
        buttons.add(refreshBtn);
        panel.add(title, BorderLayout.WEST);
        panel.add(buttons, BorderLayout.EAST);
        return panel;
    }

    private void handleDisburse() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn học bổng cần chi trả.", "Chưa chọn", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = table.convertRowIndexToModel(viewRow);
        int maHocBong = (int) tableModel.getValueAt(modelRow, 0);
        int maHocVien = (int) tableModel.getValueAt(modelRow, 1);
        String trangThai = (String) tableModel.getValueAt(modelRow, 4);

        if (!"Chua nhan".equals(trangThai)) {
            JOptionPane.showMessageDialog(this,
                    "Học bổng này đã được phát trước đó.\nTrạng thái: " + trangThai,
                    "Không hợp lệ", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Xác nhận chi trả học bổng #" + maHocBong + " cho học viên #" + maHocVien + "?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            disburseScholarship(maHocBong, maHocVien);
            JOptionPane.showMessageDialog(this,
                    "Chi trả học bổng thành công!\nHóa đơn xuất quỹ đã được tạo.",
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
            loadData();
        } catch (Exception ex) {
            String errorMsg = ex.getMessage();
            if (errorMsg != null && errorMsg.contains("ORA-")) {
                errorMsg = errorMsg.substring(errorMsg.indexOf("ORA-"));
            }
            JOptionPane.showMessageDialog(this, "Lỗi: " + errorMsg, "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Tạo hóa đơn loại 'Hoc bong' - trigger TRG_HOCBONG_MARK_USED tự cập nhật trạng thái học bổng
    private void disburseScholarship(int maHocBong, int maHocVien) throws SQLException {
        String sqlGetInfo = "SELECT GIATRI FROM HOCBONG WHERE MAHOCBONG = ?";
        String sqlGetMaxId = "SELECT NVL(MAX(MAHOADON), 0) + 1 FROM HOADONHOCPHI";
        String sqlInsert = "INSERT INTO HOADONHOCPHI (MAHOADON, MAHOCVIEN, MALOP, NGAYLAP, TONGTIEN, " +
                           "TRANGTHAIHD, LOAIHD, HINHTHUC, MAHOCBONG) " +
                           "VALUES (?, ?, 1, SYSDATE, ?, 'Da thanh toan', 'Hoc bong', 'Tien mat', ?)";

        try (Connection con = DatabaseConnection.getConnection()) {
            double giaTri;
            try (PreparedStatement ps = con.prepareStatement(sqlGetInfo)) {
                ps.setInt(1, maHocBong);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) throw new SQLException("Không tìm thấy học bổng #" + maHocBong);
                    giaTri = rs.getDouble("GIATRI");
                }
            }

            int newInvoiceId;
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(sqlGetMaxId)) {
                rs.next();
                newInvoiceId = rs.getInt(1);
            }

            try (PreparedStatement ps = con.prepareStatement(sqlInsert)) {
                ps.setInt(1, newInvoiceId);
                ps.setInt(2, maHocVien);
                ps.setDouble(3, giaTri);
                ps.setInt(4, maHocBong);
                ps.executeUpdate();
            }

            con.commit();
        }
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<HocBong> list = hocBongController.layDanhSach();
        for (HocBong hb : list) {
            tableModel.addRow(new Object[]{
                    hb.getMaHocBong(), hb.getMaHocVien(),
                    String.format("%,.0f VNĐ", hb.getGiaTri()),
                    hb.getNgayCap(), hb.getTrangThaiHB()
            });
        }
    }
}
