package genericzombieshooter.structures.items;

import genericzombieshooter.actors.Player;
import genericzombieshooter.misc.Images;
import genericzombieshooter.misc.Sounds;
import genericzombieshooter.structures.Item;
import java.awt.geom.Point2D;

/**
 * Used to represent health packs that spawn on the ground.
 * @author Darin Beaudreau
 */
public class HealthPack extends Item {
    public static final long SPAWN_TIME = 20 * 1000;
    private int healAmount;
    
    public HealthPack(int healAmount, Point2D.Double p) {
        super("Health Pack", Images.HEALTH_PACK);
        this.healAmount = healAmount;
        this.x = p.x;
        this.y = p.y;
    }
    
    @Override
    public void applyEffect(Player player) {
        player.addHealth(this.healAmount);
        Sounds.POWERUP.play();
    }
}
