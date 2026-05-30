package com.trungtam.ui.hocvien;

import com.trungtam.controller.DangKyController;
import com.trungtam.controller.GiaoVienController;
import com.trungtam.controller.KhoaHocController;
import com.trungtam.controller.LopHocController;
import com.trungtam.model.DangKy;
import com.trungtam.model.GiaoVien;
import com.trungtam.model.KhoaHoc;
import com.trungtam.model.LopHoc;
import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tra cứu khóa học/lớp học — dữ liệu từ database, kiểm tra slot trước khi đăng ký.
 */
public class TraCuuKhoaHocPanel extends JPanel {

    private static final Color[] SUBJECT_COLORS = {
            new Color(0x1565C0), new Color(0x2E7D32), new Color(0x6A1B9A),
            new Color(0xBF360C), new Color(0x00838F), new Color(0xAD1457)
    };

    private final int maHocVien;
    private final LopHocController lopHocController = new LopHocController();
    private final KhoaHocController khoaHocController = new KhoaHocController();
    private final DangKyController dangKyController = new DangKyController();
    private final GiaoVienController giaoVienController = new GiaoVienController();

    private final JPanel cardGrid = new JPanel(new GridLayout(0, 3, 14, 14));
    private final JTextField txtSearch = new JTextField(18);
    private final JComboBox<String> cboCap = new JComboBox<>();

    private List<LopHocInfo> allLopHoc = new ArrayList<>();

    public TraCuuKhoaHocPanel(int maHocVien) {
        this.maHocVien = maHocVien;
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(UiTheme.PAD_L, UiTheme.PAD_L, UiTheme.PAD_L, UiTheme.PAD_L));
        setBackground(UiTheme.APP_BG);

