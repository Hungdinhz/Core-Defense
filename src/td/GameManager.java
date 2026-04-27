package td;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.swing.Timer;

/**
 * Quản lý trạng thái game, loop update, wave, tài nguyên.
 */
public class GameManager {
    public static final int WIDTH = 900;
    public static final int HEIGHT = 600;
    public static final int TOP_UI_HEIGHT = 70;

    private static final int TOWER_COST = 80;
    private static final int PLAYER_START_HP = 20;
    private static final int PLAYER_START_GOLD = 180;

    private final List<Enemy> enemies;
    private final List<Tower> towers;
    private final List<Bullet> bullets;
    private final List<Effect> effects;
    private final List<Point> pathPoints;
    private final Rectangle startWaveButtonRect;
    private final Rectangle pauseButtonRect;
    private final Rectangle restartButtonRect;

    private final Random random;
    private final SaveManager saveManager;
    private final SoundManager soundManager;
    private Timer gameTimer;
    private GamePanel gamePanel;

    private int playerHp;
    private int gold;
    private int currentWave;
    private boolean waveRunning;
    private int enemiesToSpawn;
    private int totalEnemiesInWave;
    private long lastSpawnTimeMs;
    private long spawnIntervalMs;
    private Tower selectedTower;
    private boolean paused;
    private boolean gameOverSoundPlayed;
    private int bossesToSpawn;

