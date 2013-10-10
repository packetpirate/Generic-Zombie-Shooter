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
import genericzombieshooter.misc.Sounds;
import genericzombieshooter.structures.Animation;
import genericzombieshooter.structures.Particle;
import genericzombieshooter.structures.items.Invulnerability;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Used to represent the boss zombie, Stitches.
 * @author Darin Beaudreau
 */
public class StitchesBoss extends Zombie {
    // Final Variables
    private static final int EXP_VALUE = 10000;
    private static final int COOL_TIME = 5000 / (int)Globals.SLEEP_TIME;
    private static final double ATTACK_DISTANCE = 364.0;
    private static final double POISON_RANGE = 114.0;
    private static final int POISON_DAMAGE = 10;
    private static final long HOOK_DURATION = 10000;
    
    // Member Variables
    private List<Particle> particles;
    @Override
    public List<Particle> getParticles() { return this.particles; }
    private int cooldown;
    public boolean canFire() { return (this.cooldown == 0 && this.particles.isEmpty()); }
    private boolean hooked;
    private long hookEndTime;
    
    public StitchesBoss(Point2D.Double p_, int health_, int damage_, double speed_, int score_, Animation animation_) {
        super(p_, Globals.ZOMBIE_BOSS_STITCHES_TYPE, health_, damage_, speed_, score_, StitchesBoss.EXP_VALUE, animation_);
        this.particles = new ArrayList<Particle>();
        this.cooldown = StitchesBoss.COOL_TIME;
        this.hooked = false;
        this.hookEndTime = 0;
    }
    
    public void drawParticles(Graphics2D g2d) {
        synchronized(this.particles) {
            Iterator<Particle> it = this.particles.iterator();
            while(it.hasNext()) {
                Particle p = it.next();
                if(p.isAlive() || this.hooked) {
                    Stroke s = g2d.getStroke();
                    g2d.setColor(Color.BLACK);
                    g2d.setStroke(new BasicStroke(2.0f));
                    g2d.drawLine((int)(this.x), (int)(this.y), (int)p.getPos().x, (int)p.getPos().y);
                    g2d.setStroke(s);
                    p.draw(g2d);
                }
            }
        }
    }
    
    @Override
    public void update(Player player, List<Zombie> zombies) {
        Point2D.Double playerPos = new Point2D.Double(player.getCenterX(), player.getCenterY());
        Point2D.Double myPos = new Point2D.Double(this.x, this.y);
        
        // Update the hook.
        if(!this.particles.isEmpty()) {
            synchronized(this.particles) {
                Iterator<Particle> it = this.particles.iterator();
                while(it.hasNext()) {
                    Particle p = it.next();
                    // If the hook hasn't attached to something yet...
                    if(!this.hooked) p.update();
                    else p.setPos(new Point2D.Double(player.getCenterX(), player.getCenterY())); // Otherwise, move it to the player...
                    
                    if(this.hooked) { // Update the hook's theta.
                        double theta = Math.atan2((player.y - p.getPos().y), (player.x - p.getPos().x)) - Math.PI;
                        p.setTheta(theta);
                    } // End hook's theta update.
                    
                    // If not hooked and the hook is not alive or is out of bounds... remove it.
                    if(!this.hooked && (!p.isAlive() || p.outOfBounds())) {
                        it.remove();
                        continue;
                    }
                    
                    // If not hooked and there's a collision between a hook and a player...
                    if(!this.hooked && p.checkCollision(player)) {
                        // If the hook hits the player...
                        this.hooked = true;
                        this.hookEndTime = Globals.gameTime.getElapsedMillis() + StitchesBoss.HOOK_DURATION;
                        p.setLife((int)(StitchesBoss.HOOK_DURATION / Globals.SLEEP_TIME));
                    }
                    
                    // If there IS a hook attached to the player...
                    if(this.hooked) {
                        // Will detach the hook if the player is invulnerable or has recently respawned.
                        if(!player.hasEffect(Invulnerability.EFFECT_NAME)) {
                            if(Globals.gameTime.getElapsedMillis() >= this.hookEndTime) {
                                // Detach the hook.
                                this.hooked = false;
                                this.hookEndTime = 0;
                                it.remove();
                                continue;
                            } else {
                                // Reel in the player.
                                double theta = Math.atan2((this.y - player.y), (this.x - player.x));
                                Point2D.Double newPos = new Point2D.Double(player.x, player.y);
                                newPos.x += (player.getSpeed() + 0.2) * Math.cos(theta);
                                newPos.y += (player.getSpeed() + 0.2) * Math.sin(theta);
                                player.move(newPos);
                            }
                        } else {
                            // Detach the hook.
                            this.hooked = false;
                            this.hookEndTime = 0;
                            it.remove();
                            continue;
                        }
                    }
                }
            }
        } else { // If there are no active hooks...
            // If Stitches can throw a hook, throw it.
            if(this.canFire()) {
                if(inRange(playerPos, myPos, StitchesBoss.ATTACK_DISTANCE)) {
                    this.throwHook(playerPos);
                    this.cooldown = StitchesBoss.COOL_TIME;
                }
            } else this.cooldown--;
        }
        // If the player is within range of Stitches, poison him...
        if(inRange(playerPos, myPos, StitchesBoss.POISON_RANGE)) {
            player.addStatusEffect(6, "Poison", Images.POISON_STATUS_ICON, 
                                   1000, StitchesBoss.POISON_DAMAGE);
        }
    }
    
    @Override
    public void draw(Graphics2D g2d) {
        AffineTransform saved = g2d.getTransform();
        if(!this.particles.isEmpty()) drawParticles(g2d);
        super.draw(g2d);
        g2d.setTransform(saved);
    }
    
    @Override
    public void moan(Player player) {
        if(!this.moaned) {
            if(Globals.gameTime.getElapsedMillis() >= this.nextMoan) {
                double xD = player.getCenterX() - this.x;
                double yD = player.getCenterY() - this.y;
                double dist = Math.sqrt((xD * xD) + (yD * yD));
                double gain = 1.0 - (dist / Player.AUDIO_RANGE);
                Sounds.MOAN6.play(gain);
                this.moaned = true;
            }
        }
    }
    
    private void throwHook(Point2D.Double playerPos) {
        double theta = Math.atan2((playerPos.y - this.y), (playerPos.x - this.x)) - Math.PI;
        
        Particle p = new Particle(theta, 0.0, 6.0, 1, new Point2D.Double(this.x, this.y), 
                                  new Dimension(16, 16), Images.STITCHES_HOOK) {
            @Override
            public void update() {
              if(this.isAlive()) {
                  // Update the position.
                  this.pos.x += -(this.speed * Math.cos(this.theta));
                  this.pos.y += -(this.speed * Math.sin(this.theta));
              }
            }
            @Override
            public boolean checkCollision(Rectangle2D.Double rect) {
                if(rect.contains(this.pos)) {
                    return true;
                } else return false;
            }
        };
        this.particles.add(p);
    }
    
    private boolean inRange(Point2D.Double playerPos, Point2D.Double myPos, double distance) {
        double xD = playerPos.x - myPos.x;
        double yD = playerPos.y - myPos.y;
        return Math.sqrt((xD * xD) + (yD * yD)) <= distance;
    }
}
