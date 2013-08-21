/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package genericzombieshooter.actors;

import genericzombieshooter.misc.Images;
import genericzombieshooter.misc.Sounds;
import genericzombieshooter.structures.Animation;
import genericzombieshooter.structures.Explosion;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author Darin Beaudreau
 */
public class ExplosiveZombie extends Zombie {
    // Final Variables
    private static final int EXPLOSION_DAMAGE = 80;
    // Member Variables
    private Explosion explosion;
    private boolean exploded;
    private boolean damageDone;
    
    public ExplosiveZombie(Point2D.Double p_, int health_, int damage_, double speed_, int score_, Animation animation_) {
        super(p_, health_, damage_, speed_, score_, animation_);
        this.explosion = new Explosion(Images.EXPLOSION_SHEET, new Point2D.Double(this.x, this.y));
        this.exploded = false;
        this.damageDone = false;
    }
    
    @Override
    public boolean isDead() {
        // If the zombie has exploded and the explosion animation is no longer active, return true.
        return (this.exploded && !this.explosion.getImage().isActive());
    }
    
    @Override
    public int getDamage() {
        if(!isDead()) return super.getDamage();
        else return 0;
    }
    
    private boolean collidesWithExplosion(Rectangle2D.Double rect) {
        int width = this.explosion.getSize().width;
        int height = this.explosion.getSize().height;
        Rectangle2D.Double expRect = new Rectangle2D.Double((this.explosion.x - (width / 2)), 
                                                            (this.explosion.y - (height / 2)), 
                                                            width, height);
        return rect.intersects(expRect);
    }
    
    @Override
    public void update(Player player) {
        // If the zombie has been shot and killed, set exploded to true.
        if(this.getHealth() <= 0 && !this.exploded) {
            this.exploded = true;
            this.explosion = new Explosion(Images.EXPLOSION_SHEET, new Point2D.Double(this.x, this.y));
            Sounds.EXPLOSION.play();
        }
        if(this.exploded) { // Otherwise...
            // Update the explosion animation and damage the player if he is touching it.
            if(collidesWithExplosion(player) && !this.damageDone) { 
                player.takeDamage(ExplosiveZombie.EXPLOSION_DAMAGE);
                this.damageDone = true;
            }
            if(this.explosion.getImage().isActive()) this.explosion.getImage().update();
        }
    }
    
    @Override
    public void draw(Graphics2D g2d) {
        if(!this.exploded) {
            // If the zombie has not been shot yet, continue drawing the zombie.
            g2d.setTransform(this.getTransform());
            this.getImage().draw((Graphics2D)g2d);
        } else {
            // Otherwise, draw the explosion.
            if(this.explosion.getImage().isActive()) this.explosion.draw(g2d);
        }
    }
}
