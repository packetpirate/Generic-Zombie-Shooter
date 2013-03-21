package genericzombieshooter.actors;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Darin Beaudreau
 */
public class Player extends Rectangle2D.Double {
    // Constant variables.
    private static final double MOVE_SPEED = 2; // How many pixels per tick the player moves.
    private static final int WEAPON_CD = 10;
    // Member variables.
    private AffineTransform af;
    private BufferedImage img;
    
    private int health;
    private int cooldown; // How many ticks before gun can be fired again.
    
    public Player() {
        super();
        af = new AffineTransform();
        LoadImage();
        
        health = 200;
        cooldown = WEAPON_CD;
    }
    
    public Player(double x_, double y_, double w_, double h_) {
        super(x_, y_, w_, h_);
        af = new AffineTransform();
        LoadImage();
        
        health = 200;
        cooldown = WEAPON_CD;
    }
    
    public Player(Rectangle2D.Double rect_) {
        super(rect_.x, rect_.y, rect_.width, rect_.height);
        af = new AffineTransform();
        LoadImage();
        
        health = 200;
        cooldown = WEAPON_CD;
    }
    
    private void LoadImage() {
        try {
            img = ImageIO.read(getClass().getResource("/resources/images/GZS_Player.png"));
        } catch(IOException io) {
            System.out.println("File not found!");
        }
    }
    
    // Getter/Setter methods.
    public double getMoveSpeed() { return MOVE_SPEED; }
    public AffineTransform getTransform() { return af; }
    public BufferedImage getImage() { return img; }
    public Shape createTransformedShape() { return af.createTransformedShape(this); }
    
    public int getHealth() { return health; }
    public int getCooldown() { return cooldown; }
    
    public void decCooldown() { if(cooldown > 0) cooldown--; }
    public boolean isOnCooldown() {
        if(cooldown == 0) {
            cooldown = WEAPON_CD;
            return false;
        } else return true;
    }
    
    // Shape manipulation.
    public void rotate(double theta_) { af.setToRotation(theta_, getCenterX(), getCenterY()); }
    
    // Player manipulation.
    /*
     * @param direction The direction to move in.
     */
    public void move(int direction) {
        switch(direction) {
            case 0: // Up
                y -= MOVE_SPEED;
                break;
            case 1: // Right
                x += MOVE_SPEED;
                break;
            case 2: // Down
                y += MOVE_SPEED;
                break;
            case 3: // Left
                x -= MOVE_SPEED;
                break;
            default: // No direction.
                break;
        }
    }
    
    public void takeDamage(int damage_) { health -= damage_; }
}
