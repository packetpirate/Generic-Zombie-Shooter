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

import genericzombieshooter.actors.AcidZombie;
import genericzombieshooter.actors.Player;
import genericzombieshooter.actors.PoisonFogZombie;
import genericzombieshooter.actors.Zombie;
import genericzombieshooter.actors.ZombieMatron;
import genericzombieshooter.misc.Globals;
import genericzombieshooter.misc.Images;
import genericzombieshooter.misc.Sounds;
import genericzombieshooter.structures.Animation;
import genericzombieshooter.structures.Item;
import genericzombieshooter.structures.Particle;
import genericzombieshooter.structures.components.ErrorWindow;
import genericzombieshooter.structures.components.WeaponsLoadout;
import genericzombieshooter.structures.items.Ammo;
import genericzombieshooter.structures.items.HealthPack;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
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
    private List<Zombie> zombies;
    public List<Zombie> getZombies() { return zombies; }
    private List<Zombie> zombiesToAdd;
    private List<Item> items;
    public List<Item> getItems() { return this.items; }
    
    private int score; // The player's current score.
    public int getScore() { return score; }
    
    private WeaponsLoadout loadout;
    public WeaponsLoadout getLoadout() { return this.loadout; }

    public GZSFramework(JFrame frame_) {
        frame = frame_;
        canvas = new GZSCanvas(this);
        Globals.started = false;
        Globals.paused = false;
        Globals.crashed = false;

        { // Begin initializing member variables.
            Globals.keys = new boolean[4];
            for (boolean k : Globals.keys) k = false;
            Globals.buttons = new boolean[2];
            for (boolean b : Globals.buttons) b = false;

            Globals.mousePos = new Point(0, 0);
        } // End member variable initialization.

        { // Begin initializing game objects.
            player = new Player(((Globals.W_WIDTH / 2) - 20), ((Globals.W_HEIGHT / 2) - 20), 40, 40);
            //zombies = new ArrayList<Zombie>();
            zombies = Collections.synchronizedList(new ArrayList<Zombie>());
            zombiesToAdd = Collections.synchronizedList(new ArrayList<Zombie>());
            items = new ArrayList<Item>();
            score = 0;
            loadout = new WeaponsLoadout(player);
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
                        if (key == Globals.ASSAULT_RIFLE.getKey()) {
                            player.setWeapon(1);
                            loadout.setCurrentWeapon(1);
                        }
                        if (key == Globals.SHOTGUN.getKey()) {
                            player.setWeapon(2);
                            loadout.setCurrentWeapon(2);
                        }
                        if (key == Globals.FLAMETHROWER.getKey()) {
                            player.setWeapon(3);
                            loadout.setCurrentWeapon(3);
                        }
                        if (key == Globals.GRENADE.getKey()) {
                            player.setWeapon(4);
                            loadout.setCurrentWeapon(4);
                        }
                        if (key == Globals.LANDMINE.getKey()) {
                            player.setWeapon(5);
                            loadout.setCurrentWeapon(5);
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

                // Update zombie vectors and positions.
                if(!zombies.isEmpty()) {
                    synchronized(zombies) {
                        Iterator<Zombie> it = zombies.iterator();
                        while(it.hasNext()) {
                            Zombie z = it.next();
                            // Update the zombie animation.
                            z.getImage().update();
                            // Update the zombie's movement vector.
                            double theta_ = Math.atan2((player.getCenterY() - z.y), 
                                                       (player.getCenterX() - z.x)) + Math.PI / 2;
                            z.rotate(theta_);
                            z.move(theta_);
                            z.update(player, zombiesToAdd);
                        }
                    }
                }

                { // Check for damage against the player. (touching zombies, projectiles, explosions, etc...)
                    if(!zombies.isEmpty()) {
                        synchronized(zombies) {
                            Iterator<Zombie> it = zombies.iterator();
                            while(it.hasNext()) {
                                Zombie z = it.next();
                                if(player.intersects(z.getRect())) player.takeDamage(z.getDamage());
                                if(z.getParticles() != null) {
                                    Iterator<Particle> pit = z.getParticles().iterator();
                                    while(pit.hasNext()) {
                                        Particle p = pit.next();
                                        if(p.checkCollision(player)) player.takeDamage(z.getParticleDamage());
                                    }
                                }
                            }
                        }
                    }
                    if(player.isPoisoned()) player.takePoisonDamage();
                } // End checking player for damage sources.

                { // Check to see if the player has collected any items.
                    Iterator<Item> it = this.items.iterator();
                    while(it.hasNext()) {
                        Item i = it.next();
                        if(this.player.contains(i)) {
                            i.applyEffect(this.player);
                            it.remove();
                        }
                    }
                } // End checking for item collisions.

                // Check to see if the player is still alive. If not, take away a life and reset.
                if(!player.isAlive()) {
                    player.die();
                    if(player.getLives() == 0) {
                        // Reset the game.
                        Globals.started = false;
                        player.reset();
                        zombies = Collections.synchronizedList(new ArrayList<Zombie>());
                        zombiesToAdd = Collections.synchronizedList(new ArrayList<Zombie>());
                        items = new ArrayList<Item>();
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

                { // Do zombie updates.
                    // Check for collisions between zombies and ammo.
                    synchronized(zombies) {
                        Iterator<Zombie> it = zombies.iterator();
                        while(it.hasNext()) {
                            Zombie z = it.next();
                            if(z.isDead()) {
                                it.remove();
                                continue;
                            } else {
                                Iterator<Weapon> wit = player.getAllWeapons().iterator();
                                while(wit.hasNext()) {
                                    Weapon w = wit.next();
                                    int damage = w.checkForDamage(z.getRect());
                                    if(damage > 0) {
                                        z.takeDamage(damage);
                                        if(z.isDead()) {
                                            score += z.getScore();
                                            it.remove();
                                        }
                                    }
                                }
                            }
                        }
                    }
                } // End zombie updates.

                { // Begin weapon updates.
                    Iterator<Weapon> it = this.player.getAllWeapons().iterator();
                    while(it.hasNext()) {
                        Weapon w = it.next();
                        w.updateWeapon(this.zombies);
                    }
                } // End weapon updates.

                { // Add any zombies in the toAdd list.
                    if(!zombiesToAdd.isEmpty()) {
                        zombies.addAll(zombiesToAdd);
                        zombiesToAdd.clear();
                    }
                } // End adding new zombies.
            } catch(Exception e) {
                createErrorWindow(e);
            }
        }
    }
    
    /**
     * Creates a new zombie on the screen.
     **/
    private void createZombie(int type) {
        try {
            synchronized(zombies) {
                // Decide which side of the screen to spawn the zombie on.
                int spawnSide = Globals.r.nextInt(4) + 1;

                double x_ = 0;
                double y_ = 0;

                switch(spawnSide) {
                    case 1:
                        x_ = Globals.r.nextInt((Globals.W_WIDTH - 40) + 1);
                        break;
                    case 2:
                        x_ = Globals.W_WIDTH - 40;
                        y_ = Globals.r.nextInt((Globals.W_HEIGHT - 40) + 1);
                        break;
                    case 3:
                        x_ = Globals.r.nextInt((Globals.W_WIDTH - 40) + 1);
                        y_ = Globals.W_HEIGHT - 40;
                        break;
                    case 4:
                        y_ = Globals.r.nextInt((Globals.W_HEIGHT - 40) + 1);
                        break;
                }

                // Create the zombie.
                Point2D.Double p_ = new Point2D.Double(x_, y_);
                if(type == Globals.ZOMBIE_REGULAR_TYPE) {
                    // Regular Zombie
                    Animation a_ = new Animation(Images.ZOMBIE_REGULAR, 40, 40, 2, (int)p_.x, (int)p_.y, 200, 0, true);
                    Zombie z_ = new Zombie(p_, 250, 1, 1, 100, a_);
                    zombies.add(z_);
                } else if(type == Globals.ZOMBIE_DOG_TYPE) {
                    // Fast Zombie Dog
                    Animation a_ = new Animation(Images.ZOMBIE_DOG, 50, 50, 4, (int)p_.x, (int)p_.y, 80, 0, true);
                    Zombie z_ = new Zombie(p_, 100, 3, 2, 150, a_);
                    zombies.add(z_);
                } else if(type == Globals.ZOMBIE_ACID_TYPE) {
                    // Acid Zombie
                    Animation a_ = new Animation(Images.ZOMBIE_ACID, 64, 64, 2, (int)p_.x, (int)p_.y, 200, 0, true);
                    AcidZombie z_ = new AcidZombie(p_, 300, 1, 1, 400, a_);
                    zombies.add(z_);
                } else if(type == Globals.ZOMBIE_POISONFOG_TYPE) {
                    // Explosive Zombie
                    Animation a_ = new Animation(Images.ZOMBIE_POISONFOG, 40, 40, 2, (int)p_.x, (int)p_.y, 100, 0, true);
                    PoisonFogZombie ez_ = new PoisonFogZombie(p_, 250, 1, 2, 200, a_);
                    zombies.add(ez_);
                } else if(type == Globals.ZOMBIE_MATRON_TYPE) {
                    // Zombie Matron
                    Animation a_ = new Animation(Images.ZOMBIE_MATRON, 48, 48, 2, (int)p_.x, (int)p_.y, 200, 0, true);
                    ZombieMatron zm_ = new ZombieMatron(p_, 500, 1, 1, 1000, a_);
                    zombies.add(zm_);
                }
            }
        } catch(Exception e) {
            createErrorWindow(e);
        }
    }
    
    private void createHealthPack() {
        int healAmount = Globals.r.nextInt(75 - 50 + 1) + 50;
        double x = Globals.r.nextInt((Globals.W_WIDTH - 20) - 20 + 1) + 20;
        double y = Globals.r.nextInt((int)((Globals.W_HEIGHT - (WeaponsLoadout.BAR_HEIGHT + 10)) - 20 + 1)) + 20;
        this.items.add(new HealthPack(healAmount, new Point2D.Double(x, y)));
    }
    
    private void createAmmoPack() {
        int numOfWeapons = this.player.getAllWeapons().size();
        int w = Globals.r.nextInt(numOfWeapons) + 1;
        if(this.player.getWeapon(w).ammoFull()) createAmmoPack();
        else {
            int ammo = this.player.getWeapon(w).getAmmoPackAmount();
            double x = Globals.r.nextInt((Globals.W_WIDTH - 20) - 20 + 1) + 20;
            double y = Globals.r.nextInt((int)((Globals.W_HEIGHT - (WeaponsLoadout.BAR_HEIGHT + 10)) - 20 + 1)) + 20;
            this.items.add(new Ammo(w, ammo, new Point2D.Double(x, y)));
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

    /**
     * Initializes the main animation thread.
     **/
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
                    }
                }
                System.exit(0);
            }
        };
        Globals.health = new Runnable() {
            @Override
            public void run() {
                while(Globals.running) {
                    if(Globals.started) {
                        try {
                            Thread.sleep(HealthPack.SPAWN_TIME);
                        } catch(InterruptedException ie) {
                            System.out.println("Error occurred in HealthPack thread...");
                        }

                        if(!Globals.paused) createHealthPack();
                    }
                } 
            }
        };
        Globals.ammo = new Runnable() {
            @Override
            public void run() {
                while(Globals.running) {
                    if(Globals.started) {
                        try {
                            Thread.sleep(Ammo.SPAWN_TIME);
                        } catch(InterruptedException ie) {
                            System.out.println("Error occurred in AmmoPack thread...");
                        }

                        if(!Globals.paused) createAmmoPack();
                    }
                }
            }
        };
        Globals.zombieSpawns = new ArrayList<Runnable>();
        Globals.zombieSpawns.add(new Runnable() {
            // Regular Zombie Spawn
            @Override
            public void run() {
                while(Globals.running) {
                    if(Globals.started) {
                        try {
                            Thread.sleep(Globals.ZOMBIE_REGULAR_SPAWN);
                        } catch(InterruptedException ie) {
                            System.out.println("Regular zombie thread interrupted...");
                        }

                        if(!Globals.paused) createZombie(Globals.ZOMBIE_REGULAR_TYPE);
                    }
                }
            }
        });
        Globals.zombieSpawns.add(new Runnable() {
            // Zombie Dog Spawn
            @Override
            public void run() {
                while(Globals.running) {
                    if(Globals.started) {
                        try {
                            Thread.sleep(Globals.ZOMBIE_DOG_SPAWN);
                        } catch(InterruptedException ie) {
                            System.out.println("Zombie Dog thread interrupted...");
                        }

                        if(!Globals.paused) createZombie(Globals.ZOMBIE_DOG_TYPE);
                    }
                }
            }
        });
        Globals.zombieSpawns.add(new Runnable() {
            // Acid Zombie Spawn
            @Override
            public void run() {
                while(Globals.running) {
                    if(Globals.started) {
                        try {
                            Thread.sleep(Globals.ZOMBIE_ACID_SPAWN);
                        } catch(InterruptedException ie) {
                            System.out.println("Acid Zombie thread interrupted...");
                        }

                        if(!Globals.paused) createZombie(Globals.ZOMBIE_ACID_TYPE);
                    }
                }
            }
        });
        Globals.zombieSpawns.add(new Runnable() {
            // Explosive Zombie Spawn
            @Override
            public void run() {
                while(Globals.running) {
                    if(Globals.started) {
                        try {
                            Thread.sleep(Globals.ZOMBIE_POISONFOG_SPAWN);
                        } catch(InterruptedException ie) {
                            System.out.println("Explosive Zombie thread interrupted...");
                        }

                        if(!Globals.paused) createZombie(Globals.ZOMBIE_POISONFOG_TYPE);
                    }
                }
            }
        });
        Globals.zombieSpawns.add(new Runnable() {
            // Zombie Matron Spawn
            @Override
            public void run() {
                while(Globals.running) {
                    if(Globals.started) {
                        try {
                            Thread.sleep(Globals.ZOMBIE_MATRON_SPAWN);
                        } catch(InterruptedException ie) {
                            System.out.println("Zombie Matron thread interrupted...");
                        }
                        
                        if(!Globals.paused) createZombie(Globals.ZOMBIE_MATRON_TYPE);
                    }
                }
            }
        });
    }

    /**
     * Creates a new thread from the animation Runnable and then starts it.
     **/
    private void startThread() {
        new Thread(Globals.animation).start();
        new Thread(Globals.health).start();
        new Thread(Globals.ammo).start();
        for(Runnable r : Globals.zombieSpawns) new Thread(r).start();
    }
}
