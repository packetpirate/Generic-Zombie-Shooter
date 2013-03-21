package genericzombieshooter.structures;

import java.awt.Point;

/**
 *
 * @author Darin Beaudreau
 */
public class Vector2D extends Point.Double {
    public Vector2D() {
        super();
    }
    
    public Vector2D(double x_, double y_) {
        super(x_, y_);
    }
    
    public Vector2D(Vector2D v_) {
        super(v_.x, v_.y);
    }
    
    public double getLength() {
        return Math.sqrt((x*x) + (y*y));
    }
    
    public Vector2D normalize() {
        double len = getLength();
        if(len != 0) return new Vector2D((x / len), (y / len));
        else return new Vector2D(0.0, 0.0);
    }
}
