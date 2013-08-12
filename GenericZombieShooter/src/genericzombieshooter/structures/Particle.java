package genericzombieshooter.structures;

import genericzombieshooter.GZSFramework;
import genericzombieshooter.misc.Globals;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

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
    
    private BufferedImage image;
    
    public Particle(double _theta, double _spread, double _speed, int _life, Point2D.Double _pos, Dimension _size) {
        this(_theta, _spread, _speed, _life, _pos, _size, null);
    }
    
    /**
     * Constructs a new Particle object.
     * @param _theta The angle between the firing position and the mouse position when fired. Given in radians.
     * @param _spread The maximum spread for this particle. Given in degrees.
     * @param _speed The multiplier used when updating the particle position.
     * @param _life Determines how long before the particle disappears.
     * @param _pos The starting position of the particle.
     * @param _size The size of the particle.
     */
    public Particle(double _theta, double _spread, double _speed, int _life, Point2D.Double _pos, Dimension _size, BufferedImage _image) {
        this.theta = _theta;
        this.speed = _speed;
        this.life = _life;
        this.pos = _pos;
        this.size = _size;
        
        this.image = _image;

        // Determine if the angle of the particle will deviate from its set value.
        boolean mod = Globals.r.nextBoolean();
        double spreadMod = Math.toRadians(Globals.r.nextDouble() * _spread);
        if(mod) spreadMod = -spreadMod;
        this.theta += spreadMod;
    }
    
    /**
     * Draws the particle to the screen.
     * @param g2d The graphics object used to draw the particle.
     **/
    public void draw(Graphics2D g2d) {
        Rectangle2D.Double rect = new Rectangle2D.Double((this.pos.x - (this.size.width / 2)), 
                                                          (this.pos.y - (this.size.height / 2)), 
                                                           this.size.width, this.size.height);
        AffineTransform at = AffineTransform.getRotateInstance(this.theta, this.pos.x, this.pos.y);
        if(this.image == null) g2d.fill(at.createTransformedShape(rect));
        else g2d.drawImage(image, (int)rect.x, (int)rect.y, null);
    }
    
    /**
     * Updates the state of the spray particle.
     **/
    public void update() {
        // Age the particle.
        if(this.isAlive()) this.life--;
        // Update the position of the particle.
        if(this.isAlive()) {
            this.pos.x += this.speed * Math.sin(this.theta);
            this.pos.y += this.speed * Math.cos(this.theta);
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