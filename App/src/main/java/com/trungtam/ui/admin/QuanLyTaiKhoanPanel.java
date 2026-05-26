package com.trungtam.ui.admin;

import com.trungtam.controller.VaiTroController;
import com.trungtam.model.TaiKhoan;
import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;
import com.trungtam.util.DatabaseConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuanLyTaiKhoanPanel extends JPanel {

    private final DefaultTableModel tableModel;
    private final JTable table;
    private final TableRowSorter<DefaultTableModel> rowSorter;
    private final VaiTroController vaiTroController = new VaiTroController();

    private static final String[] COT = {
            "Mã TK", "Tên Tài Khoản", "Vai Trò", "Trạng Thái", "Ngày Tạo", "Đăng Nhập Cuối"
    };

    public QuanLyTaiKhoanPanel() {
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
        rowSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(rowSorter);
        int[] widths = { 60, 180, 140, 120, 150, 150 };
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
        UiComponents.setColumnAlignments(table,
                SwingConstants.CENTER, SwingConstants.LEFT, SwingConstants.CENTER,
                SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.CENTER);
        add(UiComponents.tableScroll(table), BorderLayout.CENTER);
        loadData();
    }

    private JPanel buildTopBar() {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setOpaque(false);
        JLabel title = new JLabel("Quản Lý Tài Khoản");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.TEXT_PRIMARY);
        JTextField searchField = new JTextField(22);
        searchField.putClientProperty("JTextField.placeholderText", "Tìm theo tên tài khoản, vai trò...");
        searchField.setFont(UiTheme.BODY);
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String kw = searchField.getText().trim();
                rowSorter.setRowFilter(kw.isEmpty() ? null : RowFilter.regexFilter("(?i)" + kw));
            }
        });
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.setOpaque(false);
        buttons.add(UiComponents.primaryButton("Thêm mới", UiTheme.ADMIN));
        JButton editBtn = UiComponents.ghostButton("Sửa");
        editBtn.addActionListener(e -> {
            if (table.getSelectedRow() < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản cần sửa.");
                return;
            }
            JOptionPane.showMessageDialog(this, "Chức năng sửa tài khoản — đang phát triển.");
        });
        buttons.add(editBtn);
        JButton delBtn = UiComponents.ghostButton("Vô hiệu hóa");
        delBtn.setForeground(UiTheme.DANGER);
        delBtn.addActionListener(e -> {
            if (table.getSelectedRow() < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản cần vô hiệu hóa.");
                return;
            }
            JOptionPane.showMessageDialog(this, "Chức năng vô hiệu hóa — đang phát triển.");
        });
        buttons.add(delBtn);
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

    private void loadData() {
        tableModel.setRowCount(0);
        String sql = "SELECT MATAIKHOAN, TENTAIKHOAN, MAVAITRO, TRANGTHAITK, NGAYTAO, LANDN_CUOI FROM TAIKHOAN";
        try (Connection con = DatabaseConnection.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                int maVaiTro = rs.getInt("MAVAITRO");
                String tenVaiTro = vaiTroController.getTenVaiTro(maVaiTro);
                if (tenVaiTro == null)
                    tenVaiTro = String.valueOf(maVaiTro);
                Timestamp ngayTao = rs.getTimestamp("NGAYTAO");
                Timestamp lanDN = rs.getTimestamp("LANDN_CUOI");
                tableModel.addRow(new Object[] {
                        rs.getInt("MATAIKHOAN"),
                        rs.getString("TENTAIKHOAN"),
                        tenVaiTro,
                        rs.getString("TRANGTHAITK"),
                        ngayTao != null ? ngayTao.toLocalDateTime().toLocalDate() : "—",
                        lanDN != null ? lanDN.toLocalDateTime().toLocalDate() : "—"
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
