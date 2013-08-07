package genericzombieshooter.structures.weapons;

import genericzombieshooter.misc.Globals;
import genericzombieshooter.structures.SprayParticle;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Used to represent the Flamethrower weapon.
 * @author Darin Beaudreau
 */
public class Flamethrower extends Weapon {
    // Final Variables
    private static final int DEFAULT_AMMO = 50;
    private static final int MAX_AMMO = 250;
    private static final int AMMO_PER_USE = 5;
    private static final int PARTICLES_PER_USE = 20;
    private static final int DAMAGE_PER_PARTICLE = 2;
    private static final double PARTICLE_SPREAD = 20.0;
    private static final int PARTICLE_LIFE = 800;
    
    // Member Variables
    private List<SprayParticle> particles;
    public List<SprayParticle> getParticles() { return this.particles; }
    
    public Flamethrower() {
        super("The Flammenwerfer", KeyEvent.VK_2, DEFAULT_AMMO, MAX_AMMO, AMMO_PER_USE);
        
        this.particles = new ArrayList<SprayParticle>();
    }
    
    @Override
    public void updateWeapon() {
        // Update all particles and remove them if their life has expired.
        for(SprayParticle s : this.particles) {
            s.update();
            if(!s.isAlive()) {
                this.particles.remove(s);
            }
        }
    }
    
    @Override
    public void drawAmmo(Graphics2D g2d) {
        // Draw all particles whose life has not yet expired.
        if(this.particles.size() > 0) {
            for(SprayParticle s : this.particles) {
                if(s.isAlive()) s.draw(g2d);
            }
        }
    }
    
    @Override
    public void fire(double theta, Point2D.Double pos) {
        // If there is enough ammo left...
        if(this.canFire()) {
            Random r = new Random();
            // Generate new particles and add them to the list.
            for(int i = 0; i < PARTICLES_PER_USE; i++) {
                int size = r.nextInt(5) + 1;
                this.particles.add(new SprayParticle(size, 
                                                     theta, 
                                                     PARTICLE_SPREAD, 
                                                     pos, 
                                                    (PARTICLE_LIFE / (int)Globals.SLEEP_TIME)));
            }
            // Use up ammo.
            this.consumeAmmo();
        }
    }
    
    @Override
    public int checkForDamage(Rectangle2D.Double rect) {
        int damage = 0;
        // Check all particles for collisions with the target rectangle.
        for(SprayParticle s : this.particles) {
            // If the particle is still alive and has collided with the target.
            if(s.isAlive() && rect.contains(s.getPos())) {
                // Add the damage of the particle and remove it from the list.
                damage += DAMAGE_PER_PARTICLE;
                this.particles.remove(s);
            }
        }
        return damage;
    }
}
