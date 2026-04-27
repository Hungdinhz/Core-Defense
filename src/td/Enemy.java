package td;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;

/**
 * Enemy cơ bản di chuyển theo path cố định.
 */
public class Enemy {
    protected double x;
    protected double y;
    protected double speed;
    protected int maxHp;
    protected int hp;
    protected int rewardGold;
    protected int damageToPlayer;
    protected int pathIndex;
    protected int size;
    protected boolean alive;
    protected boolean escaped;
    protected Color color;

    public Enemy(List<Point> path, int hp, double speed, int rewardGold, int damageToPlayer, int size, Color color) {
        Point start = path.get(0);
        this.x = start.x;
        this.y = start.y;
        this.maxHp = hp;
        this.hp = hp;
        this.speed = speed;
        this.rewardGold = rewardGold;
        this.damageToPlayer = damageToPlayer;
        this.pathIndex = 1;
        this.size = size;
        this.alive = true;
        this.escaped = false;
        this.color = color;
    }

    /**
     * Di chuyển enemy tới waypoint tiếp theo.
     */
    public boolean update(List<Point> path) {
        if (!alive) {
            return false;
        }
        if (pathIndex >= path.size()) {
            return true;
        }

        Point target = path.get(pathIndex);
        double dx = target.x - x;
        double dy = target.y - y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance <= speed) {
            x = target.x;
            y = target.y;
            pathIndex++;
        } else {
            x += (dx / distance) * speed;
            y += (dy / distance) * speed;
        }
        return pathIndex >= path.size();
    }

    public void draw(Graphics2D g2d) {
        int drawX = (int) x - size / 2;
        int drawY = (int) y - size / 2;

        g2d.setColor(color);
        g2d.fillOval(drawX, drawY, size, size);
        g2d.setColor(new Color(30, 30, 30));
        g2d.drawOval(drawX, drawY, size, size);

        // Vẽ thanh máu đơn giản phía trên enemy.
        int barWidth = size;
        int barHeight = 5;
        int barX = drawX;
        int barY = drawY - 8;

        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(barX, barY, barWidth, barHeight);

        double hpPercent = Math.max(0, (double) hp / maxHp);
        g2d.setColor(Color.GREEN);
        g2d.fillRect(barX, barY, (int) (barWidth * hpPercent), barHeight);
    }

    public void takeDamage(int damage) {
        hp -= damage;
        if (hp <= 0) {
            hp = 0;
            alive = false;
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean isEscaped() {
        return escaped;
    }

    public void markEscaped() {
        this.escaped = true;
    }

    public double distanceTo(double tx, double ty) {
        double dx = tx - x;
        double dy = ty - y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public int getRewardGold() {
        return rewardGold;
    }

    public int getDamageToPlayer() {
        return damageToPlayer;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getSize() {
        return size;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }
}
