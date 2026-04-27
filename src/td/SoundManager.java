package td;

import java.awt.Toolkit;

/**
 * Âm thanh đơn giản bằng beep hệ thống.
 * Không cần thêm file âm thanh ngoài để dễ chạy ở mọi máy.
 */
public class SoundManager {
    private boolean muted;

    public SoundManager() {
        this.muted = false;
    }

    public void toggleMute() {
        muted = !muted;
    }

    public boolean isMuted() {
        return muted;
    }

    public void playShoot() {
        if (muted) {
            return;
        }
        Toolkit.getDefaultToolkit().beep();
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
