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
package genericzombieshooter.structures;

import genericzombieshooter.misc.Globals;
import java.awt.image.BufferedImage;

/**
 * Used to manage status effects on the player.
 * @author Darin Beaudreau
 */
public class StatusEffect {
    // Member Variables
    private BufferedImage img;
    public BufferedImage getImage() { return this.img; }
    private long endTime;
    public long getEndTime() { return this.endTime; }
    public void refresh(long duration) { this.endTime = Globals.gameTime.getElapsedMillis() + duration; }
    public boolean isActive() { return (Globals.gameTime.getElapsedMillis() < this.endTime); }
    private int value;
    public long getValue() { return this.value; }
    
    public StatusEffect(BufferedImage img, long duration, int value) {
        this.img = img;
        this.endTime = Globals.gameTime.getElapsedMillis() + duration;
        this.value = value;
    }
}
