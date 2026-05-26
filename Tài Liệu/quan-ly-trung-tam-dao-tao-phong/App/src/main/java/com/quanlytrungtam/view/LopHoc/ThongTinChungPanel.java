package com.quanlytrungtam.view.LopHoc;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.quanlytrungtam.ui.UiTheme;

import java.awt.*;

/**
 * Thông tin chung tab — aligned with LOPHOC, KHOAHOC, LICHHOC, PHONGHOC schema.
 */
public class ThongTinChungPanel extends JPanel {

        public ThongTinChungPanel(String tenLop, String tenKhoaHoc, String giangVien,
                        String tanSuat, String capDo, String ngayBD, String ngayKT,
                        int siSo, String trangThai) {
                setLayout(new GridLayout(2, 2, 16, 16));
                setOpaque(false);

                // String capDoDisplay = capDoDisplay(capDo);

                add(createInfoSubCard("Thông Tin Lớp Học", new String[][] {
                                { "Tên lớp", tenLop },
                                { "Khóa học ", tenKhoaHoc },
                                { "Giảng viên ", giangVien },
                }));

                add(createInfoSubCard("Thời Gian & Lịch Học", new String[][] {
                                { "Tần suất ", tanSuat },
                                { "Ngày bắt đầu ", ngayBD },
                                { "Ngày kết thúc ", ngayKT }
                }));

                add(createInfoSubCard("", new String[][] {
                                { "Sĩ số ", siSo + " Học viên" },
                                { "Trạng thái ", trangThai },
                                { "Phòng học ", "Phòng 302" }
                }));

        }

        /*
         * private String capDoDisplay(String dbVal) {
         * switch (dbVal) {
         * case "Co ban":
         * return "Cơ bản";
         * case "Trung cap":
         * return "Trung cấp";
         * case "Nang cao":
         * return "Nâng cao";
         * default:
         * return dbVal;
         * }
         * }
         */

        private JPanel createInfoSubCard(String title, String[][] details) {
                JPanel card = new JPanel(new BorderLayout());
                card.setBackground(UiTheme.CARD_BG);
                card.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(UiTheme.CARD_BORDER, 1),
                                new EmptyBorder(14, 16, 14, 16)));

                JLabel lblTitle = new JLabel(title);
                lblTitle.setFont(UiTheme.TITLE_S);
                lblTitle.setForeground(UiTheme.SECONDARY);
                lblTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
                card.add(lblTitle, BorderLayout.NORTH);

                JPanel content = new JPanel();
                content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
                content.setOpaque(false);

                for (String[] row : details) {
                        JPanel rowPanel = new JPanel(new BorderLayout());
                        rowPanel.setOpaque(false);
                        rowPanel.setBorder(new EmptyBorder(4, 0, 4, 0));

                        JLabel lblKey = new JLabel(row[0] + ":");
                        lblKey.setFont(UiTheme.BODY_B);
                        lblKey.setForeground(UiTheme.TEXT_SECONDARY);

                        JLabel lblVal = new JLabel(row[1]);
                        lblVal.setFont(UiTheme.BODY);
                        lblVal.setForeground(UiTheme.TEXT_PRIMARY);

                        rowPanel.add(lblKey, BorderLayout.WEST);
                        rowPanel.add(lblVal, BorderLayout.EAST);
                        content.add(rowPanel);
                }

                card.add(content, BorderLayout.CENTER);
                return card;
        }
}
