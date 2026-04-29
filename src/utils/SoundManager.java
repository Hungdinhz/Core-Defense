package utils;

import java.awt.Toolkit;

public class SoundManager {
    private boolean muted;

    public void toggleMute() {
        muted = !muted;
    }

    public boolean isMuted() {
        return muted;
    }

    public void playShoot() {
        if (!muted) {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    public void playEnemyDeath() {
        if (muted) {
            return;
        }

        new Thread(() -> {
            Toolkit.getDefaultToolkit().beep();
            sleepQuietly(40);
            Toolkit.getDefaultToolkit().beep();
        }, "sound-enemy-death").start();
    }

    public void playGameOver() {
        if (muted) {
            return;
        }

        new Thread(() -> {
            Toolkit.getDefaultToolkit().beep();
            sleepQuietly(120);
            Toolkit.getDefaultToolkit().beep();
            sleepQuietly(120);
            Toolkit.getDefaultToolkit().beep();
        }, "sound-game-over").start();
    }

    private void sleepQuietly(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
}
