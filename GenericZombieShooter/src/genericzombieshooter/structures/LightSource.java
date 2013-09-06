/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package genericzombieshooter.structures;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;

/**
 *
 * @author Darin Beaudreau
 */
public class LightSource extends Point2D.Double {
    // Member Variables
    private long timeExpiring;
    private long life;
    public long getLife() { return this.life; }
    private float radius;
    public float getIntensity() { return this.radius; }
    private float [] distance;
    private Color [] colors;
    
    /**
     * Creates a new light source at the center point with the specified intensity.
     * @param center The center point of the light source.
     * @param intensity The radius of the light source.
     **/
    public LightSource(Point2D.Double center, float intensity) {
        this(center, 0, intensity, new float[]{0.0f, 1.0f}, new Color[]{new Color(0.0f, 0.0f, 0.0f, 0.0f), Color.BLACK});
    }
    
    /**
     * Creates a new light source at the center point with the specified intensity
     * using the colors specified.
     * @param center The center point of the light source.
     * @param life Indicates how long the light source lasts. Zero if permanent.
     * @param intensity The radius of the light source.
     * @param distance The distance parameters of the radial gradient.
     * @param colors The colors of the radial gradient.
     **/
    public LightSource(Point2D.Double center, long life, float intensity, float [] distance, Color [] colors) {
        super(center.x, center.y);
        this.timeExpiring = System.currentTimeMillis() + life;
        this.life = life;
        this.radius = intensity;
        this.distance = distance;
        this.colors = colors;
    }
    
    public boolean isAlive() {
        return (this.life > 0) && (System.currentTimeMillis() <= this.timeExpiring);
    }
    
    /**
     * Moves the center of the light source to the specified center point.
     * @param center The point to move the center of the light source to.
     **/
    public void move(Point2D.Double center) {
        this.x = center.x;
        this.y = center.y;
    }
    
    public void draw(Graphics2D g2d) {
        RadialGradientPaint rgp = new RadialGradientPaint(this, this.radius, this.distance, this.colors);
        g2d.setPaint(rgp);
        g2d.fillRect((int)(this.x - this.radius), (int)(this.y - this.radius), 
                     (int)(this.radius * 2), (int)(this.radius * 2));
    }
}
