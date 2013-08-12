package genericzombieshooter.structures.weapons;

import genericzombieshooter.misc.Globals;
import genericzombieshooter.structures.Particle;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Used to represent the Flamethrower weapon.
 * @author Darin Beaudreau
 */
public class Flamethrower extends Weapon {
    // Final Variables
    private static final int DEFAULT_AMMO = 50;
    private static final int MAX_AMMO = 250;
    private static final int AMMO_PER_USE = 0;
    private static final int PARTICLES_PER_USE = 8;
    private static final int DAMAGE_PER_PARTICLE = 2;
    private static final double PARTICLE_SPREAD = 15.0;
    private static final int PARTICLE_LIFE_MIN = 1000;
    private static final int PARTICLE_LIFE_MAX = 1400;
    
    public Flamethrower() {
        super("The Flammenwerfer", KeyEvent.VK_3, "/resources/images/GZS_Flamethrower.png", DEFAULT_AMMO, MAX_AMMO, AMMO_PER_USE, 0);
        this.particles = new ArrayList<Particle>();
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
            for(Particle p : this.particles) {
                if(p.isAlive()) p.draw(g2d);
            }
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
                                       new Dimension(size, size));
                this.particles.add(p);
            }
            // Use up ammo.
            this.consumeAmmo();
            this.resetCooldown();
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
}
