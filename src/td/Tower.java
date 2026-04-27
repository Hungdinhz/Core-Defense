package td;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

/**
 * Tower đứng yên, tự tìm enemy gần nhất trong tầm.
 */
public class Tower {
    private int x;
    private int y;
    private int level;
    private double range;
    private int damage;
    private double fireRatePerSecond;
    private long lastShotTimeMs;

    public Tower(int x, int y) {
        this.x = x;
        this.y = y;
        this.level = 1;
        this.range = 110;
        this.damage = 15;
        this.fireRatePerSecond = 1.2;
        this.lastShotTimeMs = 0;
    }

    public Bullet tryShoot(List<Enemy> enemies, long nowMs) {
        long cooldownMs = (long) (1000.0 / fireRatePerSecond);
        if (nowMs - lastShotTimeMs < cooldownMs) {
            return null;
        }

        Enemy target = findNearestEnemyInRange(enemies);
        if (target == null) {
            return null;
        }

        lastShotTimeMs = nowMs;
        return new Bullet(x, y, target, damage, 5.5);
    }

    private Enemy findNearestEnemyInRange(List<Enemy> enemies) {
        Enemy nearest = null;
        double nearestDist = Double.MAX_VALUE;
        for (Enemy enemy : enemies) {
            if (!enemy.isAlive()) {
                continue;
            }
            double dist = enemy.distanceTo(x, y);
            if (dist <= range && dist < nearestDist) {
                nearestDist = dist;
                nearest = enemy;
            }
        }
        return nearest;
    }

    public boolean canUpgrade() {
        return level < 3;
    }

    public int getUpgradeCost() {
        if (level == 1) {
            return 50;
        }
        if (level == 2) {
            return 90;
        }
        return Integer.MAX_VALUE;
    }

    public void upgrade() {
        if (!canUpgrade()) {
            return;
        }
        level++;
        range += 20;
        damage += 10;
        fireRatePerSecond += 0.35;
    }

    public void draw(Graphics2D g2d, boolean selected) {
        int size = 30;
        int drawX = x - size / 2;
        int drawY = y - size / 2;

        // Thân tower dạng tròn để nhìn mềm mại hơn.
        g2d.setColor(new Color(49, 143, 67));
        g2d.fillOval(drawX, drawY, size, size);

        g2d.setColor(new Color(35, 103, 48));
        g2d.fillOval(drawX + 5, drawY + 5, size - 10, size - 10);

        g2d.setColor(Color.BLACK);
        g2d.drawOval(drawX, drawY, size, size);
        g2d.drawString("L" + level, x - 8, y + 5);

        if (selected) {
            g2d.setColor(new Color(79, 196, 106, 60));
            int r = (int) range;
            g2d.fillOval(x - r, y - r, r * 2, r * 2);
            g2d.setColor(new Color(32, 104, 49));
            g2d.drawOval(x - r, y - r, r * 2, r * 2);
        }
    }

    public boolean isInside(int mx, int my) {
        int half = 14;
        return mx >= x - half && mx <= x + half && my >= y - half && my <= y + half;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getRange() {
        return range;
    }

    public int getLevel() {
        return level;
    }

    public int getDamage() {
        return damage;
    }

    public double getFireRatePerSecond() {
        return fireRatePerSecond;
    }
}
