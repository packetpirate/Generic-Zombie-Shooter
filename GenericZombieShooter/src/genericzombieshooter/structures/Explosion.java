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
package genericzombieshooter.structures;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 * Used to create explosions from weapons or zombies.
 * @author Darin Beaudreau
 */
public class Explosion extends Point2D.Double {
    private Animation img;
    public Animation getImage() { return this.img; }
    private Dimension size;
    public Dimension getSize() { return this.size; }
    
    public Explosion(BufferedImage bi, Point2D.Double p) {
        super(p.x, p.y);
        this.size = new Dimension(128, 128);
        this.img = new Animation(bi, this.size.width, this.size.height, 8, (int)p.x, (int)p.y, 50, 0, false) {
            @Override
            public void draw(Graphics2D g2d) {
                if((this.timeCreated + this.delay) <= System.currentTimeMillis()) {
                    g2d.drawImage(this.img, (this.x - (this.frameWidth / 2)), (this.y - (this.frameHeight / 2)),
                                 (this.x + this.frameWidth), (this.y + this.frameHeight),
                                  this.startX, 0, this.endX, this.frameHeight, null);
                }
            }
        };
    }
    
    public void draw(Graphics2D g2d) {
        this.img.draw(g2d);
    }
}
