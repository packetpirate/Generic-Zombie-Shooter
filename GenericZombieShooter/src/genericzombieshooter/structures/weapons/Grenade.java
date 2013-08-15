package genericzombieshooter.structures.weapons;

import genericzombieshooter.misc.Images;
import genericzombieshooter.structures.Explosion;
import genericzombieshooter.structures.Particle;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

/**
 * Used to represent the grenade weapon.
 * @author Darin Beaudreau
 */
public class Grenade extends Weapon {
    // Final Variables
    private static final int DEFAULT_AMMO = 1;
    private static final int MAX_AMMO = 3;
    private static final int AMMO_PER_USE = 1;
    private static final int DAMAGE_PER_PARTICLE = 0;
    private static final double PARTICLE_SPREAD = 5.0;
    private static final int THROWING_DISTANCE = 1500;
    
    // Member Variables
    private List<Explosion> explosions;
    public List<Explosion> getExplosions() { return this.explosions; }
    
    public Grenade() {
        super("Nade", KeyEvent.VK_4, "/resources/images/GZS_Grenade.png", DEFAULT_AMMO, MAX_AMMO, AMMO_PER_USE, 100);
        this.explosions = new ArrayList<Explosion>();
    }
    
    @Override
    public void updateWeapon() {
        { // Update particles.
            Iterator<Particle> it = this.particles.iterator();
            while(it.hasNext()) {
                Particle p = it.next();
                p.update();
                if(!p.isAlive()) {
                    this.explosions.add(new Explosion(Images.EXPLOSION_SHEET, p.getPos()));
                    it.remove();
                    continue;
                }
                if(p.outOfBounds()) {
                    it.remove();
                    continue;
                }
            }
        } // End particle updates.
        { // Update explosions.
            Iterator<Explosion> it = this.explosions.iterator();
            while(it.hasNext()) {
                Explosion e = it.next();
                e.getImage().update();
                if(!e.getImage().isActive()) {
                    it.remove();
                }
            }
        } // End explosion updates.
        this.cool();
    }
    
    @Override
    public void drawAmmo(Graphics2D g2d) {
        if(this.particles.size() > 0) {
            try {
                Iterator<Particle> it = this.particles.iterator();
                while(it.hasNext()) {
                    Particle p = it.next();
                    if(p.isAlive()) p.draw(g2d);
                }
            } catch(ConcurrentModificationException cme) {}
        }
        if(this.explosions.size() > 0) {
            try {
                Iterator<Explosion> it = this.explosions.iterator();
                while(it.hasNext()) {
                    Explosion e = it.next();
                    if(e.getImage().isActive()) e.draw(g2d);
                }
            } catch(ConcurrentModificationException cme) {}
        }
    }
    
    @Override
    public void fire(double theta, Point2D.Double pos) {
        if(this.canFire()) {
            Particle p = createGrenadeParticle(theta, pos);
            this.particles.add(p);
            this.consumeAmmo();
            this.resetCooldown();
        }
    }
    
    @Override
    public int getAmmoPackAmount() {
        return DEFAULT_AMMO;
    }
    
    @Override
    public int checkForDamage(Rectangle2D.Double rect) {
        /* The grenade particle itself does nothing. Upon contact with a zombie,
           it stops moving, and once its timer goes off, it explodes. */
        return 0;
    }
    
    /**
     * Creates a new grenade particle via an anonymous inner class so custom
     * update and draw behavior can be used.
     * @param theta The angle to throw the grenade along.
     * @param pos The starting position of the grenade.
     * @return 
     **/
    private Particle createGrenadeParticle(double theta, Point2D.Double pos) {
        Dimension size = new Dimension(16, 16);
        Particle p = new Particle(theta, PARTICLE_SPREAD, 5.0, THROWING_DISTANCE,
                                  pos, size, Images.GRENADE_PARTICLE) {
            @Override
            public void draw(Graphics2D g2d) {
                if(this.image != null) {
                    double x = this.pos.x - (this.size.width / 2);
                    double y = this.pos.y - (this.size.height / 2);
                    g2d.drawImage(this.image, (int)x, (int)y, null);
                }
            }
            
            @Override
            public void update() {
                
            }
        };
        
        return p;
    }
}
