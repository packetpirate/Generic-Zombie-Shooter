package genericzombieshooter.structures.components;

import genericzombieshooter.misc.Globals;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

/**
 * Used to draw the weapons loadout GUI component to the screen.
 * @author Darin Beaudreau
 */
public class WeaponsLoadout {
    public static final double BAR_WIDTH = (10 * 48) + (11 * 4);
    public static final double BAR_HEIGHT = 56;
    
    private int currentWeapon;
    public void setCurrentWeapon(int w) { this.currentWeapon = w; }
    
    public WeaponsLoadout() {
        this.currentWeapon = 1;
    }
    
    public void draw(Graphics2D g2d) {
        Stroke oldStroke = g2d.getStroke();
        { // Draw the bar under the individual weapon slots.
            double x = (Globals.W_WIDTH / 2) - (WeaponsLoadout.BAR_WIDTH / 2);
            double y = Globals.W_HEIGHT - (WeaponsLoadout.BAR_HEIGHT + 15);
            Rectangle2D.Double rect = new Rectangle2D.Double(x, y, WeaponsLoadout.BAR_WIDTH, WeaponsLoadout.BAR_HEIGHT);
            g2d.setColor(Color.GRAY);
            g2d.fill(rect);
            g2d.setColor(Color.BLACK);
            g2d.draw(rect);
        } // Stop drawing the bar under the weapon slots.
        { // Draw the filler color for the weapon slots.
            for(int s = 0; s < 10; s++) {
                int slot = s * 48;
                int spacing = (s + 1) * 4;
                double size = 48;
                double x = ((Globals.W_WIDTH / 2) - (WeaponsLoadout.BAR_WIDTH / 2)) + (slot + spacing);
                double y = (Globals.W_HEIGHT - (WeaponsLoadout.BAR_HEIGHT + 15)) + 4;
                Rectangle2D.Double rect = new Rectangle2D.Double(x, y, size, size);
                g2d.setStroke(oldStroke);
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.fill(rect);
                g2d.setColor(Color.BLACK);
                g2d.draw(rect);
                // If the current iteration is the slot of the currently equipped weapon...
                if((s + 1) == this.currentWeapon) {
                    x += 3;
                    y += 3;
                    size = 42;
                    Rectangle2D.Double border = new Rectangle2D.Double(x, y, size, size);
                    g2d.setColor(Color.WHITE);
                    g2d.setStroke(new BasicStroke(3.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                                                  10.0f, new float[]{10.0f}, 0.0f));
                    g2d.draw(border);
                }
            }
        } // Stop drawing the filler color for the weapon slots.
        g2d.setStroke(oldStroke);
    }
}
