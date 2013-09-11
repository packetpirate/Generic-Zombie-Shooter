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
import genericzombieshooter.misc.Images;
import genericzombieshooter.misc.Sounds;
import genericzombieshooter.structures.Particle;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.List;

/**
 * Used to represent the Assault Rifle weapon.
 * @author Darin Beaudreau
 */
public class AssaultRifle extends Weapon {
    // Final Variables
    private static final int WEAPON_PRICE = 0;
    private static final int AMMO_PRICE = 0;
    private static final int DEFAULT_AMMO = 60;
    private static final int MAX_AMMO = 300;
    private static final int AMMO_PER_USE = 1;
    private static final int DAMAGE_PER_PARTICLE = 100;
    private static final double PARTICLE_SPREAD = 5.0;
    private static final int PARTICLE_LIFE = 2000;
    
    public AssaultRifle() {
        super("RTPS", KeyEvent.VK_2, "/resources/images/GZS_RTPS.png", 
              AssaultRifle.DEFAULT_AMMO, AssaultRifle.MAX_AMMO, AssaultRifle.AMMO_PER_USE, 
              10, true);
    }
    
    @Override
    public int getWeaponPrice() { return AssaultRifle.WEAPON_PRICE; }
    
    @Override
    public int getAmmoPrice() { return AssaultRifle.AMMO_PRICE; }
    
    @Override
    public int getAmmoPackAmount() {
        return AssaultRifle.DEFAULT_AMMO;
    }
    
    @Override
    public void resetAmmo() {
        super.resetAmmo();
        this.ammoLeft = AssaultRifle.DEFAULT_AMMO;
    }
    
    @Override
    public void updateWeapon(List<Zombie> zombies) {
        synchronized(this.particles) {
            if(!this.particles.isEmpty()) {
                // Update all particles and remove them if their life has expired or they are out of bounds.
                Iterator<Particle> it = this.particles.iterator();
                while(it.hasNext()) {
                    Particle p = it.next();
                    p.update();
                    if(!p.isAlive() || p.outOfBounds()) {
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
        synchronized(this.particles) {
            // Draw all particles whose life has not yet expired.
            if(!this.particles.isEmpty()) {
                g2d.setColor(Color.ORANGE);
                Iterator<Particle> it = this.particles.iterator();
                while(it.hasNext()) {
                    Particle p = it.next();
                    if(p.isAlive()) p.draw(g2d);
                }
            }
        }
    }
    
    @Override
    public void fire(double theta, Point2D.Double pos, Player player) {
        // If there is enough ammo left...
        if(this.canFire()) {
            // Create a new bullet and add it to the list.
            int width = 4;
            int height = 10;
            Particle p = new Particle(theta, AssaultRifle.PARTICLE_SPREAD, 8.0,
                          (AssaultRifle.PARTICLE_LIFE / (int)Globals.SLEEP_TIME), new Point2D.Double(pos.x, pos.y),
                           new Dimension(width, height), Images.RTPS_BULLET);
            this.particles.add(p);
            // Use up ammo.
            this.consumeAmmo();
            this.resetCooldown();
            Sounds.RTPS.play();
        }
    }
    
    @Override
    public int checkForDamage(Rectangle2D.Double rect) {
        synchronized(this.particles) {
            int damage = 0;
            if(!this.particles.isEmpty()) {
                // Check all particles for collisions with the target rectangle.
                Iterator<Particle> it = this.particles.iterator();
                while(it.hasNext()) {
                    Particle p = it.next();
                    // If the particle is still alive and has collided with the target.
                    if(p.isAlive() && p.checkCollision(rect)) {
                        // Add the damage of the particle and remove it from the list.
                        damage += AssaultRifle.DAMAGE_PER_PARTICLE;
                        it.remove();
                    }
                }
            }
            return damage;
        }
    }
}
