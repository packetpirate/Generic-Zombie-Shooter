package genericzombieshooter.structures;

import genericzombieshooter.misc.Globals;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

/**
 * Used to represent particles emitted from spray weapons, such as a flamethrower.
 * 
 * @author Darin Beaudreau
 */
public class SprayParticle {
    private int size; // The size in pixels of the particle. Used for width and height.
    private double theta; // The angle that the particle moves in.
    private Point2D.Double pos; // The location of the particle on the screen.
    public Point2D.Double getPos() { return this.pos; }
    private int life; // The current time left before the particle expires. Measured in MS / SLEEP TIME.
    public boolean isAlive() { return this.life > 0; }
    
    /**
     * Creates a new spray particle object.
     * @param size The size of the particle in pixels.
     * @param theta The angle that the particle will travel along and be rotated to face.
     * @param spread The maximum potential deviation from the angle specified. Is calculated both negative and positive.
     * @param pos The position of the particle on the screen when it is created.
     * @param life The lifespan of the particle before it destroys itself. Measured in MS / SLEEP_TIME. So a particle that lasts
     *             500 ms should receive a parameter of (500 / SLEEP_TIME).
     **/
    public SprayParticle(int size, double theta, double spread, Point2D.Double pos, int life) {
        this.size = size;
        this.theta = theta;
        this.pos = pos;
        this.life = life;
        
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
        g2d.drawRect(((int)this.pos.x - (this.size / 2)), 
                     ((int)this.pos.y - (this.size / 2)), 
                     this.size, this.size);
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
            this.pos.x += dx * 0.3;
            this.pos.y += dy * 0.3;
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
