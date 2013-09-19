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
import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

/**
 * Used to display messages above the weapons loadout.
 * @author Darin Beaudreau
 */
public class Message {
    // Member Variables
    private String text;
    public String getText() { return this.text; }
    private long expirationTime;
    
    public Message(String text, long duration) {
        this.text = text;
        this.expirationTime = Globals.gameTime.getElapsedMillis() + duration;
    }
    
    public boolean isAlive() { return (Globals.gameTime.getElapsedMillis() < this.expirationTime); }
    
    public void draw(Graphics2D g2d, Point2D.Double pos) {
        Composite savedComp = g2d.getComposite();
        
        double opacity = 1.0f;
        if(Globals.gameTime.getElapsedMillis() >= (this.expirationTime - 3000)) {
            opacity = ((double)this.expirationTime - (double)Globals.gameTime.getElapsedMillis()) / 3000;
        }
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)opacity));
        g2d.drawString(this.text, (int)pos.x, (int)pos.y);
        
        g2d.setComposite(savedComp);
    }
}
