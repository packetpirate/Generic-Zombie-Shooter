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

import java.awt.Point;

/**
 * Represents the distance between two points in a 2D environment.
 * @author Darin Beaudreau
 **/
public class Vector2D extends Point.Double {
    public Vector2D(double x_, double y_) {
        super(x_, y_);
    }
    
    public double getLength() { return Math.sqrt((x*x) + (y*y)); }
    
    public Vector2D normalize() {
        double len = getLength();
        if(len != 0) return new Vector2D((x / len), (y / len));
        else return new Vector2D(0.0, 0.0);
    }
}
