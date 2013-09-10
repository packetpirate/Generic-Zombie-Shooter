package genericzombieshooter.structures;

import genericzombieshooter.actors.AcidZombie;
import genericzombieshooter.actors.Player;
import genericzombieshooter.actors.PoisonFogZombie;
import genericzombieshooter.actors.Zombie;
import genericzombieshooter.actors.ZombieMatron;
import genericzombieshooter.misc.Globals;
import genericzombieshooter.misc.Images;
import genericzombieshooter.structures.weapons.Weapon;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents all the zombies in a wave of combat. Used to handle all zombies on screen.
 * @author Darin Beaudreau
 */
public class ZombieWave {
    // Final Variables
    private static final long ZOMBIE_SPAWN_TIME = 1000;
    private static final int ZOMBIES_PER_WAVE = 3;
    // Member Variables
    private int waveNumber;
    private List<Zombie> zombiesUnborn;
    private List<Zombie> zombiesAlive;
    private List<Zombie> zombiesToDie;
    private long nextZombieSpawn;
    public int getWaveNumber() { return this.waveNumber; }
    public List<Zombie> getUnbornZombies() { return this.zombiesUnborn; }
    public List<Zombie> getZombies() { return this.zombiesAlive; }
    public boolean waveFinished() { return (this.zombiesAlive.isEmpty() && this.zombiesUnborn.isEmpty()); }
    
    public ZombieWave(int wave) {
        this.waveNumber = wave;
        this.zombiesUnborn = constructWave(wave);
        this.zombiesAlive = new ArrayList<Zombie>();
        this.zombiesToDie = new ArrayList<Zombie>();
        this.nextZombieSpawn = System.currentTimeMillis() + ZombieWave.ZOMBIE_SPAWN_TIME;
    }
    
    private List<Zombie> constructWave(int currentWave) {
        List<Zombie> wave = new ArrayList<Zombie>();
        
        int difficulty = 2;
        if(currentWave >= 5) difficulty++;
        if(currentWave >= 10) difficulty++;
        if(currentWave >= 20) difficulty++;
        
        int enemyCount = currentWave * ZombieWave.ZOMBIES_PER_WAVE;
        int specialsThisWave = (int)(enemyCount / 4);
        int specialsSpawned = 0;
        for(int i = 0; i < enemyCount; i++) {
            // Decide which side of the screen to spawn the zombie on.
            double x = 0;
            double y = 0;
            
            int spawnSide = Globals.r.nextInt(4) + 1;
            if(spawnSide == 1) x = Globals.r.nextInt((Globals.W_WIDTH - 40) + 1);
            else if(spawnSide == 2) {
                x = Globals.W_WIDTH - 40;
                y = Globals.r.nextInt((Globals.W_HEIGHT - 40) + 1);
            } else if(spawnSide == 3) {
                x = Globals.r.nextInt((Globals.W_WIDTH - 40) + 1);
                y = Globals.W_HEIGHT - 40;
            } else if(spawnSide == 4) y = Globals.r.nextInt((Globals.W_HEIGHT - 40) + 1);
            
            Point2D.Double p_ = new Point2D.Double(x, y);
            
            if(specialsSpawned >= specialsThisWave) difficulty = 2;
            int zombieType = Globals.r.nextInt(difficulty) + 1;
            if(zombieType == Globals.ZOMBIE_REGULAR_TYPE) {
                // Zumby
                Animation a_ = new Animation(Images.ZOMBIE_REGULAR, 40, 40, 2, (int)p_.x, (int)p_.y, 200, 0, true);
                Zombie z_ = new Zombie(p_, Globals.ZOMBIE_REGULAR_TYPE, 250, 1, 1, 10, a_);
                wave.add(z_);
            } else if(zombieType == Globals.ZOMBIE_DOG_TYPE) {
                // Rotdog
                Animation a_ = new Animation(Images.ZOMBIE_DOG, 50, 50, 4, (int)p_.x, (int)p_.y, 80, 0, true);
                Zombie z_ = new Zombie(p_, Globals.ZOMBIE_DOG_TYPE, 100, 3, 2, 20, a_);
                wave.add(z_);
            } else if(zombieType == Globals.ZOMBIE_ACID_TYPE) {
                // Up-Chuck
                Animation a_ = new Animation(Images.ZOMBIE_ACID, 64, 64, 2, (int)p_.x, (int)p_.y, 200, 0, true);
                AcidZombie z_ = new AcidZombie(p_, 300, 1, 1, 50, a_);
                wave.add(z_);
                specialsSpawned++;
            } else if(zombieType == Globals.ZOMBIE_POISONFOG_TYPE) {
                // Gasbag
                Animation a_ = new Animation(Images.ZOMBIE_POISONFOG, 40, 40, 2, (int)p_.x, (int)p_.y, 100, 0, true);
                PoisonFogZombie pfz_ = new PoisonFogZombie(p_, 250, 1, 2, 80, a_);
                wave.add(pfz_);
                specialsSpawned++;
            } else if(zombieType == Globals.ZOMBIE_MATRON_TYPE) {
                // Big Mama
                Animation a_ = new Animation(Images.ZOMBIE_MATRON, 48, 48, 2, (int)p_.x, (int)p_.y, 200, 0, true);
                ZombieMatron zm_ = new ZombieMatron(p_, 500, 1, 1, 150, a_);
                wave.add(zm_);
                specialsSpawned++;
            }
        }
        
        return wave;
    }
    
