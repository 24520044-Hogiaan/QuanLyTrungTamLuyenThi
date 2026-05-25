package com.trungtam.ui;

import java.awt.*;

/**
 * Design tokens — single source of truth for all colours, fonts, and spacing.
 * Every panel/window should reference these constants instead of hardcoding
 * values.
 */
public final class UiTheme {

    private UiTheme() {
    }

    // ── Palette ──────────────────────────────────────────────────────────────

    /** Main app background */
    public static final Color APP_BG = new Color(0xF5F7FA);
    /** Card / panel background */
    public static final Color CARD_BG = Color.WHITE;
    /** Subtle border for cards and panels */
    public static final Color CARD_BORDER = new Color(0xE2E8F0);
    /** Alternating row tint (even rows stay WHITE) */
    public static final Color ROW_ALT = new Color(0xF8FAFC);

    /** Primary brand colour — navy blue (Giáo Viên module) */
    public static final Color PRIMARY = new Color(0x1A237E);
    /** Lighter shade used for selected sidebar item */
    public static final Color PRIMARY_SEL = new Color(0x283593);
    /** Accent rail / marker on selected sidebar item */
    public static final Color PRIMARY_RAIL = new Color(0x90CAF9);
    /** Sidebar foreground (unselected) */
    public static final Color SIDEBAR_FG = new Color(0xC5CAE9);

    /** Admin brand colour — purple */
    public static final Color ADMIN = new Color(0x6A1B9A);
    public static final Color ADMIN_SEL = new Color(0x7B1FA2);
    /** QuanLy brand colour — deep orange */
    public static final Color QUANLY = new Color(0xE65100);
    public static final Color QUANLY_SEL = new Color(0xEF6C00);
    /** KeToan brand colour — teal */
    public static final Color KETOAN = new Color(0x00695C);
    public static final Color KETOAN_SEL = new Color(0x00796B);

    /** Secondary brand colour — green (Học Viên module) */
    public static final Color SECONDARY = new Color(0x1B5E20);
    public static final Color SECONDARY_SEL = new Color(0x2E7D32);
    public static final Color SECONDARY_RAIL = new Color(0xA5D6A7);
    public static final Color SECONDARY_FG = new Color(0xC8E6C9);

    /** Semantic colours */
    public static final Color SUCCESS = new Color(0x2E7D32);
    public static final Color DANGER = new Color(0xC62828);
    public static final Color WARNING = new Color(0xE65100);
    public static final Color INFO = new Color(0x1565C0);
    public static final Color MUTED = new Color(0x757575);

    /** Text colours */
    public static final Color TEXT_PRIMARY = new Color(0x1A202C);
    public static final Color TEXT_SECONDARY = new Color(0x4A5568);
    public static final Color TEXT_MUTED = new Color(0x718096);

    /** Table header background */
    public static final Color TABLE_HEADER_BG = new Color(0xEDF2F7);

    // ── Fonts ─────────────────────────────────────────────────────────────────

    public static final Font TITLE_L = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font TITLE_M = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font TITLE_S = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font BODY_B = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font CAPTION = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font CAPTION_I = new Font("Segoe UI", Font.ITALIC, 11);
    public static final Font MONO = new Font("Consolas", Font.PLAIN, 13);

    // ── Spacing ───────────────────────────────────────────────────────────────

    public static final int PAD_S = 8;
    public static final int PAD_M = 14;
    public static final int PAD_L = 20;
    public static final int PAD_XL = 28;

    /** Standard table row height */
    public static final int ROW_HEIGHT = 34;

    /** Sidebar tab height */
    public static final int TAB_HEIGHT = 46;
    /** Sidebar tab width */
    public static final int TAB_WIDTH = 168;

    // ── FlatLaf UIManager tokens ──────────────────────────────────────────────

    /**
     * Call once at startup (after FlatLightLaf.setup()) to apply global tokens.
     */
    public static void applyGlobalTokens() {
        // Scrollbar — slim, rounded, modern
        javax.swing.UIManager.put("ScrollBar.width", 10);
        javax.swing.UIManager.put("ScrollBar.trackArc", 999);
        javax.swing.UIManager.put("ScrollBar.thumbArc", 999);
        javax.swing.UIManager.put("ScrollBar.track", new Color(0xF0F0F0));
        javax.swing.UIManager.put("ScrollBar.thumb", new Color(0xBDBDBD));
        javax.swing.UIManager.put("ScrollBar.hoverThumbColor", new Color(0x9E9E9E));

        // Buttons — rounded
        javax.swing.UIManager.put("Button.arc", 8);
        javax.swing.UIManager.put("Component.arc", 6);
        javax.swing.UIManager.put("TextComponent.arc", 6);

        // Table header
        javax.swing.UIManager.put("TableHeader.background", TABLE_HEADER_BG);
        javax.swing.UIManager.put("TableHeader.foreground", TEXT_PRIMARY);
        javax.swing.UIManager.put("TableHeader.font", BODY_B);

        // TabbedPane — suppress default focus ring
        javax.swing.UIManager.put("TabbedPane.focusColor", new Color(0, 0, 0, 0));
        javax.swing.UIManager.put("TabbedPane.tabHeight", TAB_HEIGHT);

        // General background
        javax.swing.UIManager.put("Panel.background", APP_BG);
    }
}
