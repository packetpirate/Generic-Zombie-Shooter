package genericzombieshooter.misc;

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Contains all pre-loaded sounds.
 *
 * @author packetpirate
 *
 */
public enum Sounds {
    RTPS("shoot1.wav", false),
    BOOMSTICK("shotgun1.wav", false),
    FLAMETHROWER("flamethrower2.wav", true),
    EXPLOSION("explosion.wav", false),
    POWERUP("powerup.wav", false);
    
    private Clip clip;
    private boolean looped;

    Sounds(String filename, boolean loop) {
        openClip(filename, loop);
    }

    private synchronized void openClip(String filename, boolean loop) {
        try {
            URL audioFile = Sounds.class.getResource("/resources/sounds/" + filename);

            AudioInputStream audio = AudioSystem.getAudioInputStream(audioFile);
            AudioFormat format = audio.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            clip = (Clip) AudioSystem.getLine(info);

            clip.open(audio);
        } catch (UnsupportedAudioFileException uae) {
            System.out.println(uae);
        } catch (IOException ioe) {
            System.out.println(ioe);
        } catch (LineUnavailableException lue) {
            System.out.println(lue);
        }
        looped = loop;
    }

    public synchronized void play() {
        Runnable soundPlay = new Runnable() {
            @Override
            public void run() {
                //if(clip.isRunning()) clip.stop();
                if(!looped) reset();
                clip.loop((looped)?Clip.LOOP_CONTINUOUSLY:0);
                
            }
        };
        new Thread(soundPlay).start();
    }
    
    public synchronized void reset() {
        synchronized(clip) { clip.stop(); }
        clip.setFramePosition(0);
    }

    public static void init() {
        values();
    }
}
