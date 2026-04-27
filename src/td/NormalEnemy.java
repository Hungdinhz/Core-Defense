package td;

import java.awt.Color;
import java.awt.Point;
import java.util.List;

/**
 * Enemy mặc định cân bằng.
 */
public class NormalEnemy extends Enemy {
    public NormalEnemy(List<Point> path, int wave) {
        super(path, 45 + wave * 6, 1.5, 12, 1, 22, new Color(220, 85, 85));
    }
}
