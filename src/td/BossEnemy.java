package td;

import java.awt.Color;
import java.awt.Point;
import java.util.List;

/**
 * Boss enemy: rất trâu, dùng cho wave đặc biệt.
 */
public class BossEnemy extends Enemy {
    public BossEnemy(List<Point> path, int wave) {
        super(path, 1200 + wave * 40, 0.55, 100, 10, 42, new Color(133, 40, 161));
        this.imageName = "enemy_boss";
    }
}
