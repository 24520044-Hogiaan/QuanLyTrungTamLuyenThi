package com.trungtam;

import com.trungtam.ui.UiTheme;
import com.trungtam.ui.auth.LoginWindow;
import com.trungtam.ui.auth.RoleSelectionWindow;

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
        SwingUtilities.invokeLater(() -> {
            final RoleSelectionWindow[] roleSelectionWindowRef = new RoleSelectionWindow[1];
            roleSelectionWindowRef[0] = new RoleSelectionWindow(roleId -> {
                roleSelectionWindowRef[0].dispose();
                LoginWindow loginWindow = new LoginWindow(roleId);
                loginWindow.setVisible(true);
            });
            roleSelectionWindowRef[0].setVisible(true);
        });
    }
}
