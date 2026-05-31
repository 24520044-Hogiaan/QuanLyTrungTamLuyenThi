package com.trungtam.ui.admin;

import com.trungtam.model.TaiKhoan;
import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class TaiKhoanDialog extends JDialog {

    private boolean saved = false;
    private TaiKhoan taiKhoan;
    private JTextField txtTenTaiKhoan;
    private JPasswordField txtMatKhau;
    private JComboBox<String> cboVaiTro;
    private JComboBox<String> cboTrangThai;

    private final Map<String, Integer> vaiTroMap = new HashMap<>();

    public TaiKhoanDialog(Window owner, TaiKhoan tk) {
        super(owner, tk == null ? "Thêm Tài Khoản Mới" : "Sửa Tài Khoản #" + tk.getMaTaiKhoan(), ModalityType.APPLICATION_MODAL);
        this.taiKhoan = tk == null ? new TaiKhoan() : tk;

        // Init Vai Tro Map based on DB values
        vaiTroMap.put("Admin", 1);
        vaiTroMap.put("Giáo Viên", 2);
        vaiTroMap.put("Học Viên", 3);
        vaiTroMap.put("Kế Toán", 4);
        vaiTroMap.put("Nhân Viên Quản Lý", 5);

        setSize(350, 350);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(0, 16));
        root.setBackground(UiTheme.APP_BG);
        root.setBorder(new EmptyBorder(UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M, UiTheme.PAD_M));

        root.add(buildForm(), BorderLayout.CENTER);
        root.add(buildButtons(), BorderLayout.SOUTH);

        setContentPane(root);
        loadDataToForm();
    }

    private JPanel buildForm() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 8, 12));
        panel.setOpaque(false);

        panel.add(createLabel("Tên Tài Khoản:"));
        txtTenTaiKhoan = new JTextField();
        txtTenTaiKhoan.setFont(UiTheme.BODY);
        panel.add(txtTenTaiKhoan);

        panel.add(createLabel("Mật Khẩu" + (taiKhoan.getMaTaiKhoan() > 0 ? " (Để trống nếu ko đổi):" : ":")));
        txtMatKhau = new JPasswordField();
        txtMatKhau.setFont(UiTheme.BODY);
        panel.add(txtMatKhau);

        panel.add(createLabel("Vai Trò:"));
        cboVaiTro = new JComboBox<>(vaiTroMap.keySet().toArray(new String[0]));
        cboVaiTro.setFont(UiTheme.BODY);
        panel.add(cboVaiTro);

        panel.add(createLabel("Trạng Thái:"));
        cboTrangThai = new JComboBox<>(new String[]{"Hoat dong", "Vo hieu hoa"});
        cboTrangThai.setFont(UiTheme.BODY);
        panel.add(cboTrangThai);

        return panel;
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(UiTheme.BODY_B);
        lbl.setForeground(UiTheme.TEXT_PRIMARY);
        return lbl;
    }

    private void loadDataToForm() {
        if (taiKhoan.getMaTaiKhoan() > 0) {
            txtTenTaiKhoan.setText(taiKhoan.getTenTaiKhoan());
            txtTenTaiKhoan.setEditable(false); // Username usually cannot be changed easily
            
            cboTrangThai.setSelectedItem(taiKhoan.getTrangThaiTK());
            
            for (Map.Entry<String, Integer> entry : vaiTroMap.entrySet()) {
                if (entry.getValue() == taiKhoan.getMaVaiTro()) {
                    cboVaiTro.setSelectedItem(entry.getKey());
                    break;
                }
            }
        }
    }

    private JPanel buildButtons() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bar.setOpaque(false);

        JButton btnCancel = UiComponents.ghostButton("Hủy");
        btnCancel.addActionListener(e -> dispose());

        JButton btnSave = UiComponents.primaryButton("Lưu Lại", UiTheme.PRIMARY);
        btnSave.addActionListener(e -> save());

        bar.add(btnCancel);
        bar.add(btnSave);
        return bar;
    }

    private void save() {
        if (taiKhoan.getMaTaiKhoan() == 0 && String.valueOf(txtMatKhau.getPassword()).trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mật khẩu cho tài khoản mới!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        taiKhoan.setTenTaiKhoan(txtTenTaiKhoan.getText().trim());
        String mk = String.valueOf(txtMatKhau.getPassword()).trim();
        if (!mk.isEmpty()) taiKhoan.setMatKhau(mk);
        
        String vaiTroStr = (String) cboVaiTro.getSelectedItem();
        if (vaiTroStr != null) {
            taiKhoan.setMaVaiTro(vaiTroMap.get(vaiTroStr));
        }
        taiKhoan.setTrangThaiTK((String) cboTrangThai.getSelectedItem());

        saved = true;
        dispose();
    }

    public boolean isSaved() { return saved; }
    public TaiKhoan getTaiKhoan() { return taiKhoan; }
}
