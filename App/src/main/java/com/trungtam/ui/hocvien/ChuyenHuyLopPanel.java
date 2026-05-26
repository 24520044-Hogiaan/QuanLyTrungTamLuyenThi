package com.trungtam.ui.hocvien;

import com.trungtam.controller.DangKyController;
import com.trungtam.controller.LopHocController;
import com.trungtam.controller.YeuCauChuyenLopController;
import com.trungtam.model.DangKy;
import com.trungtam.model.LopHoc;
import com.trungtam.model.YeuCauChuyenLop;
import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Chuyển lớp / Hủy lớp — dữ liệu từ database.
 */
public class ChuyenHuyLopPanel extends JPanel {

    private final int maHocVien;
    private final DangKyController dangKyController = new DangKyController();
    private final LopHocController lopHocController = new LopHocController();
    private final YeuCauChuyenLopController ycController = new YeuCauChuyenLopController();

    private JComboBox<String> cboHien;
    private JComboBox<String> cboMoi;
    private JComboBox<String> cboLopHuy;
    private Map<String, Integer> tenLopToMa = new HashMap<>();

    public ChuyenHuyLopPanel(int maHocVien) {
        this.maHocVien = maHocVien;
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(UiTheme.PAD_L, UiTheme.PAD_L, UiTheme.PAD_L, UiTheme.PAD_L));
        setBackground(UiTheme.APP_BG);

