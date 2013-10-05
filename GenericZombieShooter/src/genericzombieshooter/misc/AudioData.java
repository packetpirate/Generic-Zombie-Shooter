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

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

/**
 * Used to store and play audio data.
 * @author Darin Beaudreau
 */
public class AudioData {
    // Member Variables
    private byte[] data;
    private AudioFormat format;
    private boolean active;
    public boolean isActive() { return this.active; }
    public void setActive(boolean active_) { this.active = active_; }
    
    public AudioData(String filename) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(AudioData.class.getResource(filename));
            format = ais.getFormat();
            int size = ais.available();
            data = new byte[size];
            ais.read(data);
            ais.close();
        } catch (Exception e) {
            System.out.println("Problem loading audio data for: \"" + filename + "\"");
            e.printStackTrace();
        }
        active = false;
    }
    
    public Clip createClip() {
        try {
            final Clip clip = AudioSystem.getClip();
            clip.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent le) {
                    LineEvent.Type type = le.getType();
                    if(type == type.STOP) clip.close();
                }
            });
            clip.open(format, data, 0, data.length);
            return clip;
        } catch(Exception e) {
            System.out.println("Problem creating audio data clip...");
            e.printStackTrace();
        }
        return null;
    }
    
    public void play(final double gain, final boolean smoothLoop) {
        final Clip clip = createClip();
        FloatControl gainControl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
        float dB = (float)(Math.log(gain) / Math.log(10.0) * 20.0);
        gainControl.setValue(dB);
        new Thread(new Runnable() {
            @Override
            public void run() {
                active = true;
                if(!smoothLoop) clip.start();
                else clip.loop(Clip.LOOP_CONTINUOUSLY);
                while(active) {
                    if(!clip.isRunning() && !smoothLoop) active = false;
                }
                if(smoothLoop) clip.stop();
            }
        }).start();
    }
}
