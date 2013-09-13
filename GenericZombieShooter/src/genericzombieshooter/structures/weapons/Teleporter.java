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
package genericzombieshooter.structures.weapons;

import genericzombieshooter.actors.Player;
import genericzombieshooter.actors.Zombie;
import genericzombieshooter.misc.Globals;
import genericzombieshooter.misc.Sounds;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.List;

/**
 * Used to teleport the player to a random point on the screen.
 * @author Darin Beaudreau
 */
public class Teleporter extends Weapon {
    // Final Variables
    private static final int WEAPON_PRICE = 0;
    private static final int AMMO_PRICE = 0;
    private static final int DEFAULT_AMMO = 1;
    private static final int MAX_AMMO = 1;
    private static final int AMMO_PER_USE = 1;
    private static final long COOLDOWN = ((60 * 1000) / Globals.SLEEP_TIME);
    private static double MIN_TELEPORT_DISTANCE = 300;
    
    public Teleporter() {
        super("Big Red Button", KeyEvent.VK_0, "/resources/images/GZS_BigRedButton.png",
              Teleporter.DEFAULT_AMMO, Teleporter.MAX_AMMO, Teleporter.AMMO_PER_USE,
              (int)Teleporter.COOLDOWN, false);
    }
    
    @Override
    public int getWeaponPrice() { return Teleporter.WEAPON_PRICE; }
    
    @Override
    public int getAmmoPrice() { return Teleporter.AMMO_PRICE; }
    
    @Override
    public int getAmmoPackAmount() { return Teleporter.DEFAULT_AMMO; }
    
    @Override
    public void resetAmmo() { this.ammoLeft = Teleporter.DEFAULT_AMMO; }
    
    @Override
    public void updateWeapon(List<Zombie> zombies) {
        this.cool();
    }
    
    @Override
    public void fire(double theta, Point2D.Double pos, Player player) {
        if(this.canFire()) {
            Point2D.Double newPos = this.getTeleportLocation(player);
            player.move(newPos);
            this.consumeAmmo();
            this.resetCooldown();
            Sounds.TELEPORT.play();
        }
    }
    
    private Point2D.Double getTeleportLocation(Player player) {
        Point2D.Double p = new Point2D.Double();
        boolean validPoint = false;
        while(!validPoint) {
            p.x = Globals.r.nextDouble() * (Globals.W_WIDTH - player.width);
            p.y = Globals.r.nextDouble() * (Globals.W_HEIGHT - player.height);
            double xD = p.x - player.x;
            double yD = p.y - player.y;
            double dist = Math.sqrt((xD * xD) + (yD * yD));
            if(dist >= Teleporter.MIN_TELEPORT_DISTANCE) validPoint = true;
        }
        return p;
    }
}
