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
import genericzombieshooter.misc.Globals;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 * Base class used to represent all items in the game.
 * @author Darin Beaudreau
 */
public abstract class Item extends Point2D.Double {
    private int id;
    public int getId() { return this.id; }
    private String name;
    public String getName() { return this.name; }
    protected BufferedImage icon;
    public BufferedImage getIcon() { return this.icon; }
    private long expirationTime;
    public boolean isActive() { return (Globals.gameTime.getElapsedMillis() < this.expirationTime); }
    
    private boolean blink;
    private long nextBlinkChange;
    
    public Item(int id, String name, BufferedImage icon, long duration) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.expirationTime = Globals.gameTime.getElapsedMillis() + duration;
        
        this.blink = false;
        this.nextBlinkChange = Globals.gameTime.getElapsedMillis();
    }
    
    public void applyEffect(Player player) {
        // To be overridden.
    }
    
    public void draw(Graphics2D g2d) {
        // Check if item should start blinking.
        if(Globals.gameTime.getElapsedMillis() >= (this.expirationTime - 3000)) {
            if(Globals.gameTime.getElapsedMillis() >= this.nextBlinkChange) {
                this.blink = ((this.blink)?false:true);
                this.nextBlinkChange = Globals.gameTime.getElapsedMillis() + 200;
            }
        }
        if(!this.blink) {
            int width = this.icon.getWidth();
            int height = this.icon.getHeight();
            g2d.drawImage(this.icon, (int)(this.x - (width / 2)), (int)(this.y - (height / 2)), null);
        }
    }
}
