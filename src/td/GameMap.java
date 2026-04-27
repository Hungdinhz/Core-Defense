package td;

import java.awt.Color;
import java.awt.Point;
import java.util.List;

/**
 * Map: chứa waypoints (đường đi) và style nền.
 */
public class GameMap {
    private final String name;
    private final List<Point> waypoints;
    private final Color bgTop;
    private final Color bgBottom;

    public GameMap(String name, List<Point> waypoints, Color bgTop, Color bgBottom) {
        this.name = name;
        this.waypoints = waypoints;
        this.bgTop = bgTop;
        this.bgBottom = bgBottom;
    }

    public String getName() {
        return name;
    }

    public List<Point> getWaypoints() {
        return waypoints;
    }

    public Color getBgTop() {
        return bgTop;
    }

    public Color getBgBottom() {
        return bgBottom;
    }
}
