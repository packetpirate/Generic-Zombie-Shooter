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
    public static final BufferedImage START_SCREEN = GZSFramework.loadImage("/resources/images/GZS_Splash.png");
    public static final BufferedImage BACKGROUND = GZSFramework.loadImage("/resources/images/GZS_Background_2.png");
    public static final BufferedImage PLAYER = GZSFramework.loadImage("/resources/images/GZS_Player.png");
    public static final BufferedImage HEALTH_PACK = GZSFramework.loadImage("/resources/images/GZS_Health.png");
    public static final BufferedImage AMMO_PACK = GZSFramework.loadImage("/resources/images/GZS_Ammo.png");
    public static final BufferedImage FIRE_PARTICLE = GZSFramework.loadImage("/resources/images/GZS_FireParticle.png");
}