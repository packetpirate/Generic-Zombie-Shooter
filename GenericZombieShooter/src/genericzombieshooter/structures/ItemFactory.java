package genericzombieshooter.structures;

import genericzombieshooter.actors.Player;
import genericzombieshooter.misc.Globals;
import genericzombieshooter.structures.components.WeaponsLoadout;
import genericzombieshooter.structures.items.Ammo;
import genericzombieshooter.structures.items.HealthPack;
import genericzombieshooter.structures.weapons.Weapon;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Darin Beaudreau
 */
public class ItemFactory {
    // Member Variables
    private List<Item> itemsDropped;
    private List<Item> itemsActive;
    private List<Item> itemsWithdrawn;
    
    private long nextHealth; // Time that the next health pack will drop.
    private long nextAmmo; // Time that the next ammo crate will drop.
    
    public ItemFactory() {
        this.itemsDropped = new ArrayList<Item>();
        this.itemsActive = new ArrayList<Item>();
        this.itemsWithdrawn = new ArrayList<Item>();
        
        long currentTime = System.currentTimeMillis();
        this.nextHealth = currentTime + HealthPack.SPAWN_TIME;
        this.nextAmmo = currentTime + Ammo.SPAWN_TIME;
    }
    
    public void update(Player player) {
        // If there are withdrawn items, remove them from the active list.
        if(!this.itemsWithdrawn.isEmpty()) this.itemsActive.removeAll(this.itemsWithdrawn);
        
        // If there are dropped items, add them to the active list.
        if(!this.itemsDropped.isEmpty()) {
            this.itemsActive.addAll(this.itemsDropped);
            this.itemsDropped.clear();
        }
        
        // Get the current time and determine if an item should drop.
        long currentTime = System.currentTimeMillis();
        if(currentTime >= this.nextHealth) {
            // Drop Health Pack
            Item i = createItem(0, player);
            this.itemsDropped.add(i);
            this.nextHealth = currentTime + HealthPack.SPAWN_TIME;
        }
        if(currentTime >= this.nextAmmo) {
            // Drop Ammo Crate
            Item i = createItem(1, player);
            this.itemsDropped.add(i);
            this.nextAmmo = currentTime + Ammo.SPAWN_TIME;
        }
        
        /* If the player has picked up an item in the active list,
           remove it and add it to the withdrawn list. */
        {
            Iterator<Item> it = this.itemsActive.iterator();
            while(it.hasNext()) {
                Item i = it.next();
                if(player.contains(i)) {
                    i.applyEffect(player);
                    this.itemsWithdrawn.add(i);
                }
            }
        }
    }
    
    public void draw(Graphics2D g2d) {
        Iterator<Item> it = this.itemsActive.iterator();
        while(it.hasNext()) {
            Item i = it.next();
            i.draw(g2d);
        }
    }
    
    private Item createItem(int type, Player player) {
        if(type == 0) {
            // Return a Health Pack
            int healAmount = Globals.r.nextInt(75 - 50 + 1) + 50;
            double x = Globals.r.nextInt((Globals.W_WIDTH - 20) - 20 + 1) + 20;
            double y = Globals.r.nextInt((int)((Globals.W_HEIGHT - (WeaponsLoadout.BAR_HEIGHT + 10)) - 20 + 1)) + 20;
            Item i = new HealthPack(healAmount, new Point2D.Double(x, y));
            return i;
        } else if(type == 1) {
            // Return an Ammo Crate
            boolean nonFullWeaponDetected = false;
            Iterator<Weapon> it = player.getAllWeapons().iterator();
            while(it.hasNext()) {
                Weapon weapon = it.next();
                if(!weapon.ammoFull()) nonFullWeaponDetected = true;
            }
            if(nonFullWeaponDetected) {
                int numOfWeapons = player.getAllWeapons().size();
                int w = Globals.r.nextInt(numOfWeapons) + 1;
                if(player.getWeapon(w).ammoFull()) return createItem(1, player);
                else {
                    int ammo = player.getWeapon(w).getAmmoPackAmount();
                    double x = Globals.r.nextInt((Globals.W_WIDTH - 20) - 20 + 1) + 20;
                    double y = Globals.r.nextInt((int)((Globals.W_HEIGHT - (WeaponsLoadout.BAR_HEIGHT + 10)) - 20 + 1)) + 20;
                    Item i = new Ammo(w, ammo, new Point2D.Double(x, y));
                    return i;
                }
            }
        }
        return null;
    }
}
