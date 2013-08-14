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

import genericzombieshooter.actors.Player;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 * Base class used to represent all items in the game.
 * @author Darin Beaudreau
 */
public abstract class Item extends Point2D.Double {
    private String name;
    public String getName() { return this.name; }
    protected BufferedImage icon;
    public BufferedImage getIcon() { return this.icon; }
    
    public Item(String name, BufferedImage icon) {
        this.name = name;
        this.icon = icon;
    }
    
    public void applyEffect(Player player) {
        // To be overridden.
    }
    
    public void draw(Graphics2D g2d) {
        int width = this.icon.getWidth();
        int height = this.icon.getHeight();
        g2d.drawImage(this.icon, (int)(this.x - (width / 2)), (int)(this.y - (height / 2)), null);
    }
}
