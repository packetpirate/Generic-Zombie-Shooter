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

import kuusisto.tinysound.Music;
import kuusisto.tinysound.TinySound;

/**
 * Contains all pre-loaded sounds.
 * @author Darin Beaudreau
 */
public enum Sounds {
    // Weapon-Related
    POPGUN("shoot2.wav", false, false),
    RTPS("shoot3.wav", false, false),
    BOOMSTICK("shotgun1.wav", false, false),
    FLAMETHROWER("flamethrower.wav", true, false),
    THROW("throw2.wav", false, false),
    EXPLOSION("explosion2.wav", false, false),
    LANDMINE_ARMED("landmine_armed.wav", false, false),
    TELEPORT("teleport.wav", false, false),
    
    // Zombie-Related
    MOAN1("zombie_moan_01.wav", false, true),
    MOAN2("zombie_moan_02.wav", false, true),
    MOAN3("zombie_moan_03.wav", false, true),
    MOAN4("zombie_moan_04.wav", false, true),
    MOAN5("zombie_moan_05.wav", false, true),
    MOAN6("zombie_moan_06.wav", false, true),
    MOAN7("zombie_moan_07.wav", false, true),
    MOAN8("zombie_moan_08.wav", false, true),
    POISONCLOUD("poison_cloud.wav", false, false),
    
    // Game Sounds
    POWERUP("powerup.wav", false, false),
    PURCHASEWEAPON("purchase_weapon.wav", false, true),
    BUYAMMO("buy_ammo2.wav", false, true),
    POINTBUY("point_buy.wav", false, true),
    PAUSE("pause.wav", false, false),
    UNPAUSE("unpause.wav", false, false);
    
    private String file;
    private Music audio;
    public Music getAudio() { return this.audio; }
    private boolean looped; 
    private boolean overlap;

    Sounds(String filename, boolean loop, boolean over) {
        openClip(filename, loop, over);
    }

    private synchronized void openClip(String filename, boolean loop, boolean over) {
        file = filename;
        audio = TinySound.loadMusic("/resources/sounds/" + filename);
        looped = loop;
        overlap = over;
    }
    
    public synchronized void play() {
        play(1.0);
    }
    
    public synchronized void play(final double gain) {
        // If the clip supports overlapping, create a new Music object to use.
        if(overlap) {
            Music m = TinySound.loadMusic("/resources/sounds/" + file);
            m.rewind();
            m.play(looped, gain);
        } else {
            audio.rewind();
            audio.play(looped, gain);
        }
    }

    public static void init() {
        values();
    }
}