        JLabel title = new JLabel("CHUYỂN / HỦY LỚP HỌC");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.SECONDARY);
        add(title, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(UiTheme.BODY_B);
        tabs.addTab("  Chuyển Lớp  ", buildChuyenLopTab());
        tabs.addTab("  Hủy Lớp  ", buildHuyLopTab());
        add(tabs, BorderLayout.CENTER);
    }

    private void loadComboData() {
        // Load all classes
        List<LopHoc> allLop = lopHocController.layDanhSach();
        tenLopToMa.clear();
        for (LopHoc lop : allLop) {
            tenLopToMa.put(lop.getTenLop(), lop.getMaLopHoc());
        }

        // Get student's registered classes
        List<DangKy> myDK = dangKyController.getDangKyByHocVien(maHocVien);

        // Populate "lớp hiện tại" combo with registered classes
        if (cboHien != null) {
            cboHien.removeAllItems();
            for (DangKy dk : myDK) {
                if ("Dang hoc".equalsIgnoreCase(dk.getTrangThaiDKY()) || "Cho thanh toan".equalsIgnoreCase(dk.getTrangThaiDKY())) {
                    for (LopHoc lop : allLop) {
                        if (lop.getMaLopHoc() == dk.getMaLopHoc()) {
                            cboHien.addItem(lop.getTenLop());
                            break;
                        }
                    }
                }
            }
        }

        if (cboLopHuy != null) {
            cboLopHuy.removeAllItems();
            for (DangKy dk : myDK) {
                if ("Dang hoc".equalsIgnoreCase(dk.getTrangThaiDKY()) || "Cho thanh toan".equalsIgnoreCase(dk.getTrangThaiDKY())) {
                    for (LopHoc lop : allLop) {
                        if (lop.getMaLopHoc() == dk.getMaLopHoc()) {
                            cboLopHuy.addItem(lop.getTenLop());
                            break;
                        }
                    }
                }
            }
        }

        // Populate "lớp mới" combo with open classes
        if (cboMoi != null) {
            cboMoi.removeAllItems();
            for (LopHoc lop : allLop) {
                if ("Dang mo".equalsIgnoreCase(lop.getTrangThai())) {
                    cboMoi.addItem(lop.getTenLop());
                }
            }
        }
    }

    private JPanel buildChuyenLopTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBorder(new EmptyBorder(UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M));
        panel.setBackground(UiTheme.APP_BG);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(8, 4, 8, 8);

        cboHien = new JComboBox<>();
        cboMoi = new JComboBox<>();
        JTextArea txtLyDo = new JTextArea(3, 0);
        txtLyDo.setLineWrap(true);
        txtLyDo.setWrapStyleWord(true);
        txtLyDo.setFont(UiTheme.BODY);
        cboHien.setFont(UiTheme.BODY);
        cboMoi.setFont(UiTheme.BODY);

        addFormRow(form, g, 0, "Lớp hiện tại:", cboHien);
        addFormRow(form, g, 1, "Lớp muốn chuyển sang:", cboMoi);
        g.gridx = 0;
        g.gridy = 2;
        g.weightx = 0;
        g.anchor = GridBagConstraints.NORTHWEST;
        JLabel lblLyDo = new JLabel("Lý do chuyển:");
        lblLyDo.setFont(UiTheme.BODY);
        form.add(lblLyDo, g);
        g.gridx = 1;
        g.weightx = 1;
        g.anchor = GridBagConstraints.CENTER;
        form.add(new JScrollPane(txtLyDo), g);

        panel.add(infoLabel("Lưu ý: Chỉ được chuyển lớp trong 7 ngày đầu sau khi đăng ký."),
                BorderLayout.NORTH);
        panel.add(form, BorderLayout.CENTER);

        JButton btn = UiComponents.primaryButton("Gửi Yêu Cầu Chuyển Lớp", UiTheme.WARNING);
        btn.addActionListener(e -> {
            if (cboHien.getSelectedItem() == null || cboMoi.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(panel, "Vui lòng chọn lớp.", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (cboHien.getSelectedItem().equals(cboMoi.getSelectedItem())) {
                JOptionPane.showMessageDialog(panel, "Lớp cũ và lớp mới không được trùng nhau.", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            String lyDo = txtLyDo.getText().trim();
            if (lyDo.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Vui lòng nhập lý do chuyển lớp.", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String tenLopCu = (String) cboHien.getSelectedItem();
            String tenLopMoi = (String) cboMoi.getSelectedItem();
            Integer maLopCu = tenLopToMa.get(tenLopCu);
            Integer maLopMoi = tenLopToMa.get(tenLopMoi);

            if (maLopCu == null || maLopMoi == null) {
                JOptionPane.showMessageDialog(panel, "Không tìm thấy mã lớp.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            YeuCauChuyenLop yc = new YeuCauChuyenLop();
            yc.setMaHocVien(maHocVien);
            yc.setMaLopCu(maLopCu);
            yc.setMaLopMoi(maLopMoi);
            yc.setLyDo(lyDo);

            boolean ok = ycController.guiYeuCau(yc);
            if (ok) {
                JOptionPane.showMessageDialog(panel,
                        "Đã gửi yêu cầu chuyển từ [" + tenLopCu + "] sang [" + tenLopMoi + "].\nHệ thống sẽ xử lý trong 24h.",
                        "Thành Công", JOptionPane.INFORMATION_MESSAGE);
                txtLyDo.setText("");
            } else {
                JOptionPane.showMessageDialog(panel, "Gửi yêu cầu thất bại. Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(buildBtnBar(btn), BorderLayout.SOUTH);

        loadComboData();
        return panel;
    }

    private JPanel buildHuyLopTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBorder(new EmptyBorder(UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M));
        panel.setBackground(UiTheme.APP_BG);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(8, 4, 8, 8);

        cboLopHuy = new JComboBox<>();
        JTextArea txtLyDo = new JTextArea(3, 0);
        txtLyDo.setLineWrap(true);
        txtLyDo.setWrapStyleWord(true);
        txtLyDo.setFont(UiTheme.BODY);
        cboLopHuy.setFont(UiTheme.BODY);

        addFormRow(form, g, 0, "Lớp muốn hủy:", cboLopHuy);
        g.gridx = 0;
        g.gridy = 1;
        g.weightx = 0;
        g.anchor = GridBagConstraints.NORTHWEST;
        JLabel lblLyDo = new JLabel("Lý do hủy:");
        lblLyDo.setFont(UiTheme.BODY);
        form.add(lblLyDo, g);
        g.gridx = 1;
        g.weightx = 1;
        g.anchor = GridBagConstraints.CENTER;
        form.add(new JScrollPane(txtLyDo), g);

        JLabel warning = new JLabel("<html><b>Lưu ý chính sách hoàn phí:</b><br>"
                + "- Hủy trong 7 ngày đầu: hoàn 100% học phí<br>"
                + "- Sau 7 ngày đến 1 tháng: hoàn 50%<br>"
                + "- Sau 1 tháng: không hoàn phí</html>");
        warning.setFont(UiTheme.BODY);
        warning.setForeground(UiTheme.DANGER);
        warning.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xEF9A9A)),
                new EmptyBorder(10, 12, 10, 12)));
        warning.setOpaque(true);
        warning.setBackground(new Color(0xFFEBEE));

        panel.add(warning, BorderLayout.NORTH);
        panel.add(form, BorderLayout.CENTER);

        JButton btn = UiComponents.primaryButton("Xác Nhận Hủy Lớp", UiTheme.DANGER);
        btn.addActionListener(e -> {
            if (cboLopHuy.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(panel, "Vui lòng chọn lớp muốn hủy.", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(panel,
                    "Bạn chắc chắn muốn hủy lớp: " + cboLopHuy.getSelectedItem() + "?",
                    "Xác Nhận Hủy Lớp", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(panel,
                        "Đã gửi yêu cầu hủy lớp. Hệ thống sẽ xử lý trong 24h.",
                        "Thành Công", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        panel.add(buildBtnBar(btn), BorderLayout.SOUTH);

        loadComboData();
        return panel;
    }

    private JPanel buildBtnBar(JButton btn) {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 6));
        bar.setOpaque(false);
        bar.add(btn);
        return bar;
    }

    private void addFormRow(JPanel form, GridBagConstraints g, int row, String label, JComponent field) {
        g.gridx = 0;
        g.gridy = row;
        g.weightx = 0.35;
        g.anchor = GridBagConstraints.WEST;
        JLabel lbl = new JLabel(label);
        lbl.setFont(UiTheme.BODY);
        form.add(lbl, g);
        g.gridx = 1;
        g.weightx = 0.65;
        g.anchor = GridBagConstraints.CENTER;
        form.add(field, g);
    }

    private JLabel infoLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UiTheme.CAPTION_I);
        l.setForeground(UiTheme.TEXT_MUTED);
        return l;
    }
}
