package genericzombieshooter.structures;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author Darin Beaudreau
 */
public class Animation {
    // Member variables.
    private BufferedImage img; // The animation sheet.
    private int frameWidth; // The width of each frame in the animation.
    private int frameHeight; // The height of each frame in the animation.
    private int frameCount; // The number of frames in the animation.
    
    private long frameTime; // Time between frames, in milliseconds.
    private long startingFrameTime; // Time when the frame started showing.
    private long nextFrameTime; // Time when to show the next frame.
    private long delay; // How long to wait before starting and displaying animation.
    private long timeCreated; // The time in milliseconds that the animation was created.
    
    private int currentFrame; // Current frame in the animation.
    
    private int x; // x coordinate of the animation on the screen.
    private int y; // y coordinate of the animation on the screen.
    
    private int startX; // Starting X coordinate of current frame in animation.
    private int endX; // Ending X coordinate of current frame in animation.
    
    private boolean loop; // Determines if the animation is looped.
    private boolean active; // Determines if the animation is active.
    
    /*
     * @param img The source image for the animation.
     * @param frameWidth The width of each frame in the animation.
     * @param frameHeight The height of each frame in the animation.
     * @param frameCount The total number of frames in the animation.
     * @param x x coordinate of the animation on the screen.
     * @param y y coordinate of the animation on the screen.
     * @param frameTime Amount of time in milliseconds to show each frame before moving to the next.
     * @param delay The delay in milliseconds before the animation starts and animates.
     * @param loop Determines if the animation will loop.
     */
    public Animation(BufferedImage img, int frameWidth, int frameHeight, int frameCount, int x, int y, long frameTime, long delay, boolean loop) {
        this.img = img;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.frameCount = frameCount;
        
        this.x = x;
        this.y = y;
        
        startX = 0;
        endX = frameWidth;
        
        this.frameTime = frameTime;
        this.delay = delay;
        
        startingFrameTime = System.currentTimeMillis() + delay;
        nextFrameTime = startingFrameTime + this.frameTime;
        
        currentFrame = 0;
        
        this.loop = loop;
        
        timeCreated = System.currentTimeMillis();
        
        active = true;
    }
    
    /* Changes the location of the animation.
     * @param x x coordinate of the animation on the screen.
     * @param y y coordinate of the animation on the screen.
     */
    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /* Checks to see if it's time for the next frame in the animation.
     * Also checks if the animation is finished.
     */
    public void update() {
        if(nextFrameTime <= System.currentTimeMillis()) {
            currentFrame++;
            if(currentFrame >= frameCount) { // If the animation has ended, reset the current frame to 0.
                currentFrame = 0;
                if(!loop) active = false; // If the animation is not looped and is over, it is no longer active.
            }
            
            // Sets the X coordinates for cropping the current frame out of the source image.
            startX = frameWidth * currentFrame;
            endX = frameWidth + startX;
            
            // Set the time for the next frame.
            startingFrameTime = System.currentTimeMillis();
            nextFrameTime = startingFrameTime + frameTime;
        }
    }
    
    /* Draws the current frame of the animation.
     * @param g2d Graphics2D
     */
    public void draw(Graphics2D g2d) {
        if((this.timeCreated + this.delay) <= System.currentTimeMillis()) {
            g2d.drawImage(img, x, y, (x + frameWidth), (y + frameHeight), startX, 0, endX, frameHeight, null);
        }
    }
}
