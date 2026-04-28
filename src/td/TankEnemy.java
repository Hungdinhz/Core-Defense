package td;

import java.awt.Color;
import java.awt.Point;
import java.util.List;

/**
 * Enemy trâu: chậm hơn nhưng máu cao.
 */
public class TankEnemy extends Enemy {
    public TankEnemy(List<Point> path, int wave) {
        super(path, 350 + wave * 10, 0.7, 25, 2, 28, new Color(102, 102, 115));
        this.imageName = "enemy_tank";
    }
}
