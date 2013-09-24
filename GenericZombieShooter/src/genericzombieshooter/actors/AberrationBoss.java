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
 * Used to represent the Aberration boss type zombie.
 * @author Darin Beaudreau
 */
public class AberrationBoss extends Zombie {
    // Final Variables
    private static final int EXP_VALUE = 5000;
    private static final int COOL_TIME = 3000 / (int)Globals.SLEEP_TIME;
    private static final double ATTACK_DISTANCE = 314.0;
    private static final int PARTICLE_LIFE = 2000;
    private static final double PARTICLE_SPREAD = 10.0;
    private static final int PARTICLE_COUNT = 5;
    private static final int PARTICLE_DAMAGE = 80;
    
    // Member Variables
    private List<Particle> particles;
    @Override
    public List<Particle> getParticles() { return this.particles; }
    @Override
    public int getParticleDamage() { return AberrationBoss.PARTICLE_DAMAGE; }
    private int cooldown;
    public boolean canFire() { return this.cooldown == 0; }
    
    public AberrationBoss(Point2D.Double p_, int health_, int damage_, double speed_, int score_, Animation animation_) {
        super(p_, Globals.ZOMBIE_BOSS_ABERRATION_TYPE, health_, damage_, speed_, score_, AberrationBoss.EXP_VALUE, animation_);
        this.particles = new ArrayList<Particle>();
        this.cooldown = AberrationBoss.COOL_TIME;
    }
    
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
                this.cooldown = AberrationBoss.COOL_TIME;
            }
        } else this.cooldown--;
    }
    
    @Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d);
        if(!this.particles.isEmpty()) drawParticles(g2d);
    }
    
    private void fire(Point2D.Double playerPos) {
        // Create group of particles.
        for(int i = 0; i < AberrationBoss.PARTICLE_COUNT; i++) {
            double theta = Math.atan2((playerPos.y - this.y), (playerPos.x - this.x)) - Math.PI;
            { // Deviate particle from intended target.
                boolean deviate = Globals.r.nextBoolean();
                if(deviate) {
                    double spread = Math.toRadians(Globals.r.nextDouble() * AberrationBoss.PARTICLE_SPREAD);
                    if(spread > 0) {
                        boolean clockwise = Globals.r.nextBoolean();
                        if(clockwise) theta += spread;
                        else theta -= spread;
                    }
                }
            } // End particle deviation.
            
            Particle p = new Particle(theta, 0.0, 8.0, (AberrationBoss.PARTICLE_LIFE / (int)Globals.SLEEP_TIME),
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
    }
    
    private boolean inRange(Point2D.Double playerPos, Point2D.Double myPos) {
        double xD = playerPos.x - myPos.x;
        double yD = playerPos.y - myPos.y;
        return Math.sqrt((xD * xD) + (yD * yD)) <= AberrationBoss.ATTACK_DISTANCE;
    }
}
