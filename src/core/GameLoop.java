package core;

import java.awt.event.ActionListener;

import javax.swing.Timer;

public class GameLoop {
    private final Timer timer;

    public GameLoop(int delayMs, ActionListener actionListener) {
        timer = new Timer(delayMs, actionListener);
    }

    public void start() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }
}
