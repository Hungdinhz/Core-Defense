package td;

/**
 * Chế độ chọn mục tiêu của tower.
 */
public enum TargetingMode {
    NEAREST("Nearest"),
    LOWEST_HP("Lowest HP"),
    FARTHEST_PROGRESS("Farthest");

    private final String label;

    TargetingMode(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
