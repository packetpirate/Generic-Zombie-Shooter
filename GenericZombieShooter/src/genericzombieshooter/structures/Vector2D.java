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
