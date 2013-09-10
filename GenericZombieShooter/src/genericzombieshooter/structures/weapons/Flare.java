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

import genericzombieshooter.actors.Player;
import genericzombieshooter.actors.Zombie;
import genericzombieshooter.misc.Images;
import genericzombieshooter.structures.Animation;
import genericzombieshooter.structures.LightSource;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Used to create a Flare, which acts as a light source once deployed.
 * @author Darin Beaudreau
 */
public class Flare extends Weapon {
    // Final Variables
    private static final int DEFAULT_AMMO = 1;
    private static final int MAX_AMMO = 3;
    private static final int AMMO_PER_USE = 1;
    private static final int PARTICLE_LIFE = 30 * 1000;
    
    // Member Variables
    private List<Animation> flares;
    private List<LightSource> lights;
    
    public Flare() {
        super("Shiny Stick", KeyEvent.VK_7, "/resources/images/GZS_Flare.png",
              Flare.DEFAULT_AMMO, Flare.MAX_AMMO, Flare.AMMO_PER_USE, 100, false);
        this.flares = Collections.synchronizedList(new ArrayList<Animation>());
        this.lights = Collections.synchronizedList(new ArrayList<LightSource>());
    }
    
    @Override
    public int getAmmoPackAmount() { return Flare.DEFAULT_AMMO; }
    
    @Override
    public void resetAmmo() {
        this.flares = Collections.synchronizedList(new ArrayList<Animation>());
        this.lights = Collections.synchronizedList(new ArrayList<LightSource>());
        this.ammoLeft = Flare.DEFAULT_AMMO;
    }
    
    @Override
    public List<LightSource> getLights() { return this.lights; }
    
    @Override
    public void updateWeapon(List<Zombie> zombies) {
        { // Update particles.
            synchronized(this.flares) {
                if(!this.flares.isEmpty()) {
                    Iterator<Animation> it = this.flares.iterator();
                    while(it.hasNext()) {
                        Animation a = it.next();
                        a.update();
                        if(!a.isActive()) {
                            it.remove();
                            continue;
                        }
                    }
                }
            }
        } // End particle updates.
        { // Update light sources.
            synchronized(this.lights) {
                if(!this.lights.isEmpty()) {
                    Iterator<LightSource> it = this.lights.iterator();
                    while(it.hasNext()) {
                        LightSource ls = it.next();
                        if(!ls.isAlive()) {
                            it.remove();
                            continue;
                        }
                    }
                }
            }
        } // End light source updates.
        this.cool();
    }
    
    @Override
    public void drawAmmo(Graphics2D g2d) {
        synchronized(this.flares) {
            if(!this.flares.isEmpty()) {
                Iterator<Animation> it = this.flares.iterator();
                while(it.hasNext()) {
                    Animation a = it.next();
                    if(a.isActive()) a.draw(g2d);
                }
            }
        }
    }
    
    @Override
    public void fire(double theta, Point2D.Double pos, Player player) {
        if(this.canFire()) {
            synchronized(this.flares) {
                Animation a = new Animation(Images.FLARE_PARTICLE, 32, 32, 3, (int)pos.x, (int)pos.y, 
                                            10, 0, Flare.PARTICLE_LIFE, true);
                this.flares.add(a);
            }
            synchronized(this.lights) {
                LightSource ls = new LightSource(new Point2D.Double(pos.x, pos.y), Flare.PARTICLE_LIFE, 150.0f,
                                                 new float[]{0.0f, 0.6f, 0.8f, 1.0f},
                                                 new Color[]{new Color(0.0f, 0.0f, 0.0f, 0.0f),
                                                             new Color(0.0f, 0.0f, 0.0f, 0.75f),
                                                             new Color(0.0f, 0.0f, 0.0f, 0.9f),
                                                             Color.BLACK});
                this.lights.add(ls);
            }
            this.consumeAmmo();
            this.resetCooldown();
            this.fired = true;
        }
    }
}