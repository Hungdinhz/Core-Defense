package td;

import java.awt.Color;

/**
 * Basic Tower: damage và fire rate trung bình.
 */
public class BasicTower extends Tower {
    public BasicTower(int x, int y) {
        super(x, y);
        this.range = 110;
        this.damage = 15;
        this.fireRatePerSecond = 1.3;
        this.outerColor = new Color(49, 143, 67);
        this.innerColor = new Color(35, 103, 48);
    }

    @Override
    public String getName() {
        return "Basic";
    }

    @Override
    public int getCost() {
        return 80;
    }
}
