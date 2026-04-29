package towers;

import java.awt.Color;

import entities.Tower;

public class RapidTower extends Tower {
    public RapidTower(int x, int y) {
        super(x, y);
        this.range = 95;
        this.damage = 8;
        this.fireRatePerSecond = 3.2;
        this.outerColor = new Color(219, 166, 62);
        this.innerColor = new Color(153, 110, 35);
        this.imageName = "tower_rapid";
    }

    @Override
    public String getName() {
        return "Rapid";
    }

    @Override
    public int getCost() {
        return 100;
    }
}
