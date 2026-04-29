package ui;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import core.GameManager;
import map.GameMap;

public class UIManager extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel root;
    private final MenuPanel menuPanel;

    public UIManager() {
        setTitle("Java Tower Defense - Swing/AWT");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        cardLayout = new CardLayout();
        root = new JPanel(cardLayout);

        menuPanel = new MenuPanel(new MenuPanel.MenuListener() {
            @Override
            public void onStart(GameMap selectedMap) {
                startGame(selectedMap);
            }

            @Override
            public void onExit() {
                dispose();
            }
        });

        root.add(menuPanel, "menu");
        setContentPane(root);
        pack();
        setLocationRelativeTo(null);
        cardLayout.show(root, "menu");
    }

    private void startGame(GameMap map) {
        GameManager gameManager = new GameManager(map);
        GamePanel gamePanel = new GamePanel(gameManager);
        gameManager.setGamePanel(gamePanel);

        root.add(gamePanel, "game");
        cardLayout.show(root, "game");
        pack();
    }
}