        JLabel title = new JLabel("TRA CỨU & ĐĂNG KÝ LỚP HỌC");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.SECONDARY);
        add(title, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(buildCardArea(),
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        JPanel center = new JPanel(new BorderLayout(0, 8));
        center.setOpaque(false);
        center.add(buildFilterBar(), BorderLayout.NORTH);
        center.add(scroll, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        loadData();
    }

    private void loadData() {
        allLopHoc.clear();

        List<LopHoc> lopList = lopHocController.layDanhSach();
        List<KhoaHoc> khoaHocList = khoaHocController.layDanhSach();
        List<GiaoVien> gvList = giaoVienController.layDanhSach();

        Map<Integer, KhoaHoc> khoaHocMap = new HashMap<>();
        for (KhoaHoc kh : khoaHocList) khoaHocMap.put(kh.getMaKhoaHoc(), kh);

        Map<Integer, GiaoVien> gvMap = new HashMap<>();
        for (GiaoVien gv : gvList) gvMap.put(gv.getMaGiaoVien(), gv);

        // Populate cboCap
        cboCap.removeAllItems();
        cboCap.addItem("Tất cả cấp độ");
        java.util.Set<String> capDoSet = new java.util.LinkedHashSet<>();
        for (KhoaHoc kh : khoaHocList) {
            if (kh.getCapDo() != null) capDoSet.add(kh.getCapDo());
        }
        for (String cd : capDoSet) cboCap.addItem(cd);

        for (LopHoc lop : lopList) {
            // Only show classes that are open for registration
            if (!"Dang mo".equalsIgnoreCase(lop.getTrangThai())) continue;

            KhoaHoc kh = khoaHocMap.get(lop.getMaKhoaHoc());
            GiaoVien gv = gvMap.get(lop.getMaGiaoVien());

            LopHocInfo info = new LopHocInfo();
            info.maLopHoc = lop.getMaLopHoc();
            info.tenLop = lop.getTenLop();
            info.tenKhoaHoc = kh != null ? kh.getTenKhoaHoc() : "";
            info.capDo = kh != null ? kh.getCapDo() : "";
            info.tenGiaoVien = gv != null ? gv.getHoTen() : "Chưa phân công";
            info.tanSuat = lop.getTanSuat() != null ? lop.getTanSuat() : "";
            info.siSoMax = lop.getSiSo();
            info.siSoHienTai = dangKyController.countByLop(lop.getMaLopHoc());
            allLopHoc.add(info);
        }

        renderCards(allLopHoc);
    }

    private JPanel buildFilterBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        bar.setBackground(UiTheme.CARD_BG);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.SECONDARY_FG),
                new EmptyBorder(6, 10, 6, 10)));

        txtSearch.putClientProperty("JTextField.placeholderText", "Tìm lớp, giảng viên...");
        txtSearch.setFont(UiTheme.BODY);
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                applyFilter();
            }
        });

        cboCap.setFont(UiTheme.BODY);
        cboCap.addActionListener(e -> applyFilter());

        JLabel lblSearch = new JLabel("Tìm kiếm:");
        lblSearch.setFont(UiTheme.BODY);
        JLabel lblCap = new JLabel("Cấp độ:");
        lblCap.setFont(UiTheme.BODY);

        JButton btnRefresh = UiComponents.ghostButton("Làm mới");
        btnRefresh.addActionListener(e -> loadData());

        bar.add(lblSearch);
        bar.add(txtSearch);
        bar.add(lblCap);
        bar.add(cboCap);
        bar.add(btnRefresh);
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

    private void applyFilter() {
        String kw = txtSearch.getText().trim().toLowerCase();
        String cap = (String) cboCap.getSelectedItem();

        List<LopHocInfo> filtered = new ArrayList<>();
        for (LopHocInfo info : allLopHoc) {
            boolean okCap = "Tất cả cấp độ".equals(cap) || (info.capDo != null && info.capDo.equals(cap));
            boolean okKw = kw.isEmpty()
                    || info.tenLop.toLowerCase().contains(kw)
                    || info.tenGiaoVien.toLowerCase().contains(kw)
                    || info.tenKhoaHoc.toLowerCase().contains(kw);
            if (okCap && okKw) filtered.add(info);
        }
        renderCards(filtered);
    }

    private void renderCards(List<LopHocInfo> data) {
        cardGrid.removeAll();
        for (LopHocInfo info : data)
            cardGrid.add(buildCard(info));
        cardGrid.revalidate();
        cardGrid.repaint();
    }

    private JPanel buildCard(LopHocInfo info) {
        Color color = SUBJECT_COLORS[Math.abs(info.tenKhoaHoc.hashCode()) % SUBJECT_COLORS.length];
        boolean conSlot = info.siSoHienTai < info.siSoMax;

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(UiTheme.CARD_BG);
        card.setBorder(BorderFactory.createLineBorder(UiTheme.CARD_BORDER, 1));

        // Coloured header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(color);
        header.setPreferredSize(new Dimension(0, 64));
        header.setBorder(new EmptyBorder(0, UiTheme.PAD_M, 0, UiTheme.PAD_M));
        JLabel lblMon = new JLabel(info.tenKhoaHoc);
        lblMon.setFont(UiTheme.BODY_B);
        lblMon.setForeground(new Color(255, 255, 255, 200));
        header.add(lblMon, BorderLayout.CENTER);
        card.add(header, BorderLayout.NORTH);

        // Body
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(UiTheme.CARD_BG);
        body.setBorder(new EmptyBorder(12, UiTheme.PAD_M, 12, UiTheme.PAD_M));

        JLabel lblTen = new JLabel(info.tenLop);
        lblTen.setFont(UiTheme.TITLE_S);
        lblTen.setForeground(UiTheme.TEXT_PRIMARY);
        lblTen.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lblGV = new JLabel(info.tenGiaoVien);
        lblGV.setFont(UiTheme.BODY);
        lblGV.setForeground(UiTheme.TEXT_SECONDARY);
        lblGV.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lblInfo = new JLabel(info.tanSuat + " | Sĩ số: " + info.siSoHienTai + "/" + info.siSoMax);
        lblInfo.setFont(UiTheme.CAPTION);
        lblInfo.setForeground(conSlot ? UiTheme.TEXT_MUTED : UiTheme.DANGER);
        lblInfo.setAlignmentX(LEFT_ALIGNMENT);

        body.add(lblTen);
        body.add(Box.createVerticalStrut(4));
        body.add(lblGV);
        body.add(Box.createVerticalStrut(4));
        body.add(lblInfo);
        body.add(Box.createVerticalStrut(10));

        // Badge + button
        JPanel footer = new JPanel(new BorderLayout(8, 0));
        footer.setOpaque(false);

        String capDoText = info.capDo != null ? info.capDo : "";
        JLabel badge = new JLabel("  " + capDoText + "  ");
        badge.setFont(UiTheme.CAPTION);
        badge.setOpaque(true);
        badge.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
        badge.setForeground(color);
        badge.setBorder(BorderFactory.createLineBorder(color, 1));

        JButton btnDK;
        if (!conSlot) {
            btnDK = UiComponents.ghostButton("Hết chỗ");
            btnDK.setEnabled(false);
        } else {
            btnDK = UiComponents.primaryButton("Đăng ký", color);
            btnDK.setFont(UiTheme.BODY_B);
            btnDK.addActionListener(e -> handleDangKy(info));
        }

        footer.add(badge, BorderLayout.WEST);
        footer.add(btnDK, BorderLayout.EAST);
        footer.setAlignmentX(LEFT_ALIGNMENT);
        body.add(footer);

        card.add(body, BorderLayout.CENTER);

        // Hover border
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

    private void handleDangKy(LopHocInfo info) {
        if (maHocVien <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Tài khoản của bạn chưa được liên kết với hồ sơ Học Viên!\n" +
                    "Vui lòng liên hệ Quản lý trung tâm để cập nhật hồ sơ trước khi đăng ký.",
                    "Không thể đăng ký", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if already registered
        if (dangKyController.existsDangKy(maHocVien, info.maLopHoc)) {
            JOptionPane.showMessageDialog(this,
                    "Bạn đã đăng ký lớp này rồi.",
                    "Không thể đăng ký", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Re-check slot availability
        int currentCount = dangKyController.countByLop(info.maLopHoc);
        if (currentCount >= info.siSoMax) {
            JOptionPane.showMessageDialog(this,
                    "Lớp đã đầy, không còn chỗ trống.",
                    "Không thể đăng ký", JOptionPane.WARNING_MESSAGE);
            loadData(); // refresh
            return;
        }

        JPanel confirmPanel = new JPanel(new GridLayout(3, 1, 0, 5));
        confirmPanel.add(new JLabel("Xác nhận đăng ký lớp: " + info.tenLop + "?"));
        confirmPanel.add(new JLabel("Vui lòng chọn hình thức đóng học phí:"));
        
        JComboBox<String> cboHinhThucTT = new JComboBox<>(new String[]{"1 thang", "Toan khoa"});
        confirmPanel.add(cboHinhThucTT);

        int confirm = JOptionPane.showConfirmDialog(this,
                confirmPanel,
                "Xác Nhận Đăng Ký", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirm != JOptionPane.OK_OPTION) return;

        DangKy dk = new DangKy();
        dk.setMaHocVien(maHocVien);
        dk.setMaLopHoc(info.maLopHoc);
        dk.setHinhThucTT((String) cboHinhThucTT.getSelectedItem());

        boolean ok = dangKyController.insertDangKy(dk);
        if (ok) {
            JOptionPane.showMessageDialog(this,
                    "Đăng ký lớp " + info.tenLop + " thành công!\n" +
                    "Vui lòng thanh toán học phí tại mục Thanh Toán.",
                    "Đăng Ký Thành Công", JOptionPane.INFORMATION_MESSAGE);
            loadData(); // refresh to update slot count
        } else {
            JOptionPane.showMessageDialog(this,
                    "Đăng ký thất bại. Vui lòng thử lại.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Helper class to hold display info
    private static class LopHocInfo {
        int maLopHoc;
        String tenLop;
        String tenKhoaHoc;
        String capDo;
        String tenGiaoVien;
        String tanSuat;
        int siSoMax;
        int siSoHienTai;
    }
}
