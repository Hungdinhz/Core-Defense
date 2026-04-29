package systems;

import java.util.List;
import java.util.Random;

import enemies.BossEnemy;
import enemies.FastEnemy;
import enemies.NormalEnemy;
import enemies.TankEnemy;
import entities.Enemy;
import map.Path;

public class WaveManager {
    private final Random random;

    private int currentWave;
    private boolean waveRunning;
    private int enemiesToSpawn;
    private int totalEnemiesInWave;
    private int bossesToSpawn;
    private long lastSpawnTimeMs;
    private long spawnIntervalMs;

    public WaveManager(Random random, int savedWave) {
        this.random = random;
        this.currentWave = Math.max(0, savedWave);
        this.spawnIntervalMs = 900;
    }

    public void startWave() {
        if (waveRunning) {
            return;
        }
        currentWave++;
        enemiesToSpawn = 6 + currentWave * 2 + currentWave / 2;
        bossesToSpawn = currentWave % 5 == 0 ? 1 : 0;
        totalEnemiesInWave = enemiesToSpawn + bossesToSpawn;
        spawnIntervalMs = Math.max(260, 850 - currentWave * 35L);
        lastSpawnTimeMs = 0;
        waveRunning = true;
    }

    public void spawnEnemies(long now, List<Enemy> enemies, Path path) {
        if (!waveRunning || (enemiesToSpawn <= 0 && bossesToSpawn <= 0)) {
            return;
        }
        if (now - lastSpawnTimeMs < spawnIntervalMs) {
            return;
        }

        if (bossesToSpawn > 0) {
            enemies.add(new BossEnemy(path, currentWave));
            bossesToSpawn--;
        } else {
            enemies.add(createEnemyForWave(path));
            enemiesToSpawn--;
        }
        lastSpawnTimeMs = now;
    }

    private Enemy createEnemyForWave(Path path) {
        int roll = random.nextInt(100);
        if (currentWave >= 5 && roll < 24) {
            return new TankEnemy(path, currentWave);
        }
        if (currentWave >= 2 && roll < 60) {
            return new FastEnemy(path, currentWave);
        }
        return new NormalEnemy(path, currentWave);
    }

    public boolean finishIfDone(List<Enemy> enemies) {
        if (waveRunning && enemiesToSpawn == 0 && bossesToSpawn == 0 && enemies.isEmpty()) {
            waveRunning = false;
            return true;
        }
        return false;
    }

    public void reset() {
        currentWave = 0;
        waveRunning = false;
        enemiesToSpawn = 0;
        totalEnemiesInWave = 0;
        bossesToSpawn = 0;
        lastSpawnTimeMs = 0;
        spawnIntervalMs = 900;
    }

    public int getCurrentWave() {
        return currentWave;
    }

    public boolean isWaveRunning() {
        return waveRunning;
    }

    public int getEnemiesToSpawn() {
        return enemiesToSpawn;
    }

    public int getEnemiesRemainingInWave(List<Enemy> enemies) {
        return enemies.size() + enemiesToSpawn + bossesToSpawn;
    }

    public int getTotalEnemiesInWave() {
        return totalEnemiesInWave;
    }
}
