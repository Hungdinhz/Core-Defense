package td;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Bullet bay theo mục tiêu và gây damage khi chạm.
 */
public class Bullet {
    private double x;
    private double y;
    private Enemy target;
    private int damage;
    private double speed;
    private boolean active;
    private int size;

    public Bullet(double x, double y, Enemy target, int damage, double speed) {
        this.x = x;
        this.y = y;
        this.target = target;
        this.damage = damage;
        this.speed = speed;
        this.active = true;
        this.size = 8;
    }

    public void update() {
        if (!active || target == null || !target.isAlive()) {
            active = false;
            return;
        }

        double dx = target.getX() - x;
        double dy = target.getY() - y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        // Va chạm đơn giản: bullet đủ gần enemy thì gây damage.
        if (distance <= target.getSize() * 0.5 + size * 0.5) {
            target.takeDamage(damage);
            active = false;
            return;
        }

        if (distance <= speed) {
            x = target.getX();
            y = target.getY();
        } else {
            x += (dx / distance) * speed;
            y += (dy / distance) * speed;
        }
    }

    public void draw(Graphics2D g2d) {
        if (!active) {
            return;
        }
        int drawX = (int) x - size / 2;
        int drawY = (int) y - size / 2;

        // Glow nhẹ cho viên đạn để trông nổi bật hơn.
        g2d.setColor(new Color(255, 200, 0, 90));
        g2d.fillOval(drawX - 3, drawY - 3, size + 6, size + 6);
        g2d.setColor(new Color(255, 230, 90));
        g2d.fillOval(drawX, drawY, size, size);
    }

    public boolean isActive() {
        return active;
    }
}
