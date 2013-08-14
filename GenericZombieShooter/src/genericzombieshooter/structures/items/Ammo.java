package genericzombieshooter.structures.items;

import genericzombieshooter.actors.Player;
import genericzombieshooter.misc.Images;
import genericzombieshooter.misc.Sounds;
import genericzombieshooter.structures.Item;
import java.awt.geom.Point2D;

/**
 * Used to restore ammo to the player's weapons.
 * @author Darin Beaudreau
 */
public class Ammo extends Item {
    public static final long SPAWN_TIME = 15 * 1000;
    private int weapon;
    private int ammoCount;
    
    public Ammo(int weapon, int ammoCount, Point2D.Double p) {
        super("Ammo Pack", Images.AMMO_PACK);
        this.weapon = weapon;
        this.ammoCount = ammoCount;
        this.x = p.x;
        this.y = p.y;
    }
    
    @Override
    public void applyEffect(Player player) {
        player.getWeapon(this.weapon).addAmmo(this.ammoCount);
        Sounds.POWERUP.play();
    }
}
