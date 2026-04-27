package td;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Danh sách map có sẵn (ít nhất 3 map).
 */
public class GameMaps {
    private static final List<GameMap> MAPS = createMaps();

    public static List<GameMap> getAll() {
        return MAPS;
    }

    public static GameMap getDefault() {
        return MAPS.get(0);
    }

    private static List<GameMap> createMaps() {
        List<GameMap> maps = new ArrayList<>();

        // Map 1: đường đơn giản (dễ) - dùng path cũ.
        List<Point> map1 = new ArrayList<>();
        map1.add(new Point(40, 180));
        map1.add(new Point(200, 180));
        map1.add(new Point(200, 420));
        map1.add(new Point(420, 420));
        map1.add(new Point(420, 150));
        map1.add(new Point(690, 150));
        map1.add(new Point(690, 360));
        map1.add(new Point(860, 360));
        maps.add(new GameMap("Meadow (Easy)", map1, new Color(201, 236, 193), new Color(164, 214, 164)));

        // Map 2: zigzag (trung bình).
        List<Point> map2 = new ArrayList<>();
        map2.add(new Point(40, 140));
        map2.add(new Point(260, 140));
        map2.add(new Point(260, 250));
        map2.add(new Point(120, 250));
        map2.add(new Point(120, 380));
        map2.add(new Point(360, 380));
        map2.add(new Point(360, 160));
        map2.add(new Point(560, 160));
        map2.add(new Point(560, 430));
        map2.add(new Point(860, 430));
        maps.add(new GameMap("Canyon Zigzag (Medium)", map2, new Color(235, 222, 197), new Color(207, 178, 132)));

        // Map 3: vòng lặp/cắt nhau (khó) - vẫn là 1 path nhưng nhiều khúc quanh.
        List<Point> map3 = new ArrayList<>();
        map3.add(new Point(40, 320));
        map3.add(new Point(200, 320));
        map3.add(new Point(200, 140));
        map3.add(new Point(420, 140));
        map3.add(new Point(420, 460));
        map3.add(new Point(260, 460));
        map3.add(new Point(260, 240));
        map3.add(new Point(620, 240));
        map3.add(new Point(620, 420));
        map3.add(new Point(480, 420));
        map3.add(new Point(480, 180));
        map3.add(new Point(860, 180));
        maps.add(new GameMap("Labyrinth Loop (Hard)", map3, new Color(203, 214, 242), new Color(157, 173, 219)));

        return Collections.unmodifiableList(maps);
    }
}
