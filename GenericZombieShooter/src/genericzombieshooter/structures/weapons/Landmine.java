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
import genericzombieshooter.misc.Globals;
import genericzombieshooter.misc.Images;
import genericzombieshooter.misc.Sounds;
import genericzombieshooter.structures.Explosion;
import genericzombieshooter.structures.Particle;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Used to represent a landmine weapon.
 * @author Darin Beaudreau
 */
public class Landmine extends Weapon {
    // Final Variables
    private static final int DEFAULT_AMMO = 1;
    private static final int MAX_AMMO = 3;
    private static final int AMMO_PER_USE = 1;
    private static final int DAMAGE_PER_EXPLOSION = 500;
    private static final int PARTICLE_LIFE = 3 * 60 * 1000;
    
    // Member variables.
    private List<Explosion> explosions;
    public List<Explosion> getExplosions() { return this.explosions; }
    
    public Landmine() {
        super("Hopper", KeyEvent.VK_5, "/resources/images/GZS_Landmine.png", DEFAULT_AMMO, MAX_AMMO, AMMO_PER_USE, 50);
        this.explosions = Collections.synchronizedList(new ArrayList<Explosion>());
    }
    
    @Override
    public int getAmmoPackAmount() { return Landmine.DEFAULT_AMMO; }
    
    @Override
    public void resetAmmo() {
        super.resetAmmo();
        this.explosions = Collections.synchronizedList(new ArrayList<Explosion>());
        this.ammoLeft = DEFAULT_AMMO;
    }
    
    @Override
    public void updateWeapon(List<Zombie> zombies) {
        { // Update particles.
            synchronized(this.particles) {
                Iterator<Particle> it = this.particles.iterator();
                while(it.hasNext()) {
                    Particle p = it.next();
                    p.update();

                    boolean collision = false;
                    synchronized(zombies) {
                        Iterator<Zombie> zit = zombies.iterator();
                        while(zit.hasNext()) {
                            Zombie z = zit.next();
                            double width = z.getImage().getWidth();
                            double height = z.getImage().getHeight();
                            Rectangle2D.Double rect = new Rectangle2D.Double((z.x - (width / 2)), (z.y - (height / 2)), width, height);
                            if(p.checkCollision(rect)) collision = true;
                        }
                    }
                    if(!p.isAlive() || collision) {
                        this.explosions.add(new Explosion(Images.EXPLOSION_SHEET, p.getPos()));
                        Sounds.EXPLOSION.play();
                        it.remove();
                        continue;
                    }
                }
            }
        } // End particle updates.
        { // Update explosions.
            synchronized(this.explosions) {
                Iterator<Explosion> it = this.explosions.iterator();
                while(it.hasNext()) {
                    Explosion e = it.next();
                    e.getImage().update();
                    if(!e.getImage().isActive()) {
                        it.remove();
                        continue;
                    }
                }
            }
        } // End explosion updates.
        this.cool();
    }
    
    @Override
    public void drawAmmo(Graphics2D g2d) {
        synchronized(this.particles) {
            if(!this.particles.isEmpty()) {
                Iterator<Particle> it = this.particles.iterator();
                while(it.hasNext()) {
                    Particle p = it.next();
                    if(p.isAlive()) p.draw(g2d);
                }
            }
        }
        synchronized(this.explosions) {
            if(!this.explosions.isEmpty()) {
                Iterator<Explosion> it = this.explosions.iterator();
                while(it.hasNext()) {
                    Explosion e = it.next();
                    if(e.getImage().isActive()) e.draw(g2d);
                }
            }
        }
    }
    
    @Override
    public void fire(double theta, Point2D.Double pos) {
        synchronized(this.particles) {
            if(this.canFire()) {
                Particle p = createLandmineParticle(theta, pos);
                this.particles.add(p);
                this.consumeAmmo();
                this.resetCooldown();
                Sounds.LANDMINE_ARMED.play();
            }
        }
    }
    
    private Particle createLandmineParticle(double theta, Point2D.Double pos) {
        Particle p = new Particle(theta, 0.0, 0.0, (Landmine.PARTICLE_LIFE / (int)Globals.SLEEP_TIME),
                                  pos, new Dimension(24, 24), Images.LANDMINE_PARTICLE) {
            @Override
            public void update() {
                if(this.isAlive()) this.life--;
            }
            
            @Override
            public void draw(Graphics2D g2d) {
                double x = this.pos.x - (this.size.width / 2);
                double y = this.pos.y - (this.size.height / 2);
                g2d.drawImage(this.image, (int)x, (int)y, null);
            }
        };
        
        return p;
    }
    
    @Override
    public int checkForDamage(Rectangle2D.Double rect) {
        synchronized(this.explosions) {
            int damage = 0;
            boolean collision = false;
            if(!this.explosions.isEmpty()) {
                Iterator<Explosion> it = this.explosions.iterator();
                while(it.hasNext()) {
                    Explosion e = it.next();
                    if(e.getImage().isActive()) {
                        Rectangle2D.Double expRect = new Rectangle2D.Double((e.x - (e.getSize().width / 2)), (e.y - (e.getSize().height / 2)),
                                                                             e.getSize().width, e.getSize().height);
                        if(rect.intersects(expRect)) {
                            damage += Landmine.DAMAGE_PER_EXPLOSION;
                            collision = true;
                        }
                    }
                }
            }
            return damage;
        }
    }
}
