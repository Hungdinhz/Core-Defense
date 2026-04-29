package utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class SaveManager {
    private static final String SAVE_FILE = "savegame.properties";

    public SaveData load() {
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream(SAVE_FILE)) {
            props.load(in);
            int gold = Integer.parseInt(props.getProperty("gold", "180"));
            int wave = Integer.parseInt(props.getProperty("wave", "0"));
            return new SaveData(gold, wave);
        } catch (IOException | NumberFormatException ignored) {
            return new SaveData(180, 0);
        }
    }

    public void save(int gold, int wave) {
        Properties props = new Properties();
        props.setProperty("gold", Integer.toString(gold));
        props.setProperty("wave", Integer.toString(wave));
        try (FileOutputStream out = new FileOutputStream(SAVE_FILE)) {
            props.store(out, "Tower Defense Save Data");
        } catch (IOException ignored) {
        }
    }

    public static class SaveData {
        public final int gold;
        public final int wave;

        public SaveData(int gold, int wave) {
            this.gold = gold;
            this.wave = wave;
        }
    }
}
