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
import genericzombieshooter.misc.Images;
import genericzombieshooter.misc.Sounds;
import genericzombieshooter.structures.GameTime;
import genericzombieshooter.structures.ItemFactory;
import genericzombieshooter.structures.Message;
import genericzombieshooter.structures.ZombieWave;
import genericzombieshooter.structures.components.ErrorWindow;
import genericzombieshooter.structures.components.LevelScreen;
import genericzombieshooter.structures.components.StoreWindow;
import genericzombieshooter.structures.components.WeaponsLoadout;
import genericzombieshooter.structures.items.NightVision;
import genericzombieshooter.structures.weapons.Weapon;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import kuusisto.tinysound.TinySound;

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
    private StoreWindow store;
    private LevelScreen levelScreen;
    
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
        store = new StoreWindow();
        levelScreen = new LevelScreen();
        canvas = new GZSCanvas(this, store, levelScreen);
        
        Globals.gameTime = new GameTime();
        Globals.started = false;
        Globals.paused = false;
        Globals.storeOpen = false;
        Globals.levelScreenOpen = false;
        Globals.crashed = false;
        Globals.deathScreen = false;
        Globals.waveInProgress = false;
        Globals.nextWave = Globals.gameTime.getElapsedMillis() + 3000;

        { // Begin initializing member variables.
            Globals.keys = new boolean[4];
            for (boolean k : Globals.keys) k = false;
            Globals.buttons = new boolean[2];
            for (boolean b : Globals.buttons) b = false;

            Globals.mousePos = new Point(0, 0);
        } // End member variable initialization.

        { // Begin initializing game objects.
            player = new Player(((Globals.W_WIDTH / 2) - 24), ((Globals.W_HEIGHT / 2) - 24), 48, 48);
            currentWave = 1;
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
                            if(!Globals.deathScreen && !Globals.crashed && 
                               !Globals.storeOpen && !Globals.levelScreenOpen) {
                                if(Globals.paused) {
                                    Globals.paused = false;
                                    Sounds.UNPAUSE.play();
                                } else {
                                    Globals.paused = true;
                                    Sounds.PAUSE.play();
                                }
                            }
                        }
                        if (key == KeyEvent.VK_B) {
                            if(!Globals.deathScreen && !Globals.crashed && !Globals.levelScreenOpen) {
                                if(Globals.storeOpen) {
                                    Globals.storeOpen = false;
                                    Globals.paused = false;
                                } else {
                                    Globals.storeOpen = true;
                                    Globals.paused = true;
                                }
                            }
                        }
                        if (key == KeyEvent.VK_T) {
                            if(!Globals.deathScreen && !Globals.crashed && !Globals.storeOpen) {
                                if(Globals.levelScreenOpen) {
                                    Globals.levelScreenOpen = false;
                                    Globals.paused = false;
                                } else {
                                    Globals.levelScreenOpen = true;
                                    Globals.paused = true;
                                }
                            }
                        }
                        if(!Globals.deathScreen && !Globals.crashed && !Globals.storeOpen && !Globals.levelScreenOpen) {
                            if (key == Globals.HANDGUN.getKey()) {
                                int r = player.setWeapon(Globals.HANDGUN.getName());
                                if(r == 1) loadout.setCurrentWeapon(Globals.HANDGUN.getName());
                            }
                            if (key == Globals.ASSAULT_RIFLE.getKey()) {
                                int r = player.setWeapon(Globals.ASSAULT_RIFLE.getName());
                                if(r == 1) loadout.setCurrentWeapon(Globals.ASSAULT_RIFLE.getName());
                            }
                            if (key == Globals.SHOTGUN.getKey()) {
                                int r = player.setWeapon(Globals.SHOTGUN.getName());
                                if(r == 1) loadout.setCurrentWeapon(Globals.SHOTGUN.getName());
                            }
                            if (key == Globals.FLAMETHROWER.getKey()) {
                                int r = player.setWeapon(Globals.FLAMETHROWER.getName());
                                if(r == 1) loadout.setCurrentWeapon(Globals.FLAMETHROWER.getName());
                            }
                            if (key == Globals.GRENADE.getKey()) {
                                int r = player.setWeapon(Globals.GRENADE.getName());
                                if(r == 1) loadout.setCurrentWeapon(Globals.GRENADE.getName());
                            }
                            if (key == Globals.LANDMINE.getKey()) {
                                int r = player.setWeapon(Globals.LANDMINE.getName());
                                if(r == 1) loadout.setCurrentWeapon(Globals.LANDMINE.getName());
                            }
                            if (key == Globals.FLARE.getKey()) {
                                int r = player.setWeapon(Globals.FLARE.getName());
                                if(r == 1) loadout.setCurrentWeapon(Globals.FLARE.getName());
                            }
                            if (key == Globals.LASERWIRE.getKey()) {
                                int r = player.setWeapon(Globals.LASERWIRE.getName());
                                if(r == 1) loadout.setCurrentWeapon(Globals.LASERWIRE.getName());
                            }
                            if (key == Globals.TURRETWEAPON.getKey()) {
                                int r = player.setWeapon(Globals.TURRETWEAPON.getName());
                                if(r == 1) loadout.setCurrentWeapon(Globals.TURRETWEAPON.getName());
                            }
                            if (key == Globals.TELEPORTER.getKey()) {
                                int r = player.setWeapon(Globals.TELEPORTER.getName());
                                if(r == 1) loadout.setCurrentWeapon(Globals.TELEPORTER.getName());
                            }
                        }
                    }
                }
            });

            canvas.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent m) {
                    if((m.getButton() == MouseEvent.BUTTON1)) {
                        if(!Globals.started) {
                            Globals.started = true;
                            Globals.gameTime.reset();
                            Globals.nextWave = Globals.gameTime.getElapsedMillis() + 3000;
                        }
                        if(Globals.started && Globals.deathScreen) {
                            Globals.started = false;
                            Globals.deathScreen = false;
                            currentWave = 1;
                            wave = new ZombieWave(currentWave);
                            player.resetStatistics();
                        }
                        if(Globals.started && Globals.storeOpen) store.click(m, player);
                        else if(Globals.started && Globals.levelScreenOpen) levelScreen.click(m, player);
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
                            if(player.getCurrentWeaponName().equals(Globals.FLAMETHROWER.getName())) {
                                Sounds.FLAMETHROWER.getAudio().stop();
                            }
                            
                            // Reset non-automatic weapons.
                            Iterator<Weapon> it = player.getWeaponsMap().values().iterator();
                            while(it.hasNext()) {
                                Weapon w = it.next();
                                if(!w.isAutomatic() && w.hasFired()) w.resetFire(); 
                            }
                        }
                        if (m.getButton() == MouseEvent.BUTTON3) Globals.buttons[1] = false;
                    }
                }
            });

            canvas.addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseMoved(MouseEvent m) {
                    if(Globals.started) {
                        Globals.mousePos.x = m.getX() + (Images.CROSSHAIR.getWidth() / 2);
                        Globals.mousePos.y = m.getY() + (Images.CROSSHAIR.getHeight() / 2);
                    }
                }

                @Override
                public void mouseDragged(MouseEvent m) {
                    if(Globals.started) {
                        Globals.mousePos.x = m.getX() + (Images.CROSSHAIR.getWidth() / 2);
                        Globals.mousePos.y = m.getY() + (Images.CROSSHAIR.getHeight() / 2);
                    }
                }
            });
            
            canvas.addMouseWheelListener(new MouseAdapter() {
                @Override
                public void mouseWheelMoved(MouseWheelEvent mw) {
                    if(player.getWeaponsMap().size() > 1) {
                        if(Globals.started && !Globals.deathScreen && !Globals.crashed &&
                           !Globals.storeOpen && !Globals.levelScreenOpen) {
                            int notches = mw.getWheelRotation();
                            String [] weaponNames = {Globals.HANDGUN.getName(), Globals.ASSAULT_RIFLE.getName(),
                                                     Globals.SHOTGUN.getName(), Globals.FLAMETHROWER.getName(),
                                                     Globals.GRENADE.getName(), Globals.LANDMINE.getName(),
                                                     Globals.FLARE.getName(), Globals.LASERWIRE.getName(),
                                                     Globals.TURRETWEAPON.getName(), Globals.TELEPORTER.getName()};
                            if(notches < 0) { // Wheel scrolled up.
                                // Move weapon selection to the right.
                                String name = Globals.HANDGUN.getName();
                                for(int i = 0; i < weaponNames.length; i++) {
                                    if(weaponNames[i].equals(player.getWeapon().getName())) {
                                        if((i + 1) > weaponNames.length) name = Globals.HANDGUN.getName();
                                        else {
                                            // Find the next weapon the player has and set it.
                                            for(int j = (i + 1); j < weaponNames.length; j++) {
                                                if(player.hasWeapon(weaponNames[j])) {
                                                    name = weaponNames[j];
                                                    break;
                                                }
                                            }
                                        }
                                        break;
                                    }
                                }
                                player.setWeapon(name);
                                loadout.setCurrentWeapon(name);
                            } else { // Wheel scrolled down.
                                // Move weapon selection to the left.
                                String name = Globals.HANDGUN.getName();
                                for(int i = 0; i < weaponNames.length; i++) {
                                    if(weaponNames[i].equals(player.getWeapon().getName())) {
                                        if((i - 1) < 0) {
                                            // Get the last weapon the player has.
                                            for(int j = 9; j >= 0; j--) {
                                                if(player.hasWeapon(weaponNames[j])) {
                                                    name = weaponNames[j];
                                                    break;
                                                }
                                            }
                                            break;
                                        } else {
                                            for(int j = (i - 1); j >= 0; j--) {
                                                if(player.hasWeapon(weaponNames[j])) {
                                                    name = weaponNames[j];
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                                player.setWeapon(name);
                                loadout.setCurrentWeapon(name);
                            }
                        }
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
        // Update the game time.
        if(Globals.started && !Globals.crashed && !Globals.deathScreen) {
            if(Globals.paused || Globals.storeOpen || Globals.levelScreenOpen) Globals.gameTime.increaseOffset();
            else Globals.gameTime.update();
        }
        
        // Update the game itself.
        if(Globals.started && !Globals.paused && !Globals.crashed && !Globals.deathScreen) {
            try {
                player.update();

                if(!Globals.waveInProgress) {
                    // If the player is in between waves, check if the countdown has reached zero.
                    if(Globals.gameTime.getElapsedMillis() >= Globals.nextWave) createWave();
                }

                // Update all zombies in the current wave.
                if(Globals.waveInProgress) this.wave.update(player, itemFactory);

                // Check player for damage.
                if(Globals.waveInProgress) this.wave.checkPlayerDamage(player);

                // Update Items
                itemFactory.update(player);

                // Check to see if the player is still alive. If not, take away a life and reset.
                if(!player.isAlive()) {
                    player.die();
                    if(player.getLives() == 0) {
                        // Show death screen and reset player.
                        Globals.deathScreen = true;
                        Globals.gameTime.reset();
                        synchronized(Globals.GAME_MESSAGES) { Globals.GAME_MESSAGES.clear(); }
                        player.reset();
                        levelScreen.resetLevels();
                        itemFactory.reset();
                        for(boolean k : Globals.keys) k = false;
                        for(boolean b : Globals.buttons) b = false;
                        Globals.resetWeapons();
                    }
                    loadout.setCurrentWeapon(Globals.HANDGUN.getName());
                    Sounds.FLAMETHROWER.getAudio().stop();
                }

                { // Begin weapon updates.
                    Iterator<Weapon> it = this.player.getWeaponsMap().values().iterator();
                    while(it.hasNext()) {
                        Weapon w = it.next();
                        w.updateWeapon(this.wave.getZombies());
                    }
                } // End weapon updates.

                // Check for end of wave.
                if(Globals.waveInProgress && this.wave.waveFinished()) {
                    Globals.waveInProgress = false;
                    Globals.nextWave = Globals.gameTime.getElapsedMillis() + (10 * 1000);
                }
                
                { // Delete expired messages.
                    synchronized(Globals.GAME_MESSAGES) {
                        Iterator<Message> it = Globals.GAME_MESSAGES.iterator();
                        while(it.hasNext()) {
                            Message m = it.next();
                            if(!m.isAlive()) {
                                it.remove();
                                continue;
                            }
                        }
                    }
                } // End deleting expired messages.
            } catch(Exception e) {
                createErrorWindow(e);
            }
        }
    }
    
    private void createWave() {
        try {
            this.wave = new ZombieWave(this.currentWave);
            this.currentWave++;
            Globals.waveInProgress = true;
        } catch(Exception e) {
            createErrorWindow(e);
        }
    }
    
    public static BufferedImage loadImage(String filename) {
        try {
            // Create a new BufferedImage from the file that supports transparency.
            BufferedImage bi = ImageIO.read(GZSFramework.class.getResource(filename));
            BufferedImage buffer = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = (Graphics2D)buffer.createGraphics();
            g2d.drawImage(bi, 0, 0, null);
            g2d.dispose();
            return buffer;
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
                    try {
                        update();
                        canvas.repaint();
                        Thread.sleep(Globals.SLEEP_TIME);
                    } catch (InterruptedException ie) {
                        System.out.println("Error occurred in main thread...");
                        createErrorWindow(ie);
                    } catch(Exception e) {
                        createErrorWindow(e);
                    }
                }
                TinySound.shutdown();
                System.exit(0);
            }
        };
    }
    
    private void startThread() {
        new Thread(Globals.animation).start();
    }
}
