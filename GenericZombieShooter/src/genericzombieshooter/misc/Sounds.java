/**
    This file is part of Generic Zombie Shooter.

    Generic Zombie Shooter is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Generic Zombie Shooter is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Generic Zombie Shooter.  If not, see <http://www.gnu.org/licenses/>.
 **/
package genericzombieshooter.misc;

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Contains all pre-loaded sounds.
 * @author Darin Beaudreau
 */
public enum Sounds {
    // Weapon-Related
    POPGUN("shoot2.wav", false),
    RTPS("shoot1.wav", false),
    BOOMSTICK("shotgun1.wav", false),
    FLAMETHROWER("flamethrower.wav", true),
    THROW("throw2.wav", false),
    EXPLOSION("explosion2.wav", false),
    LANDMINE_ARMED("landmine_armed.wav", false),
    TELEPORT("teleport.wav", false),
    
    // Zombie-Related
    MOAN1("zombie_moan_01.wav", false),
    MOAN2("zombie_moan_02.wav", false),
    MOAN3("zombie_moan_03.wav", false),
    MOAN4("zombie_moan_04.wav", false),
    MOAN5("zombie_moan_05.wav", false),
    MOAN6("zombie_moan_06.wav", false),
    MOAN7("zombie_moan_07.wav", false),
    MOAN8("zombie_moan_08.wav", false),
    POISONCLOUD("poison_cloud.wav", false),
    
    // Game Sounds
    POWERUP("powerup.wav", false),
    PURCHASEWEAPON("purchase_weapon.wav", false),
    BUYAMMO("buy_ammo2.wav", false),
    POINTBUY("point_buy.wav", false),
    PAUSE("pause.wav", false),
    UNPAUSE("unpause.wav", false);
    
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
        play(1.0);
    }
    
    public synchronized void play(final double gain) {
        Runnable soundPlay = new Runnable() {
            @Override
            public void run() {
                Clip clipCopy = (Clip)clip;
                FloatControl gainControl = (FloatControl)clipCopy.getControl(FloatControl.Type.MASTER_GAIN);
                float dB = (float)(Math.log(gain) / Math.log(10.0) * 20.0);
                gainControl.setValue(dB);
                if(!looped) reset(clipCopy);
                clipCopy.loop((looped)?Clip.LOOP_CONTINUOUSLY:0);
                
            }
        };
        new Thread(soundPlay).start();
    }
    
    public synchronized void reset() {
        reset(clip);
    }
    
    public synchronized void reset(Clip clipCopy) {
        synchronized(clipCopy) { 
            clipCopy.stop(); 
            clipCopy.setFramePosition(0);
        }
    }

    public static void init() {
        values();
    }
}
