package map;

import java.awt.Color;
import java.awt.Point;
import java.util.List;

public class GameMap {
    private final String name;
    private final Path path;
    private final Color bgTop;
    private final Color bgBottom;

    public GameMap(String name, Path path, Color bgTop, Color bgBottom) {
        this.name = name;
        this.path = path;
        this.bgTop = bgTop;
        this.bgBottom = bgBottom;
    }

    public String getName() {
        return name;
    }

    public Path getPath() {
        return path;
    }

    public List<Point> getWaypoints() {
        return path.getWaypoints();
    }

    public Color getBgTop() {
        return bgTop;
    }

    public Color getBgBottom() {
        return bgBottom;
    }
}
