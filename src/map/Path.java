package map;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Path {
    private final List<Point> waypoints;

    public Path(List<Point> waypoints) {
        this.waypoints = Collections.unmodifiableList(new ArrayList<>(waypoints));
    }

    public List<Point> getWaypoints() {
        return waypoints;
    }
}
