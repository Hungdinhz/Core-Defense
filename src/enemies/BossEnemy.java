package enemies;

import java.awt.Color;

import entities.Enemy;
import map.Path;

public class BossEnemy extends Enemy {
    public BossEnemy(Path path, int wave) {
        super(path, 1200 + wave * 40, 0.55, 100, 10, 42, new Color(133, 40, 161));
        this.imageName = "enemy_boss";
    }
}
