package genericzombieshooter.actors;

import genericzombieshooter.interfaces.Enemy;
import genericzombieshooter.structures.Animation;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Darin Beaudreau
 */
public class Zombie extends Rectangle2D.Double implements Enemy {
    // Constant variables.
    private static final double MOVE_SPEED = 1;
    // Member variables.
    private AffineTransform af;
    private Animation img;
    
    private int health; // How much health the zombie has.
    private int damage; // How much damage the zombie does per tick to the player.
    private int scoreValue; // How many points the zombie is worth.
    
    public Zombie(Rectangle2D.Double rect_, int health_, int damage_, int score_, String fileName_) {
        super(rect_.x, rect_.y, rect_.width, rect_.height);
        af = new AffineTransform();
        LoadImage(fileName_);
        
        health = health_;
        damage = damage_;
        scoreValue = score_;
    }
    
    private void LoadImage(String fileName_) {
        BufferedImage i;
        try {
            i = ImageIO.read(getClass().getResource(fileName_));
            img = new Animation(i, 40, 40, 2, (int)x, (int)y, 200, 0, true);
        } catch(IOException io) {
        }
    }
    
    // Getter/Setter methods.
    public int getHealth() { return health; }
    public int getDamage() { return damage; }
    public int getScore() { return scoreValue; }
    public AffineTransform getTransform() { return af; }
    public Animation getImage() { return img; }
    
    public boolean isDead() { return health <= 0; }
    
    // Shape manipulation.
    public void rotate(double theta_) { af.setToRotation(theta_, getCenterX(), getCenterY()); }
    
    // Implemented interface methods.
    @Override
    public void move(double x_, double y_) {
        x += x_ * MOVE_SPEED;
        y += y_ * MOVE_SPEED;
        img.move((int)x, (int)y);
    }
    
    @Override
    public void takeDamage(int damage_) { health -= damage_; }
}
