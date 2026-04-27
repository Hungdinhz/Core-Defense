package td;

import javax.swing.JFrame;

/**
 * Cửa sổ chính chứa GamePanel.
 */
public class GameFrame extends JFrame {
    public GameFrame() {
        setTitle("Java Tower Defense - Swing/AWT");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        GameManager gameManager = new GameManager();
        GamePanel gamePanel = new GamePanel(gameManager);
        gameManager.setGamePanel(gamePanel);

        setContentPane(gamePanel);
        pack();
        setLocationRelativeTo(null);
    }
}
