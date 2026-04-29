package main;

import javax.swing.SwingUtilities;

import ui.UIManager;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UIManager uiManager = new UIManager();
            uiManager.setVisible(true);
        });
    }
}
