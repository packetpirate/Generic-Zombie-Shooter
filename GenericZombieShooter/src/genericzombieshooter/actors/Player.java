package genericzombieshooter.actors;

import genericzombieshooter.misc.Globals;
import genericzombieshooter.structures.weapons.Weapon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Represents the player character.
 * @author Darin Beaudreau
 **/
public class Player extends Rectangle2D.Double {
    // Final variables.
    private static final double MOVE_SPEED = 2; // How many pixels per tick the player moves.
    
    // Member variables.
    private AffineTransform af;
    private BufferedImage img;
    private int health;
    private Weapon weapon;

    public Player(double x_, double y_, double w_, double h_) {
        super(x_, y_, w_, h_);
        af = new AffineTransform();
        LoadImage();

        health = 200;
        weapon = Globals.ASSAULT_RIFLE;
    }

    private void LoadImage() {
        try {
            img = ImageIO.read(getClass().getResource("/resources/images/GZS_Player.png"));
        } catch (IOException io) {
            System.out.println("File not found!");
        }
    }

    // Getter/Setter methods.
    public AffineTransform getTransform() { return af; }
    public BufferedImage getImage() { return img; }

    public int getHealth() { return health; }
    
    public Weapon getWeapon() { return weapon; }
    public void setWeapon(int w) {
        if(w == 1) weapon = Globals.ASSAULT_RIFLE;
        else if(w == 2) weapon = Globals.SHOTGUN;
        else if(w == 3) weapon = Globals.FLAMETHROWER;
    }

    // Shape manipulation.
    public void rotate(double theta_) { af.setToRotation(theta_, getCenterX(), getCenterY()); }

    // Player manipulation.
    /**
     * Moves the player object on the screen.
     * @param direction The direction to move in.
     **/
    public void move(int direction) {
        switch (direction) {
            case 0: // Up
                y -= MOVE_SPEED;
                break;
            case 1: // Left
                x -= MOVE_SPEED;
                break;
            case 2: // Down
                y += MOVE_SPEED;
                break;
            case 3: // Right
                x += MOVE_SPEED;
                break;
            default: // No direction.
                break;
        }
    }

    public void takeDamage(int damage_) { health -= damage_; }
}
