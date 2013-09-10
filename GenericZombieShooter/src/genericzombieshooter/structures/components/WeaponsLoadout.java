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
    private int currentWeapon;
    private String currentWeaponName;
    //public void setCurrentWeapon(int w) { this.currentWeapon = w; }
    public void setCurrentWeapon(String name) { this.currentWeaponName = name; }
    
    public WeaponsLoadout(Player p) {
        this.player = p;
        this.currentWeapon = 1;
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
                /*if((s + 1) <= player.getAllWeapons().size()) { // Draw the weapon's icon.
                    BufferedImage image = player.getWeapon(s + 1).getImage();
                    double imageX = (x + 24) - (image.getWidth() / 2);
                    double imageY = (y + 24) - (image.getHeight() / 2);
                    g2d.drawImage(image, (int)imageX, (int)imageY, null);
                } // End drawing of the weapon icon.*/
                { // Draw Weapon Icon
                    BufferedImage image = null;
                    HashMap<String, Weapon> weaponsMap = player.getWeaponsMap();
                    
                    int w = s + 1;
                    if(w == 1 && weaponsMap.containsKey(Globals.HANDGUN.getName())) image = weaponsMap.get(Globals.HANDGUN.getName()).getImage();
                    else if(w == 2 && weaponsMap.containsKey(Globals.ASSAULT_RIFLE.getName())) image = weaponsMap.get(Globals.ASSAULT_RIFLE.getName()).getImage();
                    else if(w == 3 && weaponsMap.containsKey(Globals.SHOTGUN.getName())) image = weaponsMap.get(Globals.SHOTGUN.getName()).getImage();
                    else if(w == 4 && weaponsMap.containsKey(Globals.FLAMETHROWER.getName())) image = weaponsMap.get(Globals.FLAMETHROWER.getName()).getImage();
                    else if(w == 5 && weaponsMap.containsKey(Globals.GRENADE.getName())) image = weaponsMap.get(Globals.GRENADE.getName()).getImage();
                    else if(w == 6 && weaponsMap.containsKey(Globals.LANDMINE.getName())) image = weaponsMap.get(Globals.LANDMINE.getName()).getImage();
                    else if(w == 7 && weaponsMap.containsKey(Globals.FLARE.getName())) image = weaponsMap.get(Globals.FLARE.getName()).getImage();
                    else if(w == 8 && weaponsMap.containsKey(Globals.LASERWIRE.getName())) image = weaponsMap.get(Globals.LASERWIRE.getName()).getImage();
                    else if(w == 9 && weaponsMap.containsKey(Globals.TURRETWEAPON.getName())) image = weaponsMap.get(Globals.TURRETWEAPON.getName()).getImage();
                    else if(w == 10 && weaponsMap.containsKey(Globals.TELEPORTER.getName())) image = weaponsMap.get(Globals.TELEPORTER.getName()).getImage();
                    
                    g2d.drawImage(image, (int)x, (int)y, null);
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
