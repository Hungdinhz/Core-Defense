package towers;

import java.awt.Color;

import entities.Tower;

public class SniperTower extends Tower {
    public SniperTower(int x, int y) {
        super(x, y);
        this.range = 190;
        this.damage = 38;
        this.fireRatePerSecond = 0.65;
        this.outerColor = new Color(62, 131, 199);
        this.innerColor = new Color(34, 86, 136);
        this.imageName = "tower_sniper";
    }

    @Override
    public String getName() {
        return "Sniper";
    }

    @Override
    public int getCost() {
        return 140;
    }
}