    public void draw(Graphics2D g2d) {
        Iterator<Zombie> it = this.zombiesAlive.iterator();
        while(it.hasNext()) {
            Zombie z = it.next();
            z.draw(g2d);
            if(!z.getParticles().isEmpty()) z.drawParticles(g2d);
        }
    }
    
    public void update(Player player) {
        // Remove dead zombies from the live list.
        if(!this.zombiesToDie.isEmpty()) this.zombiesAlive.removeAll(this.zombiesToDie);
        // If the spawn timer is up, spawn a new zombie.
        if(!this.zombiesUnborn.isEmpty() && (System.currentTimeMillis() >= this.nextZombieSpawn)) {
            Zombie z = this.zombiesUnborn.remove(0);
            if(z.getType() == Globals.ZOMBIE_MATRON_TYPE) z.set(0, (System.currentTimeMillis() + ZombieMatron.TIME_TO_BURST));
            this.zombiesAlive.add(z);
            this.nextZombieSpawn = System.currentTimeMillis() + ZombieWave.ZOMBIE_SPAWN_TIME;
            
        }
        if(!this.zombiesUnborn.isEmpty()) {
            // If there are any tiny zombies in the list, add them all at once.
            Iterator<Zombie> it = this.zombiesUnborn.iterator();
            while(it.hasNext()) {
                Zombie z = it.next();
                if(z.getType() == Globals.ZOMBIE_TINY_TYPE) {
                    this.zombiesAlive.add(z);
                    it.remove();
                }
            }
        }
        // Update "living" zombies.
        Iterator<Zombie> it = this.zombiesAlive.iterator();
        while(it.hasNext()) {
            Zombie z = it.next();
            
            { // Handle the zombie's movement and animation.
                // Update the zombie's animation.
                z.getImage().update();

                // Update the zombie.
                double theta_ = Math.atan2((player.getCenterY() - z.y), 
                                           (player.getCenterX() - z.x)) + Math.PI / 2;
                z.rotate(theta_);
                z.move(theta_);
                z.update(player, this.zombiesUnborn);
            } // End movement and animation updates.
            { // Check the zombie for collisions with ammo, etc.
                if(!z.isDead()) {
                    // Check for collisions with ammo, etc.
                    //Iterator<Weapon> wit = player.getAllWeapons().iterator();
                    Iterator<Weapon> wit = player.getWeaponsMap().values().iterator();
                    while(wit.hasNext()) {
                        Weapon w = wit.next();
                        int damage = w.checkForDamage(z.getRect());
                        if(damage > 0) {
                            z.takeDamage(damage);
                            if(z.isDead()) {
                                // Give the player some cash.
                                player.addCash(z.getCashValue());
                                player.addKill();
                            }
                        }
                    }
                }
                
                // Check again, and if the zombie is dead, add to the toDie list.
                if(z.isDead()) this.zombiesToDie.add(z);
            } // End checking zombie for death.
        }
    }
    
    public void checkPlayerDamage(Player player) {
        Iterator<Zombie> it = this.zombiesAlive.iterator();
        while(it.hasNext()) {
            Zombie z = it.next();
            if(!player.isInvincible()) {
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
        if(player.isPoisoned()) player.takePoisonDamage();
    }
}
