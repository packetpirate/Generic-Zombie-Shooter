package genericzombieshooter.interfaces;

/**
 *
 * @author Darin Beaudreau
 */
public interface Enemy {
    public void move(double x_, double y_);
    public void takeDamage(int damage_);
}
