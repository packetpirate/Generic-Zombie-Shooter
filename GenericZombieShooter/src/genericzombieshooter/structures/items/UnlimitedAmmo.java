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
package genericzombieshooter.structures.items;

import genericzombieshooter.actors.Player;
import genericzombieshooter.misc.Images;
import genericzombieshooter.misc.Sounds;
import genericzombieshooter.structures.Item;
import java.awt.geom.Point2D;

/**
 * Used to temporarily remove ammo consumption of certain player weapons.
 * @author Darin Beaudreau
 */
public class UnlimitedAmmo extends Item {
    // Final Variables
    public static final int ID = 4;
    public static final String EFFECT_NAME = "Unlimited Ammo";
    public static final long DURATION = 10 * 1000;
    
    public UnlimitedAmmo(Point2D.Double p) {
        super(UnlimitedAmmo.ID, UnlimitedAmmo.EFFECT_NAME, Images.UNLIMITED_AMMO);
        this.x = p.x;
        this.y = p.y;
    }
    
    @Override
    public void applyEffect(Player p) {
        p.addStatusEffect(UnlimitedAmmo.ID, this.getName(), UnlimitedAmmo.DURATION, 0);
        Sounds.POWERUP.play();
    }
}
