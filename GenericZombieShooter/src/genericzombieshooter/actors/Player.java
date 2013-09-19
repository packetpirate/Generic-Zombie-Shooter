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
import genericzombieshooter.structures.Message;
import genericzombieshooter.structures.StatusEffect;
import genericzombieshooter.structures.items.ExpMultiplier;
import genericzombieshooter.structures.items.SpeedUp;
import genericzombieshooter.structures.items.UnlimitedAmmo;
import genericzombieshooter.structures.weapons.Weapon;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * Represents the player character.
 * @author Darin Beaudreau
 **/
public class Player extends Rectangle2D.Double {
    // Final variables.
    public static final int DEFAULT_HEALTH = 150;
    public static final long INVINCIBILITY_LENGTH = 3000;
    private static final double MOVE_SPEED = 2; // How many pixels per tick the player moves.
    
    public static final int MAX_HEALTH_ID = 1;
    private static final int MAX_HEALTH_INC = 50;
    public static final int DAMAGE_ID = 2;
    private static final double DAMAGE_INC = 0.2;
    public static final int SPEED_ID = 3;
    private static final double SPEED_INC = 0.2;
    
    // Member variables.
    private AffineTransform af;
    private BufferedImage img;
    private LightSource light;
    
    private int health;
    private int maxHealth;
    private double damageBonus;
    private double speed;
    private double speedBonus;
    private int cash;
    
    private int experience;
    private int experienceMultiplier;
    private int level;
    private int skillPoints;
    
    private int lives;
    
    private boolean blink;
    private long nextBlinkChange;
    
    private HashMap<String, StatusEffect> statusEffects;
    private long lastPoisoned;
    
    private String currentWeaponName;
    private HashMap<String, Weapon> weaponsMap;
    
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

        this.health = Player.DEFAULT_HEALTH;
        this.maxHealth = Player.DEFAULT_HEALTH;
        this.damageBonus = 0;
        this.speed = Player.MOVE_SPEED;
        this.speedBonus = 0;
        this.cash = 0;
        
        this.experience = 0;
        this.experienceMultiplier = 1;
        this.level = 1;
        this.skillPoints = 0;
        
        this.lives = 3;
        
        this.blink = false;
        this.nextBlinkChange = System.currentTimeMillis();
        
        this.statusEffects = new HashMap<String, StatusEffect>();
        
        this.lastPoisoned = System.currentTimeMillis();
        
        this.deathTime = System.currentTimeMillis();
        this.killCount = 0;
        this.medkitsUsed = 0;
        this.ammoCratesUsed = 0;
        
