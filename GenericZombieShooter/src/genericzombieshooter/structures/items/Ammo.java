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
 * Used to restore ammo to the player's weapons.
 * @author Darin Beaudreau
 */
public class Ammo extends Item {
    public static final int ID = 2;
    public static final long SPAWN_TIME = 15 * 1000;
    private String weapon;
    private int ammoCount;
    
    public Ammo(String weapon, int ammoCount, Point2D.Double p) {
        super(Ammo.ID, "Ammo Pack", Images.AMMO_PACK);
        this.weapon = weapon;
        this.ammoCount = ammoCount;
        this.x = p.x;
        this.y = p.y;
    }
    
    @Override
    public void applyEffect(Player player) {
        player.getWeapon(this.weapon).addAmmo(this.ammoCount);
        Sounds.POWERUP.play();
    }
}
