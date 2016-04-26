package Audio;

import javax.sound.sampled.*;
/**
 * Created by Krzysztof on 05.04.2016.
 */
public class AudioPlayer {

    private Clip clip;

    public AudioPlayer(String s) {

        try {

            AudioInputStream ais =
                    AudioSystem.getAudioInputStream(
                            getClass().getResourceAsStream(s)
                    );
            AudioFormat baseFormat = ais.getFormat();
            AudioFormat decodeFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(),
                    16,
                    baseFormat.getChannels(),
                    baseFormat.getChannels() * 2,
                    baseFormat.getSampleRate(),
                    false
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

    public void stop() {
        clip.stop();
    }

    public boolean isRunning(){
        return clip.isRunning();
    }

    public void close() {
        stop();
        clip.close();
    }

}














