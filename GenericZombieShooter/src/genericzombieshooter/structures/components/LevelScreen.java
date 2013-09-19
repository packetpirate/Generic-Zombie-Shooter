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
import genericzombieshooter.misc.Images;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

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
    private static final String DAMAGE_BAR_NAME = "Damage Bar";
    private static final String DAMAGE_BUTTON_NAME = "Add Damage Button";
    private static final String SPEED_BAR_NAME = "Speed Bar";
    private static final String SPEED_BUTTON_NAME = "Add Speed Button";
    private static final String SKILL_POINTS_LABEL_NAME = "Skill Points";
    
    // Member Variables
    private BufferedImage background;
    private HashMap<String, Rectangle2D.Double> rects;
    
    private int healthLevel;
    private int damageLevel;
    private int speedLevel;
    
    public LevelScreen() {
        this.background = GZSFramework.loadImage("/resources/images/GZS_LevelScreen.png");
        this.rects = new HashMap<String, Rectangle2D.Double>();
        { // Add rectangles to map.
            this.rects.put(LevelScreen.EXP_BAR_NAME, new Rectangle2D.Double(482, 187, 242, 22));
            this.rects.put(LevelScreen.HEALTH_BAR_NAME, new Rectangle2D.Double(482, 286, 242, 22));
            this.rects.put(LevelScreen.HEALTH_BUTTON_NAME, new Rectangle2D.Double(697, 235, 30, 30));
            this.rects.put(LevelScreen.DAMAGE_BAR_NAME, new Rectangle2D.Double(482, 384, 242, 22));
            this.rects.put(LevelScreen.DAMAGE_BUTTON_NAME, new Rectangle2D.Double(697, 334, 30, 30));
            this.rects.put(LevelScreen.SPEED_BAR_NAME, new Rectangle2D.Double(482, 482, 242, 22));
            this.rects.put(LevelScreen.SPEED_BUTTON_NAME, new Rectangle2D.Double(697, 433, 30, 30));
            this.rects.put(LevelScreen.SKILL_POINTS_LABEL_NAME, new Rectangle2D.Double(478, 529, 153, 30));
        } // End adding rectangles to map.
        this.healthLevel = 0;
        this.damageLevel = 0;
        this.speedLevel = 0;
    }
    
    public void draw(Graphics2D g2d, Player player) {
        // Draw the background image.
        g2d.drawImage(this.background, 0, 0, null);
        { // Draw the rectangles in the map.
            FontMetrics metrics = g2d.getFontMetrics();
            { // Draw the experience bar.
                g2d.setColor(new Color(67, 158, 22));
                Rectangle2D.Double expBar = this.rects.get(LevelScreen.EXP_BAR_NAME);
                double w = ((double)player.getExp() / (double)player.getNextLevelExp()) * expBar.width;
                g2d.fillRect((int)expBar.x, (int)expBar.y, (int)w, (int)expBar.height);
                g2d.setColor(Color.WHITE);
                String s = player.getExp() + " / " + player.getNextLevelExp();
                int h = metrics.getHeight();
                g2d.drawString(s, (int)(expBar.x + 5), (int)((expBar.y + (expBar.height / 2)) + (h / 3)));
            } // End drawing the experience bar.
            { // Draw the health attribute progress bar.
                Rectangle2D.Double healthBar = this.rects.get(LevelScreen.HEALTH_BAR_NAME);
                for(int i = 0; i < this.healthLevel; i++) {
                    int x = (int)((healthBar.x + 1) + (i * 48));
                    g2d.setColor(new Color(209, 21, 33));
                    g2d.fillRect(x, (int)healthBar.y, 48, 22);
                    g2d.setColor(Color.BLACK);
                    g2d.drawRect(x, (int)healthBar.y, 48, 22);
                }
            } // End drawing the health attribute progress bar.
            { // Draw the damage attribute progress bar.
                Rectangle2D.Double damageBar = this.rects.get(LevelScreen.DAMAGE_BAR_NAME);
                for(int i = 0; i < this.damageLevel; i++) {
                    int x = (int)((damageBar.x + 1) + (i * 48));
                    g2d.setColor(new Color(209, 21, 33));
                    g2d.fillRect(x, (int)damageBar.y, 48, 22);
                    g2d.setColor(Color.BLACK);
                    g2d.drawRect(x, (int)damageBar.y, 48, 22);
                }
            } // End drawing the damage attribute progress bar.
            { // Draw the speed attribute progress bar.
                Rectangle2D.Double speedBar = this.rects.get(LevelScreen.SPEED_BAR_NAME);
                for(int i = 0; i < this.speedLevel; i++) {
                    int x = (int)((speedBar.x + 1) + (i * 48));
                    g2d.setColor(new Color(209, 21, 33));
                    g2d.fillRect(x, (int)speedBar.y, 48, 22);
                    g2d.setColor(Color.BLACK);
                    g2d.drawRect(x, (int)speedBar.y, 48, 22);
                }
            } // End drawing the speed attribute progress bar.
            { // Draw the number of skill points the player has.
                Font f = g2d.getFont();
                g2d.setFont(new Font("Arial", Font.BOLD, 30));
                g2d.setColor(Color.WHITE);
                Rectangle2D.Double rect = this.rects.get(LevelScreen.SKILL_POINTS_LABEL_NAME);
                int x = (int)((rect.x + rect.width) + 10);
                g2d.drawString(("" + player.getSkillPoints()), x, (int)((rect.y + rect.height) - 5));
                g2d.setFont(f);
            } // End drawing the number of skill points the player has.
        } // End drawing mapped rectangles.
    }
    
    public void click(MouseEvent m, Player player) {
        // Assume click is left mouse since it is checked before it is passed.
        Point2D.Double mousePos = new Point2D.Double(m.getX(), m.getY());
        // Offset the mouse position by half the width and height of the mouse cursor image.
        mousePos.x += Images.CROSSHAIR.getWidth() / 2;
        mousePos.y += Images.CROSSHAIR.getHeight() / 2;
        
        { // Check Add Health Button
            if(this.rects.get(LevelScreen.HEALTH_BUTTON_NAME).contains(mousePos)) {
                // Attempt to raise the player's max health.
                int val = player.spendPoint(Player.MAX_HEALTH_ID, this.healthLevel);
                if(val == 1) this.healthLevel++;
            } else if(this.rects.get(LevelScreen.DAMAGE_BUTTON_NAME).contains(mousePos)) {
                // Attempt to raise the player's damage.
                int val = player.spendPoint(Player.DAMAGE_ID, this.damageLevel);
                if(val == 1) this.damageLevel++;
            } else if(this.rects.get(LevelScreen.SPEED_BUTTON_NAME).contains(mousePos)) {
                // Attempt to raise the player's speed.
                int val = player.spendPoint(Player.SPEED_ID, this.speedLevel);
                if(val == 1) this.speedLevel++;
            }
        } // End checking Add Health Button
    }
}
