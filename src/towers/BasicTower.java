package towers;

import java.awt.Color;

import entities.Tower;

public class BasicTower extends Tower {
    public BasicTower(int x, int y) {
        super(x, y);
        this.range = 110;
        this.damage = 15;
        this.fireRatePerSecond = 1.3;
        this.outerColor = new Color(49, 143, 67);
        this.innerColor = new Color(35, 103, 48);
        this.imageName = "tower_basic";
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
