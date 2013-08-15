package genericzombieshooter.structures;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author Darin Beaudreau
 */
public class Explosion extends Point2D.Double {
    private Animation img;
    public Animation getImage() { return this.img; }
    
    public Explosion(BufferedImage bi, Point2D.Double p) {
        super(p.x, p.y);
        this.img = new Animation(bi, 128, 128, 8, (int)p.x, (int)p.y, 50, 0, false);
    }
    
    public void draw(Graphics2D g2d) {
        
    }
}
