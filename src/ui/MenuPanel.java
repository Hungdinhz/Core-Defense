package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.List;

import javax.swing.JPanel;

import core.GameManager;
import map.GameMap;
import map.GameMaps;

public class MenuPanel extends JPanel {
    public interface MenuListener {
        void onStart(GameMap selectedMap);

        void onExit();
    }

    private final MenuListener listener;
    private final List<GameMap> maps;

    private GameMap selectedMap;
    private boolean showMapSelect;
    private int mouseX;
    private int mouseY;

    private Rectangle startRect;
    private Rectangle selectMapRect;
    private Rectangle exitRect;

    private Rectangle map1Rect;
    private Rectangle map2Rect;
    private Rectangle map3Rect;
    private Rectangle backRect;

    public MenuPanel(MenuListener listener) {
        this.listener = listener;
        this.maps = GameMaps.getAll();
        this.selectedMap = GameMaps.getDefault();
        this.showMapSelect = false;

        setPreferredSize(new Dimension(GameManager.WIDTH, GameManager.HEIGHT));

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                handleClick(e.getX(), e.getY());
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
        });
    }

    public GameMap getSelectedMap() {
        return selectedMap;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawBackground(g2d);
        drawTitle(g2d);

        if (!showMapSelect) {
            drawMainButtons(g2d);
            drawSelectedMapInfo(g2d);
        } else {
            drawMapSelect(g2d);
        }

        g2d.dispose();
    }

    private void drawBackground(Graphics2D g2d) {
        g2d.setPaint(new GradientPaint(0, 0, new Color(20, 24, 34), 0, getHeight(), new Color(44, 52, 78)));
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    private void drawTitle(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 44));
        g2d.drawString("Tower Defense", 270, 140);

        g2d.setFont(new Font("SansSerif", Font.PLAIN, 16));
        g2d.setColor(new Color(255, 255, 255, 160));
        g2d.drawString("Java Swing Edition", 365, 170);
    }

    private void drawMainButtons(Graphics2D g2d) {
        int cx = getWidth() / 2 - 140;
        int y = 230;
        startRect = new Rectangle(cx, y, 280, 52);
        selectMapRect = new Rectangle(cx, y + 70, 280, 52);
        exitRect = new Rectangle(cx, y + 140, 280, 52);

        drawButton(g2d, startRect, "Start Game", new Color(52, 165, 96));
        drawButton(g2d, selectMapRect, "Select Map", new Color(83, 126, 189));
        drawButton(g2d, exitRect, "Exit", new Color(189, 84, 84));
    }

    private void drawSelectedMapInfo(Graphics2D g2d) {
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 16));
        g2d.setColor(new Color(255, 255, 255, 190));
        g2d.drawString("Selected map: " + selectedMap.getName(), 300, 455);
    }

    private void drawMapSelect(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 22));
        g2d.drawString("Select Map", 380, 220);

        int cx = getWidth() / 2 - 220;
        map1Rect = new Rectangle(cx, 260, 440, 52);
        map2Rect = new Rectangle(cx, 330, 440, 52);
        map3Rect = new Rectangle(cx, 400, 440, 52);
        backRect = new Rectangle(getWidth() / 2 - 90, 480, 180, 44);

        drawMapButton(g2d, map1Rect, maps.get(0));
        drawMapButton(g2d, map2Rect, maps.get(1));
        drawMapButton(g2d, map3Rect, maps.get(2));
        drawButton(g2d, backRect, "Back", new Color(110, 113, 120));
    }

    private void drawMapButton(Graphics2D g2d, Rectangle rect, GameMap map) {
        Color base = new Color(30, 35, 50);
        if (map == selectedMap) {
            base = new Color(52, 165, 96);
        }
        drawButton(g2d, rect, map.getName(), base);
    }

    private void drawButton(Graphics2D g2d, Rectangle rect, String text, Color baseColor) {
        boolean hover = rect.contains(mouseX, mouseY);
        Color fill = hover ? baseColor.brighter() : baseColor;

        g2d.setColor(new Color(0, 0, 0, 120));
        g2d.fillRoundRect(rect.x + 3, rect.y + 4, rect.width, rect.height, 14, 14);

        g2d.setColor(fill);
        g2d.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 14, 14);

        g2d.setColor(new Color(255, 255, 255, hover ? 220 : 140));
        g2d.drawRoundRect(rect.x, rect.y, rect.width, rect.height, 14, 14);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 18));
        g2d.drawString(text, rect.x + 18, rect.y + 33);
    }

    private void handleClick(int x, int y) {
        if (!showMapSelect) {
            if (startRect != null && startRect.contains(x, y)) {
                listener.onStart(selectedMap);
            } else if (selectMapRect != null && selectMapRect.contains(x, y)) {
                showMapSelect = true;
            } else if (exitRect != null && exitRect.contains(x, y)) {
                listener.onExit();
            }
            return;
        }

        if (map1Rect != null && map1Rect.contains(x, y)) {
            selectedMap = maps.get(0);
        } else if (map2Rect != null && map2Rect.contains(x, y)) {
            selectedMap = maps.get(1);
        } else if (map3Rect != null && map3Rect.contains(x, y)) {
            selectedMap = maps.get(2);
        } else if (backRect != null && backRect.contains(x, y)) {
            showMapSelect = false;
        }
    }
}
