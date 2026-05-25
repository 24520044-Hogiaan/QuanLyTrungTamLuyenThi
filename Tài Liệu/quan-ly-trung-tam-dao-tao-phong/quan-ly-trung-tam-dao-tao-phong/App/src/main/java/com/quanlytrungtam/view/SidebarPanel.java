package com.quanlytrungtam.view;

import javax.swing.*;
import javax.swing.border.*;

import com.quanlytrungtam.ui.UiTheme;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class SidebarPanel extends JPanel {

    public interface SelectionListener {
        void onSelected(int index);
    }

    private static final Color SIDEBAR_BG = Color.WHITE;
    private static final Color SECTION_FG = new Color(0x9E9E9E);
    private static final Color ITEM_FG = new Color(0x37474F);
    private static final Color ITEM_HOVER_BG = new Color(0xF5F5F5);
    private static final Color BORDER_COLOR = new Color(0xE0E0E0);

    private final Color accentColor;
    private final List<ItemRow> items = new ArrayList<>();
    private int selectedIndex = 0;
    private SelectionListener listener;

    /**
     * @param accentColor brand colour for the selected-item pill
     */
    public SidebarPanel(Color accentColor) {
        this.accentColor = accentColor;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(SIDEBAR_BG);
        setBorder(new MatteBorder(0, 0, 0, 1, BORDER_COLOR));
        setPreferredSize(new Dimension(UiTheme.TAB_WIDTH + 20, 0));
    }

    /** Add a section header (non-clickable label). */
    public SidebarPanel addSection(String title) {
        JLabel lbl = new JLabel(title.toUpperCase());
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lbl.setForeground(SECTION_FG);
        lbl.setBorder(new EmptyBorder(16, 16, 4, 8));
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        add(lbl);
        return this;
    }

    /** Add a clickable nav item. Returns this for chaining. */
    public SidebarPanel addItem(String label) {
        int idx = items.size();
        ItemRow row = new ItemRow(label, idx);
        items.add(row);
        add(row);
        return this;
    }

    public void setSelectionListener(SelectionListener l) {
        this.listener = l;
    }

    public void select(int index) {
        if (index < 0 || index >= items.size())
            return;
        selectedIndex = index;
        items.forEach(ItemRow::repaint);
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    private class ItemRow extends JPanel {
        private final String label;
        private final int index;
        private boolean hovered = false;

        ItemRow(String label, int index) {
            this.label = label;
            this.index = index;
            setOpaque(false);
            setMaximumSize(new Dimension(Integer.MAX_VALUE, UiTheme.TAB_HEIGHT));
            setPreferredSize(new Dimension(0, UiTheme.TAB_HEIGHT));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setAlignmentX(LEFT_ALIGNMENT);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hovered = false;
                    repaint();
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    selectedIndex = index;
                    items.forEach(ItemRow::repaint);
                    if (listener != null)
                        listener.onSelected(index);
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g0) {
            Graphics2D g = (Graphics2D) g0.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

            int w = getWidth(), h = getHeight();
            boolean selected = (index == selectedIndex);

            if (selected) {
                int margin = 8;
                g.setColor(accentColor);
                g.fillRoundRect(margin, 4, w - margin * 2, h - 8, 8, 8);
            } else if (hovered) {
                g.setColor(ITEM_HOVER_BG);
                g.fillRect(0, 0, w, h);
            }

            if (selected) {
                g.setColor(accentColor.brighter());
                g.fillRoundRect(0, 8, 4, h - 16, 4, 4);
            }

            g.setFont(selected ? UiTheme.BODY_B : UiTheme.BODY);
            g.setColor(selected ? Color.WHITE : ITEM_FG);
            FontMetrics fm = g.getFontMetrics();
            int textY = (h - fm.getHeight()) / 2 + fm.getAscent();
            g.drawString(label, 20, textY);

            g.dispose();
        }
    }
}
