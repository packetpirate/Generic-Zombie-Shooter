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

import genericzombieshooter.structures.GameTime;
import genericzombieshooter.structures.Message;
import genericzombieshooter.structures.weapons.AssaultRifle;
import genericzombieshooter.structures.weapons.Flamethrower;
import genericzombieshooter.structures.weapons.Flare;
import genericzombieshooter.structures.weapons.Grenade;
import genericzombieshooter.structures.weapons.Handgun;
import genericzombieshooter.structures.weapons.Landmine;
import genericzombieshooter.structures.weapons.LaserWire;
import genericzombieshooter.structures.weapons.Shotgun;
import genericzombieshooter.structures.weapons.Teleporter;
import genericzombieshooter.structures.weapons.TurretWeapon;
import genericzombieshooter.structures.weapons.Weapon;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Contains publicly accessible variables for the entire project.
 * @author Darin Beaudreau
 */
public class Globals {
    // Game Information
    public static final String VERSION = "1.0";
    public static final int W_WIDTH = 800; // The width of the game window.
    public static final int W_HEIGHT = 640; // The height of the game window.
    public static final long SLEEP_TIME = 20; // The sleep time of the animation thread.
    public static final long WAVE_BREAK_TIME = 30 * 1000;
    public static final Random r = new Random();
    public static List<Message> GAME_MESSAGES = new ArrayList<Message>();
    
    // Zombie Information
    public static final int ZOMBIE_REGULAR_TYPE = 1;
    public static final long ZOMBIE_REGULAR_SPAWN = 1000;
    public static final int ZOMBIE_DOG_TYPE = 2;
    public static final long ZOMBIE_DOG_SPAWN = 10000;
    public static final int ZOMBIE_ACID_TYPE = 3;
    public static final long ZOMBIE_ACID_SPAWN = 30000;
    public static final int ZOMBIE_POISONFOG_TYPE = 4;
    public static final long ZOMBIE_POISONFOG_SPAWN = 20000;
    public static final int ZOMBIE_MATRON_TYPE = 5;
    public static final long ZOMBIE_MATRON_SPAWN = 50000;
    public static final int ZOMBIE_TINY_TYPE = 6;
    
    // Boss Information
    public static final int ZOMBIE_BOSS_ABERRATION_TYPE = 7;
    public static final int ZOMBIE_BOSS_ZOMBAT_TYPE = 8;
    public static final int ZOMBIE_BOSS_STITCHES_TYPE = 9;
    
    // Game-State Related
    public static Runnable animation; // The primary animation thread.
    public static GameTime gameTime; // Used to keep track of the time.
    
    public static boolean running; // Whether or not the game is currently running.
    public static boolean started;
    public static boolean crashed; // Tells the game whether or not there was a crash.
    public static boolean paused;
    public static boolean storeOpen;
    public static boolean levelScreenOpen;
    public static boolean deathScreen;
    public static boolean waveInProgress; // Whether the player is fighting or waiting for another wave.
    public static long nextWave;
    
    // Input Related
    public static boolean [] keys; // The state of the game key controls.
    public static boolean [] buttons; // The state of the game mouse button controls.
    public static Point mousePos; // The current position of the mouse on the screen.
    
    // Static Weapons
    public static Handgun HANDGUN = new Handgun();
    public static AssaultRifle ASSAULT_RIFLE = new AssaultRifle();
    public static Shotgun SHOTGUN = new Shotgun();
    public static Flamethrower FLAMETHROWER = new Flamethrower();
    public static Grenade GRENADE = new Grenade();
    public static Landmine LANDMINE = new Landmine();
    public static Flare FLARE = new Flare();
    public static LaserWire LASERWIRE = new LaserWire();
    public static TurretWeapon TURRETWEAPON = new TurretWeapon();
    public static Teleporter TELEPORTER = new Teleporter();
    
    public static Weapon getWeaponByName(String name) {
        if(name.equals(Globals.HANDGUN.getName())) return Globals.HANDGUN;
        else if(name.equals(Globals.ASSAULT_RIFLE.getName())) return Globals.ASSAULT_RIFLE;
        else if(name.equals(Globals.SHOTGUN.getName())) return Globals.SHOTGUN;
        else if(name.equals(Globals.FLAMETHROWER.getName())) return Globals.FLAMETHROWER;
        else if(name.equals(Globals.GRENADE.getName())) return Globals.GRENADE;
        else if(name.equals(Globals.LANDMINE.getName())) return Globals.LANDMINE;
        else if(name.equals(Globals.FLARE.getName())) return Globals.FLARE;
        else if(name.equals(Globals.LASERWIRE.getName())) return Globals.LASERWIRE;
        else if(name.equals(Globals.TURRETWEAPON.getName())) return Globals.TURRETWEAPON;
        else if(name.equals(Globals.TELEPORTER.getName())) return Globals.TELEPORTER;
        else return null;
    }
    
    public static void resetWeapons() {
        Globals.HANDGUN.resetAmmo();
        Globals.ASSAULT_RIFLE.resetAmmo();
        Globals.SHOTGUN.resetAmmo();
        Globals.FLAMETHROWER.resetAmmo();
        Globals.GRENADE.resetAmmo();
        Globals.LANDMINE.resetAmmo();
        Globals.FLARE.resetAmmo();
        Globals.LASERWIRE.resetAmmo();
        Globals.TURRETWEAPON.resetAmmo();
        Globals.TELEPORTER.resetAmmo();
    }
}
