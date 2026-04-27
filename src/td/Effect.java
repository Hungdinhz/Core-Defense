package td;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;

/**
 * Hiệu ứng nổ/fade đơn giản khi enemy chết.
 */
public class Effect {
    private double x;
    private double y;
    private int life;
    private int maxLife;
    private int startSize;
    private Color color;

    public Effect(double x, double y, int maxLife, int startSize, Color color) {
        this.x = x;
        this.y = y;
        this.maxLife = maxLife;
        this.life = maxLife;
        this.startSize = startSize;
        this.color = color;
    }

    public void update() {
        life--;
    }

    public boolean isAlive() {
        return life > 0;
    }

    public void draw(Graphics2D g2d) {
        if (!isAlive()) {
            return;
        }

        float alpha = Math.max(0f, (float) life / maxLife);
        int size = (int) (startSize + (maxLife - life) * 0.8);
        int drawX = (int) x - size / 2;
        int drawY = (int) y - size / 2;

        Composite old = g2d.getComposite();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2d.setColor(color);
        g2d.fillOval(drawX, drawY, size, size);
        g2d.setComposite(old);
    }
}
