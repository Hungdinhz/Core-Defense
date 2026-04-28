package td;

import java.awt.Color;
import java.awt.Point;
import java.util.List;

/**
 * Enemy nhanh nhưng máu thấp.
 */
public class FastEnemy extends Enemy {
    public FastEnemy(List<Point> path, int wave) {
        super(path, 75 + wave * 3, 2.3, 10, 1, 20, new Color(196, 179, 84));
        this.imageName = "enemy_fast";
    }
}
