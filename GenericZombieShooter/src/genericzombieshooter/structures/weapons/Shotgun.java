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
import java.util.List;

/**
 * Used to represent the Shotgun weapon.
 * @author Darin Beaudreau
 */
public class Shotgun extends Weapon {
    // Final Variables
    private static final int DEFAULT_AMMO = 24;
    private static final int MAX_AMMO = 64;
    private static final int AMMO_PER_USE = 0;
    private static final int PARTICLES_PER_USE = 8;
    private static final int DAMAGE_PER_PARTICLE = 20;
    private static final double PARTICLE_SPREAD = 50.0;
    private static final int PARTICLE_LIFE = 1000;
    
    // Member Variables
    private List<Particle> particles;
    public List<Particle> getParticles() { return this.particles; }
    
    public Shotgun() {
        super("Boomstick", KeyEvent.VK_2, DEFAULT_AMMO, MAX_AMMO, AMMO_PER_USE, 25);
        this.particles = new ArrayList<Particle>();
    }
    
    @Override
    public void updateWeapon() {
        // Update all particles and remove them if their life has expired or they are out of bounds.
        Iterator<Particle> it = this.particles.iterator();
        while(it.hasNext()) {
            Particle p = it.next();
            p.update();
            if(!p.isAlive()) {
                it.remove();
                continue;
            }
            if(p.outOfBounds()) {
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
            g2d.setColor(Color.YELLOW);
            for(Particle p : this.particles) {
                if(p.isAlive()) p.draw(g2d);
            }
        }
    }
    
    @Override
    public void fire(double theta, Point2D.Double pos) {
        // If there is enough ammo left...
        if(this.canFire()) {
            // GenerSystem.out.println("(theta, spread, speed, life, pos, size)");ate new particles and add them to the list.
            //System.out.println("(theta, spread, speed, life, pos, size)");
            for(int i = 0; i < Shotgun.PARTICLES_PER_USE; i++) {
                /*System.out.println("(" + theta + ", " + Shotgun.PARTICLE_SPREAD + 
                                   ", " + 1.0 + ", " + (Shotgun.PARTICLE_LIFE / (int)Globals.SLEEP_TIME) + 
                                   ", (" + pos.x + ", " + pos.y + "), " + 5 + ")");*/
                this.particles.add(new Particle(theta, Shotgun.PARTICLE_SPREAD, 1.0,
                                                (Shotgun.PARTICLE_LIFE / (int)Globals.SLEEP_TIME), pos,
                                                new Dimension(5, 5)));
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
