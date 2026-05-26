package com.quanlytrungtam.view.LopHoc;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.quanlytrungtam.controller.LopHocController;
import com.quanlytrungtam.dao.LopHocDAO;
import com.quanlytrungtam.model.LopHoc;
import com.quanlytrungtam.ui.UiTheme;
import com.quanlytrungtam.view.Refreshable;
import com.toedter.calendar.JDateChooser;

/**
 * Tra cứu khóa học/lớp học — dạng lưới thẻ (card grid).
 */
public class TraCuuLopHocPanel extends JPanel implements Refreshable {
        private static final Color[] SUBJECT_COLORS = {
                        new Color(0x1565C0), new Color(0x2E7D32), new Color(0x6A1B9A),
                        new Color(0xBF360C), new Color(0x00838F), new Color(0xAD1457)
        };
        private static final String[] CAPDO_DISPLAY = { "Tất cả cấp độ", "Cơ bản", "Trung cấp", "Nâng cao" };
        private static final String[] CAPDO_DB = { "", "Co ban", "Trung cap", "Nang cao" };

        private final JPanel cardGrid = new JPanel(new GridLayout(0, 3, 14, 14));
        private final JTextField txtSearch = new JTextField(18);
        private final JComboBox<String> cboMon = new JComboBox<>(new String[] { "Tất cả môn" });
        private final JComboBox<String> cboCap = new JComboBox<>(CAPDO_DISPLAY);
        private LopHocMainPanel parent;
        private LopHocDAO lopHocDAO = new LopHocDAO();
        private Object[][] lopHocData = new Object[0][];

