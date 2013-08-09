package genericzombieshooter.structures;

import genericzombieshooter.misc.Globals;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

/**
 *
 * @author Darin Beaudreau
 */
public class Particle {
    protected double theta;
    protected double speed;
    protected int life;
    public boolean isAlive() { return this.life > 0; }
    protected Point2D.Double pos;
    public Point2D.Double getPos() { return this.pos; }
    protected Dimension size;
    public Dimension getSize() { return this.size; }
    
    public Particle(double theta, double spread, double speed, int life, Point2D.Double pos, Dimension size) {
        this.theta = theta;
        this.speed = speed;
        this.life = life;
        this.pos = pos;
        this.size = size;
        
        // Determine if the angle of the particle will deviate from its set value.
        Random r = new Random();
        boolean deviate = r.nextBoolean();
        if(deviate) {
            boolean mod = r.nextBoolean();
            double spreadMod = r.nextDouble() * spread;
            spread = (spreadMod * ((mod)?1:-1));
            this.theta += spread;
        }
    }
    
    /**
     * Draws the particle to the screen.
     * @param g2d The graphics object used to draw the particle.
     **/
    public void draw(Graphics2D g2d) {
        AffineTransform at = AffineTransform.getRotateInstance(this.theta, this.pos.x, this.pos.y);
        g2d.setTransform(at);
        g2d.fillRect(((int)this.pos.x - (this.size.width / 2)), 
                     ((int)this.pos.y - (this.size.height / 2)), 
                     this.size.width, this.size.height);
    }
    
    /**
     * Updates the state of the spray particle.
     **/
    public void update() {
        // Age the particle.
        if(this.life > 0) --this.life;
        // Update the position of the particle.
        if(this.isAlive()) {
            double dx = Math.sin(this.theta);
            double dy = Math.cos(this.theta);
            this.pos.x += dx * speed;
            this.pos.y += dy * speed;
        }
    }
    
    public boolean checkCollision(Rectangle2D.Double rect) {
        if(rect.contains(pos)) {
            this.life = 0;
            return true;
        } else return false;
    }
    
    public boolean outOfBounds() {
        boolean top = this.pos.y < 0;
        boolean right = this.pos.x > Globals.W_WIDTH;
        boolean bottom = this.pos.y > Globals.W_HEIGHT;
        boolean left = this.pos.x < 0;
        return top || right || bottom || left;
    }
}