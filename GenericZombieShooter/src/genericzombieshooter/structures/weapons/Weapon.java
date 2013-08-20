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
package genericzombieshooter.structures.weapons;

import genericzombieshooter.GZSFramework;
import genericzombieshooter.actors.Zombie;
import genericzombieshooter.structures.Particle;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    
    protected int ammoLeft;
    public int getAmmoLeft() { return this.ammoLeft; }
    private int maxAmmo;
    public int getMaxAmmo() { return this.maxAmmo; }
    private int ammoPerUse;
    
    private int cooldown;
    private int coolPeriod;
    public void resetCooldown() { this.cooldown = this.coolPeriod; }
    public void cool() { if(this.cooldown > 0) this.cooldown--; }
    
    public boolean canFire() { return (this.ammoLeft >= this.ammoPerUse) && (this.cooldown == 0); }
    public boolean ammoFull() { return this.ammoLeft == this.maxAmmo; }
    public void addAmmo(int amount) { 
        if((this.ammoLeft + amount) > this.maxAmmo) this.ammoLeft = this.maxAmmo;
        else this.ammoLeft += amount;
    }
    public int getAmmoPackAmount() {
        // To be overridden...
        return 0;
    }
    public void resetAmmo() {
        this.particles = Collections.synchronizedList(new ArrayList<Particle>());
    }
    public void reload() {
        // Needs moar code...
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
        
        this.image = GZSFramework.loadImage(filename);
        
        this.ammoLeft = ammoLeft;
        this.maxAmmo = maxAmmo;
        this.ammoPerUse = ammoPerUse;
        
        this.cooldown = cooldown;
        this.coolPeriod = cooldown;
        
        this.particles = Collections.synchronizedList(new ArrayList<Particle>());
    }
    
    public void updateWeapon(List<Zombie> zombies) {
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
