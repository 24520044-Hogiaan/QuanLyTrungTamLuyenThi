package com.trungtam.ui.quanly;

import com.trungtam.controller.BoMonController;
import com.trungtam.controller.GiaoVienController;
import com.trungtam.controller.KhoaHocController;
import com.trungtam.controller.LopHocController;
import com.trungtam.model.GiaoVien;
import com.trungtam.model.KhoaHoc;
import com.trungtam.model.LopHoc;
import com.trungtam.model.LichHoc;
import com.trungtam.model.HocVien;
import com.trungtam.dao.LichHocDAO;
import com.trungtam.controller.HocVienController;
import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import com.trungtam.controller.DangKyController;
import com.trungtam.model.DangKy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Trigger IDE rebuild
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
        btnThemLop.addActionListener(e -> {
            LopHocDialog dialog = new LopHocDialog(SwingUtilities.getWindowAncestor(this), null, khoaHocController.layDanhSach(), giaoVienController.layDanhSach());
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                if (lopHocController.themLopHoc(dialog.getLopHoc())) {
                    JOptionPane.showMessageDialog(this, "Thêm lớp học thành công!");
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm lớp học thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

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
        btnSua.addActionListener(e -> {
            LopHocDialog dialog = new LopHocDialog(SwingUtilities.getWindowAncestor(this), lh, khoaHocController.layDanhSach(), giaoVienController.layDanhSach());
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                if (lopHocController.capNhatLopHoc(dialog.getLopHoc())) {
                    JOptionPane.showMessageDialog(this, "Cập nhật lớp học thành công!");
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật lớp học thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton btnXoa = buildOutlineButton("Xóa", UiTheme.DANGER);
        btnXoa.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa lớp học này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                if (lopHocController.xoaLopHoc(lh.getMaLopHoc())) {
                    JOptionPane.showMessageDialog(this, "Xóa lớp học thành công!");
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa lớp học thất bại! (Có thể do có học viên đã đăng ký)", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

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
        detailContent.add(buildHocVienTab(lh), "HV");
        detailContent.add(buildLichHocTab(lh, detailContent), "LH");

        String[] tabNames = {"Thông Tin Chung", "Học Viên", "Lịch Học"};
        String[] tabKeys = {"TTC", "HV", "LH"};
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
        JPanel wrapper = new JPanel(new GridLayout(1, 2, 16, 16));
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(16, 16, 16, 16));

        // Left Column
        JPanel leftCol = new JPanel(new BorderLayout(0, 16));
        leftCol.setOpaque(false);

        JPanel card1 = buildGridCard("Thông Tin Lớp Học", 
                new String[]{"Tên lớp :", "Khóa học :", "Giảng viên :"},
                new String[]{lh.getTenLop() != null ? lh.getTenLop() : "", "Lớp học " + lh.getMaKhoaHoc(), "GV " + lh.getMaGiaoVien()});
        
        HocVienController hvController = new HocVienController();
        int currentStudents = hvController.layDanhSachTheoLop(lh.getMaLopHoc()).size();
        JPanel card2 = buildGridCard(null, 
                new String[]{"Sĩ số :", "Trạng thái :", "Phòng học :"},
                new String[]{currentStudents + " Học viên", lh.getTrangThai() != null ? lh.getTrangThai() : "", "Phòng 302"});

        leftCol.add(card1, BorderLayout.NORTH);
        leftCol.add(card2, BorderLayout.CENTER);

        // Right Column
        String ngayBD = lh.getNgayBatDau() != null ? lh.getNgayBatDau().format(DATE_FMT) : "?";
        String ngayKT = lh.getNgayKetThuc() != null ? lh.getNgayKetThuc().format(DATE_FMT) : "?";
        
        JPanel rightCol = new JPanel(new BorderLayout(0, 16));
        rightCol.setOpaque(false);

        JPanel card3 = buildGridCard("Thời Gian & Lịch Học", 
                new String[]{"Tần suất :", "Ngày bắt đầu :", "Ngày kết thúc :"},
                new String[]{lh.getTanSuat() != null ? lh.getTanSuat() : "", ngayBD, ngayKT});
        
        rightCol.add(card3, BorderLayout.CENTER);

        wrapper.add(leftCol);
        wrapper.add(rightCol);

        return wrapper;
    }

    private JPanel buildGridCard(String title, String[] labels, String[] values) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(UiTheme.CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.CARD_BORDER, 1),
                new EmptyBorder(24, 24, 24, 24)));

        if (title != null && !title.isEmpty()) {
            JLabel lblTitle = new JLabel(title);
            lblTitle.setFont(UiTheme.TITLE_S);
            lblTitle.setForeground(UiTheme.SUCCESS); // Dark green to match mockup
            lblTitle.setBorder(new EmptyBorder(0, 0, 32, 0)); 
            card.add(lblTitle, BorderLayout.NORTH);
        }

        JPanel rowsPanel = new JPanel();
        rowsPanel.setLayout(new BoxLayout(rowsPanel, BoxLayout.Y_AXIS));
        rowsPanel.setOpaque(false);

        for (int i = 0; i < labels.length; i++) {
            JPanel row = new JPanel(new BorderLayout());
            row.setOpaque(false);
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
            
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(UiTheme.BODY_B);
            lbl.setForeground(UiTheme.TEXT_SECONDARY);
            
            JLabel val = new JLabel(values[i]);
            val.setFont(UiTheme.BODY);
            val.setForeground(UiTheme.TEXT_PRIMARY);
            val.setHorizontalAlignment(SwingConstants.RIGHT);
            
            row.add(lbl, BorderLayout.WEST);
            row.add(val, BorderLayout.CENTER);
            
            rowsPanel.add(row);
            if (i < labels.length - 1) {
                rowsPanel.add(Box.createVerticalStrut(40));
            }
        }
        rowsPanel.add(Box.createVerticalGlue());

        card.add(rowsPanel, BorderLayout.CENTER);
        return card;
    }


    private JPanel buildHocVienTab(LopHoc lh) {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        
        HocVienController hvController = new HocVienController();
        List<HocVien> students = hvController.layDanhSachTheoLop(lh.getMaLopHoc());
        
        JLabel lblCount = new JLabel("Danh sách học viên đăng ký: " + students.size() + "/" + lh.getSiSo());
        lblCount.setFont(UiTheme.BODY_B);
        lblCount.setForeground(UiTheme.TEXT_PRIMARY);
        topBar.add(lblCount, BorderLayout.WEST);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnPanel.setOpaque(false);
        
        JButton btnDel = UiComponents.primaryButton("Xóa học viên", UiTheme.DANGER);
        JButton btnAdd = UiComponents.primaryButton("Thêm học viên vào lớp", UiTheme.PRIMARY);
        
        String[] cols = {"Mã HV", "Họ Tên", "Ngày Sinh", "Giới Tính", "SĐT", "Email"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        
        for (HocVien hv : students) {
            String ngaySinh = hv.getNgaySinh() != null ? hv.getNgaySinh().format(DATE_FMT) : "";
            model.addRow(new Object[]{
                hv.getMaHocVien(), hv.getHoTen(), ngaySinh, hv.getGioiTinh(),
                hv.getSoDienThoai(), hv.getEmail()
            });
        }
        
        JTable table = new JTable(model);
        UiComponents.styleTable(table);
        UiComponents.setColumnAlignments(table,
                SwingConstants.CENTER, SwingConstants.LEFT, SwingConstants.CENTER,
                SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.LEFT);
        table.getColumnModel().getColumn(0).setMaxWidth(60);

        btnDel.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một học viên để xóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa học viên này khỏi lớp?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                int maHocVien = (int) table.getValueAt(row, 0);
                DangKyController dkController = new DangKyController();
                if (dkController.deleteDangKy(maHocVien, lh.getMaLopHoc())) {
                    JOptionPane.showMessageDialog(this, "Đã xóa học viên khỏi lớp.");
                    model.removeRow(row);
                    lblCount.setText("Danh sách học viên đăng ký: " + model.getRowCount() + "/" + lh.getSiSo());
                    loadData(); 
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi khi xóa học viên.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnAdd.addActionListener(e -> {
            List<HocVien> allStudents = hvController.layDanhSach();
            List<HocVien> unassigned = new ArrayList<>();
            DangKyController dkController = new DangKyController();
            for(HocVien h : allStudents) {
                if(!dkController.existsDangKy(h.getMaHocVien(), lh.getMaLopHoc())) {
                    unassigned.add(h);
                }
            }
            if(unassigned.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tất cả học viên đã có trong lớp này.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            showAddStudentDialog(lh, unassigned, model, lblCount);
        });
        
        btnPanel.add(btnDel);
        btnPanel.add(btnAdd);
        topBar.add(btnPanel, BorderLayout.EAST);
        
        panel.add(topBar, BorderLayout.NORTH);
        panel.add(UiComponents.tableScroll(table), BorderLayout.CENTER);

        return panel;
    }

    private void showAddStudentDialog(LopHoc lh, List<HocVien> unassigned, DefaultTableModel model, JLabel lblCount) {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Thêm học viên vào lớp", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(650, 450);
        dialog.setLocationRelativeTo(this);
        
        JPanel contentPane = new JPanel(new BorderLayout(16, 16));
        contentPane.setBorder(new EmptyBorder(16, 16, 16, 16));
        contentPane.setBackground(UiTheme.APP_BG);
        
        JTextField txtSearch = new JTextField();
        txtSearch.putClientProperty("JTextField.placeholderText", "Tìm kiếm theo tên, mã học viên, SĐT...");
        txtSearch.setFont(UiTheme.BODY);
        
        JPanel topPanel = new JPanel(new BorderLayout(8, 0));
        topPanel.setOpaque(false);
        JLabel lblSearch = new JLabel("Tìm kiếm:");
        lblSearch.setFont(UiTheme.BODY_B);
        lblSearch.setForeground(UiTheme.TEXT_PRIMARY);
        topPanel.add(lblSearch, BorderLayout.WEST);
        topPanel.add(txtSearch, BorderLayout.CENTER);
        
        String[] cols = {"Mã HV", "Họ Tên", "SĐT", "Email"};
        DefaultTableModel searchModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        for(HocVien h : unassigned) {
            searchModel.addRow(new Object[]{h.getMaHocVien(), h.getHoTen(), h.getSoDienThoai(), h.getEmail()});
        }
        
        JTable searchTable = new JTable(searchModel);
        UiComponents.styleTable(searchTable);
        searchTable.getColumnModel().getColumn(0).setMaxWidth(80);
        
        javax.swing.table.TableRowSorter<DefaultTableModel> sorter = new javax.swing.table.TableRowSorter<>(searchModel);
        searchTable.setRowSorter(sorter);
        
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override public void keyReleased(java.awt.event.KeyEvent e) {
                String kw = txtSearch.getText().trim();
                sorter.setRowFilter(kw.isEmpty() ? null : javax.swing.RowFilter.regexFilter("(?i)" + kw));
            }
        });
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        bottomPanel.setOpaque(false);
        JButton btnCancel = UiComponents.ghostButton("Hủy");
        JButton btnAdd = UiComponents.primaryButton("Thêm vào lớp", UiTheme.PRIMARY);
        
        btnCancel.addActionListener(e -> dialog.dispose());
        btnAdd.addActionListener(e -> {
            int viewRow = searchTable.getSelectedRow();
            if(viewRow < 0) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng chọn một học viên trong bảng để thêm!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int modelRow = searchTable.convertRowIndexToModel(viewRow);
            int maHV = (int) searchModel.getValueAt(modelRow, 0);
            
            HocVien sel = unassigned.stream().filter(h -> h.getMaHocVien() == maHV).findFirst().orElse(null);
            if(sel != null) {
                DangKy dk = new DangKy();
                dk.setMaHocVien(sel.getMaHocVien());
                dk.setMaLopHoc(lh.getMaLopHoc());
                dk.setNgayDangKy(java.time.LocalDate.now());
                dk.setTrangThaiDKY("Thanh cong");
                dk.setHinhThucTT("Tien mat");
                
                DangKyController dkController = new DangKyController();
                if(dkController.insertDangKy(dk)) {
                    JOptionPane.showMessageDialog(dialog, "Thêm học viên thành công!");
                    String ngaySinh = sel.getNgaySinh() != null ? sel.getNgaySinh().format(DATE_FMT) : "";
                    model.addRow(new Object[]{sel.getMaHocVien(), sel.getHoTen(), ngaySinh, sel.getGioiTinh(), sel.getSoDienThoai(), sel.getEmail()});
                    lblCount.setText("Danh sách học viên đăng ký: " + model.getRowCount() + "/" + lh.getSiSo());
                    loadData();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Lỗi khi thêm học viên.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        bottomPanel.add(btnCancel);
        bottomPanel.add(btnAdd);
        
        contentPane.add(topPanel, BorderLayout.NORTH);
        contentPane.add(UiComponents.tableScroll(searchTable), BorderLayout.CENTER);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);
        
        dialog.setContentPane(contentPane);
        dialog.setVisible(true);
        

    }



    private JPanel buildLichHocTab(LopHoc lh, JPanel detailContent) {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel title = new JLabel("Lịch Học Trong Tuần");
        title.setFont(UiTheme.TITLE_S);
        title.setForeground(UiTheme.PRIMARY);
        
        JButton btnAdd = UiComponents.primaryButton("Thêm", UiTheme.PRIMARY);
        // Do not override font here, let it use default BODY_B
        btnAdd.setPreferredSize(new Dimension(80, 28));
        btnAdd.addActionListener(e -> {
            JPanel inputPanel = new JPanel(new GridLayout(3, 2, 8, 8));
            JComboBox<String> cboThu = new JComboBox<>(new String[]{"Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ nhật"});
            JTextField txtBatDau = new JTextField("07:00");
            JTextField txtKetThuc = new JTextField("09:00");
            
            inputPanel.add(new JLabel("Thứ:")); inputPanel.add(cboThu);
            inputPanel.add(new JLabel("Giờ Bắt Đầu:")); inputPanel.add(txtBatDau);
            inputPanel.add(new JLabel("Giờ Kết Thúc:")); inputPanel.add(txtKetThuc);
            
            int result = JOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(this), inputPanel, "Thêm Lịch Học", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                LichHoc newLich = new LichHoc();
                newLich.setMaLop(lh.getMaLopHoc());
                newLich.setThu(cboThu.getSelectedItem().toString());
                newLich.setGioBatDau(txtBatDau.getText().trim());
                newLich.setGioKetThuc(txtKetThuc.getText().trim());
                
                LichHocDAO dao = new LichHocDAO();
                try {
                    if (dao.insert(newLich)) {
                        detailContent.add(buildLichHocTab(lh, detailContent), "LH_NEW");
                        ((CardLayout) detailContent.getLayout()).show(detailContent, "LH_NEW");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Thêm thất bại!");
                }
            }
        });

        headerPanel.add(title, BorderLayout.WEST);
        headerPanel.add(btnAdd, BorderLayout.EAST);
        panel.add(headerPanel, BorderLayout.NORTH);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);

        LichHocDAO dao = new LichHocDAO();
        try {
            List<LichHoc> schedule = dao.findByLop(lh.getMaLopHoc());
            if (schedule.isEmpty()) {
                JLabel empty = new JLabel("Chưa có lịch học nào cho lớp này.");
                empty.setFont(UiTheme.BODY);
                empty.setForeground(UiTheme.TEXT_MUTED);
                listPanel.add(empty);
            } else {
                for (LichHoc lich : schedule) {
                    JPanel row = new JPanel(new BorderLayout(16, 0));
                    row.setBackground(UiTheme.CARD_BG);
                    row.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(UiTheme.CARD_BORDER, 1),
                            new EmptyBorder(12, 16, 12, 16)
                    ));
                    row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
                    
                    String thu = lich.getThu();
                    if (thu != null && thu.startsWith("Thu")) thu = thu.replace("Thu", "Thứ");
                    
                    JLabel lblThu = new JLabel(thu);
                    lblThu.setFont(UiTheme.BODY_B);
                    lblThu.setForeground(UiTheme.TEXT_PRIMARY);
                    
                    String gioStr = "Chưa xếp giờ";
                    if (lich.getGioBatDau() != null && lich.getGioKetThuc() != null) {
                        gioStr = lich.getGioBatDau() + " - " + lich.getGioKetThuc();
                    }
                    
                    JLabel lblGio = new JLabel(gioStr);
                    lblGio.setFont(UiTheme.BODY);
                    lblGio.setForeground(UiTheme.TEXT_SECONDARY);
                    
                    JButton btnDel = new JButton("Xóa");
                    btnDel.setFont(UiTheme.BODY_B);
                    btnDel.setForeground(UiTheme.DANGER);
                    btnDel.setBackground(UiTheme.CARD_BG);
                    btnDel.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(UiTheme.CARD_BORDER, 1),
                            new EmptyBorder(2, 12, 2, 12)
                    ));
                    btnDel.setFocusPainted(false);
                    btnDel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    btnDel.addActionListener(e -> {
                        int conf = JOptionPane.showConfirmDialog(this, "Xóa lịch học này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                        if (conf == JOptionPane.YES_OPTION) {
                            try {
                                if (dao.delete(lich.getMaLich())) {
                                    detailContent.add(buildLichHocTab(lh, detailContent), "LH_NEW");
                                    ((CardLayout) detailContent.getLayout()).show(detailContent, "LH_NEW");
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                    
                    JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
                    right.setOpaque(false);
                    right.add(lblGio);
                    right.add(btnDel);
                    
                    row.add(lblThu, BorderLayout.WEST);
                    row.add(right, BorderLayout.EAST);
                    
                    listPanel.add(row);
                    listPanel.add(Box.createVerticalStrut(8));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JLabel err = new JLabel("Lỗi tải lịch học.");
            err.setForeground(UiTheme.DANGER);
            listPanel.add(err);
        }

        JScrollPane scroll = new JScrollPane(listPanel, 
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }
}
