package td;

/**
 * Loại tower để dùng cho thanh chọn tower (build bar).
 */
public enum TowerType {
    BASIC("Basic"),
    SNIPER("Sniper"),
    RAPID("Rapid");

    private final String label;

    TowerType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public Tower create(int x, int y) {
        switch (this) {
            case SNIPER:
                return new SniperTower(x, y);
            case RAPID:
                return new RapidTower(x, y);
            case BASIC:
            default:
                return new BasicTower(x, y);
        }
    }

    public int getCost() {
        switch (this) {
            case SNIPER:
                return 140;
            case RAPID:
                return 100;
            case BASIC:
            default:
                return 80;
        }
    }
}
