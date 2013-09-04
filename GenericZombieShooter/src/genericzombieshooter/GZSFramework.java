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
package genericzombieshooter;

import genericzombieshooter.actors.Player;
import genericzombieshooter.misc.Globals;
import genericzombieshooter.misc.Sounds;
import genericzombieshooter.structures.ItemFactory;
import genericzombieshooter.structures.ZombieWave;
import genericzombieshooter.structures.components.ErrorWindow;
import genericzombieshooter.structures.components.WeaponsLoadout;
import genericzombieshooter.structures.weapons.Weapon;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * Contains many of the methods used to update game objects and handles the game
 * thread.
 *
 * @author packetpirate
 */
public class GZSFramework {
    // Member variables.
    public JFrame frame;
    public GZSCanvas canvas;
    
    // Game objects.
    private Player player; // The player character.
    public Player getPlayer() { return player; }
    private int currentWave;
    private ZombieWave wave;
    public ZombieWave getWave() { return this.wave; }
    
    private WeaponsLoadout loadout;
    public WeaponsLoadout getLoadout() { return this.loadout; }
    
    private ItemFactory itemFactory;
    public ItemFactory getItemFactory() { return this.itemFactory; }

    public GZSFramework(JFrame frame_) {
        frame = frame_;
        canvas = new GZSCanvas(this);
        Globals.started = false;
        Globals.paused = false;
        Globals.crashed = false;
        Globals.waveInProgress = false;
        Globals.nextWave = System.currentTimeMillis() + 3000;

        { // Begin initializing member variables.
            Globals.keys = new boolean[4];
            for (boolean k : Globals.keys) k = false;
            Globals.buttons = new boolean[2];
            for (boolean b : Globals.buttons) b = false;

            Globals.mousePos = new Point(0, 0);
        } // End member variable initialization.

        { // Begin initializing game objects.
            player = new Player(((Globals.W_WIDTH / 2) - 20), ((Globals.W_HEIGHT / 2) - 20), 40, 40);
            currentWave = 0;
            wave = new ZombieWave(currentWave);
            loadout = new WeaponsLoadout(player);
            itemFactory = new ItemFactory();
        } // End game object initialization.

        { // Begin adding key and mouse listeners to canvas.
            canvas.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent k) {
                    if(Globals.started) {
                        int key = k.getKeyCode();
                        if (key == KeyEvent.VK_W) Globals.keys[0] = true;
                        if (key == KeyEvent.VK_A) Globals.keys[1] = true;
                        if (key == KeyEvent.VK_S) Globals.keys[2] = true;
                        if (key == KeyEvent.VK_D) Globals.keys[3] = true;
                    }
                }

                @Override
                public void keyReleased(KeyEvent k) {
                    if(Globals.started) {
                        int key = k.getKeyCode();
                        if (key == KeyEvent.VK_W) Globals.keys[0] = false;
                        if (key == KeyEvent.VK_A) Globals.keys[1] = false;
                        if (key == KeyEvent.VK_S) Globals.keys[2] = false;
                        if (key == KeyEvent.VK_D) Globals.keys[3] = false;
                        if (key == KeyEvent.VK_P) {
                            if(Globals.paused) {
                                Globals.paused = false;
                                Sounds.UNPAUSE.play();
                            }
                            else {
                                Globals.paused = true;
                                Sounds.PAUSE.play();
                            }
                        }
                        if (key == Globals.HANDGUN.getKey()) {
                            player.setWeapon(1);
                            loadout.setCurrentWeapon(1);
                        }
                        if (key == Globals.ASSAULT_RIFLE.getKey()) {
                            player.setWeapon(2);
                            loadout.setCurrentWeapon(2);
                        }
                        if (key == Globals.SHOTGUN.getKey()) {
                            player.setWeapon(3);
                            loadout.setCurrentWeapon(3);
                        }
                        if (key == Globals.FLAMETHROWER.getKey()) {
                            player.setWeapon(4);
                            loadout.setCurrentWeapon(4);
                        }
                        if (key == Globals.GRENADE.getKey()) {
                            player.setWeapon(5);
                            loadout.setCurrentWeapon(5);
                        }
                        if (key == Globals.LANDMINE.getKey()) {
                            player.setWeapon(6);
                            loadout.setCurrentWeapon(6);
                        }
                    }
                }
            });

            canvas.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent m) {
                    if((m.getButton() == MouseEvent.BUTTON1) && (!Globals.started)) {
                        Globals.started = true;
                    }
                }
                @Override
                public void mousePressed(MouseEvent m) {
                    if(Globals.started) {
                        if (m.getButton() == MouseEvent.BUTTON1) Globals.buttons[0] = true;
                        if (m.getButton() == MouseEvent.BUTTON3) Globals.buttons[1] = true;
                    }
                }

                @Override
                public void mouseReleased(MouseEvent m) {
                    if(Globals.started) {
                        if (m.getButton() == MouseEvent.BUTTON1) {
                            Globals.buttons[0] = false;
                            if(player.getCurrentWeapon() == 3) Sounds.FLAMETHROWER.reset();
                        }
                        if (m.getButton() == MouseEvent.BUTTON3) Globals.buttons[1] = false;
                    }
                }
            });

            canvas.addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseMoved(MouseEvent m) {
                    if(Globals.started) {
                        Globals.mousePos.x = m.getX();
                        Globals.mousePos.y = m.getY();
                    }
                }

                @Override
                public void mouseDragged(MouseEvent m) {
                    if(Globals.started) {
                        Globals.mousePos.x = m.getX();
                        Globals.mousePos.y = m.getY();
                    }
                }
            });
            
            canvas.addMouseWheelListener(new MouseAdapter() {
                @Override
                public void mouseWheelMoved(MouseWheelEvent mw) {
                    int notches = mw.getWheelRotation();
                    if(notches < 0) { // Wheel scrolled up.
                        // Move weapon selection to the left.
                        int w = player.getCurrentWeapon();
                        if(w == 1) w = player.getAllWeapons().size();
                        else w--;
                        player.setWeapon(w);
                        loadout.setCurrentWeapon(w);
                    } else { // Wheel scrolled down.
                        // Move weapon selection to the right.
                        int w = player.getCurrentWeapon();
                        if(w == player.getAllWeapons().size()) w = 1;
                        else w++;
                        player.setWeapon(w);
                        loadout.setCurrentWeapon(w);
                    }
                }
            });
        } // End adding key and mouse listeners to canvas.

        Sounds.init();
        initializeThread();
        startThread();
    }

    /**
     * Updates the game objects in the animation loop.
     **/
    public void update() {
        if(Globals.started && !Globals.paused && !Globals.crashed) {
            try {
                // Calculate the player's angle based on the mouse position.
                double cX = player.getCenterX();
                double cY = player.getCenterY();
                double pAngle = Math.atan2((cY - Globals.mousePos.y), (cX - Globals.mousePos.x)) - Math.PI / 2;
                player.rotate(pAngle);

                // Move the player according to which keys are being held down.
                for(int i = 0; i < Globals.keys.length; i++)  {
                    if(Globals.keys[i]) player.move(i);
                }

                // If the left mouse button is held down, create a new projectile.
                if(Globals.buttons[0]) {
                    Point target = new Point(Globals.mousePos);
                    Point2D.Double pos = new Point2D.Double((player.x + 28), (player.y - 8));
                    AffineTransform.getRotateInstance(pAngle, player.getCenterX(), player.getCenterY()).transform(pos, pos);
                    double theta = Math.atan2((target.x - pos.x), (target.y - pos.y));
                    player.getWeapon().fire(theta, pos);
                }
                
                if(!Globals.waveInProgress) {
                    // If the player is in between waves, check if the countdown has reached zero.
                    if(System.currentTimeMillis() >= Globals.nextWave) createWave();
                }
                
                // Update all zombies in the current wave.
                if(Globals.waveInProgress) this.wave.update(player);

                // Check player for damage.
                if(Globals.waveInProgress) this.wave.checkPlayerDamage(player);
                
                { // If the player's invincibility timer has run out, remove it.
                    if(player.isInvincible()) {
                        long startTime = player.getInvincibilityStartTime();
                        long currentTime = System.currentTimeMillis();
                        if(currentTime >= (startTime + Player.INVINCIBILITY_LENGTH)) player.removeInvincibility(); 
                    }
                } // End removing player invincibility.

                // Update Items
                itemFactory.update(player);

                // Check to see if the player is still alive. If not, take away a life and reset.
                if(!player.isAlive()) {
                    player.die();
                    if(player.getLives() == 0) {
                        // Reset the game.
                        Globals.started = false;
                        player.reset();
                        currentWave = 1;
                        wave = new ZombieWave(currentWave);
                        for(boolean k : Globals.keys) k = false;
                        for(boolean b : Globals.buttons) b = false;
                        Iterator<Weapon> it = player.getAllWeapons().iterator();
                        while(it.hasNext()) {
                            Weapon w = it.next();
                            w.resetAmmo();
                        }
                    }
                    loadout.setCurrentWeapon(1);
                }

                { // Begin weapon updates.
                    Iterator<Weapon> it = this.player.getAllWeapons().iterator();
                    while(it.hasNext()) {
                        Weapon w = it.next();
                        w.updateWeapon(this.wave.getZombies());
                    }
                } // End weapon updates.
                
                // Check for end of wave.
                if(Globals.waveInProgress && this.wave.waveFinished()) {
                    Globals.waveInProgress = false;
                    Globals.nextWave = System.currentTimeMillis() + (10 * 1000);
                }
            } catch(Exception e) {
                createErrorWindow(e);
            }
        }
    }
    
    private void createWave() {
        try {
            this.currentWave++;
            this.wave = new ZombieWave(this.currentWave);
            Globals.waveInProgress = true;
        } catch(Exception e) {
            createErrorWindow(e);
        }
    }
    
    public static BufferedImage loadImage(String filename) {
        try {
            return ImageIO.read(GZSFramework.class.getResource(filename));
        } catch(IOException io) {
            System.out.println(io.getMessage());
            System.out.println("Error reading file: " + filename);
            return null;
        }
    }
    
    public void createErrorWindow(Exception e) {
        ErrorWindow error = new ErrorWindow(e);
        Globals.crashed = true;
        frame.getContentPane().removeAll();
        frame.add(error);
        frame.pack();
        frame.setVisible(true);
    }
    
    private void initializeThread() {
        Globals.animation = new Runnable() {
            @Override
            public void run() {
                Globals.running = true;
                while (Globals.running) {
                    update();
                    canvas.repaint();

                    try {
                        Thread.sleep(Globals.SLEEP_TIME);
                    } catch (InterruptedException ie) {
                        System.out.println("Error occurred in main thread...");
                        createErrorWindow(ie);
                    }
                }
                System.exit(0);
            }
        };
    }
    
    private void startThread() {
        new Thread(Globals.animation).start();
    }
}
