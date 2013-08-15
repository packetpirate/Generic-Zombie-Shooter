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

import genericzombieshooter.structures.weapons.AssaultRifle;
import genericzombieshooter.structures.weapons.Flamethrower;
import genericzombieshooter.structures.weapons.Grenade;
import genericzombieshooter.structures.weapons.Shotgun;
import java.awt.Point;
import java.util.Random;

/**
 * Contains publicly accessible variables for the entire project.
 * @author packetpirate
 */
public class Globals {
    // Final
    public static final int W_WIDTH = 800; // The width of the game window.
    public static final int W_HEIGHT = 640; // The height of the game window.
    public static final long SLEEP_TIME = 20; // The sleep time of the animation thread.
    public static final long SPAWN_TIME = 50; // The time it takes zombies to spawn. (SLEEP_TIME * SPAWN_TIME)
    public static final Random r = new Random();
    
    // Non-Final
    public static Runnable animation; // The primary animation thread.
    public static Runnable health; // The thread for spawning health packs.
    public static Runnable ammo; // The thread for spawning ammo packs.
    public static boolean running; // Whether or not the game is currently running.
    public static boolean started;
    public static boolean paused;
    
    public static boolean [] keys; // The state of the game key controls.
    public static boolean [] buttons; // The state of the game mouse button controls.
    
    public static Point mousePos; // The current position of the mouse on the screen.
    
    public static AssaultRifle ASSAULT_RIFLE = new AssaultRifle();
    public static Shotgun SHOTGUN = new Shotgun();
    public static Flamethrower FLAMETHROWER = new Flamethrower();
    public static Grenade GRENADE = new Grenade();
}
