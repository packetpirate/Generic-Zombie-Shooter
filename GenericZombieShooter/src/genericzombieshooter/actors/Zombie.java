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
import genericzombieshooter.misc.Sounds;
import genericzombieshooter.structures.Animation;
import genericzombieshooter.structures.Particle;
import java.awt.Color;
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
    protected double health; // How much health the zombie has.
    private int damage; // How much damage the zombie does per tick to the player.
    private double speed; // How fast the zombie moves.
    private int cashValue; // How many points the zombie is worth.
    private int experience; // How many experience points the zombie is worth.
    
    protected long nextMoan;
    
    public Zombie(Point2D.Double p_, int type_,  int health_, int damage_, double speed_, int cash_, int exp_, Animation animation_) {
        super(p_.x, p_.y);
        this.af = new AffineTransform();
        this.img = animation_;
        
        this.type = type_;
        this.health = health_;
        this.damage = damage_;
        this.speed = speed_;
        this.cashValue = cash_;
        this.experience = exp_;
        
        this.nextMoan = Globals.gameTime.getElapsedMillis() + ((Globals.r.nextInt(7) + 6) * 1000);
    }
    
    // Getter/Setter methods.
    public void set(int id, long value) {
        // To be overridden.
    }
    public int getType() { return this.type; }
    public double getHealth() { return this.health; }
    public void takeDamage(int damage_) { this.health -= damage_; }
    public int getDamage() { return this.damage; }
    public int getCashValue() { return this.cashValue; }
    public int getExpValue() { return this.experience; }
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
        { // Draw zombie's shadow.
            int width = this.img.getWidth();
            int height = this.img.getHeight();
            if(this.type == Globals.ZOMBIE_REGULAR_TYPE) height /= 2;
            else if(this.type == Globals.ZOMBIE_DOG_TYPE) width /= 2;
            else {
                width /= 2;
                height /= 2;
            }
            int xPos = (int)(this.x - (width / 2));
            int yPos = (int)(this.y - (height / 2));
            g2d.setColor(new Color(0, 0, 0, 100));
            g2d.fillOval(xPos, yPos, width, height);
        } // End drawing zombie's shadow.
        this.img.draw((Graphics2D)g2d);
    }
    
    public void moan(Player player) {
        // To be overridden.
        boolean regular = this.type == Globals.ZOMBIE_REGULAR_TYPE;
        boolean dog = this.type == Globals.ZOMBIE_DOG_TYPE;
        if(regular || dog) {
            if(Globals.gameTime.getElapsedMillis() >= this.nextMoan) {
                double xD = player.getCenterX() - this.x;
                double yD = player.getCenterY() - this.y;
                double dist = Math.sqrt((xD * xD) + (yD * yD));
                double gain = 1.0 - (dist / Player.AUDIO_RANGE);
                if(regular) Sounds.MOAN1.play(gain);
                else if(dog) Sounds.MOAN2.play(gain);
                this.nextMoan = Globals.gameTime.getElapsedMillis() + ((Globals.r.nextInt(7) + 6) * 1000);
            }
        }
    }
    
    public List<Particle> getParticles() {
        // To be overridden.
        return null;
    }
    
    public int getParticleDamage() { 
        // To be overridden.
        return 0;
    }
}
