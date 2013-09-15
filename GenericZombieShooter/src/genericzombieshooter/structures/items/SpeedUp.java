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
import genericzombieshooter.misc.Globals;
import genericzombieshooter.misc.Images;
import genericzombieshooter.misc.Sounds;
import genericzombieshooter.structures.Item;
import genericzombieshooter.structures.Message;
import java.awt.geom.Point2D;

/**
 * Used to increase the player's speed temporarily.
 * @author Darin Beaudreau
 */
public class SpeedUp extends Item {
    // Final Variables
    public static final int ID = 3;
    public static final String EFFECT_NAME = "SpeedUp";
    public static final long ITEM_DURATION = 15 * 1000;
    public static final long DURATION = 10 * 1000;
    public static final int SPEED_MULTIPLIER = 2;
    
    public SpeedUp(Point2D.Double p) {
        super(SpeedUp.ID, SpeedUp.EFFECT_NAME, Images.SPEED_UP, SpeedUp.DURATION);
        this.x = p.x;
        this.y = p.y;
    }
    
    @Override
    public void applyEffect(Player p) {
        p.addStatusEffect(SpeedUp.ID, this.getName(), Images.SPEED_UP, SpeedUp.DURATION, SpeedUp.SPEED_MULTIPLIER);
        synchronized(Globals.GAME_MESSAGES) {
            Globals.GAME_MESSAGES.add(new Message("Player speed doubled for " + SpeedUp.DURATION + " seconds!", 5000));
        }
        Sounds.POWERUP.play();
    }
}
