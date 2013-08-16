/**
    This file is part of Generic Zombie Shooter.

    Generic Zombie Shooter is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Generic Zombie Shooter is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Generic Zombie Shooter.  If not, see <http://www.gnu.org/licenses/>.
 **/
package genericzombieshooter.structures;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author Darin Beaudreau
 */
public class Animation {
    // Member variables.
    protected BufferedImage img; // The animation sheet.
    protected int frameWidth; // The width of each frame in the animation.
    protected int frameHeight; // The height of each frame in the animation.
    protected int frameCount; // The number of frames in the animation.
    
    protected long frameTime; // Time between frames, in milliseconds.
    protected long startingFrameTime; // Time when the frame started showing.
    protected long nextFrameTime; // Time when to show the next frame.
    protected long delay; // How long to wait before starting and displaying animation.
    protected long timeCreated; // The time in milliseconds that the animation was created.
    
    protected int currentFrame; // Current frame in the animation.
    
    protected int x; // x coordinate of the animation on the screen.
    protected int y; // y coordinate of the animation on the screen.
    
    protected int startX; // Starting X coordinate of current frame in animation.
    protected int endX; // Ending X coordinate of current frame in animation.
    
    protected boolean loop; // Determines if the animation is looped.
    protected boolean active; // Determines if the animation is active.
    
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
    
    public boolean isActive() {
        return this.active;
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
