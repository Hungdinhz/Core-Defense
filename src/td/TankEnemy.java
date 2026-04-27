package td;

import java.awt.Color;
import java.awt.Point;
import java.util.List;

/**
 * Enemy trâu: chậm hơn nhưng máu cao.
 */
public class TankEnemy extends Enemy {
    public TankEnemy(List<Point> path, int wave) {
        super(path, 90 + wave * 10, 1.0, 18, 2, 28, new Color(130, 130, 130));
    }
}
