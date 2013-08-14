/**
    This file is part of Generic Zombie Shooter.

    Generic Zombie Shooter is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Generic Zombie Shooter is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Generic Zombie Shooter.  If not, see <http://www.gnu.org/licenses/>.
 **/
package genericzombieshooter.actors;

import genericzombieshooter.misc.Globals;
import genericzombieshooter.misc.Images;
import genericzombieshooter.misc.Sounds;
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
    public static final int MAX_HEALTH = 150;
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
        this.af = new AffineTransform();
        this.img = Images.PLAYER;

        this.health = Player.MAX_HEALTH;
        this.lives = 3;
        this.currentWeapon = 1;
        this.weapons = new ArrayList<Weapon>();
        this.weapons.add(Globals.ASSAULT_RIFLE); // Weapon 1
        this.weapons.add(Globals.SHOTGUN); // Weapon 2
        this.weapons.add(Globals.FLAMETHROWER); // Weapon 3
    }

    // Getter/Setter methods.
    public AffineTransform getTransform() { return this.af; }
    public BufferedImage getImage() { return this.img; }

    public int getHealth() { return this.health; }
    public void addHealth(int amount) { 
        if((this.health + amount) > Player.MAX_HEALTH) this.health = Player.MAX_HEALTH;
        else this.health += amount;
    }
    public boolean isAlive() { return this.health > 0; }
    public int getLives() { return this.lives; }
    public void die() { 
        this.lives--;
        if(this.lives > 0) reset();
    }
    public void reset() {
        this.health = Player.MAX_HEALTH;
        if(this.lives == 0) this.lives = 3;
        this.currentWeapon = 1;
        this.x = (Globals.W_WIDTH / 2) - (this.width / 2);
        this.y = (Globals.W_HEIGHT / 2) - (this.height / 2);
    }
    
    public Weapon getWeapon() { return this.weapons.get(this.currentWeapon - 1); }
    public Weapon getWeapon(int w) { return this.weapons.get(w - 1); }
    public int getCurrentWeapon() { return this.currentWeapon; }
    public List<Weapon> getAllWeapons() { return this.weapons; }
    public void setWeapon(int w) { 
        this.currentWeapon = w;
        Sounds.RTPS.reset();
        Sounds.BOOMSTICK.reset();
        Sounds.FLAMETHROWER.reset();
    }

    // Shape manipulation.
    public void rotate(double theta_) { this.af.setToRotation(theta_, getCenterX(), getCenterY()); }

    // Player manipulation.
    /**
     * Moves the player object on the screen.
     * @param direction The direction to move in.
     **/
    public void move(int direction) {
        if(direction == 0) y -= Player.MOVE_SPEED;
        else if(direction == 1) x -= Player.MOVE_SPEED;
        else if(direction == 2) y += Player.MOVE_SPEED;
        else if(direction == 3) x += Player.MOVE_SPEED;
    }

    public void takeDamage(int damage_) { this.health -= damage_; }
}
