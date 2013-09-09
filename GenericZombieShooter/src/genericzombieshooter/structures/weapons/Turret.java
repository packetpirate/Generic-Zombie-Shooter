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
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An automated turret that fires at nearby zombies.
 * @author Darin Beaudreau
 */
public class Turret extends Point2D.Double {
    // Final Variables
    private static final int DAMAGE = 100;
    private static final long FIRING_RATE = 150;
    
    // Member Variables
    private BufferedImage turretMount;
    private BufferedImage turretHead;
    
    private List<Particle> particles;
    
    private Zombie target; // Used to calculate position to fire at each update.
    
    private long lastFired;
    
    public Turret(Point2D.Double pos) {
        super(pos.x, pos.y);
        try {
            BufferedImage turretPieces = GZSFramework.loadImage("/resources/images/GZS_TurretPieces.png");
            this.turretMount = turretPieces.getSubimage(0, 0, 48, 48);
            this.turretHead = turretPieces.getSubimage(48, 0, 48, 48);
        } catch(RasterFormatException rfe) {
            System.out.println("Problem getting subimages of turret.");
        }
        this.particles = Collections.synchronizedList(new ArrayList<Particle>());
        this.target = null;
        this.lastFired = System.currentTimeMillis();
    }
    
    private boolean canFire() { return (System.currentTimeMillis() >= (this.lastFired + Turret.FIRING_RATE)); }
    
    public void update(List<Zombie> targets) {
        
    }
    
    public void draw(Graphics2D g2d) {
        
    }
    
    public void fire() {
        
    }
    
    public int checkForDamage(Rectangle2D.Double rect) {
        int damage = 0;
        
        return damage;
    }
}
