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
import java.util.ConcurrentModificationException;
import java.util.Iterator;

/**
 * Used to represent the Flamethrower weapon.
 * @author Darin Beaudreau
 */
public class Flamethrower extends Weapon {
    // Final Variables
    private static final int DEFAULT_AMMO = 100;
    private static final int MAX_AMMO = 300;
    private static final int AMMO_PER_USE = 1;
    private static final int PARTICLES_PER_USE = 8;
    private static final int DAMAGE_PER_PARTICLE = 2;
    private static final double PARTICLE_SPREAD = 15.0;
    private static final int PARTICLE_LIFE_MIN = 1000;
    private static final int PARTICLE_LIFE_MAX = 1400;
    
    public Flamethrower() {
        super("The Flammenwerfer", KeyEvent.VK_3, "/resources/images/GZS_Flamethrower.png", DEFAULT_AMMO, MAX_AMMO, AMMO_PER_USE, 0);
    }
    
    @Override
    public void updateWeapon() {
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
        this.cool();
    }
    
    @Override
    public void drawAmmo(Graphics2D g2d) {
        // Draw all particles whose life has not yet expired.
        if(this.particles.size() > 0) {
            g2d.setColor(Color.ORANGE);
            try {
                Iterator<Particle> it = this.particles.iterator();
                while(it.hasNext()) {
                    Particle p = it.next();
                    if(p.isAlive()) p.draw(g2d);
                }
            } catch(ConcurrentModificationException cme) {}
        }
    }
    
    @Override
    public void fire(double theta, Point2D.Double pos) {
        // If there is enough ammo left...
        if(this.canFire()) {
            // Generate new particles and add them to the list.
            for(int i = 0; i < Flamethrower.PARTICLES_PER_USE; i++) {
                int life = Flamethrower.PARTICLE_LIFE_MIN + (int)(Globals.r.nextInt((Flamethrower.PARTICLE_LIFE_MAX - Flamethrower.PARTICLE_LIFE_MIN) + 1));
                int size = Globals.r.nextInt(8) + 1;
                Particle p = new Particle(theta, Flamethrower.PARTICLE_SPREAD, 4.0,
                                      (life / (int)Globals.SLEEP_TIME), new Point2D.Double(pos.x, pos.y),
                                       new Dimension(size, size), Images.FIRE_PARTICLE);
                this.particles.add(p);
            }
            // Use up ammo.
            this.consumeAmmo();
            this.resetCooldown();
            Sounds.FLAMETHROWER.play();
        }
    }
    
    @Override
    public int checkForDamage(Rectangle2D.Double rect) {
        int damage = 0;
        // Check all particles for collisions with the target rectangle.
        Iterator<Particle> it = this.particles.iterator();
        while(it.hasNext()) {
            Particle p = it.next();
            // If the particle is still alive and has collided with the target.
            if(p.isAlive() && rect.contains(p.getPos())) {
                // Add the damage of the particle and remove it from the list.
                damage += DAMAGE_PER_PARTICLE;
                it.remove();
            }
        }
        return damage;
    }
    
    @Override
    public void resetAmmo() {
        super.resetAmmo();
        this.ammoLeft = DEFAULT_AMMO;
    }
    
    @Override
    public int getAmmoPackAmount() {
        return Flamethrower.DEFAULT_AMMO;
    }
}
