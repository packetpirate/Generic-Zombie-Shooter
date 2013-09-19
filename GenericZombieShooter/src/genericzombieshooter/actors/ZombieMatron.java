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
import genericzombieshooter.structures.Explosion;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

/**
 * Used to represent the Zombie Matrons, which if not killed within a certain
 * time frame, will burst, releasing 5-10 smaller, faster, weaker versions of
 * normal zombies.
 * @author Darin Beaudreau
 */
public class ZombieMatron extends Zombie {
    // Final Variables
    public static final int EXP_VALUE = 200;
    public static final long TIME_TO_BURST = 8000;
    private static final int MIN_ZOMBIES = 5;
    private static final int MAX_ZOMBIES = 10;
    private static final int MIN_RADIUS = 50;
    private static final int MAX_RADIUS = 100;
    private static final int EXPLOSION_DAMAGE = 50;
    // Member Variables
    private boolean burst;
    private boolean damageDone;
    private Explosion explosion;
    private long spawnTime;
    private long burstTime;
    
    public ZombieMatron(Point2D.Double p_, int health_, int damage_, double speed_, int score_, Animation animation_) {
        super(p_, Globals.ZOMBIE_MATRON_TYPE, health_, damage_, speed_, score_, ZombieMatron.EXP_VALUE, animation_);
        this.burst = false;
        this.damageDone = false;
        this.explosion = new Explosion(Images.BLOOD_SHEET, new Point2D.Double(this.x, this.y));
        this.spawnTime = Globals.gameTime.getElapsedMillis();
        this.burstTime = spawnTime + ZombieMatron.TIME_TO_BURST;
    }
    
    @Override
    public void set(int id, long value) {
        if(id == 0) this.burstTime = value;
    }
    
    @Override
    public boolean isDead() { return (this.getHealth() <= 0 || (this.burst && !this.explosion.getImage().isActive())); }
    
    private boolean collidesWithExplosion(Rectangle2D.Double rect) {
        // Checks to see if the player is intersecting the explosion.
        int width = this.explosion.getSize().width;
        int height = this.explosion.getSize().height;
        Rectangle2D.Double expRect = new Rectangle2D.Double((this.explosion.x - (width / 2)), 
                                                            (this.explosion.y - (height / 2)), 
                                                            width, height);
        return rect.intersects(expRect);
    }
    
    @Override
    public void update(Player player, List<Zombie> toAdd) {
        // If the Zombie Matron's timer has run out, the Matron bursts...
        if((Globals.gameTime.getElapsedMillis() >= burstTime) && !this.burst) {
            this.burst = true;
            this.explosion = new Explosion(Images.BLOOD_SHEET, new Point2D.Double(this.x, this.y));
            Sounds.EXPLOSION.play();
            // A certain number of mini zombies are added to a list, which are later added to the zombies list.
            int numOfZombies = Globals.r.nextInt((ZombieMatron.MAX_ZOMBIES - ZombieMatron.MIN_ZOMBIES) + 1) + ZombieMatron.MIN_ZOMBIES;
            for(int i = 0; i < numOfZombies; i++) {
                Point2D.Double p_ = new Point2D.Double(this.x, this.y);
                double theta_ = Globals.r.nextDouble() * (Math.PI * 2);
                int radius_ = Globals.r.nextInt((ZombieMatron.MAX_RADIUS - ZombieMatron.MIN_RADIUS) + 1) + ZombieMatron.MIN_RADIUS;
                p_.x += radius_ * Math.cos(theta_);
                p_.y += radius_ * Math.sin(theta_);
                Animation a_ = new Animation(Images.ZOMBIE_TINY, 20, 20, 2, (int)p_.x, (int)p_.y, 100, 0, true);
                Zombie z_ = new Zombie(p_, Globals.ZOMBIE_TINY_TYPE, 100, 1, 2, 50, (ZombieMatron.EXP_VALUE / numOfZombies), a_);
                toAdd.add(z_);
            }
        }
        // If the Matron has already burst...
        if(this.burst) {
            // And if the explosion has collided with the player, but no damage has yet been done to the player...
            if(collidesWithExplosion(player) && (!this.damageDone)) {
                // Deal damage to the player and set the damageDone flag to true so it can't damage again.
                player.takeDamage(ZombieMatron.EXPLOSION_DAMAGE);
                this.damageDone = true;
            }
            // If the blood burst animation is not yet finished, update it.
            if(this.explosion.getImage().isActive()) this.explosion.getImage().update();
        }
    }
    
    @Override
    public void draw(Graphics2D g2d) {
        // If the Matron has not yet burst, draw the Matron herself.
        if(!this.burst) super.draw(g2d);
        else {
            // Otherwise, if the explosion animation is still active, draw it.
            if(this.explosion.getImage().isActive()) this.explosion.draw(g2d);
        }
    }
}
