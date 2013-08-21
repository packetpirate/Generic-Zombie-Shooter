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
 * Used to represent health packs that spawn on the ground.
 * @author Darin Beaudreau
 */
public class HealthPack extends Item {
    public static final long SPAWN_TIME = 20 * 1000;
    private int healAmount;
    
    public HealthPack(int healAmount, Point2D.Double p) {
        super("Health Pack", Images.HEALTH_PACK);
        this.healAmount = healAmount;
        this.x = p.x;
        this.y = p.y;
    }
    
    @Override
    public void applyEffect(Player player) {
        player.addHealth(this.healAmount);
        player.removePoison();
        Sounds.POWERUP.play();
    }
}
