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

import genericzombieshooter.GZSFramework;
import java.awt.image.BufferedImage;

/**
 * Contains constants of images so they don't need to be loaded multiple times.
 * @author Darin Beaudreau
 */
public class Images {
    // Background Images
    public static final BufferedImage START_SCREEN = GZSFramework.loadImage("/resources/images/GZS_Splash.png");
    public static final BufferedImage BACKGROUND = GZSFramework.loadImage("/resources/images/GZS_Background_3.png");
    public static final BufferedImage DEATH_SCREEN = GZSFramework.loadImage("/resources/images/GZS_DeathScreen.png");
    
    // Player-Related
    public static final BufferedImage PLAYER = GZSFramework.loadImage("/resources/images/GZS_Player.png");
    public static final BufferedImage CROSSHAIR = GZSFramework.loadImage("/resources/images/GZS_Crosshair.png");
    
    // Zombie-Related
    public static final BufferedImage ZOMBIE_REGULAR = GZSFramework.loadImage("/resources/images/GZS_Zombie_2.png");
    public static final BufferedImage ZOMBIE_DOG = GZSFramework.loadImage("/resources/images/GZS_ZombieDog2.png");
    public static final BufferedImage ZOMBIE_ACID = GZSFramework.loadImage("/resources/images/GZS_AcidZombie.png");
    public static final BufferedImage ZOMBIE_POISONFOG = GZSFramework.loadImage("/resources/images/GZS_ZombieExplosive.png");
    public static final BufferedImage ZOMBIE_MATRON = GZSFramework.loadImage("/resources/images/GZS_ZombieMatron.png");
    public static final BufferedImage ZOMBIE_TINY = GZSFramework.loadImage("/resources/images/GZS_ZombieTiny.png");
    public static final BufferedImage ACID_PARTICLE = GZSFramework.loadImage("/resources/images/GZS_AcidParticle.png");
    public static final BufferedImage POISON_GAS_SHEET = GZSFramework.loadImage("/resources/images/GZS_PoisonExplosion.png");
    public static final BufferedImage POISON_STATUS_ICON = GZSFramework.loadImage("/resources/images/GZS_PoisonIcon.png");
    public static final BufferedImage BLOOD_SHEET = GZSFramework.loadImage("/resources/images/GZS_BloodExplosion.png");
    
    // Power-Up Related
    public static final BufferedImage HEALTH_PACK = GZSFramework.loadImage("/resources/images/GZS_Health.png");
    public static final BufferedImage AMMO_PACK = GZSFramework.loadImage("/resources/images/GZS_Ammo.png");
    public static final BufferedImage SPEED_UP = GZSFramework.loadImage("/resources/images/GZS_SpeedUp.png");
    public static final BufferedImage UNLIMITED_AMMO = GZSFramework.loadImage("/resources/images/GZS_UnlimitedAmmo.png");
    public static final BufferedImage EXTRA_LIFE = GZSFramework.loadImage("/resources/images/GZS_ExtraLife.png");
    public static final BufferedImage EXP_MULTIPLIER = GZSFramework.loadImage("/resources/images/GZS_ExpMultiplier.png");
    
    // Ammo-Related
    public static final BufferedImage POPGUN_BULLET = GZSFramework.loadImage("/resources/images/GZS_Bullet.png");
    public static final BufferedImage RTPS_BULLET = GZSFramework.loadImage("/resources/images/GZS_Bullet2.png");
    public static final BufferedImage FIRE_PARTICLE = GZSFramework.loadImage("/resources/images/GZS_FireParticle.png");
    public static final BufferedImage GRENADE_PARTICLE = GZSFramework.loadImage("/resources/images/GZS_GrenadeParticle.png");
    public static final BufferedImage LANDMINE_PARTICLE = GZSFramework.loadImage("/resources/images/GZS_LandmineParticle.png");
    public static final BufferedImage FLARE_PARTICLE = GZSFramework.loadImage("/resources/images/GZS_FlareParticle.png");
    public static final BufferedImage LASER_TERMINAL = GZSFramework.loadImage("/resources/images/GZS_LaserTerminal.png");
    public static final BufferedImage EXPLOSION_SHEET = GZSFramework.loadImage("/resources/images/GZS_Explosion.png");
}
