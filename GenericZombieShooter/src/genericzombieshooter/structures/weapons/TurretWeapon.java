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

import genericzombieshooter.actors.Zombie;
import genericzombieshooter.misc.Sounds;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Used to drop turrets on the field.
 * @author Darin Beaudreau
 */
public class TurretWeapon extends Weapon {
    // Final Variables
    private static final int DEFAULT_AMMO = 1;
    private static final int MAX_AMMO = 1;
    private static final int AMMO_PER_USE = 1;
    private static final long TURRET_LIFE = 60 * 1000;
    
    // Member Variables
    private List<Turret> turrets;
    
    public TurretWeapon() {
        super("Sentry Gun", KeyEvent.VK_9, "/resources/images/GZS_Turret.png",
              TurretWeapon.DEFAULT_AMMO, TurretWeapon.MAX_AMMO, TurretWeapon.AMMO_PER_USE, 
              50, false);
        this.turrets = Collections.synchronizedList(new ArrayList<Turret>());
    }
    
    @Override
    public int getAmmoPackAmount() { return TurretWeapon.DEFAULT_AMMO; }
    
    @Override
    public void resetAmmo() {
        this.turrets = Collections.synchronizedList(new ArrayList<Turret>());
        this.ammoLeft = TurretWeapon.DEFAULT_AMMO;
    }
    
    @Override
    public boolean canFire() {
        boolean superBool = super.canFire();
        boolean turretsEmpty = this.turrets.isEmpty();
        //System.out.println("SuperBool: " + superBool + ", TurretsEmpty: " + turretsEmpty);
        return superBool && turretsEmpty; 
    }
    
    @Override
    public void updateWeapon(List<Zombie> zombies) {
        synchronized(this.turrets) {
            if(!this.turrets.isEmpty()) {
                Iterator<Turret> it = this.turrets.iterator();
                while(it.hasNext()) {
                    Turret t = it.next();
                    if(t.isAlive()) t.update(zombies);
                    else {
                        it.remove();
                        continue;
                    }
                }
            }
        }
        this.cool();
    }
    
    @Override
    public void drawAmmo(Graphics2D g2d) {
        synchronized(this.turrets) {
            if(!this.turrets.isEmpty()) {
                Iterator<Turret> it = this.turrets.iterator();
                while(it.hasNext()) {
                    Turret t = it.next();
                    if(t.isAlive()) t.draw(g2d);
                }
            }
        }
    }
    
    @Override
    public void fire(double theta, Point2D.Double pos) {
        if(this.canFire()) {
            this.turrets.add(new Turret(pos, TurretWeapon.TURRET_LIFE));
            this.consumeAmmo();
            this.resetCooldown();
            Sounds.LANDMINE_ARMED.play();
        }
    }
    
    @Override
    public int checkForDamage(Rectangle2D.Double rect) {
        int damage = 0;
        synchronized(this.turrets) {
            Iterator<Turret> it = this.turrets.iterator();
            while(it.hasNext()) {
                Turret t = it.next();
                if(t.isAlive()) damage += t.checkForDamage(rect);
            }
        }
        return damage;
    }
}
