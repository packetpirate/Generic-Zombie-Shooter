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

import genericzombieshooter.actors.Player;
import genericzombieshooter.misc.Globals;
import genericzombieshooter.structures.weapons.Weapon;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * Used to draw the weapons loadout GUI component to the screen.
 * @author Darin Beaudreau
 */
public class WeaponsLoadout {
    public static final double BAR_WIDTH = (10 * 48) + (11 * 4);
    public static final double BAR_HEIGHT = 56;
    
    private Player player;
    private String currentWeaponName;
    public void setCurrentWeapon(String name) { this.currentWeaponName = name; }
    
    public WeaponsLoadout(Player p) {
        this.player = p;
        this.currentWeaponName = p.getWeapon().getName();
    }
    
    public void draw(Graphics2D g2d) {
        Stroke oldStroke = g2d.getStroke();
        { // Draw the bar under the individual weapon slots.
            double x = (Globals.W_WIDTH / 2) - (WeaponsLoadout.BAR_WIDTH / 2);
            double y = Globals.W_HEIGHT - (WeaponsLoadout.BAR_HEIGHT + 15);
            Rectangle2D.Double rect = new Rectangle2D.Double(x, y, WeaponsLoadout.BAR_WIDTH, WeaponsLoadout.BAR_HEIGHT);
            g2d.setColor(Color.GRAY);
            g2d.fill(rect);
            g2d.setColor(Color.BLACK);
            g2d.draw(rect);
        } // Stop drawing the bar under the weapon slots.
        { // Draw the filler color for the weapon slots.
            String [] weaponNames = {Globals.HANDGUN.getName(), Globals.ASSAULT_RIFLE.getName(),
                                                 Globals.SHOTGUN.getName(), Globals.FLAMETHROWER.getName(),
                                                 Globals.GRENADE.getName(), Globals.LANDMINE.getName(),
                                                 Globals.FLARE.getName(), Globals.LASERWIRE.getName(),
                                                 Globals.TURRETWEAPON.getName(), Globals.TELEPORTER.getName()};
            for(int s = 0; s < 10; s++) {
                int slot = s * 48;
                int spacing = (s + 1) * 4;
                double size = 48;
                double x = ((Globals.W_WIDTH / 2) - (WeaponsLoadout.BAR_WIDTH / 2)) + (slot + spacing);
                double y = (Globals.W_HEIGHT - (WeaponsLoadout.BAR_HEIGHT + 15)) + 4;
                Rectangle2D.Double rect = new Rectangle2D.Double(x, y, size, size);
                g2d.setStroke(oldStroke);
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.fill(rect);
                g2d.setColor(Color.BLACK);
                g2d.draw(rect);
                { // Draw Weapon Icon
                    BufferedImage image = null;
                    HashMap<String, Weapon> weaponsMap = player.getWeaponsMap();
                    
                    int w = s + 1;
                    Weapon weapon = null;
                    for(int i = 1; i <= 10; i++) {
                        if((w == i) && weaponsMap.containsKey(weaponNames[i - 1])) {
                            weapon = weaponsMap.get(weaponNames[i - 1]);
                            image = weapon.getImage();
                            break;
                        }
                    }
                    if(image != null) g2d.drawImage(image, (int)x, (int)y, null);
                
                    { // Draw Translucent Box To Show Cooldown
                        if((weapon != null) && player.hasWeapon(weapon.getName())) {
                            g2d.setColor(new Color(0, 0, 0, 200));
                            double height = weapon.getCooldownPercentage() * 48;
                            Rectangle2D.Double coolBox = new Rectangle2D.Double(x, y, 48, height);
                            g2d.fill(coolBox);
                        }
                    } // End drawing translucent cooldown box.
                } // End drawing weapon icon.
                // If the current iteration is the slot of the currently equipped weapon...
                if(this.currentWeaponName.equals(weaponNames[s])) {
                    x += 3;
                    y += 3;
                    size = 42;
                    Rectangle2D.Double border = new Rectangle2D.Double(x, y, size, size);
                    g2d.setColor(Color.WHITE);
                    g2d.setStroke(new BasicStroke(3.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                                                  10.0f, new float[]{10.0f}, 0.0f));
                    g2d.draw(border);
                }
            }
        } // Stop drawing the filler color for the weapon slots.
        g2d.setStroke(oldStroke);
    }
}
