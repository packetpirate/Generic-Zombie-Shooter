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
import genericzombieshooter.structures.LightSource;
import genericzombieshooter.structures.weapons.Weapon;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
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
    public static final long INVINCIBILITY_LENGTH = 3000;
    private static final double MOVE_SPEED = 2; // How many pixels per tick the player moves.
    
    // Member variables.
    private AffineTransform af;
    private BufferedImage img;
    private LightSource light;
    private int health;
    private int cash;
    private int lives;
    private boolean invincible;
    private long invincibleStartTime;
    private int currentWeapon;
    private List<Weapon> weapons;
    
    // Statistic Variables
    private long deathTime;
    public int killCount;
    public int medkitsUsed;
    public int ammoCratesUsed;

    public Player(double x_, double y_, double w_, double h_) {
        super(x_, y_, w_, h_);
        this.af = new AffineTransform();
        this.img = Images.PLAYER;
        
        {
            int xLoc = (int)this.getCenterX();
            int yLoc = (int)this.getCenterY();
            float intensity = 200.0f;
            float [] distance = {0.0f, 0.6f, 0.8f, 1.0f};
            Color [] colors = {new Color(0.0f, 0.0f, 0.0f, 0.0f),
                                       new Color(0.0f, 0.0f, 0.0f, 0.75f),
                                       new Color(0.0f, 0.0f, 0.0f, 0.9f),
                                       Color.BLACK};
            this.light = new LightSource(new Point2D.Double(xLoc, yLoc), 0, intensity, distance, colors);
        }

        this.health = Player.MAX_HEALTH;
        this.cash = 0;
        this.lives = 3;
        this.invincible = false;
        this.invincibleStartTime = System.currentTimeMillis();
        this.currentWeapon = 1;
        
        this.deathTime = System.currentTimeMillis();
        this.killCount = 0;
        this.medkitsUsed = 0;
        this.ammoCratesUsed = 0;
        
        this.weapons = new ArrayList<Weapon>();
        this.weapons.add(Globals.HANDGUN); // Weapon 1
        this.weapons.add(Globals.ASSAULT_RIFLE); // Weapon 2
        this.weapons.add(Globals.SHOTGUN); // Weapon 3
        this.weapons.add(Globals.FLAMETHROWER); // Weapon 4
        this.weapons.add(Globals.GRENADE); // Weapon 5
        this.weapons.add(Globals.LANDMINE); // Weapon 6
        this.weapons.add(Globals.FLARE); // Weapon 7
        this.weapons.add(Globals.LASERWIRE); // Weapon 8
        this.weapons.add(Globals.TURRETWEAPON); // Weapon 9
    }

    // Getter/Setter methods.
    public AffineTransform getTransform() { return this.af; }
    public BufferedImage getImage() { return this.img; }
    public LightSource getLightSource() { return this.light; }

    public int getHealth() { return this.health; }
    public int getCash() { return this.cash; }
    public void addCash(int amount) { this.cash += amount; }
    public void addKill() { this.killCount++; }
    public void takeDamage(int damage_) { this.health -= damage_; }
    public void addHealth(int amount) { 
        if((this.health + amount) > Player.MAX_HEALTH) this.health = Player.MAX_HEALTH;
        else this.health += amount;
    }
    public boolean isAlive() { return this.health > 0; }
    public boolean isInvincible() { return this.invincible; }
    public long getInvincibilityStartTime() { return this.invincibleStartTime; }
    public long getDeathTime() { return this.deathTime; }
    public void removeInvincibility() { this.invincible = false; }
    public int getLives() { return this.lives; }
    public void die() { 
        this.lives--;
        if(this.lives > 0) reset();
    }
    public void reset() {
        this.health = Player.MAX_HEALTH;
        if(this.lives == 0) {
            this.cash = 0;
            this.lives = 3;
            this.deathTime = System.currentTimeMillis();
        }
        else {
            this.invincible = true;
            this.invincibleStartTime = System.currentTimeMillis();
        }
        if(this.isPoisoned()) this.removePoison();
        this.currentWeapon = 1;
        this.x = (Globals.W_WIDTH / 2) - (this.width / 2);
        this.y = (Globals.W_HEIGHT / 2) - (this.height / 2);
        this.light.move(new Point2D.Double((int)this.getCenterX(), (int)this.getCenterY()));
    }
    public void resetStatistics() {
        this.killCount = 0;
        this.medkitsUsed = 0;
        this.ammoCratesUsed = 0;
    }
    
    public Weapon getWeapon() { return this.weapons.get(this.currentWeapon - 1); }
    public Weapon getWeapon(int w) { return this.weapons.get(w - 1); }
    public int getCurrentWeapon() { return this.currentWeapon; }
    public List<Weapon> getAllWeapons() { return this.weapons; }
    public void setWeapon(int w) { 
        this.currentWeapon = w;
        Sounds.FLAMETHROWER.reset();
    }
    
    public void draw(Graphics2D g2d) {
        g2d.setTransform(this.af);
        { // Draw player shadow.
            int w = this.img.getWidth();
            int h = this.img.getHeight();
            int xPos = (int)(this.getCenterX() - (w / 2));
            int yPos = (int)(this.getCenterY() - (h / 2) + 5);
            g2d.setColor(new Color(0, 0, 0, 100));
            g2d.fillOval(xPos, yPos, w, h);
        } // End drawing player shadow.
        g2d.drawImage(this.img, (int) this.x, (int) this.y, null);
        if(this.isInvincible()) {
            g2d.setColor(Color.WHITE);
            Ellipse2D.Double halo = new Ellipse2D.Double((this.x - 10), (this.y - 10), 
                                                         (this.width + 20), (this.height + 20));
            g2d.draw(halo);
        }
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
        this.light.move(new Point2D.Double((int)this.getCenterX(), (int)this.getCenterY()));
    }
    
    private boolean poisoned;
    public boolean isPoisoned() { return this.poisoned; }
    private long lastPoisoned;
    private long endTime;
    public long getPoisonEndTime() { return this.endTime; }
    private int poisonDamage;
    public void poison(long length, int dps) {
        /* If the player is not already poisoned, set the current time as
           the last time the player was poisoned, set the boolean poisoned
           flag to true, and set the end time of the poison effect to the
           current time plus the length specified. Also, set the poison damage. */
        long startTime = System.currentTimeMillis();
        if(!this.poisoned) {
            this.poisoned = true;
            this.endTime = startTime + length;
            this.poisonDamage = dps;
        } else {
            /* If the player is already poisoned, simply refresh the duration to
               the current time plus the length and set the damage in case
               it has changed. */
            this.endTime = startTime + length;
            this.poisonDamage = dps;
        }
        this.lastPoisoned = startTime;
    }
    public void takePoisonDamage() {
        /* If it has been at least one second since the last time the player
           took poison damage, deal poison damage again. If the duration has
           run its course, set the poisoned boolean flag to false. */
        long currentTime = System.currentTimeMillis();
        if(currentTime >= (this.lastPoisoned + 1000)) {
            this.takeDamage(this.poisonDamage);
            this.lastPoisoned = currentTime;
        }
        if(currentTime >= this.endTime) this.poisoned = false;
    }
    public void removePoison() { this.poisoned = false; }
}
