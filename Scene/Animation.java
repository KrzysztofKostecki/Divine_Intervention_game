package Scene;

import java.awt.image.BufferedImage;

/**
 * Klasa do obsługi animacji ruchu postaci.
 */
public class Animation {
    private BufferedImage[] frames;
    private int currentFrame;

    private long startTime;
    private long delay;

    private boolean playedOnce;

    /**
     * Ustawia flagę #playedOnce, określającą czy animacja została odegrana do końca.
     */
    public void Animation() {
        playedOnce = false;
    }

    /**
     * Ustawia klatkę animacji (każda klatka to inny sprite).
     * @param frames Tablica sprite'ów.
     */
    public void setFrames(BufferedImage[] frames) {
        this.frames = frames;
        currentFrame = 0;
        startTime = System.nanoTime();
        playedOnce = false;
    }

    /**
     * Ustawia opóźnienie.
     * @param d Opóźnienie.
     */
    public void setDelay(long d) { delay = d; }

    /**
     * Ustawia numer klatki.
     * @param i Numer klatki.
     */
    public void setFrame(int i) { currentFrame = i; }

    /**
     * Zwiększa obecny numer klatki.
     * <p>Jeśli postać jest w ostatniej fazie animacji, zeruje numer klatki i animacja zaczyna się od nowa.</p>
     */
    public void update() {

        if(delay == -1) return;

        long elapsed = (System.nanoTime() - startTime) / 1000000;
        if(elapsed > delay) {
            currentFrame++;
            startTime = System.nanoTime();
        }
        if(currentFrame == frames.length) {
            currentFrame = 0;
            playedOnce = true;
        }

    }

    /**
     * Zwraca obecny numer klatki.
     * @return Numer klatki.
     */
    public int getFrame() { return currentFrame; }

    /**
     * Zwraca obecną klatkę w postaci obiektu {@link BufferedImage} (grafiki).
     * @return Klatka.
     */
    public BufferedImage getImage() { return frames[currentFrame]; }

    /**
     * Zwraca flagę określającą, czy animacja została odegrana do końca.
     * @return True jeśli została odegrana do końca.
     */
    public boolean hasPlayedOnce() { return playedOnce; }
}
