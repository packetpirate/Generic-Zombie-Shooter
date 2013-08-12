package genericzombieshooter.structures.weapons;

import genericzombieshooter.structures.Particle;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * Generic class to be extended for all weapons.
 * @author Darin Beaudreau
 */
public class Weapon {
    private String name;
    public String getName() { return this.name; }
    private int key;
    public int getKey() { return this.key; }
    private BufferedImage image;
    public BufferedImage getImage() { return this.image; }
    
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
    
    protected List<Particle> particles;
    public List<Particle> getParticles() { return this.particles; }
    
    public Weapon(String name) {
        this(name, KeyEvent.VK_1);
    }
    
    public Weapon(String name, int key) {
        this(name, key, "", 0, 100, 1, 0);
    }
    
    public Weapon(String name, int key, String filename, int ammoLeft, int maxAmmo, int ammoPerUse, int cooldown) {
        this.name = name;
        this.key = key;
        
        if(!filename.equals("") && filename != null) {
            try {
                this.image = ImageIO.read(getClass().getResource(filename));
            } catch(IOException io) { System.out.println("Error reading file: " + filename); }
        }
        
        this.ammoLeft = ammoLeft;
        this.maxAmmo = maxAmmo;
        this.ammoPerUse = ammoPerUse;
        
        this.cooldown = cooldown;
        this.coolPeriod = cooldown;
        
        this.particles = new ArrayList<Particle>();
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