    public GameManager() {
        this.enemies = new ArrayList<>();
        this.towers = new ArrayList<>();
        this.bullets = new ArrayList<>();
        this.effects = new ArrayList<>();
        this.pathPoints = createPath();
        this.startWaveButtonRect = new Rectangle(740, 18, 130, 34);
        this.pauseButtonRect = new Rectangle(570, 18, 72, 34);
        this.restartButtonRect = new Rectangle(652, 18, 78, 34);
        this.random = new Random();
        this.saveManager = new SaveManager();
        this.soundManager = new SoundManager();

        SaveManager.SaveData save = saveManager.load();
        this.playerHp = PLAYER_START_HP;
        this.gold = Math.max(0, save.gold);
        this.currentWave = Math.max(0, save.wave);
        this.waveRunning = false;
        this.enemiesToSpawn = 0;
        this.totalEnemiesInWave = 0;
        this.lastSpawnTimeMs = 0;
        this.spawnIntervalMs = 900;
        this.selectedTower = null;
        this.paused = false;
        this.gameOverSoundPlayed = false;
        this.bossesToSpawn = 0;
    }

    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        startLoop();
    }

    private void startLoop() {
        gameTimer = new Timer(16, e -> updateGame());
        gameTimer.start();
    }

    private void updateGame() {
        if (paused) {
            if (gamePanel != null) {
                gamePanel.repaint();
            }
            return;
        }

        if (playerHp <= 0) {
            if (!gameOverSoundPlayed) {
                soundManager.playGameOver();
                gameOverSoundPlayed = true;
                saveManager.save(gold, currentWave);
            }
            return;
        }

        long now = System.currentTimeMillis();
        spawnEnemies(now);
        updateEnemies();
        updateTowers(now);
        updateBullets();
        updateEffects();
        cleanupDeadEnemies();
        checkWaveFinished();

        if (gamePanel != null) {
            gamePanel.repaint();
        }
    }

    private void spawnEnemies(long now) {
        if (!waveRunning || enemiesToSpawn <= 0) {
            return;
        }
        if (now - lastSpawnTimeMs < spawnIntervalMs) {
            return;
        }

        if (bossesToSpawn > 0) {
            enemies.add(new BossEnemy(pathPoints, currentWave));
            bossesToSpawn--;
        } else {
            enemies.add(createEnemyForWave(currentWave));
            enemiesToSpawn--;
        }
        lastSpawnTimeMs = now;
    }

    private Enemy createEnemyForWave(int wave) {
        int roll = random.nextInt(100);
        if (wave >= 5 && roll < 24) {
            return new TankEnemy(pathPoints, wave);
        }
        if (wave >= 2 && roll < 60) {
            return new FastEnemy(pathPoints, wave);
        }
        return new NormalEnemy(pathPoints, wave);
    }

    private void updateEnemies() {
        for (Enemy enemy : enemies) {
            boolean reachedEnd = enemy.update(pathPoints);
            if (reachedEnd && enemy.isAlive()) {
                playerHp -= enemy.getDamageToPlayer();
                enemy.markEscaped();
                enemy.takeDamage(Integer.MAX_VALUE);
            }
        }
    }

    private void updateTowers(long now) {
        for (Tower tower : towers) {
            Bullet bullet = tower.tryShoot(enemies, now);
            if (bullet != null) {
                bullets.add(bullet);
                soundManager.playShoot();
            }
        }
    }

    private void updateBullets() {
        for (Bullet bullet : bullets) {
            bullet.update();
        }
    }

    private void updateEffects() {
        for (Effect effect : effects) {
            effect.update();
        }
        effects.removeIf(e -> !e.isAlive());
    }

    private void cleanupDeadEnemies() {
        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            if (!enemy.isAlive()) {
                // Chỉ cộng gold khi chết do tower bắn, không phải khi tới đích.
                if (!enemy.isEscaped()) {
                    gold += enemy.getRewardGold();
                    effects.add(new Effect(enemy.getX(), enemy.getY(), 24, enemy.getSize(), enemy.getColor()));
                    soundManager.playEnemyDeath();
                }
                enemyIterator.remove();
            }
        }

        bullets.removeIf(b -> !b.isActive());
    }

    private void checkWaveFinished() {
        if (waveRunning && enemiesToSpawn == 0 && enemies.isEmpty()) {
            waveRunning = false;
            saveManager.save(gold, currentWave);
        }
    }

    private List<Point> createPath() {
        List<Point> path = new ArrayList<>();
        path.add(new Point(40, 180));
        path.add(new Point(200, 180));
        path.add(new Point(200, 420));
        path.add(new Point(420, 420));
        path.add(new Point(420, 150));
        path.add(new Point(690, 150));
        path.add(new Point(690, 360));
        path.add(new Point(860, 360));
        return path;
    }

    public void startWave() {
        if (waveRunning || playerHp <= 0 || paused) {
            return;
        }
        currentWave++;
        enemiesToSpawn = 6 + currentWave * 2 + currentWave / 2;
        bossesToSpawn = (currentWave % 5 == 0) ? 1 : 0;
        totalEnemiesInWave = enemiesToSpawn + bossesToSpawn;
        // Wave càng cao spawn càng nhanh để tăng độ khó.
        spawnIntervalMs = Math.max(260, 850 - currentWave * 35L);
        lastSpawnTimeMs = 0;
        waveRunning = true;
        saveManager.save(gold, currentWave);
    }

    public void placeTower(int mouseX, int mouseY) {
        if (playerHp <= 0) {
            return;
        }
        if (!canPlaceTowerAt(mouseX, mouseY) || gold < TOWER_COST) {
            return;
        }
        towers.add(new Tower(mouseX, mouseY));
        gold -= TOWER_COST;
    }

    public void handleLeftClick(int mouseX, int mouseY) {
        if (pauseButtonRect.contains(mouseX, mouseY)) {
            paused = !paused;
            return;
        }
        if (restartButtonRect.contains(mouseX, mouseY)) {
            restartGame();
            return;
        }
        if (startWaveButtonRect.contains(mouseX, mouseY)) {
            startWave();
            return;
        }
        if (paused) {
            return;
        }

        selectedTower = findTowerAt(mouseX, mouseY);
        if (selectedTower == null) {
            placeTower(mouseX, mouseY);
        }
    }

    public void handleRightClick(int mouseX, int mouseY) {
        if (paused) {
            return;
        }
        Tower tower = findTowerAt(mouseX, mouseY);
        if (tower != null && tower.canUpgrade() && gold >= tower.getUpgradeCost()) {
            gold -= tower.getUpgradeCost();
            tower.upgrade();
            selectedTower = tower;
        }
    }

    public void handleMiddleClick(int mouseX, int mouseY) {
        if (paused) {
            return;
        }
        Tower tower = findTowerAt(mouseX, mouseY);
        if (tower != null) {
            tower.cycleTargetingMode();
            selectedTower = tower;
        }
    }

    private void restartGame() {
        enemies.clear();
        towers.clear();
        bullets.clear();
        effects.clear();
        selectedTower = null;
        playerHp = PLAYER_START_HP;
        gold = PLAYER_START_GOLD;
        currentWave = 0;
        waveRunning = false;
        enemiesToSpawn = 0;
        totalEnemiesInWave = 0;
        bossesToSpawn = 0;
        paused = false;
        gameOverSoundPlayed = false;
        saveManager.save(gold, currentWave);
    }

    private Tower findTowerAt(int mx, int my) {
        for (Tower tower : towers) {
            if (tower.isInside(mx, my)) {
                return tower;
            }
        }
        return null;
    }

    private boolean isTooCloseToOtherTower(int mx, int my) {
        for (Tower tower : towers) {
            double dx = tower.getX() - mx;
            double dy = tower.getY() - my;
            if (Math.sqrt(dx * dx + dy * dy) < 42) {
                return true;
            }
        }
        return false;
    }

    private boolean isOnPath(int mx, int my) {
        for (int i = 0; i < pathPoints.size() - 1; i++) {
            Point a = pathPoints.get(i);
            Point b = pathPoints.get(i + 1);
            if (distancePointToSegment(mx, my, a.x, a.y, b.x, b.y) <= 24) {
                return true;
            }
        }
        return false;
    }

    private double distancePointToSegment(double px, double py, double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        if (dx == 0 && dy == 0) {
            dx = px - x1;
            dy = py - y1;
            return Math.sqrt(dx * dx + dy * dy);
        }
        double t = ((px - x1) * dx + (py - y1) * dy) / (dx * dx + dy * dy);
        t = Math.max(0, Math.min(1, t));
        double projX = x1 + t * dx;
        double projY = y1 + t * dy;
        double ddx = px - projX;
        double ddy = py - projY;
        return Math.sqrt(ddx * ddx + ddy * ddy);
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public List<Tower> getTowers() {
        return towers;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public List<Effect> getEffects() {
        return effects;
    }

    public List<Point> getPathPoints() {
        return pathPoints;
    }

    public int getPlayerHp() {
        return playerHp;
    }

    public int getGold() {
        return gold;
    }

    public int getCurrentWave() {
        return currentWave;
    }

    public boolean isWaveRunning() {
        return waveRunning;
    }

    public Rectangle getStartWaveButtonRect() {
        return startWaveButtonRect;
    }

    public Tower getSelectedTower() {
        return selectedTower;
    }

    public int getTowerCost() {
        return TOWER_COST;
    }

    public int getEnemiesToSpawn() {
        return enemiesToSpawn;
    }

    public int getEnemiesRemainingInWave() {
        return enemies.size() + enemiesToSpawn + bossesToSpawn;
    }

    public int getTotalEnemiesInWave() {
        return totalEnemiesInWave;
    }

    public boolean canPlaceTowerAt(int x, int y) {
        if (y < TOP_UI_HEIGHT) {
            return false;
        }
        return !isOnPath(x, y) && !isTooCloseToOtherTower(x, y);
    }

    public Rectangle getPauseButtonRect() {
        return pauseButtonRect;
    }

    public Rectangle getRestartButtonRect() {
        return restartButtonRect;
    }

    public boolean isPaused() {
        return paused;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }
}
