package td;

import java.awt.Color;
import java.awt.Point;
import java.util.List;

/**
 * Enemy nhanh nhưng máu thấp.
 */
public class FastEnemy extends Enemy {
    public FastEnemy(List<Point> path, int wave) {
        super(path, 25 + wave * 3, 2.6, 8, 1, 18, new Color(66, 135, 245));
    }
}
