package genericzombieshooter.structures;

import genericzombieshooter.actors.Player;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 * Base class used to represent all items in the game.
 * @author Darin Beaudreau
 */
public abstract class Item extends Point2D.Double {
    private String name;
    public String getName() { return this.name; }
    protected BufferedImage icon;
    public BufferedImage getIcon() { return this.icon; }
    
    public Item(String name, BufferedImage icon) {
        this.name = name;
        this.icon = icon;
    }
    
    public void applyEffect(Player player) {
        // To be overridden.
    }
    
    public void draw(Graphics2D g2d) {
        int width = this.icon.getWidth();
        int height = this.icon.getHeight();
        g2d.drawImage(this.icon, (int)(this.x - (width / 2)), (int)(this.y - (height / 2)), null);
    }
}
