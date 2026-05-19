package com.trungtam.ui.giaovien;

import com.trungtam.model.GiaoVien;
import com.trungtam.ui.UiComponents;
import com.trungtam.ui.UiTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Dialog thêm / sửa thông tin giáo viên.
 * Trả kết quả qua getResult() sau khi đóng.
 */
public class GiaoVienDialog extends JDialog {

    private final JTextField txtHoTen = new JTextField(24);
    private final JTextField txtEmail = new JTextField(24);
    private final JTextField txtSdt = new JTextField(24);
    private final JTextField txtChuyenMon = new JTextField(24);
    private final JTextField txtNgaySinh = new JTextField(24);
    private final JComboBox<String> cboBangCap = new JComboBox<>(
            new String[] { "Cử nhân", "Thạc sĩ", "Tiến sĩ" });
    private final JComboBox<String> cboTrangThai = new JComboBox<>(
            new String[] { "Đang làm", "Nghỉ việc" });

    private GiaoVien result = null;
    private final GiaoVien editingTarget;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public GiaoVienDialog(Frame parent, GiaoVien existing) {
        super(parent, existing == null ? "Thêm Giáo Viên" : "Sửa Giáo Viên", true);
        this.editingTarget = existing;

        setSize(480, 420);
        setMinimumSize(new Dimension(420, 380));
        setLocationRelativeTo(parent);
        setResizable(true);

        JPanel content = new JPanel(new BorderLayout(0, 16));
        content.setBorder(new EmptyBorder(UiTheme.PAD_L, UiTheme.PAD_XL, UiTheme.PAD_L, UiTheme.PAD_XL));
        content.setBackground(UiTheme.APP_BG);

        content.add(buildForm(), BorderLayout.CENTER);
        content.add(buildButtons(), BorderLayout.SOUTH);

        add(content);

        if (existing != null)
            prefillForm(existing);
    }

    private JPanel buildForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 4, 6, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNgaySinh.putClientProperty("JTextField.placeholderText", "dd/MM/yyyy");

        Object[][] rows = {
                { "Họ và tên *", txtHoTen },
                { "Email *", txtEmail },
                { "Số điện thoại", txtSdt },
                { "Ngày sinh", txtNgaySinh },
                { "Chuyên môn", txtChuyenMon },
                { "Bằng cấp", cboBangCap },
                { "Trạng thái", cboTrangThai },
        };

        for (int i = 0; i < rows.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0.35;
            JLabel label = new JLabel((String) rows[i][0]);
            label.setFont(UiTheme.BODY);
            label.setForeground(UiTheme.TEXT_SECONDARY);
            form.add(label, gbc);

            gbc.gridx = 1;
            gbc.weightx = 0.65;
            JComponent field = (JComponent) rows[i][1];
            field.setFont(UiTheme.BODY);
            form.add(field, gbc);
        }
        return form;
    }

    private JPanel buildButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panel.setOpaque(false);

        JButton btnHuy = UiComponents.ghostButton("Hủy");
        JButton btnLuu = UiComponents.primaryButton("Lưu", UiTheme.SUCCESS);

        btnLuu.addActionListener(e -> save());
        btnHuy.addActionListener(e -> dispose());

        panel.add(btnHuy);
        panel.add(btnLuu);
        return panel;
    }

    private void prefillForm(GiaoVien gv) {
        txtHoTen.setText(gv.getHoTen());
        txtEmail.setText(gv.getEmail());
        txtSdt.setText(gv.getSoDienThoai());
        txtChuyenMon.setText(gv.getChuyenMon());
        if (gv.getNgaySinh() != null)
            txtNgaySinh.setText(gv.getNgaySinh().format(DATE_FORMAT));
        cboBangCap.setSelectedItem(gv.getBangCap());
        cboTrangThai.setSelectedItem(gv.getTrangThai());
    }

    private void save() {
        String hoTen = txtHoTen.getText().trim();
        String email = txtEmail.getText().trim();

        if (hoTen.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng điền đầy đủ Họ tên và Email.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDate ngaySinh = null;
        String ngaySinhText = txtNgaySinh.getText().trim();
        if (!ngaySinhText.isEmpty()) {
            try {
                ngaySinh = LocalDate.parse(ngaySinhText, DATE_FORMAT);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this,
                        "Ngày sinh không đúng định dạng dd/MM/yyyy.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        int maGV = (editingTarget != null) ? editingTarget.getMaGiaoVien() : 0;
        result = new GiaoVien(maGV, hoTen, email,
                txtSdt.getText().trim(), txtChuyenMon.getText().trim(),
                (String) cboBangCap.getSelectedItem(),
                (String) cboTrangThai.getSelectedItem(),
                ngaySinh);
        dispose();
    }

    /** @return GiaoVien nếu người dùng bấm Lưu, null nếu Hủy */
    public GiaoVien getResult() {
        return result;
    }
}
