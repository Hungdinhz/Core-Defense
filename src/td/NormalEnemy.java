package td;

import java.awt.Color;
import java.awt.Point;
import java.util.List;

/**
 * Enemy mặc định cân bằng.
 */
public class NormalEnemy extends Enemy {
    public NormalEnemy(List<Point> path, int wave) {
        super(path, 120 + wave * 6, 1.2, 12, 1, 24, new Color(184, 82, 82));
        this.imageName = "enemy_normal";
    }
}
