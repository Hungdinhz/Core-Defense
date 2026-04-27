package td;

import java.awt.Color;
import java.awt.Point;
import java.util.List;

/**
 * Boss enemy: rất trâu, dùng cho wave đặc biệt.
 */
public class BossEnemy extends Enemy {
    public BossEnemy(List<Point> path, int wave) {
        super(path, 420 + wave * 40, 0.8, 60, 5, 38, new Color(116, 72, 184));
    }
}
