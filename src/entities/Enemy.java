package entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;

import map.Path;
import utils.AssetManager;

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
    protected double visualHp;
    protected double pathProgress;
    protected String imageName;

    public Enemy(Path path, int hp, double speed, int rewardGold, int damageToPlayer, int size, Color color) {
        Point start = path.getWaypoints().get(0);
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
        this.visualHp = hp;
        this.pathProgress = 0;
    }

    public boolean update(Path path) {
        List<Point> waypoints = path.getWaypoints();
        if (!alive) {
            updateVisualHp();
            return false;
        }
        if (pathIndex >= waypoints.size()) {
            updateVisualHp();
            return true;
        }

        Point target = waypoints.get(pathIndex);
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

        updateProgress(waypoints);
        updateVisualHp();
        return pathIndex >= waypoints.size();
    }

    public void draw(Graphics2D g2d) {
        int renderSize = size + 16;
        int drawX = (int) x - renderSize / 2;
        int drawY = (int) y - renderSize / 2;

        java.awt.Image img = imageName != null ? AssetManager.getInstance().getImage(imageName) : null;
        if (img != null) {
            g2d.drawImage(img, drawX, drawY, renderSize, renderSize, null);
        } else {
            drawX = (int) x - size / 2;
            drawY = (int) y - size / 2;
            g2d.setColor(color);
            g2d.fillOval(drawX, drawY, size, size);
            g2d.setColor(new Color(30, 30, 30));
            g2d.drawOval(drawX, drawY, size, size);
        }

        int barWidth = size + 4;
        int barHeight = 5;
        int barX = (int) x - barWidth / 2;
        int barY = (int) y - renderSize / 2 - 8;

        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(barX, barY, barWidth, barHeight);

        double hpPercent = Math.max(0, visualHp / maxHp);
        g2d.setColor(Color.GREEN);
        g2d.fillRect(barX, barY, (int) (barWidth * hpPercent), barHeight);
    }

    private void updateVisualHp() {
        visualHp += (hp - visualHp) * 0.2;
    }

    private void updateProgress(List<Point> waypoints) {
        if (pathIndex >= waypoints.size()) {
            pathProgress = waypoints.size() * 1000.0;
            return;
        }
        Point target = waypoints.get(pathIndex);
        double distanceToNext = distanceTo(target.x, target.y);
        pathProgress = pathIndex * 1000.0 - distanceToNext;
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
        escaped = true;
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

    public double getPathProgress() {
        return pathProgress;
    }

    public Color getColor() {
        return color;
    }
}
