package td;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.List;

import javax.swing.JPanel;

/**
 * JPanel chuyên render game và nhận input chuột.
 */
public class GamePanel extends JPanel {
    private final GameManager gameManager;
    private int mouseX;
    private int mouseY;

    public GamePanel(GameManager gameManager) {
        this.gameManager = gameManager;

        setPreferredSize(new Dimension(GameManager.WIDTH, GameManager.HEIGHT));
        setBackground(new Color(205, 230, 200));

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    gameManager.handleLeftClick(e.getX(), e.getY());
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    gameManager.handleRightClick(e.getX(), e.getY());
                }
                repaint();
            }
        };
        addMouseListener(mouseAdapter);

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                mouseMoved(e);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawMapBackground(g2d);
        drawTopBar(g2d);
        drawPath(g2d, gameManager.getPathPoints());
        drawEntities(g2d);
        drawPlacementPreview(g2d);
        drawSelectedTowerCard(g2d);
        drawBottomHint(g2d);
        drawGameOver(g2d);

        g2d.dispose();
    }

    private void drawTopBar(Graphics2D g2d) {
        g2d.setPaint(new GradientPaint(0, 0, new Color(26, 30, 40), getWidth(), 0, new Color(38, 45, 57)));
        g2d.fillRect(0, 0, getWidth(), GameManager.TOP_UI_HEIGHT);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 16));
        g2d.drawString("HP: " + gameManager.getPlayerHp(), 18, 28);
        g2d.drawString("Gold: " + gameManager.getGold(), 130, 28);
        g2d.drawString("Wave: " + gameManager.getCurrentWave(), 270, 28);
        g2d.drawString("Tower: " + gameManager.getTowerCost() + "g", 390, 28);

        String waveStatus = gameManager.isWaveRunning()
                ? "Enemies: " + (gameManager.getEnemies().size() + gameManager.getEnemiesToSpawn())
                : "Ready";
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 14));
        g2d.drawString(waveStatus, 18, 52);

        var rect = gameManager.getStartWaveButtonRect();
        g2d.setColor(gameManager.isWaveRunning() ? new Color(110, 113, 120) : new Color(52, 165, 96));
        g2d.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 10, 10);
        g2d.setColor(new Color(255, 255, 255, 90));
        g2d.drawRoundRect(rect.x, rect.y, rect.width, rect.height, 10, 10);
        g2d.setColor(Color.WHITE);
        g2d.drawString(gameManager.isWaveRunning() ? "Wave Running" : "Start Wave", rect.x + 18, rect.y + 22);
    }

    private void drawPath(Graphics2D g2d, List<Point> path) {
        g2d.setStroke(new BasicStroke(30, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setColor(new Color(160, 124, 91));
        for (int i = 0; i < path.size() - 1; i++) {
            Point a = path.get(i);
            Point b = path.get(i + 1);
            g2d.drawLine(a.x, a.y, b.x, b.y);
        }

        g2d.setStroke(new BasicStroke(18, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setColor(new Color(191, 158, 120));
        for (int i = 0; i < path.size() - 1; i++) {
            Point a = path.get(i);
            Point b = path.get(i + 1);
            g2d.drawLine(a.x, a.y, b.x, b.y);
        }
        g2d.setStroke(new BasicStroke(1f));
    }

    private void drawEntities(Graphics2D g2d) {
        Tower selected = gameManager.getSelectedTower();

        for (Tower tower : gameManager.getTowers()) {
            tower.draw(g2d, tower == selected);
        }

        for (Enemy enemy : gameManager.getEnemies()) {
            enemy.draw(g2d);
        }

        for (Bullet bullet : gameManager.getBullets()) {
            bullet.draw(g2d);
        }
    }

    private void drawPlacementPreview(Graphics2D g2d) {
        if (mouseY < GameManager.TOP_UI_HEIGHT || gameManager.getPlayerHp() <= 0) {
            return;
        }
        boolean canPlace = gameManager.canPlaceTowerAt(mouseX, mouseY) && gameManager.getGold() >= gameManager.getTowerCost();
        Color tint = canPlace ? new Color(63, 201, 94, 90) : new Color(224, 82, 82, 90);

        int previewSize = 30;
        g2d.setColor(tint);
        g2d.fillOval(mouseX - previewSize / 2, mouseY - previewSize / 2, previewSize, previewSize);
    }

    private void drawSelectedTowerCard(Graphics2D g2d) {
        Tower selected = gameManager.getSelectedTower();
        if (selected == null) {
            return;
        }

        int cardW = 200;
        int cardH = 120;
        int cardX = getWidth() - cardW - 15;
        int cardY = GameManager.TOP_UI_HEIGHT + 12;

        g2d.setColor(new Color(20, 25, 33, 210));
        g2d.fillRoundRect(cardX, cardY, cardW, cardH, 12, 12);
        g2d.setColor(new Color(255, 255, 255, 60));
        g2d.drawRoundRect(cardX, cardY, cardW, cardH, 12, 12);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 15));
        g2d.drawString("Selected Tower", cardX + 14, cardY + 24);
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 14));
        g2d.drawString("Level: " + selected.getLevel(), cardX + 14, cardY + 45);
        g2d.drawString("Damage: " + selected.getDamage(), cardX + 14, cardY + 64);
        g2d.drawString("Range: " + (int) selected.getRange(), cardX + 14, cardY + 83);
        g2d.drawString(String.format("Fire Rate: %.2f/s", selected.getFireRatePerSecond()), cardX + 14, cardY + 102);
    }

    private void drawMapBackground(Graphics2D g2d) {
        g2d.setPaint(new GradientPaint(0, 0, new Color(201, 236, 193), 0, getHeight(), new Color(164, 214, 164)));
        g2d.fillRect(0, GameManager.TOP_UI_HEIGHT, getWidth(), getHeight() - GameManager.TOP_UI_HEIGHT);

        // Vẽ lưới nhẹ để map có chiều sâu.
        g2d.setColor(new Color(255, 255, 255, 35));
        for (int x = 0; x < getWidth(); x += 40) {
            g2d.drawLine(x, GameManager.TOP_UI_HEIGHT, x, getHeight());
        }
        for (int y = GameManager.TOP_UI_HEIGHT; y < getHeight(); y += 40) {
            g2d.drawLine(0, y, getWidth(), y);
        }
    }

    private void drawBottomHint(Graphics2D g2d) {
        g2d.setColor(new Color(20, 20, 20, 180));
        g2d.fillRoundRect(10, getHeight() - 34, 470, 24, 8, 8);
        g2d.setColor(new Color(255, 255, 255));
        g2d.drawString("Left click: place/select tower | Right click tower: upgrade", 18, getHeight() - 16);
    }

    private void drawGameOver(Graphics2D g2d) {
        if (gameManager.getPlayerHp() > 0) {
            return;
        }

        g2d.setColor(new Color(0, 0, 0, 120));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 40));
        g2d.drawString("GAME OVER", getWidth() / 2 - 130, getHeight() / 2);
    }
}
