
package genericzombieshooter.structures.weapons;

import genericzombieshooter.actors.Zombie;
import genericzombieshooter.misc.Globals;
import genericzombieshooter.misc.Images;
import genericzombieshooter.structures.Particle;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Used to deploy laser terminals. When two are deployed, a laser is drawn
 * between them that damages enemies that pass through it. Only one laser
 * wire can be deployed at a time.
 * @author Darin Beaudreau
 */
public class LaserWire extends Weapon {
    // Final Variables
    private static final int DEFAULT_AMMO = 1;
    private static final int MAX_AMMO = 1;
    private static final int AMMO_PER_USE = 1;
    private static final int DAMAGE_BY_LASER = 100;
    private static final long LASER_COOLDOWN = 500;
    private static final int PARTICLE_LIFE = 2 * 60 * 1000;
    private static final int LASER_LIFE = 30 * 1000;
    private static final int MAX_LASER_DIST = 300;
    
    // Member Variables
    private List<Line2D.Double> lasers;
    private long lastDamageDone;
    
    public LaserWire() {
        super("Laser Wire", KeyEvent.VK_8, "/resources/images/GZS_LaserWire.png",
              LaserWire.DEFAULT_AMMO, LaserWire.MAX_AMMO, LaserWire.AMMO_PER_USE, 50, false);
        this.lasers = Collections.synchronizedList(new ArrayList<Line2D.Double>());
        this.lastDamageDone = System.currentTimeMillis();
    }
    
    @Override
    public int getAmmoPackAmount() { return LaserWire.DEFAULT_AMMO; }
    
    @Override
    public void resetAmmo() {
        super.resetAmmo();
        this.lasers = Collections.synchronizedList(new ArrayList<Line2D.Double>());
        this.ammoLeft = LaserWire.DEFAULT_AMMO;
    }
    
    @Override
    public boolean canFire() {
        boolean lessThanTwoTerminals = this.particles.size() < 2;
        return super.canFire() && lessThanTwoTerminals;
    }
    
    @Override
    public void updateWeapon(List<Zombie> zombies) {
        { // Update particles.
            synchronized(this.particles) {
                if(!this.particles.isEmpty()) {
                    Iterator<Particle> it = this.particles.iterator();
                    while(it.hasNext()) {
                        Particle p = it.next();
                        p.update();
                        
                        if(!p.isAlive()) {
                            it.remove();
                            continue;
                        }
                    }
                } else {
                    synchronized(this.lasers) {
                        if(!this.lasers.isEmpty()) this.lasers.clear();
                    }
                }
                
                // Check if there are exactly two terminals. If so, create a laser.
                if((this.particles.size() == 2) && this.lasers.isEmpty()) {
                    Point2D.Double p1 = this.particles.get(0).getPos();
                    Point2D.Double p2 = this.particles.get(1).getPos();
                    /* If the distance between the two terminals is too far,
                       refund the player's ammo and delete the two terminals placed. */
                    double xD = p1.x - p2.x;
                    double yD = p1.y - p2.y;
                    if((Math.sqrt((xD * xD) + (yD * yD))) >= LaserWire.MAX_LASER_DIST) {
                        this.particles.clear();
                        this.ammoLeft = LaserWire.DEFAULT_AMMO;
                    } else {
                        this.lasers.add(new Line2D.Double(p1, p2));
                        int newLife = LaserWire.LASER_LIFE / (int)Globals.SLEEP_TIME;
                        this.particles.get(0).setLife(newLife);
                        this.particles.get(1).setLife(newLife);
                        this.lastDamageDone = System.currentTimeMillis();
                    }
                }
            }
        } // End particle updates.
        this.cool();
    }
    
    @Override
    public void drawAmmo(Graphics2D g2d) {
        { // Draw particles.
            synchronized(this.particles) {
                if(!this.particles.isEmpty()) {
                    Iterator<Particle> it = this.particles.iterator();
                    while(it.hasNext()) {
                        Particle p = it.next();
                        if(p.isAlive()) p.draw(g2d);
                    }
                }
                if(this.particles.size() == 1) {
                    Point2D.Double pos = this.particles.get(0).getPos();
                    g2d.setColor(new Color(191, 74, 99));
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawOval((int)(pos.x - LaserWire.MAX_LASER_DIST), (int)(pos.y - LaserWire.MAX_LASER_DIST), 
                                 (LaserWire.MAX_LASER_DIST * 2), (LaserWire.MAX_LASER_DIST * 2));
                }
            }
        } // End drawing particles.
        { // Draw lasers.
            synchronized(this.lasers) {
                if(!this.lasers.isEmpty()) {
                    g2d.setColor(new Color(191, 74, 99));
                    g2d.setStroke(new BasicStroke(2));
                    Iterator<Line2D.Double> it = this.lasers.iterator();
                    while(it.hasNext()) {
                        Line2D.Double line = it.next();
                        g2d.draw(line);
                    }
                }
            }
        } // End drawing lasers.
    }
    
    @Override
    public void fire(double theta, Point2D.Double pos) {
        synchronized(this.particles) {
            if(this.canFire()) {
                Particle p = createLaserTerminal(theta, pos);
                this.particles.add(p);
                if(this.particles.size() == 2) this.consumeAmmo();
                this.resetCooldown();
                this.fired = true;
            }
        }
    }
    
    private Particle createLaserTerminal(double theta, Point2D.Double pos) {
        Particle p = new Particle(theta, 0.0, 0.0, (LaserWire.PARTICLE_LIFE / (int)Globals.SLEEP_TIME),
                                  pos, new Dimension(16, 16), Images.LASER_TERMINAL) {
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
        synchronized(this.lasers) {
            int damage = 0;
            if(System.currentTimeMillis() >= (this.lastDamageDone + LaserWire.LASER_COOLDOWN)) {
                if(!this.lasers.isEmpty()) {
                    Iterator<Line2D.Double> it = this.lasers.iterator();
                    while(it.hasNext()) {
                        Line2D.Double laser = it.next();
                        if(rect.intersectsLine(laser)) {
                            damage += LaserWire.DAMAGE_BY_LASER;
                            this.lastDamageDone = System.currentTimeMillis();
                        }
                    }
                }
            }
            return damage;
        }
    }
}
