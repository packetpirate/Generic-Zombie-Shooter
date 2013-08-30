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

import genericzombieshooter.structures.Animation;
import genericzombieshooter.structures.Particle;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

/**
 * Used to represent the various types of zombies.
 * @author Darin Beaudreau
 */
public class Zombie extends Point2D.Double {
    // Member variables.
    private AffineTransform af;
    private Animation img;
    
    private int type;
    private int health; // How much health the zombie has.
    private int damage; // How much damage the zombie does per tick to the player.
    private double speed; // How fast the zombie moves.
    private int scoreValue; // How many points the zombie is worth.
    
    public Zombie(Point2D.Double p_, int type_,  int health_, int damage_, double speed_, int score_, Animation animation_) {
        super(p_.x, p_.y);
        this.af = new AffineTransform();
        this.img = animation_;
        
        this.type = type_;
        this.health = health_;
        this.damage = damage_;
        this.speed = speed_;
        this.scoreValue = score_;
    }
    
    // Getter/Setter methods.
    public void set(int id, long value) {
        // To be overridden.
    }
    public int getType() { return this.type; }
    public int getHealth() { return this.health; }
    public void takeDamage(int damage_) { this.health -= damage_; }
    public int getDamage() { return this.damage; }
    public int getScore() { return this.scoreValue; }
    public AffineTransform getTransform() { return this.af; }
    public Animation getImage() { return this.img; }
    public Rectangle2D.Double getRect() {
        double width = this.getImage().getWidth();
        double height = this.getImage().getHeight();
        return new Rectangle2D.Double((this.x - (width / 2)), (this.y - (height / 2)), width, height); 
    }
    
    public boolean isDead() { return this.health <= 0; }
    
    // Shape manipulation.
    public void rotate(double theta_) { this.af.setToRotation(theta_, this.x, this.y); }
    
    public void move(double theta_) {
        this.x += this.speed * Math.cos(theta_ - (Math.PI / 2));
        this.y += this.speed * Math.sin(theta_ - (Math.PI / 2));
        this.img.move((int)this.x, (int)this.y);
    }
    
    public void update(Player player, List<Zombie> zombies) {
        // To be overridden.
    }
    
    public void draw(Graphics2D g2d) {
        // Can be overridden.
        g2d.setTransform(this.af);
        this.img.draw((Graphics2D)g2d);
    }
    
    public List<Particle> getParticles() {
        // To be overridden.
        return null;
    }
    
    public void drawParticles(Graphics2D g2d) {
        // To be overridden.
    }
    
    public int getParticleDamage() { 
        // To be overridden.
        return 0;
    }
}
