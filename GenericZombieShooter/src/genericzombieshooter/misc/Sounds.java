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
    
    private AudioData audio;
    public AudioData getAudio() { return this.audio; }
    private boolean looped; 

    Sounds(String filename, boolean loop) {
        openClip(filename, loop);
    }

    private synchronized void openClip(String filename, boolean loop) {
        audio = new AudioData("/resources/sounds/" + filename);
        looped = loop;
    }
    
    public synchronized void play() {
        play(1.0);
    }
    
    public synchronized void play(final double gain) {
        audio.play(gain, looped);
    }

    public static void init() {
        values();
    }
}
