package enemies;

import java.awt.Color;

import entities.Enemy;
import map.Path;

public class TankEnemy extends Enemy {
    public TankEnemy(Path path, int wave) {
        super(path, 350 + wave * 10, 0.7, 25, 2, 28, new Color(102, 102, 115));
        this.imageName = "enemy_tank";
    }
}
