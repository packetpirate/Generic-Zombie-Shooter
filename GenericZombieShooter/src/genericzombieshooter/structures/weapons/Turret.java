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

import genericzombieshooter.GZSFramework;
import genericzombieshooter.actors.Zombie;
import genericzombieshooter.misc.Globals;
import genericzombieshooter.misc.Images;
import genericzombieshooter.misc.Sounds;
import genericzombieshooter.structures.Particle;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * An automated turret that fires at nearby zombies.
 * @author Darin Beaudreau
 */
public class Turret extends Point2D.Double {
    // Final Variables
    private static final int DAMAGE = 100;
    private static final double PARTICLE_SPREAD = 5.0;
    private static final long PARTICLE_LIFE = 2000;
    private static final long FIRING_RATE = 400;
    private static final int ATTACK_RADIUS = 250;
    
    // Member Variables
    private BufferedImage turretMount;
    private BufferedImage turretHead;
    
    private List<Particle> particles;
    
    private Zombie target; // Used to calculate position to fire at each update.
    private double theta;
    
    private long lastFired;
    private long deathTime;
    
    public Turret(Point2D.Double pos, long life) {
        super(pos.x, pos.y);
        try {
            BufferedImage turretPieces = GZSFramework.loadImage("/resources/images/GZS_TurretPieces.png");
            this.turretMount = turretPieces.getSubimage(0, 0, 48, 48);
            this.turretHead = turretPieces.getSubimage(48, 0, 48, 48);
        } catch(RasterFormatException rfe) {
            System.out.println("Problem getting subimages of turret.");
        }
        this.particles = Collections.synchronizedList(new ArrayList<Particle>());
        this.target = null;
        this.theta = 0;
        this.lastFired = Globals.gameTime.getElapsedMillis();
        this.deathTime = Globals.gameTime.getElapsedMillis() + life;
    }
    
    public boolean isAlive() { return (Globals.gameTime.getElapsedMillis() < this.deathTime); }
    private boolean canFire() { return (Globals.gameTime.getElapsedMillis() >= (this.lastFired + Turret.FIRING_RATE)); }
    
    public void reset() {
        synchronized(this.particles) { this.particles.clear(); }
    }
    
    public void update(List<Zombie> targets) {
        if(this.isAlive()) {
            { // Update particles.
                synchronized(this.particles) {
                    if(!this.particles.isEmpty()) {
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
            } // End particle updates.
            { // Update zombie target.
                if(!targets.isEmpty()) {
                    double xD = 1000;
                    double yD = 1000;
                    double dist = Math.sqrt((xD * xD) + (yD * yD));
                    if(this.target != null) {
                        xD = this.target.x - this.x;
                        yD = this.target.y - this.y;
                        dist = Math.sqrt((xD * xD) + (yD * yD));
                    }
                    synchronized(targets) {
                        Iterator<Zombie> it = targets.iterator();
                        while(it.hasNext()) {
                            Zombie z = it.next();
                            double xD2 = z.x - this.x;
                            double yD2 = z.y - this.y;
                            double dist2 = Math.sqrt((xD2 * xD2) + (yD2 * yD2));
                            if((!z.isDead() && (dist2 < dist)) && (dist2 <= Turret.ATTACK_RADIUS)) {
                                // Switch targets.
                                this.target = z;
                            }
                        }
                    }
                    
                    if(this.target != null) {
                        // Update theta for new target.
                        this.theta = Math.atan2((this.y - this.target.y), (this.x - this.target.x)) - (Math.PI / 2);
                    }
                }
            } // End updating target.

            // If the current target is dead, set the target to null.
            if((this.target != null) && (this.target.isDead())) this.target = null;
            
            // Fire a bullet at the nearest zombie.
            if(this.target != null) this.fire();
        }
    }
    
    public void draw(Graphics2D g2d) {
        { // Draw the turrent's mount.
            if(this.turretMount != null) {
                int xPos = (int)(this.x - (this.turretMount.getWidth() / 2));
                int yPos = (int)(this.y - (this.turretMount.getHeight() / 2));
                g2d.drawImage(this.turretMount, xPos, yPos, null);
            } else {
                int xPos = (int)(this.x - 24);
                int yPos = (int)(this.y - 24);
                g2d.setColor(Color.GRAY);
                g2d.fillRect(xPos, yPos, 48, 48);
            }
        } // End drawing the turret's mount.
        { // Draw particles.
            synchronized(this.particles) {
                if(!this.particles.isEmpty()) {
                    Iterator<Particle> it = this.particles.iterator();
                    while(it.hasNext()) {
                        Particle p = it.next();
                        if(p.isAlive()) p.draw(g2d);
                    }
                }
            }
        } // End drawing particles.
        { // Draw turret head.
            AffineTransform saved = g2d.getTransform();
            g2d.setTransform(AffineTransform.getRotateInstance(this.theta, this.x, this.y));
            if(this.turretHead != null) {
                int xPos = (int)(this.x - (this.turretHead.getWidth() / 2));
                int yPos = (int)(this.y - (this.turretHead.getHeight() / 2));
                g2d.drawImage(this.turretHead, xPos, yPos, null);
            } else {
                int xPos = (int)(this.x - 18);
                int yPos = (int)(this.y - 18);
                g2d.setColor(Color.RED);
                g2d.fillRect(xPos, yPos, 36, 36);
            }
            g2d.setTransform(saved);
        } // End drawing turret head.
        { // Draw turret's ring of influence.
            int xPos = (int)(this.x - Turret.ATTACK_RADIUS);
            int yPos = (int)(this.y - Turret.ATTACK_RADIUS);
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(xPos, yPos, (Turret.ATTACK_RADIUS * 2), (Turret.ATTACK_RADIUS * 2));
        } // End drawing ring of influence.
    }
    
    public void fire() {
        if(this.canFire()) {
            Point2D.Double firingPos = new Point2D.Double(this.x, (this.y - 25));
            AffineTransform.getRotateInstance(this.theta, this.x, this.y).transform(firingPos, firingPos);
            Particle p = new Particle((-this.theta + Math.PI), Turret.PARTICLE_SPREAD, 10.0,
                                      (int)(Turret.PARTICLE_LIFE / Globals.SLEEP_TIME), firingPos,
                                      new Dimension(4, 10), Images.RTPS_BULLET);
            this.particles.add(p);
            this.lastFired = Globals.gameTime.getElapsedMillis();
            Sounds.RTPS.play();
        }
    }
    
    public int checkForDamage(Rectangle2D.Double rect) {
        int damage = 0;
        synchronized(this.particles) {
            if(!this.particles.isEmpty()) {
                Iterator<Particle> it = this.particles.iterator();
                while(it.hasNext()) {
                    Particle p = it.next();
                    if(p.isAlive() && p.checkCollision(rect)) {
                        damage += Turret.DAMAGE;
                        it.remove();
                    }
                }
            }
        }
        return damage;
    }
}
