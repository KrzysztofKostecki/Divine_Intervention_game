package Audio;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;

/**
 * Klasa zawierająca metody odpowiedzialne za odczytywanie audio z plików i ich odtwarzanie.
 * @see AudioInputStream
 * @see AudioSystem
 */
public class AudioPlayer {

    private Clip clip;

    /**
     * Odczytuje audio z pliku z podanej w argumencie ścieżki.
     * Uzyskuje klip, który może być wykorzystywany do odtwarzania plików dźwiękowych lub strumienia audio.
     * @param s Ścieżka do utworu.
     */

    public AudioPlayer(String s) {

        try {

            URL url = AudioPlayer.class.getResource(s);
            AudioInputStream ais =  AudioSystem.getAudioInputStream(url);

            AudioFormat baseFormat = ais.getFormat();
            AudioFormat decodeFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(),
                    16,
                    baseFormat.getChannels(),
                    baseFormat.getChannels() * 2,
                    baseFormat.getSampleRate(),
                    baseFormat.isBigEndian()
            );
            AudioInputStream dais =
                    AudioSystem.getAudioInputStream(
                            decodeFormat, ais);
            clip = AudioSystem.getClip();
            clip.open(dais);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Rozpoczyna odtwarzanie utworu.
     */
    public void play() {
        if(!isRunning()) {
            if (clip == null) return;
            stop();
            clip.setFramePosition(0);
            clip.start();
        }else {
            return;
        }
    }

    /**
     * Kończy odtwarzanie utworu.
     */
    public void stop() {
        clip.stop();
    }

    /**
     * Zwraca informację, czy utwór jest odtwarzany.
     * @return True jeśli jest odwarzany.
     */
    public boolean isRunning(){
        return clip.isRunning();
    }

    /**
     * Wywołuje metodę {@link #stop()} i zamyka clip.
     */
    public void close() {
        stop();
        clip.close();
    }

}














