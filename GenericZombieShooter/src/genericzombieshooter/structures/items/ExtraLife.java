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
 * Used to increase the player's remaining lives by 1.
 * @author Darin Beaudreau
 */
public class ExtraLife extends Item {
    // Final Variables
    public static final int ID = 5;
    public static final String EFFECT_NAME = "Extra Life";
    
    public ExtraLife(Point2D.Double p) {
        super(ExtraLife.ID, ExtraLife.EFFECT_NAME, Images.EXTRA_LIFE);
        this.x = p.x;
        this.y = p.y;
    }
    
    @Override
    public void applyEffect(Player p) {
        p.addLife();
        Sounds.POWERUP.play();
    }
}
