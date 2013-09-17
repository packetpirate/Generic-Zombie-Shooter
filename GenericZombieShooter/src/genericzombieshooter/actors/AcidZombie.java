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
package genericzombieshooter.actors;

import genericzombieshooter.misc.Globals;
import genericzombieshooter.misc.Images;
import genericzombieshooter.structures.Animation;
import genericzombieshooter.structures.Particle;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Extends the Zombie class. Used to represent the special acid zombie type.
 * @author Darin Beaudreau
 */
public class AcidZombie extends Zombie {
    // Final Variables
    private static final int EXP_VALUE = 50;
    private static final int COOL_TIME = 3000 / (int)Globals.SLEEP_TIME;
    private static final double ATTACK_DISTANCE = 300.0;
    private static final int PARTICLE_LIFE = 2000;
    private static final int ACID_DAMAGE = 40;
    // Member Variables
    private List<Particle> particles;
    @Override
    public List<Particle> getParticles() { return this.particles; }
    @Override
    public int getParticleDamage() { return AcidZombie.ACID_DAMAGE; }
    private int cooldown;
    public boolean canFire() { return this.cooldown == 0; }
    
    public AcidZombie(Point2D.Double p_, int health_, int damage_, double speed_, int score_, Animation animation_) {
        super(p_, Globals.ZOMBIE_ACID_TYPE, health_, damage_, speed_, score_, AcidZombie.EXP_VALUE, animation_);
        this.particles = new ArrayList<Particle>();
        this.cooldown = AcidZombie.COOL_TIME;
    }
    
    @Override
    public void drawParticles(Graphics2D g2d) {
        synchronized(this.particles) {
            Iterator<Particle> it = this.particles.iterator();
            while(it.hasNext()) {
                Particle p = it.next();
                if(p.isAlive()) p.draw(g2d);
            }
        }
    }
    
    @Override
    public void update(Player player, List<Zombie> zombies) {
        // Update the zombie's acid particles.
        if(!this.particles.isEmpty()) {
            synchronized(this.particles) {
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
        // If the zombie's attack has cooled down, fire a particle at the player.
        if(canFire()) {
            Point2D.Double playerPos = new Point2D.Double(player.getCenterX(), player.getCenterY());
            Point2D.Double myPos = new Point2D.Double(this.x, this.y);
            if(inRange(playerPos, myPos)) {
                // Fire a new particle at the player.
                this.fire(playerPos);
                this.cooldown = AcidZombie.COOL_TIME;
            }
        } else this.cooldown--;
    }
    
    @Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d);
        if(!this.particles.isEmpty()) drawParticles(g2d);
    }
    
    private void fire(Point2D.Double playerPos) {
        double theta = Math.atan2((playerPos.y - this.y), (playerPos.x - this.x)) - Math.PI;
        Particle p = new Particle(theta, 0.0, 8.0, (AcidZombie.PARTICLE_LIFE / (int)Globals.SLEEP_TIME),
            new Point2D.Double(this.x, this.y), new Dimension(20, 20), Images.ACID_PARTICLE) {
              @Override
              public void update() {
                  if(this.isAlive()) {
                      // Age the particle.
                      this.life--;
                      // Update the position.
                      this.pos.x += -(this.speed * Math.cos(this.theta));
                      this.pos.y += -(this.speed * Math.sin(this.theta));
                  }
              }
            };
        this.particles.add(p);
    }
    
    private boolean inRange(Point2D.Double playerPos, Point2D.Double myPos) {
        double xD = playerPos.x - myPos.x;
        double yD = playerPos.y - myPos.y;
        return Math.sqrt((xD * xD) + (yD * yD)) <= AcidZombie.ATTACK_DISTANCE;
    }
}
