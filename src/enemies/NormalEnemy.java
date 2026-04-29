package enemies;

import java.awt.Color;

import entities.Enemy;
import map.Path;

public class NormalEnemy extends Enemy {
    public NormalEnemy(Path path, int wave) {
        super(path, 120 + wave * 6, 1.2, 12, 1, 24, new Color(184, 82, 82));
        this.imageName = "enemy_normal";
    }
}
