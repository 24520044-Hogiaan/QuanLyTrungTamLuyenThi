package com.trungtam;

import com.trungtam.ui.UiTheme;
import com.trungtam.ui.auth.LoginWindow;

import javax.swing.*;

public class AppLauncher {

    public static void main(String[] args) {
        try {
            Class<?> lafClass = Class.forName("com.formdev.flatlaf.FlatLightLaf");
            lafClass.getMethod("setup").invoke(null);
        } catch (Exception ignored) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        try {
            UiTheme.applyGlobalTokens();
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new LoginWindow().setVisible(true));
    }
}