        this.weaponsMap = new HashMap<String, Weapon>();
        this.addWeapon(Globals.HANDGUN);
        this.currentWeaponName = Globals.HANDGUN.getName();
    }

    // Getter/Setter methods.
    public AffineTransform getTransform() { return this.af; }
    public BufferedImage getImage() { return this.img; }
    public LightSource getLightSource() { return this.light; }

    public int getHealth() { return this.health; }
    public int getMaxHealth() { return this.maxHealth; }
    public double getDamageBonus() { return this.damageBonus; }
    public int getCash() { return this.cash; }
    public void addCash(int amount) { this.cash += amount; }
    public void removeCash(int amount) { this.cash -= amount; }
    public int getExp() { return this.experience; }
    public void addExp(int amount) { this.experience += (amount * this.experienceMultiplier); }
    public int getNextLevelExp() {
        return ((this.level * 1000) + (((int)(this.level / 4)) * 2000));
    }
    public int getLevel() { return this.level; }
    public int getSkillPoints() { return this.skillPoints; }
    public void addKill() { this.killCount++; }
    public void takeDamage(int damage_) { this.health -= damage_; }
    public void addHealth(int amount) { 
        if((this.health + amount) > this.maxHealth) this.health = this.maxHealth;
        else this.health += amount;
    }
    public boolean isAlive() { return this.health > 0; }
    public long getDeathTime() { return this.deathTime; }
    public int getLives() { return this.lives; }
    public void addLife() { this.lives++; }
    public void die() { 
        this.lives--;
        if(this.lives > 0) reset();
    }
    public int spendPoint(int id, int currentLevel) {
        if(id == Player.MAX_HEALTH_ID) {
            // Determine if player has enough points to buy the next upgrade.;
            if(this.skillPoints >= (currentLevel + 1)) {
                // Determine if the player has already maxed out this skill.
                if(currentLevel < 5) {
                    // Deduct the appropriate number of skill points and raise the skill to the next level.
                    this.skillPoints -= (currentLevel + 1);
                    this.maxHealth += Player.MAX_HEALTH_INC;
                    this.addHealth(Player.MAX_HEALTH_INC);
                    synchronized(Globals.GAME_MESSAGES) { Globals.GAME_MESSAGES.add(new Message("Max Health increased!", 5000)); }
                    return 1;
                }
            }
        } else if(id == Player.DAMAGE_ID) {
            // Determine if player has enough points to buy the next upgrade.
            if(this.skillPoints >= (currentLevel + 1)) {
                // Determine if the player has already maxed out this skill.
                if(currentLevel < 5) {
                    // Deduct the appropriate number of skill points and raise the skill to the next level.
                    this.skillPoints -= (currentLevel + 1);
                    this.damageBonus += Player.DAMAGE_INC;
                    synchronized(Globals.GAME_MESSAGES) { Globals.GAME_MESSAGES.add(new Message("Damage increased!", 5000)); }
                    return 1;
                }
            }
        } else if(id == Player.SPEED_ID) {
            // Determine if player has enough points to buy the next upgrade.
            if(this.skillPoints >= (currentLevel + 1)) {
                // Determine if the player has already maxed out this skill.
                if(currentLevel < 5) {
                    // Deduct the appropriate number of skill points and raise the skill to the next level.
                    this.skillPoints -= (currentLevel + 1);
                    this.speedBonus += Player.SPEED_INC;
                    synchronized(Globals.GAME_MESSAGES) { Globals.GAME_MESSAGES.add(new Message("Speed increased!", 5000)); }
                    return 1;
                }
            }
        }
        return 0;
    }
    public void reset() {
        this.statusEffects.clear();
        
        if(this.lives == 0) {
            this.maxHealth = Player.DEFAULT_HEALTH;
            this.damageBonus = 0;
            this.speedBonus = 0;
            this.cash = 0;
            
            this.experience = 0;
            this.level = 1;
            this.skillPoints = 0;
            
            this.lives = 3;
            
            this.deathTime = System.currentTimeMillis();
            this.weaponsMap = new HashMap<String, Weapon>();
            this.weaponsMap.put(Globals.HANDGUN.getName(), Globals.HANDGUN);
        }
        else {
            this.addStatusEffect(7, "Invincibility", null, 3000, 0);
            this.blink = true;
            this.nextBlinkChange = System.currentTimeMillis() + 300;
        }
        if(this.hasEffect("Poison")) this.removeEffect("Poison");
        this.lastPoisoned = System.currentTimeMillis();
        
        this.health = this.maxHealth;
        this.speed = Player.MOVE_SPEED;
        this.experienceMultiplier = 1;
        this.currentWeaponName = Globals.HANDGUN.getName();
        this.x = (Globals.W_WIDTH / 2) - (this.width / 2);
        this.y = (Globals.W_HEIGHT / 2) - (this.height / 2);
        this.light.move(new Point2D.Double((int)this.getCenterX(), (int)this.getCenterY()));
    }
    public void resetStatistics() {
        this.killCount = 0;
        this.medkitsUsed = 0;
        this.ammoCratesUsed = 0;
    }
    
    public boolean hasWeapon(String name) { return this.weaponsMap.containsKey(name); }
    public Weapon getWeapon() { return this.weaponsMap.get(this.currentWeaponName); }
    public Weapon getWeapon(String name) { return this.weaponsMap.get(name); }
    public String getCurrentWeaponName() { return this.currentWeaponName; }
    public HashMap<String, Weapon> getWeaponsMap() { return this.weaponsMap; }
    public int setWeapon(String name) {
        if(this.weaponsMap.containsKey(name)) {
            this.currentWeaponName = name;
            Sounds.FLAMETHROWER.reset();
            return 1;
        } else return 0;
    }
    public void addWeapon(Weapon w) {
        this.weaponsMap.put(w.getName(), w);
    }
    
    public void update() {
        // Calculate the player's angle based on the mouse position.
        double cX = this.getCenterX();
        double cY = this.getCenterY();
        double pAngle = Math.atan2((cY - Globals.mousePos.y), (cX - Globals.mousePos.x)) - Math.PI / 2;
        this.rotate(pAngle);
        
        // Move the player according to which keys are being held down.
        for(int i = 0; i < Globals.keys.length; i++)  {
            if(Globals.keys[i]) this.move(i);
        }
        
        // If the left mouse button is held down, create a new projectile.
        if(Globals.buttons[0]) {
            Point target = new Point(Globals.mousePos);
            Point2D.Double pos = new Point2D.Double((this.x + 28), (this.y - 8));
            AffineTransform.getRotateInstance(pAngle, this.getCenterX(), this.getCenterY()).transform(pos, pos);
            double theta = Math.atan2((target.x - pos.x), (target.y - pos.y));
            this.getWeapon().fire(theta, pos, this);
        }
        
        { // Resolve status effects.
            // Power-Up Effects
            if(this.hasEffect(SpeedUp.EFFECT_NAME)) {
                StatusEffect status = this.statusEffects.get(SpeedUp.EFFECT_NAME);
                if(status.isActive()) {
                    // Change the player's move speed.
                    this.speed = Player.MOVE_SPEED * status.getValue();
                } else {
                    // Reset the player's movement speed and remove the status.
                    this.speed = Player.MOVE_SPEED;
                    this.removeEffect(SpeedUp.EFFECT_NAME);
                }
            }
            if(this.hasEffect(UnlimitedAmmo.EFFECT_NAME)) {
                // If the player has the unlimited ammo effect, but it is no longer active, remove it.
                StatusEffect status = this.statusEffects.get(UnlimitedAmmo.EFFECT_NAME);
                if(!status.isActive()) this.removeEffect(UnlimitedAmmo.EFFECT_NAME);
            }
            if(this.hasEffect(ExpMultiplier.EFFECT_NAME)) {
                // If the player no longer has the experience multiplier effect, remove it and reset the multiplier.
                StatusEffect status = this.statusEffects.get(ExpMultiplier.EFFECT_NAME);
                if(status.isActive()) {
                    // Change the player's experience multiplier.
                    this.experienceMultiplier = 2;
                } else {
                    this.experienceMultiplier = 1;
                    this.removeEffect(ExpMultiplier.EFFECT_NAME);
                }
            }
            
            // Negative Effects
            if(this.hasEffect("Poison")) {
                StatusEffect status = this.statusEffects.get("Poison");
                if(status.isActive()) {
                    // If the player is poisoned, take damage.
                    if(System.currentTimeMillis() >= (this.lastPoisoned + 1000)) {
                        this.lastPoisoned = System.currentTimeMillis();
                        this.takeDamage((int)status.getValue());
                    }
                } else this.removeEffect("Poison");
            }
            
            // Other Effects
            if(this.hasEffect("Invincibility")) {
                StatusEffect status = this.statusEffects.get("Invincibility");
                if(status.isActive()) {
                    if(System.currentTimeMillis() >= this.nextBlinkChange) {
                        this.blink = ((this.blink)?false:true);
                        this.nextBlinkChange = System.currentTimeMillis() + 300;
                    }
                } else this.removeEffect("Invincibility");
            }
            
            // Check player to see if he has leveled up.
            if(this.experience >= this.getNextLevelExp()) {
                this.experience = (this.experience % this.getNextLevelExp());
                this.level++;
                this.skillPoints++;
                synchronized(Globals.GAME_MESSAGES) { 
                    Globals.GAME_MESSAGES.add(new Message(("Player reached level " + this.level + "!"), 5000)); }
            }
        } // End resolving status effects.
    }
    
    public void draw(Graphics2D g2d) {
        if(!this.blink) {
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
        }
    }

    // Shape manipulation.
    public void rotate(double theta_) { this.af.setToRotation(theta_, getCenterX(), getCenterY()); }

    // Player manipulation.
    public void move(int direction) {
        if(direction == 0) y -= (this.speed + this.speedBonus);
        else if(direction == 1) x -= (this.speed + this.speedBonus);
        else if(direction == 2) y += (this.speed + this.speedBonus);
        else if(direction == 3) x += (this.speed + this.speedBonus);
        this.light.move(new Point2D.Double((int)this.getCenterX(), (int)this.getCenterY()));
    }
    
    public void move(Point2D.Double p) {
        this.x = p.x;
        this.y = p.y;
        this.light.move(p);
    }
    
    public HashMap<String, StatusEffect> getStatusEffects() { return this.statusEffects; }
    
    public void addStatusEffect(int id, String name, BufferedImage img, long duration, int value) {
        if(!this.statusEffects.containsKey(name)) this.statusEffects.put(name, new StatusEffect(img, duration, value));
        else this.statusEffects.get(name).refresh(duration);
    }
    
    public void removeEffect(String name) {
        if(this.hasEffect(name)) this.statusEffects.remove(name);
    }
    
    public boolean hasEffect(String name) {
        return this.statusEffects.containsKey(name);
    }
}
