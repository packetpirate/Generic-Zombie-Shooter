package genericzombieshooter.misc;

import genericzombieshooter.GZSFramework;
import java.awt.image.BufferedImage;

/**
 * Contains constants of images so they don't need to be loaded multiple times.
 * @author Darin Beaudreau
 */
public class Images {
    public static final BufferedImage START_SCREEN = GZSFramework.loadImage("/resources/images/GZS_Splash.png");
    public static final BufferedImage PLAYER = GZSFramework.loadImage("/resources/images/GZS_Player.png");
    public static final BufferedImage FIRE_PARTICLE = GZSFramework.loadImage("/resources/images/GZS_FireParticle.png");
}
