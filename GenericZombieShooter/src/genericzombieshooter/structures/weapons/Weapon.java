package genericzombieshooter.structures.weapons;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Generic class to be extended for all weapons.
 * @author Darin Beaudreau
 */
public class Weapon {
    private String name;
    public String getName() { return this.name; }
    private int key;
    public int getKey() { return this.key; }
    
    private int ammoLeft;
    private int maxAmmo;
    private int ammoPerUse;
    
    private int cooldown;
    private int coolPeriod;
    public void resetCooldown() { this.cooldown = this.coolPeriod; }
    public void cool() { if(this.cooldown > 0) this.cooldown--; }
    
    public boolean canFire() { return (this.ammoLeft >= this.ammoPerUse) && (this.cooldown == 0); }
    public void addAmmo(int amount) { 
        if((this.ammoLeft + amount) > this.maxAmmo) this.ammoLeft = this.maxAmmo;
        else this.ammoLeft += amount;
    }
    
    public Weapon(String name) {
        this(name, KeyEvent.VK_1);
    }
    
    public Weapon(String name, int key) {
        this(name, key, 0, 100, 1, 0);
    }
    
    public Weapon(String name, int key, int ammoLeft, int maxAmmo, int ammoPerUse, int cooldown) {
        this.name = name;
        this.key = key;
        
        this.ammoLeft = ammoLeft;
        this.maxAmmo = maxAmmo;
        this.ammoPerUse = ammoPerUse;
        
        this.cooldown = cooldown;
        this.coolPeriod = cooldown;
    }
    
    public void updateWeapon() {
        // To be overridden.
    }
    
    public void drawAmmo(Graphics2D g2d) {
        // To be overridden.
    }
    
    public void fire(double theta, Point2D.Double pos) {
        // To be overridden.
    }
    
    public int checkForDamage(Rectangle2D.Double rect) {
        // To be overridden.
        return 0;
    }
    
    public void consumeAmmo() {
        this.ammoLeft -= this.ammoPerUse;
    }
}
