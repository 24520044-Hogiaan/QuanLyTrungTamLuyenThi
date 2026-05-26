package com.trungtam.ui.quanly;

import com.trungtam.controller.BoMonController;
import com.trungtam.controller.GiaoVienController;
import com.trungtam.controller.KhoaHocController;
import com.trungtam.controller.LopHocController;
import com.trungtam.model.GiaoVien;
import com.trungtam.model.KhoaHoc;
import com.trungtam.model.LopHoc;
import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QuanLyLopHocPanel extends JPanel {

    private final LopHocController lopHocController = new LopHocController();
    private final KhoaHocController khoaHocController = new KhoaHocController();
    private final GiaoVienController giaoVienController = new GiaoVienController();
    private final BoMonController boMonController = new BoMonController();

    private JPanel cardContainer;
    private JTextField txtSearch;
    private JComboBox<String> cboMon;
    private JComboBox<String> cboCapDo;
    private CardLayout mainCardLayout;
    private JPanel mainPanel;

    private Map<Integer, String> khoaHocNameMap = new HashMap<>();
    private Map<Integer, String> giaoVienNameMap = new HashMap<>();
    private Map<Integer, Integer> khoaHocBoMonMap = new HashMap<>();
    private Map<Integer, String> khoaHocCapDoMap = new HashMap<>();
    private Map<Integer, String> boMonNameMap = new HashMap<>();

    private static final Color[] CARD_COLORS = {
            new Color(0x5D4037), new Color(0x2E7D32), new Color(0x6A1B9A),
            new Color(0xE65100), new Color(0x00838F), new Color(0xC62828),
            new Color(0x1565C0), new Color(0x827717)
    };
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public QuanLyLopHocPanel() {
        mainCardLayout = new CardLayout();
        mainPanel = new JPanel(mainCardLayout);
        mainPanel.setOpaque(false);

        setLayout(new BorderLayout());
        setBackground(UiTheme.APP_BG);

        mainPanel.add(buildListView(), "LIST");
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);

        loadMaps();
        loadData();
    }

    private void loadMaps() {
        khoaHocNameMap.clear();
        giaoVienNameMap.clear();
        khoaHocBoMonMap.clear();
        khoaHocCapDoMap.clear();
        boMonNameMap.clear();

        for (var bm : boMonController.getListBoMon()) {
            boMonNameMap.put(bm.getMaBoMon(), bm.getTenBoMon());
        }
        for (KhoaHoc kh : khoaHocController.layDanhSach()) {
            khoaHocNameMap.put(kh.getMaKhoaHoc(), kh.getTenKhoaHoc());
            khoaHocBoMonMap.put(kh.getMaKhoaHoc(), kh.getMaBoMon());
            khoaHocCapDoMap.put(kh.getMaKhoaHoc(), kh.getCapDo());
        }
        for (GiaoVien gv : giaoVienController.layDanhSach()) {
            giaoVienNameMap.put(gv.getMaGiaoVien(), "GV " + gv.getMaGiaoVien());
        }
    }

    private JPanel buildListView() {
        JPanel view = new JPanel(new BorderLayout(0, 12));
        view.setOpaque(false);
        view.setBorder(new EmptyBorder(UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M));

        view.add(buildTopBar(), BorderLayout.NORTH);
        view.add(buildFilterBar(), BorderLayout.CENTER);

        return view;
    }

    private JPanel buildTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        JLabel title = new JLabel("TRA CỨU LỚP HỌC");
        title.setFont(UiTheme.TITLE_M);
        title.setForeground(UiTheme.QUANLY);

        JButton btnThemLop = UiComponents.ghostButton("Thêm lớp học");
        btnThemLop.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Chức năng thêm lớp học đang phát triển.", "Thông báo", JOptionPane.INFORMATION_MESSAGE));

        topBar.add(title, BorderLayout.WEST);
        topBar.add(btnThemLop, BorderLayout.EAST);
        return topBar;
    }

    private JPanel buildFilterBar() {
        JPanel wrapper = new JPanel(new BorderLayout(0, 12));
        wrapper.setOpaque(false);

        JPanel filterRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 4));
        filterRow.setOpaque(false);
        filterRow.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.CARD_BORDER, 1),
                new EmptyBorder(8, 12, 8, 12)));
        filterRow.setBackground(UiTheme.CARD_BG);

        JLabel lblSearch = new JLabel("Tìm kiếm:");
        lblSearch.setFont(UiTheme.BODY);
        txtSearch = new JTextField(18);
        txtSearch.putClientProperty("JTextField.placeholderText", "Tìm lớp học...");
        txtSearch.setFont(UiTheme.BODY);
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override public void keyReleased(java.awt.event.KeyEvent e) { filterCards(); }
        });

        JLabel lblMon = new JLabel("Môn:");
        lblMon.setFont(UiTheme.BODY);
        cboMon = new JComboBox<>();
        cboMon.setFont(UiTheme.BODY);
        cboMon.addItem("Tất cả môn");
        for (var bm : boMonController.getListBoMon()) {
            cboMon.addItem(bm.getTenBoMon());
        }
        cboMon.addActionListener(e -> filterCards());

        JLabel lblCapDo = new JLabel("Cấp độ:");
        lblCapDo.setFont(UiTheme.BODY);
        cboCapDo = new JComboBox<>(new String[]{"Tất cả cấp độ", "Dai tra", "Nang cao"});
        cboCapDo.setFont(UiTheme.BODY);
        cboCapDo.addActionListener(e -> filterCards());

        filterRow.add(lblSearch);
        filterRow.add(txtSearch);
        filterRow.add(lblMon);
        filterRow.add(cboMon);
        filterRow.add(lblCapDo);
        filterRow.add(cboCapDo);

        cardContainer = new JPanel(new GridLayout(0, 3, 16, 16));
        cardContainer.setOpaque(false);
        
        JPanel cardWrapper = new JPanel(new BorderLayout());
        cardWrapper.setOpaque(false);
        cardWrapper.add(cardContainer, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(cardWrapper,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        wrapper.add(filterRow, BorderLayout.NORTH);
        wrapper.add(scrollPane, BorderLayout.CENTER);
        return wrapper;
    }

    private void loadData() {
        cardContainer.removeAll();
        List<LopHoc> list = lopHocController.layDanhSach();
        for (int i = 0; i < list.size(); i++) {
            cardContainer.add(buildClassCard(list.get(i), i));
        }
        cardContainer.revalidate();
        cardContainer.repaint();
    }

    private void filterCards() {
        String keyword = txtSearch.getText().trim().toLowerCase();
        String selectedMon = (String) cboMon.getSelectedItem();
        String selectedCapDo = (String) cboCapDo.getSelectedItem();

        cardContainer.removeAll();
        List<LopHoc> list = lopHocController.layDanhSach();

        List<LopHoc> filtered = list.stream().filter(lh -> {
            if (!keyword.isEmpty()) {
                String tenLop = lh.getTenLop() != null ? lh.getTenLop().toLowerCase() : "";
                if (!tenLop.contains(keyword)) return false;
            }
            if (!"Tất cả môn".equals(selectedMon)) {
                Integer maBM = khoaHocBoMonMap.get(lh.getMaKhoaHoc());
                String tenBM = maBM != null ? boMonNameMap.get(maBM) : null;
                if (tenBM == null || !tenBM.equals(selectedMon)) return false;
            }
            if (!"Tất cả cấp độ".equals(selectedCapDo)) {
                String capDo = khoaHocCapDoMap.get(lh.getMaKhoaHoc());
                if (capDo == null || !capDo.equalsIgnoreCase(selectedCapDo)) return false;
            }
            return true;
        }).collect(Collectors.toList());

        for (int i = 0; i < filtered.size(); i++) {
            cardContainer.add(buildClassCard(filtered.get(i), i));
        }
        cardContainer.revalidate();
        cardContainer.repaint();
    }

    private JPanel buildClassCard(LopHoc lh, int index) {
        Color cardColor = CARD_COLORS[index % CARD_COLORS.length];

        JPanel card = new JPanel(new BorderLayout(0, 0));
        card.setBackground(UiTheme.CARD_BG);
        card.setBorder(BorderFactory.createLineBorder(UiTheme.CARD_BORDER, 1));

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(cardColor);
        header.setPreferredSize(new Dimension(0, 40));
        JLabel headerLabel = new JLabel("Lớp học " + lh.getMaLopHoc());
        headerLabel.setFont(UiTheme.BODY_B);
        headerLabel.setForeground(Color.WHITE);
        header.add(headerLabel);

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(UiTheme.CARD_BG);
        body.setBorder(new EmptyBorder(12, 14, 8, 14));

        JLabel lblTenLop = new JLabel(lh.getTenLop());
        lblTenLop.setFont(UiTheme.TITLE_S);
        lblTenLop.setForeground(UiTheme.TEXT_PRIMARY);
        lblTenLop.setAlignmentX(LEFT_ALIGNMENT);

        String gvName = giaoVienNameMap.getOrDefault(lh.getMaGiaoVien(), "Chưa phân công");
        JLabel lblGV = new JLabel("GV: " + gvName);
        lblGV.setFont(UiTheme.BODY);
        lblGV.setForeground(UiTheme.TEXT_SECONDARY);
        lblGV.setAlignmentX(LEFT_ALIGNMENT);

        String tanSuat = lh.getTanSuat() != null ? lh.getTanSuat() : "";
        JLabel lblSiSo = new JLabel(tanSuat + "  •  Sĩ số: " + lh.getSiSo());
        lblSiSo.setFont(UiTheme.CAPTION);
        lblSiSo.setForeground(UiTheme.TEXT_MUTED);
        lblSiSo.setAlignmentX(LEFT_ALIGNMENT);

        String ngayBD = lh.getNgayBatDau() != null ? lh.getNgayBatDau().format(DATE_FMT) : "?";
        String ngayKT = lh.getNgayKetThuc() != null ? lh.getNgayKetThuc().format(DATE_FMT) : "?";
        JLabel lblDate = new JLabel(ngayBD + " - " + ngayKT);
        lblDate.setFont(UiTheme.CAPTION);
        lblDate.setForeground(UiTheme.TEXT_MUTED);
        lblDate.setAlignmentX(LEFT_ALIGNMENT);

        body.add(lblTenLop);
        body.add(Box.createVerticalStrut(4));
        body.add(lblGV);
        body.add(Box.createVerticalStrut(4));
        body.add(lblSiSo);
        body.add(Box.createVerticalStrut(2));
        body.add(lblDate);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        footer.setBackground(UiTheme.CARD_BG);

        String capDo = khoaHocCapDoMap.getOrDefault(lh.getMaKhoaHoc(), "");
        String capDoDisplay = "Nang cao".equalsIgnoreCase(capDo) ? "Nâng cao" : "Trung cấp";
        JLabel badgeCapDo = new JLabel(capDoDisplay);
        badgeCapDo.setFont(UiTheme.CAPTION);
        badgeCapDo.setForeground(cardColor);
        badgeCapDo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(cardColor, 1),
                new EmptyBorder(2, 8, 2, 8)));

        JButton btnChiTiet = buildOutlineButton("Chi tiết", UiTheme.PRIMARY);
        btnChiTiet.addActionListener(e -> showDetail(lh));

        JButton btnSua = buildOutlineButton("Sửa", UiTheme.WARNING);
        btnSua.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Chức năng sửa lớp học đang phát triển.", "Thông báo", JOptionPane.INFORMATION_MESSAGE));

        JButton btnXoa = buildOutlineButton("Xóa", UiTheme.DANGER);
        btnXoa.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Chức năng xóa lớp học đang phát triển.", "Thông báo", JOptionPane.INFORMATION_MESSAGE));

        footer.add(badgeCapDo);
        footer.add(Box.createHorizontalStrut(16));
        footer.add(btnChiTiet);
        footer.add(btnSua);
        footer.add(btnXoa);

        card.add(header, BorderLayout.NORTH);
        card.add(body, BorderLayout.CENTER);
        card.add(footer, BorderLayout.SOUTH);
        return card;
    }

    private JButton buildOutlineButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(UiTheme.CAPTION);
        btn.setForeground(color);
        btn.setBackground(UiTheme.CARD_BG);
        btn.setBorder(BorderFactory.createLineBorder(color, 1));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        return btn;
    }

    private void showDetail(LopHoc lh) {
        JPanel detailPanel = buildDetailPanel(lh);
        String key = "DETAIL_" + lh.getMaLopHoc();
        mainPanel.add(detailPanel, key);
        mainCardLayout.show(mainPanel, key);
    }

    private JPanel buildDetailPanel(LopHoc lh) {
        JPanel detail = new JPanel(new BorderLayout(0, 0));
        detail.setBackground(UiTheme.APP_BG);
        detail.setBorder(new EmptyBorder(UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 12, 0));

        JLabel lblTitle = new JLabel("CHI TIẾT LỚP HỌC");
        lblTitle.setFont(UiTheme.TITLE_M);
        lblTitle.setForeground(UiTheme.TEXT_PRIMARY);

        String khoaHocName = khoaHocNameMap.getOrDefault(lh.getMaKhoaHoc(), "Khóa " + lh.getMaKhoaHoc());
        String gvName = giaoVienNameMap.getOrDefault(lh.getMaGiaoVien(), "Chưa phân công");
        JLabel lblSub = new JLabel("Lớp: " + lh.getTenLop() + "  •  Khóa học: " + khoaHocName + "  •  Giảng viên: " + gvName);
        lblSub.setFont(UiTheme.CAPTION);
        lblSub.setForeground(UiTheme.TEXT_MUTED);

        JPanel titleBlock = new JPanel(new GridLayout(2, 1, 0, 2));
        titleBlock.setOpaque(false);
        titleBlock.add(lblTitle);
        titleBlock.add(lblSub);
        header.add(titleBlock, BorderLayout.CENTER);

        JPanel content = new JPanel(new BorderLayout(0, 0));
        content.setOpaque(false);

        JPanel sideNav = new JPanel();
        sideNav.setLayout(new BoxLayout(sideNav, BoxLayout.Y_AXIS));
        sideNav.setBackground(UiTheme.CARD_BG);
        sideNav.setBorder(new EmptyBorder(8, 0, 8, 0));
        sideNav.setPreferredSize(new Dimension(160, 0));

        JLabel lblLopHeader = new JLabel("LỚP: " + lh.getTenLop().toUpperCase());
        lblLopHeader.setFont(UiTheme.CAPTION);
        lblLopHeader.setForeground(UiTheme.TEXT_MUTED);
        lblLopHeader.setBorder(new EmptyBorder(8, 12, 12, 12));
        lblLopHeader.setAlignmentX(LEFT_ALIGNMENT);
        sideNav.add(lblLopHeader);

        CardLayout detailCards = new CardLayout();
        JPanel detailContent = new JPanel(detailCards);
        detailContent.setOpaque(false);

        detailContent.add(buildThongTinChungPanel(lh), "TTC");
        detailContent.add(buildHocVienTabPlaceholder(), "HV");
        detailContent.add(buildPlaceholderTab("Lịch Học"), "LH");
        detailContent.add(buildPlaceholderTab("Kiểm Tra"), "KT");

        String[] tabNames = {"Thông Tin Chung", "Học Viên", "Lịch Học", "Kiểm Tra"};
        String[] tabKeys = {"TTC", "HV", "LH", "KT"};
        JButton[] navButtons = new JButton[tabNames.length];

        for (int i = 0; i < tabNames.length; i++) {
            final int idx = i;
            JButton btn = new JButton(tabNames[i]);
            btn.setFont(UiTheme.BODY);
            btn.setFocusPainted(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            btn.setAlignmentX(LEFT_ALIGNMENT);
            btn.addActionListener(e -> {
                detailCards.show(detailContent, tabKeys[idx]);
                for (JButton b : navButtons) {
                    b.setBackground(UiTheme.CARD_BG);
                    b.setForeground(UiTheme.TEXT_PRIMARY);
                }
                btn.setBackground(UiTheme.PRIMARY);
                btn.setForeground(Color.WHITE);
            });
            navButtons[i] = btn;
            sideNav.add(btn);
        }

        navButtons[0].setBackground(UiTheme.PRIMARY);
        navButtons[0].setForeground(Color.WHITE);

        JButton btnBack = new JButton("← Quay lại");
        btnBack.setFont(UiTheme.BODY_B);
        btnBack.setFocusPainted(false);
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBack.setAlignmentX(LEFT_ALIGNMENT);
        btnBack.addActionListener(e -> mainCardLayout.show(mainPanel, "LIST"));

        sideNav.add(Box.createVerticalGlue());
        sideNav.add(btnBack);

        content.add(sideNav, BorderLayout.WEST);
        content.add(detailContent, BorderLayout.CENTER);

        detail.add(header, BorderLayout.NORTH);
        detail.add(content, BorderLayout.CENTER);
        return detail;
    }

    private JPanel buildThongTinChungPanel(LopHoc lh) {
        JPanel panel = new JPanel(new GridLayout(1, 2, 16, 0));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        String khoaHocName = khoaHocNameMap.getOrDefault(lh.getMaKhoaHoc(), "Khóa " + lh.getMaKhoaHoc());
        String gvName = giaoVienNameMap.getOrDefault(lh.getMaGiaoVien(), "Chưa phân công");
        String ngayBD = lh.getNgayBatDau() != null ? lh.getNgayBatDau().format(DATE_FMT) : "?";
        String ngayKT = lh.getNgayKetThuc() != null ? lh.getNgayKetThuc().format(DATE_FMT) : "?";

        JPanel leftCard = buildInfoCard("Thông Tin Lớp Học", UiTheme.PRIMARY,
                new String[]{"Tên lớp:", "Khóa học:", "Giáo viên:", "Sĩ số:", "Trạng thái:", "Phòng học:"},
                new String[]{lh.getTenLop(), khoaHocName, gvName,
                        lh.getSiSo() + " Học viên",
                        lh.getTrangThai() != null ? lh.getTrangThai() : "",
                        "Chưa cập nhật"});

        JPanel rightCard = buildInfoCard("Thời Gian & Lịch Học", UiTheme.PRIMARY,
                new String[]{"Tần suất:", "Ngày bắt đầu:", "Ngày kết thúc:"},
                new String[]{lh.getTanSuat() != null ? lh.getTanSuat() : "", ngayBD, ngayKT});

        panel.add(leftCard);
        panel.add(rightCard);
        return panel;
    }

    private JPanel buildInfoCard(String title, Color accentColor, String[] labels, String[] values) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(UiTheme.CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.CARD_BORDER, 1),
                new EmptyBorder(16, 20, 16, 20)));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(UiTheme.TITLE_S);
        lblTitle.setForeground(accentColor);
        lblTitle.setBorder(new EmptyBorder(0, 0, 12, 0));

        JPanel rows = new JPanel(new GridLayout(labels.length, 2, 8, 20));
        rows.setOpaque(false);
        for (int i = 0; i < labels.length; i++) {
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(UiTheme.BODY_B);
            lbl.setForeground(UiTheme.TEXT_PRIMARY);
            JLabel val = new JLabel(values[i]);
            val.setFont(UiTheme.BODY);
            val.setForeground(UiTheme.TEXT_SECONDARY);
            val.setHorizontalAlignment(SwingConstants.RIGHT);
            rows.add(lbl);
            rows.add(val);
        }

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(rows, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildHocVienTabPlaceholder() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel lbl = new JLabel("Danh sách học viên đăng ký lớp");
        lbl.setFont(UiTheme.BODY);
        lbl.setForeground(UiTheme.TEXT_MUTED);
        panel.add(lbl, BorderLayout.NORTH);
        return panel;
    }

    private JPanel buildPlaceholderTab(String name) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel lbl = new JLabel("Chức năng " + name + " đang phát triển.");
        lbl.setFont(UiTheme.BODY);
        lbl.setForeground(UiTheme.TEXT_MUTED);
        panel.add(lbl, BorderLayout.CENTER);
        return panel;
    }
}
