package core;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entities.Bullet;
import entities.Effect;
import entities.Enemy;
import entities.Tower;
import map.GameMap;
import map.GameMaps;
import map.Path;
import systems.CollisionSystem;
import systems.WaveManager;
import towers.TowerType;
import ui.GamePanel;
import utils.SaveManager;
import utils.SoundManager;

public class GameManager {
    public static final int WIDTH = 900;
    public static final int HEIGHT = 600;
    public static final int TOP_UI_HEIGHT = 70;

    private static final int PLAYER_START_HP = 20;
    private static final int PLAYER_START_GOLD = 180;

    private final List<Enemy> enemies;
    private final List<Tower> towers;
    private final List<Bullet> bullets;
    private final List<Effect> effects;
    private final GameMap map;
    private final Path path;
    private final Rectangle startWaveButtonRect;
    private final Rectangle pauseButtonRect;
    private final Rectangle restartButtonRect;
    private final SaveManager saveManager;
    private final SoundManager soundManager;
    private final WaveManager waveManager;
    private final CollisionSystem collisionSystem;

    private GameLoop gameLoop;
    private GamePanel gamePanel;
    private int playerHp;
    private int gold;
    private Tower selectedTower;
    private boolean paused;
    private boolean gameOverSoundPlayed;
    private TowerType selectedBuildType;

    public GameManager() {
        this(GameMaps.getDefault());
    }

    public GameManager(GameMap map) {
        this.enemies = new ArrayList<>();
        this.towers = new ArrayList<>();
        this.bullets = new ArrayList<>();
        this.effects = new ArrayList<>();
        this.map = map != null ? map : GameMaps.getDefault();
        this.path = this.map.getPath();
        this.startWaveButtonRect = new Rectangle(740, 18, 130, 34);
        this.pauseButtonRect = new Rectangle(570, 18, 72, 34);
        this.restartButtonRect = new Rectangle(652, 18, 78, 34);
        this.saveManager = new SaveManager();
        this.soundManager = new SoundManager();
        this.waveManager = new WaveManager(new Random(), saveManager.load().wave);
        this.collisionSystem = new CollisionSystem();

        SaveManager.SaveData save = saveManager.load();
        this.playerHp = PLAYER_START_HP;
        this.gold = Math.max(0, save.gold);
        this.selectedBuildType = TowerType.BASIC;
    }

    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        startLoop();
    }

    private void startLoop() {
        gameLoop = new GameLoop(16, e -> updateGame());
        gameLoop.start();
    }

    private void updateGame() {
        if (paused) {
            repaintPanel();
            return;
        }

        if (playerHp <= 0) {
            if (!gameOverSoundPlayed) {
                soundManager.playGameOver();
                gameOverSoundPlayed = true;
                saveManager.save(gold, waveManager.getCurrentWave());
            }
            return;
        }

        long now = System.currentTimeMillis();
        waveManager.spawnEnemies(now, enemies, path);
        playerHp -= collisionSystem.updateEnemies(enemies, path);
        updateTowers(now);
        collisionSystem.updateBullets(bullets);
        collisionSystem.updateEffects(effects);

        int earnedGold = collisionSystem.cleanupDeadEnemies(enemies, bullets, effects);
        if (earnedGold > 0) {
            gold += earnedGold;
            soundManager.playEnemyDeath();
        }

        if (waveManager.finishIfDone(enemies)) {
            saveManager.save(gold, waveManager.getCurrentWave());
        }

        repaintPanel();
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

    private void repaintPanel() {
        if (gamePanel != null) {
            gamePanel.repaint();
        }
    }

    public void startWave() {
        if (waveManager.isWaveRunning() || playerHp <= 0 || paused) {
            return;
        }
        waveManager.startWave();
        saveManager.save(gold, waveManager.getCurrentWave());
    }

    public void placeTower(int mouseX, int mouseY) {
        if (playerHp <= 0 || selectedBuildType == null) {
            return;
        }
        int cost = selectedBuildType.getCost();
        if (!canPlaceTowerAt(mouseX, mouseY) || gold < cost) {
            return;
        }
        towers.add(selectedBuildType.create(mouseX, mouseY));
        gold -= cost;
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
        waveManager.reset();
        paused = false;
        gameOverSoundPlayed = false;
        selectedBuildType = TowerType.BASIC;
        saveManager.save(gold, waveManager.getCurrentWave());
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
            if (Math.sqrt(dx * dx + dy * dy) < 34) {
                return true;
            }
        }
        return false;
    }

    private boolean isOnPath(int mx, int my) {
        List<Point> waypoints = path.getWaypoints();
        for (int i = 0; i < waypoints.size() - 1; i++) {
            Point a = waypoints.get(i);
            Point b = waypoints.get(i + 1);
            if (distancePointToSegment(mx, my, a.x, a.y, b.x, b.y) <= 18) {
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

    public boolean canPlaceTowerAt(int x, int y) {
        if (y < TOP_UI_HEIGHT) {
            return false;
        }
        return !isOnPath(x, y) && !isTooCloseToOtherTower(x, y);
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

    public Path getPath() {
        return path;
    }

    public int getPlayerHp() {
        return playerHp;
    }

    public int getGold() {
        return gold;
    }

    public int getCurrentWave() {
        return waveManager.getCurrentWave();
    }

    public boolean isWaveRunning() {
        return waveManager.isWaveRunning();
    }

    public Rectangle getStartWaveButtonRect() {
        return startWaveButtonRect;
    }

    public Tower getSelectedTower() {
        return selectedTower;
    }

    public GameMap getMap() {
        return map;
    }

    public int getEnemiesToSpawn() {
        return waveManager.getEnemiesToSpawn();
    }

    public int getEnemiesRemainingInWave() {
        return waveManager.getEnemiesRemainingInWave(enemies);
    }

    public int getTotalEnemiesInWave() {
        return waveManager.getTotalEnemiesInWave();
    }

    public TowerType getSelectedBuildType() {
        return selectedBuildType;
    }

    public void setSelectedBuildType(TowerType selectedBuildType) {
        this.selectedBuildType = selectedBuildType;
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
