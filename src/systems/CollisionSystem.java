package systems;

import java.util.Iterator;
import java.util.List;

import entities.Bullet;
import entities.Effect;
import entities.Enemy;
import map.Path;

public class CollisionSystem {
    public int updateEnemies(List<Enemy> enemies, Path path) {
        int hpLost = 0;
        for (Enemy enemy : enemies) {
            boolean reachedEnd = enemy.update(path);
            if (reachedEnd && enemy.isAlive()) {
                hpLost += enemy.getDamageToPlayer();
                enemy.markEscaped();
                enemy.takeDamage(Integer.MAX_VALUE);
            }
        }
        return hpLost;
    }

    public int cleanupDeadEnemies(List<Enemy> enemies, List<Bullet> bullets, List<Effect> effects) {
        int earnedGold = 0;
        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            if (!enemy.isAlive()) {
                if (!enemy.isEscaped()) {
                    earnedGold += enemy.getRewardGold();
                    effects.add(new Effect(enemy.getX(), enemy.getY(), 24, enemy.getSize(), enemy.getColor()));
                }
                enemyIterator.remove();
            }
        }
        bullets.removeIf(bullet -> !bullet.isActive());
        return earnedGold;
    }

    public void updateBullets(List<Bullet> bullets) {
        for (Bullet bullet : bullets) {
            bullet.update();
        }
    }

    public void updateEffects(List<Effect> effects) {
        for (Effect effect : effects) {
            effect.update();
        }
        effects.removeIf(effect -> !effect.isAlive());
    }
}
