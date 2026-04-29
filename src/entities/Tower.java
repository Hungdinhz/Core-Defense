package entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import utils.AssetManager;

public abstract class Tower {
    protected int x;
    protected int y;
    protected int level;
    protected double range;
    protected int damage;
    protected double fireRatePerSecond;
    protected long lastShotTimeMs;
    protected TargetingMode targetingMode;
    protected Color outerColor;
    protected Color innerColor;
    protected String imageName;

    protected Tower(int x, int y) {
        this.x = x;
        this.y = y;
        this.level = 1;
        this.lastShotTimeMs = 0;
        this.targetingMode = TargetingMode.NEAREST;
    }

    public abstract String getName();

    public abstract int getCost();

    public Bullet tryShoot(List<Enemy> enemies, long nowMs) {
        long cooldownMs = (long) (1000.0 / fireRatePerSecond);
        if (nowMs - lastShotTimeMs < cooldownMs) {
            return null;
        }

        Enemy target = findTarget(enemies);
        if (target == null) {
            return null;
        }

        lastShotTimeMs = nowMs;
        return new Bullet(x, y, target, damage, 5.5 + level * 0.4);
    }

    protected Enemy findTarget(List<Enemy> enemies) {
        Enemy best = null;
        double bestValue = Double.MAX_VALUE;
        for (Enemy enemy : enemies) {
            if (!enemy.isAlive()) {
                continue;
            }
            double dist = enemy.distanceTo(x, y);
            if (dist > range) {
                continue;
            }
            double value = computePriorityValue(enemy, dist);
            if (best == null || value < bestValue) {
                bestValue = value;
                best = enemy;
            }
        }
        return best;
    }

    protected double computePriorityValue(Enemy enemy, double distance) {
        switch (targetingMode) {
            case LOWEST_HP:
                return enemy.getHp() + distance * 0.02;
            case FARTHEST_PROGRESS:
                return -enemy.getPathProgress();
            case NEAREST:
            default:
                return distance;
        }
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

    public void cycleTargetingMode() {
        TargetingMode[] values = TargetingMode.values();
        targetingMode = values[(targetingMode.ordinal() + 1) % values.length];
    }

    public void draw(Graphics2D g2d, boolean selected) {
        int size = 38;
        int drawX = x - size / 2;
        int drawY = y - size / 2;

        java.awt.Image img = imageName != null ? AssetManager.getInstance().getImage(imageName) : null;
        if (img != null) {
            g2d.drawImage(img, drawX, drawY, size, size, null);
        } else {
            g2d.setColor(outerColor != null ? outerColor : new Color(49, 143, 67));
            g2d.fillOval(drawX, drawY, size, size);
            g2d.setColor(innerColor != null ? innerColor : new Color(35, 103, 48));
            g2d.fillOval(drawX + 5, drawY + 5, size - 10, size - 10);
            g2d.setColor(Color.BLACK);
            g2d.drawOval(drawX, drawY, size, size);
        }

        g2d.setColor(Color.WHITE);
        g2d.drawString("L" + level, x - 8, y + 5);

        if (selected) {
            g2d.setColor(new Color(79, 196, 106, 60));
            int radius = (int) range;
            g2d.fillOval(x - radius, y - radius, radius * 2, radius * 2);
            g2d.setColor(new Color(32, 104, 49));
            g2d.drawOval(x - radius, y - radius, radius * 2, radius * 2);
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

    public TargetingMode getTargetingMode() {
        return targetingMode;
    }
}