        public TraCuuLopHocPanel(LopHocMainPanel parent) {
                this.parent = parent;
                setLayout(new BorderLayout(0, 12));
                setBorder(new EmptyBorder(UiTheme.PAD_L, UiTheme.PAD_L, UiTheme.PAD_L, UiTheme.PAD_L));
                setBackground(UiTheme.APP_BG);

                JLabel title = new JLabel("TRA CỨU LỚP HỌC");
                title.setFont(UiTheme.TITLE_M);
                title.setForeground(UiTheme.SECONDARY);
                // add(title, BorderLayout.NORTH);

                JButton btnAdd = new JButton("Thêm lớp học");
                btnAdd.setFont(UiTheme.BODY_B);
                btnAdd.addActionListener(e -> showAddDialog());

                JPanel topPanel = new JPanel(new BorderLayout());
                topPanel.setOpaque(false);
                topPanel.add(title, BorderLayout.WEST);
                topPanel.add(btnAdd, BorderLayout.EAST);
                add(topPanel, BorderLayout.NORTH);

                JScrollPane scroll = new JScrollPane(buildCardArea(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                scroll.getVerticalScrollBar().setUnitIncrement(20);
                scroll.setBorder(BorderFactory.createEmptyBorder());

                JPanel center = new JPanel(new BorderLayout(0, 8));
                center.setOpaque(false);
                center.add(buildFilterBar(), BorderLayout.NORTH);
                center.add(scroll, BorderLayout.CENTER);
                add(center, BorderLayout.CENTER);

                loadDataFromDatabase();
                renderCards(lopHocData);
        }

        @Override
        public void refresh() {
                if (txtSearch != null) {
                        txtSearch.setText("");
                }
                if (cboMon != null) {
                        cboMon.setSelectedIndex(0);
                }
                if (cboCap != null) {
                        cboCap.setSelectedIndex(0);
                }
                loadDataFromDatabase();
                applyFilter();
        }

        private void loadDataFromDatabase() {
                List<LopHoc> list = lopHocDAO.getListLopHoc();
                LopHocController ctrl = new LopHocController();
                java.util.Map<Integer, Integer> countsMap = ctrl.getMapSiSoHocVien();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                lopHocData = new Object[list.size()][];
                for (int i = 0; i < list.size(); i++) {
                        LopHoc lh = list.get(i);
                        int colorIndex = i % SUBJECT_COLORS.length;
                        int siSoHocVien = countsMap.getOrDefault(lh.getMaLopHoc(), 0);
                        lopHocData[i] = new Object[] {
                                        lh.getMaLopHoc(), lh.getTenLop(), "Lớp học " + lh.getMaLopHoc(),
                                        "GV " + lh.getMaGiaoVien(), lh.getTansuat(), "Trung cap",
                                        sdf.format(lh.getNgayBd()), sdf.format(lh.getNgayKt()), lh.getSiso(),
                                        lh.getTrangThai(), colorIndex, siSoHocVien
                        };
                }
        }

        private JPanel buildFilterBar() {
                JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
                bar.setBackground(UiTheme.CARD_BG);
                bar.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(UiTheme.SECONDARY_FG),
                                new EmptyBorder(6, 10, 6, 10)));
                txtSearch.putClientProperty("JTextField.placeholderText", "Tìm lớp học...");
                txtSearch.setFont(UiTheme.BODY);
                txtSearch.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyReleased(KeyEvent e) {
                                applyFilter();
                        }
                });
                cboMon.setFont(UiTheme.BODY);
                cboCap.setFont(UiTheme.BODY);
                cboMon.addActionListener(e -> applyFilter());
                cboCap.addActionListener(e -> applyFilter());

                JLabel lblSearch = new JLabel("Tìm kiếm:");
                lblSearch.setFont(UiTheme.BODY);
                JLabel lblMon = new JLabel("Môn:");
                lblMon.setFont(UiTheme.BODY);
                JLabel lblCap = new JLabel("Cấp độ:");
                lblCap.setFont(UiTheme.BODY);
                bar.add(lblSearch);
                bar.add(txtSearch);
                bar.add(lblMon);
                bar.add(cboMon);
                bar.add(lblCap);
                bar.add(cboCap);
                return bar;
        }

        private JPanel buildCardArea() {
                JPanel wrapper = new JPanel(new BorderLayout());
                wrapper.setOpaque(false);
                wrapper.setBorder(new EmptyBorder(4, 0, 4, 0));
                cardGrid.setOpaque(false);
                cardGrid.setBorder(new EmptyBorder(4, 4, 4, 4));
                wrapper.add(cardGrid, BorderLayout.NORTH);
                return wrapper;
        }

        private String getCapDoDb(int comboIndex) {
                return CAPDO_DB[comboIndex];
        }

        private void applyFilter() {
                String kw = txtSearch.getText().trim().toLowerCase();
                String mon = (String) cboMon.getSelectedItem();
                String capDb = getCapDoDb(cboCap.getSelectedIndex());
                List<Object[]> filtered = new ArrayList<>();
                for (Object[] row : lopHocData) {
                        String tenKH = (String) row[2], capDo = (String) row[5], tenLop = (String) row[1],
                                        gvHoTen = (String) row[3];
                        boolean okMon = "Tất cả môn".equals(mon) || tenKH.equals(mon);
                        boolean okCap = capDb.isEmpty() || capDo.equals(capDb);
                        boolean okKw = kw.isEmpty() || tenLop.toLowerCase().contains(kw)
                                        || gvHoTen.toLowerCase().contains(kw);
                        if (okMon && okCap && okKw)
                                filtered.add(row);
                }
                renderCards(filtered.toArray(new Object[0][]));
        }

        private void renderCards(Object[][] data) {
                cardGrid.removeAll();
                for (Object[] row : data)
                        cardGrid.add(buildCard(row));
                cardGrid.revalidate();
                cardGrid.repaint();
        }

        private String capDoDisplay(String dbVal) {
                switch (dbVal) {
                        case "Co ban":
                                return "Cơ bản";
                        case "Trung cap":
                                return "Trung cấp";
                        case "Nang cao":
                                return "Nâng cao";
                        default:
                                return dbVal;
                }
        }

        private JPanel buildCard(Object[] row) {
                int maLopHoc = (int) row[0];
                String tenLop = (String) row[1], tenKH = (String) row[2], gvHoTen = (String) row[3],
                                tanSuat = (String) row[4], capDo = (String) row[5], ngayBD = (String) row[6],
                                ngayKT = (String) row[7], trangThai = (String) row[9];
                int siSo = (int) row[8];
                int siSoHocVien = (int) row[11];
                Color color = SUBJECT_COLORS[(int) row[10]];

                JPanel card = new JPanel(new BorderLayout());
                card.setBackground(UiTheme.CARD_BG);
                card.setBorder(BorderFactory.createLineBorder(UiTheme.CARD_BORDER, 1));

                JPanel header = new JPanel(new BorderLayout());
                header.setBackground(color);
                header.setPreferredSize(new Dimension(0, 64));
                header.setBorder(new EmptyBorder(0, UiTheme.PAD_M, 0, UiTheme.PAD_M));
                JLabel lblMon = new JLabel(tenKH);
                lblMon.setFont(UiTheme.BODY_B);
                lblMon.setForeground(new Color(255, 255, 255, 200));
                header.add(lblMon, BorderLayout.CENTER);
                card.add(header, BorderLayout.NORTH);

                JPanel body = new JPanel();
                body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
                body.setBackground(UiTheme.CARD_BG);
                body.setBorder(new EmptyBorder(12, UiTheme.PAD_M, 12, UiTheme.PAD_M));

                JLabel lblTen = new JLabel(tenLop);
                lblTen.setFont(UiTheme.TITLE_S);
                lblTen.setForeground(UiTheme.TEXT_PRIMARY);
                JLabel lblGV = new JLabel("GV: " + gvHoTen);
                lblGV.setFont(UiTheme.BODY);
                lblGV.setForeground(UiTheme.TEXT_SECONDARY);
                JLabel lblTanSuat = new JLabel(tanSuat + "  •  Sĩ số: " + siSoHocVien + "/" + siSo);
                lblTanSuat.setFont(UiTheme.CAPTION);
                lblTanSuat.setForeground(UiTheme.TEXT_MUTED);
                JLabel lblDate = new JLabel(ngayBD + " - " + ngayKT);
                lblDate.setFont(UiTheme.CAPTION);
                lblDate.setForeground(UiTheme.TEXT_MUTED);

                body.add(lblTen);
                body.add(Box.createVerticalStrut(4));
                body.add(lblGV);
                body.add(Box.createVerticalStrut(4));
                body.add(lblTanSuat);
                body.add(Box.createVerticalStrut(8));
                body.add(lblDate);
                body.add(Box.createVerticalStrut(10));

                JPanel footer = new JPanel(new BorderLayout(8, 0));
                footer.setOpaque(false);
                JLabel badge = new JLabel("  " + capDoDisplay(capDo) + "  ");
                badge.setFont(UiTheme.CAPTION);
                badge.setOpaque(true);
                badge.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
                badge.setForeground(color);
                badge.setBorder(BorderFactory.createLineBorder(color, 1));

                JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
                actionPanel.setOpaque(false);
                JButton btnEdit = new JButton("Sửa");
                btnEdit.setFont(UiTheme.BODY_B);
                btnEdit.setFocusPainted(false);
                btnEdit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                btnEdit.setBackground(Color.WHITE);
                btnEdit.setForeground(color);
                btnEdit.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(color),
                                new EmptyBorder(4, 12, 4, 12)));
                btnEdit.addActionListener(e -> {
                        JTextField txtTenLop = new JTextField(tenLop), txtTanSuat = new JTextField(tanSuat),
                                        txtSiSo = new JTextField(String.valueOf(siSo));
                        JComboBox<String> cboTrangThai = new JComboBox<>(
                                        new String[] { "Dang mo", "Da ket thuc", "Tam dung" });
                        cboTrangThai.setSelectedItem(trangThai);
                        JPanel panel = new JPanel(new GridLayout(0, 1, 6, 6));
                        panel.add(new JLabel("Tên lớp:"));
                        panel.add(txtTenLop);
                        panel.add(new JLabel("Tần suất:"));
                        panel.add(txtTanSuat);
                        panel.add(new JLabel("Sĩ số:"));
                        panel.add(txtSiSo);
                        panel.add(new JLabel("Trạng thái:"));
                        panel.add(cboTrangThai);
                        if (JOptionPane.showConfirmDialog(this, panel, "Sửa lớp học", JOptionPane.OK_CANCEL_OPTION,
                                        JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
                                try {
                                        LopHoc lh = new LopHoc();
                                        lh.setMaLopHoc(maLopHoc);
                                        lh.setTenLop(txtTenLop.getText());
                                        lh.setTansuat(txtTanSuat.getText());
                                        lh.setSiso(Integer.parseInt(txtSiSo.getText()));
                                        lh.setTrangThai((String) cboTrangThai.getSelectedItem());
                                        lh.setMaKhoaHoc(maLopHoc);
                                        lh.setMaGiaoVien(maLopHoc);
                                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                        lh.setNgayBd(sdf.parse(ngayBD));
                                        lh.setNgayKt(sdf.parse(ngayKT));
                                        if (lopHocDAO.edit(lh)) {
                                                loadDataFromDatabase();
                                                applyFilter();
                                                JOptionPane.showMessageDialog(this, "Cập nhật lớp học thành công!");
                                        } else
                                                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
                                } catch (Exception ex) {
                                        ex.printStackTrace();
                                        JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
                                }
                        }
                });

                Color deleteColor = new Color(220, 53, 69);
                JButton btnDelete = new JButton("Xóa");
                btnDelete.setFont(UiTheme.BODY_B);
                btnDelete.setFocusPainted(false);
                btnDelete.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                btnDelete.setBackground(Color.WHITE);
                btnDelete.setForeground(deleteColor);
                btnDelete.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(deleteColor),
                                new EmptyBorder(4, 12, 4, 12)));

                btnDelete.addActionListener(e -> {
                        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa lớp học này?",
                                        "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

                        if (confirm == JOptionPane.YES_OPTION) {
                                try {
                                        LopHoc lh = new LopHoc();
                                        lh.setMaLopHoc(maLopHoc);

                                        if (lopHocDAO.delete(lh)) {
                                                loadDataFromDatabase();
                                                applyFilter();
                                                JOptionPane.showMessageDialog(this, "Xóa lớp học thành công!");
                                        } else {
                                                JOptionPane.showMessageDialog(this, "Xóa thất bại!");
                                        }
                                } catch (Exception ex) {
                                        ex.printStackTrace();
                                        JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
                                }
                        }
                });

                JButton btnDetail = new JButton("Chi tiết");
                btnDetail.setFont(UiTheme.BODY_B);
                btnDetail.setFocusPainted(false);
                btnDetail.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                btnDetail.setBackground(Color.WHITE);
                btnDetail.setForeground(color);
                btnDetail.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(color),
                                new EmptyBorder(4, 12, 4, 12)));
                btnDetail.addActionListener(e -> parent.showDetailPanel(maLopHoc, tenLop, tenKH, gvHoTen, tanSuat,
                                capDo, ngayBD, ngayKT, siSo, trangThai));
                actionPanel.add(btnDetail);
                actionPanel.add(btnEdit);
                actionPanel.add(btnDelete);
                footer.add(badge, BorderLayout.WEST);
                footer.add(actionPanel, BorderLayout.EAST);
                body.add(footer);
                card.add(body, BorderLayout.CENTER);

                card.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) {
                                card.setBorder(BorderFactory.createLineBorder(color, 1));
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                                card.setBorder(BorderFactory.createLineBorder(UiTheme.CARD_BORDER, 1));
                        }
                });
                return card;
        }

        private void showAddDialog() {
                JTextField txtMaLop = new JTextField(), txtTenLop = new JTextField(), txtMaKhoaHoc = new JTextField();
                JTextField txtMaGV = new JTextField(), txtTanSuat = new JTextField(), txtSiSo = new JTextField();
                JDateChooser dcNgayBD = new JDateChooser(), dcNgayKT = new JDateChooser();
                dcNgayBD.setDateFormatString("dd/MM/yyyy");
                dcNgayKT.setDateFormatString("dd/MM/yyyy");

                JComboBox<String> cboTrangThai = new JComboBox<>(new String[] { "Dang mo", "Da ket thuc", "Tam dung" });

                JPanel panel = new JPanel(new GridLayout(0, 1, 6, 6));
                panel.add(new JLabel("Mã lớp học:"));
                panel.add(txtMaLop);
                panel.add(new JLabel("Tên lớp:"));
                panel.add(txtTenLop);
                panel.add(new JLabel("Mã khóa học:"));
                panel.add(txtMaKhoaHoc);
                panel.add(new JLabel("Mã giáo viên:"));
                panel.add(txtMaGV);
                panel.add(new JLabel("Ngày bắt đầu:"));
                panel.add(dcNgayBD);
                panel.add(new JLabel("Ngày kết thúc:"));
                panel.add(dcNgayKT);
                panel.add(new JLabel("Tần suất:"));
                panel.add(txtTanSuat);
                panel.add(new JLabel("Sĩ số:"));
                panel.add(txtSiSo);
                panel.add(new JLabel("Trạng thái:"));
                panel.add(cboTrangThai);

                int result = JOptionPane.showConfirmDialog(this, panel, "Thêm lớp học",
                                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                        try {
                                if (dcNgayBD.getDate() == null || dcNgayKT.getDate() == null) {
                                        JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày!");
                                        return;
                                }

                                LopHoc lh = new LopHoc();
                                lh.setMaLopHoc(Integer.parseInt(txtMaLop.getText()));
                                lh.setTenLop(txtTenLop.getText());
                                lh.setMaKhoaHoc(Integer.parseInt(txtMaKhoaHoc.getText()));
                                lh.setMaGiaoVien(Integer.parseInt(txtMaGV.getText()));
                                lh.setNgayBd(dcNgayBD.getDate());
                                lh.setNgayKt(dcNgayKT.getDate());
                                lh.setTansuat(txtTanSuat.getText());
                                lh.setSiso(Integer.parseInt(txtSiSo.getText()));
                                lh.setTrangThai((String) cboTrangThai.getSelectedItem());

                                if (lopHocDAO.add(lh)) {
                                        loadDataFromDatabase();
                                        applyFilter();
                                        JOptionPane.showMessageDialog(this, "Thêm lớp học thành công!");
                                } else {
                                        JOptionPane.showMessageDialog(this, "Thêm lớp học thất bại!");
                                }
                        } catch (Exception ex) {
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
                        }
                }
        }

}