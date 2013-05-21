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
    RIFLE("rifle_fire.wav");
    
    private Clip clip;

    Sounds(String filename) {
        openClip(filename);
    }

    private synchronized void openClip(String filename) {
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
    }

    public synchronized void play() {
        Runnable soundPlay = new Runnable() {
            @Override
            public void run() {
                synchronized(clip) { clip.stop(); }
                //if(clip.isRunning()) clip.stop();
                clip.setFramePosition(0);
                clip.start();
            }
        };
        new Thread(soundPlay).start();
    }

    public static void init() {
        values();
    }
}
