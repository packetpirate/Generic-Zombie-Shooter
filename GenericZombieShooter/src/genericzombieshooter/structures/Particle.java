package genericzombieshooter.structures;

import genericzombieshooter.misc.Globals;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Used to represent the particles emitted from weapons.
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
    
    /**
     * Constructs a new Particle object.
     * @param _theta The angle between the firing position and the mouse position when fired. Given in radians.
     * @param _spread The maximum spread for this particle. Given in degrees.
     * @param _speed The multiplier used when updating the particle position.
     * @param _life Determines how long before the particle disappears.
     * @param _pos The starting position of the particle.
     * @param _size The size of the particle.
     */
    public Particle(double _theta, double _spread, double _speed, int _life, Point2D.Double _pos, Dimension _size) {
        this.theta = _theta;
        this.speed = _speed;
        this.life = _life;
        this.pos = _pos;
        this.size = _size;

        // Determine if the angle of the particle will deviate from its set value.
        boolean deviate = Globals.r.nextBoolean();
        if(deviate) {
            boolean mod = Globals.r.nextBoolean();
            double spreadMod = Math.toRadians(Globals.r.nextDouble() * _spread);
            if(mod) spreadMod = -spreadMod;
            this.theta += spreadMod;
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
        if(this.isAlive()) --this.life;
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