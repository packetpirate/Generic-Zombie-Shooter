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
package genericzombieshooter.structures.components;

import genericzombieshooter.GZSFramework;
import genericzombieshooter.actors.Player;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Used to display the player's experience and current attributes, along with
 * buttons that can be used to increase the user's attributes.
 * @author Darin Beaudreau
 */
public class LevelScreen {
    // Final Variables
    private static final String EXP_BAR_NAME = "Experience Bar";
    private static final String HEALTH_BAR_NAME = "Health Bar";
    private static final String HEALTH_BUTTON_NAME = "Add Health Button";
    
    // Member Variables
    private BufferedImage background;
    HashMap<String, Rectangle2D.Double> rects;
    
    public LevelScreen() {
        this.background = GZSFramework.loadImage("/resources/images/GZS_LevelScreen.png");
        this.rects = new HashMap<String, Rectangle2D.Double>();
        { // Add rectangles to map.
            this.rects.put(LevelScreen.EXP_BAR_NAME, new Rectangle2D.Double(488, 188, 240, 20));
            this.rects.put(LevelScreen.HEALTH_BAR_NAME, new Rectangle2D.Double(487, 303, 240, 20));
            this.rects.put(LevelScreen.HEALTH_BUTTON_NAME, new Rectangle2D.Double(700, 251, 30, 30));
        } // End adding rectangles to map.
    }
    
    public void draw(Graphics2D g2d, Player player) {
        // Draw the background image.
        g2d.drawImage(this.background, 0, 0, null);
        { // Draw the rectangles in the map.
            /*g2d.setColor(new Color(209, 21, 33));
            Iterator<Rectangle2D.Double> it = this.rects.values().iterator();
            while(it.hasNext()) {
                Rectangle2D.Double rect = it.next();
                g2d.fill(rect);
            }*/
        } // End drawing mapped rectangles.
    }
    
    public void click(MouseEvent m, Player player) {
        
    }
}
