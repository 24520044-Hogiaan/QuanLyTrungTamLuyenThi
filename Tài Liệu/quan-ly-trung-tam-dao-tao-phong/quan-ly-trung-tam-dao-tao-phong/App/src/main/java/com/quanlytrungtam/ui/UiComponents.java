package com.quanlytrungtam.ui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Component factory — reusable builders for buttons, tables, scroll panes,
 * dividers, and stat cards. All styling is driven by UiTheme tokens.
 */
public final class UiComponents {

    private UiComponents() {
    }

    // ── Buttons ───────────────────────────────────────────────────────────────

    /** Solid filled primary-action button. */
    public static JButton primaryButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(UiTheme.BODY_B);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        // Subtle hover: darken by 15%
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(bg.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bg);
            }
        });
        return btn;
    }

    /** Ghost / secondary button — border only, no fill. */
    public static JButton ghostButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(UiTheme.BODY);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ── Section divider ───────────────────────────────────────────────────────

    /**
     * Horizontal section label with a bottom underline — used inside forms
     * to separate logical groups.
     */
    public static JLabel divider(String title, Color color) {
        JLabel lbl = new JLabel(title);
        lbl.setFont(UiTheme.BODY_B);
        lbl.setForeground(color);
        lbl.setBorder(new MatteBorder(0, 0, 1, 0, color.brighter()));
        return lbl;
    }

    // ── Tables ────────────────────────────────────────────────────────────────

    /**
     * Apply the standard playbook table style to an existing JTable:
     * - row height 34
     * - both horizontal AND vertical grid lines (border-style)
     * - alternating row renderer
     */
    public static void styleTable(JTable table) {
        table.setRowHeight(UiTheme.ROW_HEIGHT);
        table.setFont(UiTheme.BODY);
        table.getTableHeader().setFont(UiTheme.BODY_B);
        table.getTableHeader().setBackground(UiTheme.TABLE_HEADER_BG);
        table.getTableHeader().setForeground(UiTheme.TEXT_PRIMARY);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(true);
        table.setGridColor(UiTheme.CARD_BORDER);
        table.setFillsViewportHeight(true);
        table.setSelectionBackground(new Color(0xBBDEFB));
        table.setSelectionForeground(UiTheme.TEXT_PRIMARY);
        table.setIntercellSpacing(new Dimension(1, 1));
        // Default alternating renderer (can be overridden per-table)
        table.setDefaultRenderer(Object.class, new AltRowRenderer());
    }

    /**
     * Set horizontal alignment for specific columns.
     * alignments array length must match column count.
     * Use SwingConstants.LEFT / CENTER / RIGHT.
     */
    public static void setColumnAlignments(JTable table, int... alignments) {
        for (int i = 0; i < alignments.length && i < table.getColumnCount(); i++) {
            final int align = alignments[i];
            final int col = i;
            table.getColumnModel().getColumn(i).setCellRenderer(new AltRowRenderer() {
                @Override
                public Component getTableCellRendererComponent(
                        JTable t, Object val, boolean sel, boolean foc, int r, int c) {
                    super.getTableCellRendererComponent(t, val, sel, foc, r, col);
                    setHorizontalAlignment(align);
                    return this;
                }
            });
        }
    }

    /**
     * Wrap a JTable in a JScrollPane with smooth unit increment.
     * Vertical scrollbar always shown; horizontal as needed.
     */
    public static JScrollPane tableScroll(JTable table) {
        JScrollPane sp = new JScrollPane(table,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        sp.getHorizontalScrollBar().setUnitIncrement(16);
        sp.setBorder(BorderFactory.createLineBorder(UiTheme.CARD_BORDER, 1));
        return sp;
    }

    /**
     * Wrap any component in a JScrollPane with smooth scrolling.
     */
    public static JScrollPane scrollPane(Component content) {
        JScrollPane sp = new JScrollPane(content,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        sp.setBorder(BorderFactory.createEmptyBorder());
        return sp;
    }

    // ── Stat / KPI card ───────────────────────────────────────────────────────

    /**
     * Build a KPI card with a title label and a large value label.
     *
     * @param title      short description
     * @param valueLabel the JLabel that will be updated with the actual value
     * @param color      accent colour for value text and border tint
     */
    public static JPanel statCard(String title, JLabel valueLabel, Color color) {
        JPanel card = new JPanel(new BorderLayout(0, 4));
        card.setBackground(UiTheme.CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(
                        color.getRed(), color.getGreen(), color.getBlue(), 60), 1),
                new EmptyBorder(12, 16, 12, 16)));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(UiTheme.CAPTION);
        lblTitle.setForeground(UiTheme.TEXT_MUTED);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valueLabel.setForeground(color);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    // ── Page wrapper ──────────────────────────────────────────────────────────

    /**
     * Wrap a panel in a page-level JScrollPane so content is never clipped
     * on smaller screens.
     */
    public static JScrollPane pageScroll(JPanel content) {
        JScrollPane sp = new JScrollPane(content,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sp.getVerticalScrollBar().setUnitIncrement(20);
        sp.getHorizontalScrollBar().setUnitIncrement(20);
        sp.setBorder(BorderFactory.createEmptyBorder());
        return sp;
    }

    // ── Alternating row renderer ──────────────────────────────────────────────

    /**
     * Default alternating-row renderer. Subclass and override to add
     * per-column colour logic.
     */
    public static class AltRowRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!isSelected) {
                setBackground(row % 2 == 0 ? UiTheme.CARD_BG : UiTheme.ROW_ALT);
                setForeground(UiTheme.TEXT_PRIMARY);
            }
            setBorder(new EmptyBorder(0, 6, 0, 6));
            return this;
        }
    }
}
