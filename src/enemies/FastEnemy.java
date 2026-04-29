package enemies;

import java.awt.Color;

import entities.Enemy;
import map.Path;

public class FastEnemy extends Enemy {
    public FastEnemy(Path path, int wave) {
        super(path, 75 + wave * 3, 2.3, 10, 1, 20, new Color(196, 179, 84));
        this.imageName = "enemy_fast";
    }
}
