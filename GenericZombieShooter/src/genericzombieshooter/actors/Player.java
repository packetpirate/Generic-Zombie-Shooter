package genericzombieshooter.actors;

import genericzombieshooter.GZSFramework;
import genericzombieshooter.misc.Globals;
import genericzombieshooter.misc.Images;
import genericzombieshooter.structures.weapons.Weapon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

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
    private int lives;
    private int currentWeapon;
    private List<Weapon> weapons;

    public Player(double x_, double y_, double w_, double h_) {
        super(x_, y_, w_, h_);
        af = new AffineTransform();
        img = Images.PLAYER;

        health = 200;
        lives = 3;
        currentWeapon = 1;
        weapons = new ArrayList<Weapon>();
        weapons.add(Globals.ASSAULT_RIFLE); // Weapon 1
        weapons.add(Globals.SHOTGUN); // Weapon 2
        weapons.add(Globals.FLAMETHROWER); // Weapon 3
    }

    // Getter/Setter methods.
    public AffineTransform getTransform() { return af; }
    public BufferedImage getImage() { return img; }

    public int getHealth() { return health; }
    public boolean isAlive() { return health > 0; }
    public int getLives() { return lives; }
    public void die() { 
        lives--;
        if(lives > 0) reset();
    }
    public void reset() {
        health = 200;
        if(lives == 0) lives = 3;
        currentWeapon = 1;
        this.x = (Globals.W_WIDTH / 2) - (this.width / 2);
        this.y = (Globals.W_HEIGHT / 2) - (this.height / 2);
    }
    
    public Weapon getWeapon() { return weapons.get(this.currentWeapon - 1); }
    public Weapon getWeapon(int w) { return weapons.get(w - 1); }
    public int getCurrentWeapon() { return this.currentWeapon; }
    public List<Weapon> getAllWeapons() { return this.weapons; }
    public void setWeapon(int w) { this.currentWeapon = w; }

    // Shape manipulation.
    public void rotate(double theta_) { af.setToRotation(theta_, getCenterX(), getCenterY()); }

    // Player manipulation.
    /**
     * Moves the player object on the screen.
     * @param direction The direction to move in.
     **/
    public void move(int direction) {
        if(direction == 0) y -= MOVE_SPEED;
        else if(direction == 1) x -= MOVE_SPEED;
        else if(direction == 2) y += MOVE_SPEED;
        else if(direction == 3) x += MOVE_SPEED;
    }

    public void takeDamage(int damage_) { health -= damage_; }
}
