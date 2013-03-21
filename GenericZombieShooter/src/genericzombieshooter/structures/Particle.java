package genericzombieshooter.structures;

import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Darin Beaudreau
 */
public class Particle extends Rectangle2D.Double {
    // Member variables.
    private Point target;
    private double playerAngle;
    private double firingAngle;
    private AffineTransform af;
    private BufferedImage img;
    
    private int damage;
    
    public Particle() { 
        super();
        target = new Point();
        playerAngle = 0.0;
        firingAngle = 0.0;
        af = new AffineTransform();
        img = null;
        
        damage = 10;
    }
    public Particle(double x_, double y_, double w_, double h_) { 
        super(x_, y_, w_, h_);
        target = new Point();
        playerAngle = 0.0;
        firingAngle = 0.0;
        af = new AffineTransform();
        img = null;
        
        damage = 10;
    }
    public Particle(double x_, double y_, double w_, double h_, Point target_) {
        super(x_, y_, w_, h_);
        target = target_;
        playerAngle = Math.atan2((target_.y - y_), (target_.x - x_));
        firingAngle = Math.atan2((target.x - x_), (target.y - y_));
        af = new AffineTransform();
        img = null;
        
        damage = 10;
    }
    public Particle(double x_, double y_, double w_, double h_, int damage_, Point target_, double playerAngle_, double firingAngle_, String fileName_) {
        super(x_, y_, w_, h_);
        target = target_;
        playerAngle = playerAngle_;
        firingAngle = firingAngle_;
        af = new AffineTransform();
        LoadImage(fileName_);
        
        damage = damage_;
    }
    private void LoadImage(String fileName_) {
        try {
            img = ImageIO.read(getClass().getResource(fileName_));
        } catch(IOException io) {
            System.out.println("File not found!");
        }
    }
    
    public Point getTarget() { return target; }
    public void setTarget(Point target_) { target = target_; }
    public double getPlayerAngle() { return playerAngle; }
    public double getFiringAngle() { return firingAngle; }
    public AffineTransform getTransform() { return af; }
    public BufferedImage getImage() { return img; }
    
    public int getDamage() { return damage; }
    
    // Shape manipulation.
    public void rotate(double theta_, double cx_, double cy_) {
        playerAngle = theta_;
        af.setToRotation(theta_, cx_, cy_);
    }
}