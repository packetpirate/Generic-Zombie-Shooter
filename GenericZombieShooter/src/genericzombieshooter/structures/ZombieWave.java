package genericzombieshooter.structures;

import genericzombieshooter.actors.AberrationBoss;
import genericzombieshooter.actors.AcidZombie;
import genericzombieshooter.actors.Player;
import genericzombieshooter.actors.PoisonFogZombie;
import genericzombieshooter.actors.StitchesBoss;
import genericzombieshooter.actors.ZombatBoss;
import genericzombieshooter.actors.Zombie;
import genericzombieshooter.actors.ZombieMatron;
import genericzombieshooter.misc.Globals;
import genericzombieshooter.misc.Images;
import genericzombieshooter.structures.items.ExpMultiplier;
import genericzombieshooter.structures.items.ExtraLife;
import genericzombieshooter.structures.items.Invulnerability;
import genericzombieshooter.structures.items.SpeedUp;
import genericzombieshooter.structures.items.UnlimitedAmmo;
import genericzombieshooter.structures.weapons.Weapon;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
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
    private boolean bossWave;
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
        this.bossWave = ((wave % 15) == 0);
        this.zombiesUnborn = constructWave(wave);
        this.zombiesAlive = new ArrayList<Zombie>();
        this.zombiesToDie = new ArrayList<Zombie>();
        this.nextZombieSpawn = Globals.gameTime.getElapsedMillis() + ZombieWave.ZOMBIE_SPAWN_TIME;
    }
    
    private List<Zombie> constructWave(int currentWave) {
        List<Zombie> wave = new ArrayList<Zombie>();
        
        if(!this.bossWave) {
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
                    Animation a_ = new Animation(Images.ZOMBIE_REGULAR, 48, 48, 4, (int)p_.x, (int)p_.y, 200, 0, true);
                    Zombie z_ = new Zombie(p_, Globals.ZOMBIE_REGULAR_TYPE, 250, 1, 1, 10, 20, a_);
                    wave.add(z_);
                } else if(zombieType == Globals.ZOMBIE_DOG_TYPE) {
                    // Rotdog
                    Animation a_ = new Animation(Images.ZOMBIE_DOG, 48, 48, 4, (int)p_.x, (int)p_.y, 80, 0, true);
                    Zombie z_ = new Zombie(p_, Globals.ZOMBIE_DOG_TYPE, 100, 3, 2, 20, 30, a_);
                    wave.add(z_);
                } else if(zombieType == Globals.ZOMBIE_ACID_TYPE) {
                    // Up-Chuck
                    Animation a_ = new Animation(Images.ZOMBIE_ACID, 64, 64, 4, (int)p_.x, (int)p_.y, 200, 0, true);
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
        } else {
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
            
            int bossType = (Globals.r.nextInt(3) + 1);
            
            if(bossType == 1) {
                // Aberration
                Animation a_ = new Animation(Images.BOSS_ABERRATION, 128, 128, 4, (int)p_.x, (int)p_.y, 150, 0, true);
                AberrationBoss ab_ = new AberrationBoss(p_, 10000, 1, 1, 1000, a_);
                wave.add(ab_);
            } else if(bossType == 2) {
                // Zombat Wave
                for(int i = 0; i < 5; i++) {
                    Animation a_ = new Animation(Images.BOSS_ZOMBAT, 64, 64, 4, (int)p_.x, (int)p_.y, 50, 0, true);
                    ZombatBoss zb_ = new ZombatBoss(p_, 2000, 1, 2, 500, a_);
                    wave.add(zb_);
                }
            } else if(bossType == 3) {
                // Stitches
                Animation a_ = new Animation(Images.BOSS_STITCHES, 128, 128, 4, (int)p_.x, (int)p_.y, 150, 0, true);
                StitchesBoss sb_ = new StitchesBoss(p_, 15000, 4, 1, 3000, a_);
                wave.add(sb_);
            }
            
            this.bossWave = false;
        }
        
        return wave;
    }
    
    public void draw(Graphics2D g2d) {
        AffineTransform saved = g2d.getTransform();
        Iterator<Zombie> it = this.zombiesAlive.iterator();
        while(it.hasNext()) {
            Zombie z = it.next();
            z.draw(g2d);
            g2d.setTransform(saved);
        }
    }
    
    public void update(Player player, ItemFactory itemFactory) {
        // Remove dead zombies from the live list.
        if(!this.zombiesToDie.isEmpty()) this.zombiesAlive.removeAll(this.zombiesToDie);
        // If the spawn timer is up, spawn a new zombie.
        if(!this.zombiesUnborn.isEmpty() && (Globals.gameTime.getElapsedMillis() >= this.nextZombieSpawn)) {
            Zombie z = this.zombiesUnborn.remove(0);
            if(z.getType() == Globals.ZOMBIE_MATRON_TYPE) z.set(0, (Globals.gameTime.getElapsedMillis() + ZombieMatron.TIME_TO_BURST));
            this.zombiesAlive.add(z);
            this.nextZombieSpawn = Globals.gameTime.getElapsedMillis() + ZombieWave.ZOMBIE_SPAWN_TIME;
            
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
                        if(player.getDamageBonus() > 0) damage += (damage * player.getDamageBonus());
                        if(damage > 0) {
                            z.takeDamage(damage);
                            if(z.isDead()) {
                                // Give the player some cash.
                                player.addCash(z.getCashValue());
                                player.addExp(z.getExpValue());
                                player.addKill();
                                if(z.getType() >= 3) {
                                    // Base chance of 10% (19-20) to drop a powerup. 
                                    // 10% extra for each tier of zombie.
                                    int dropRoll = Globals.r.nextInt(20) + 1;
                                    if((z.getType() >= Globals.ZOMBIE_ACID_TYPE) && (z.getType() < Globals.ZOMBIE_TINY_TYPE)) 
                                        dropRoll += (z.getType() % Globals.ZOMBIE_ACID_TYPE) * 2;
                                    if(dropRoll >= 19) {
                                        SpeedUp speed = new SpeedUp(z);
                                        UnlimitedAmmo unlimited = new UnlimitedAmmo(z);
                                        ExtraLife extra = new ExtraLife(z);
                                        ExpMultiplier exp = new ExpMultiplier(z);
                                        Invulnerability invuln = new Invulnerability(z);
                                        Item [] statusItems = {speed, unlimited, speed, invuln, 
                                                               speed, extra, speed, exp, speed, exp};
                                        int i = Globals.r.nextInt(statusItems.length);
                                        itemFactory.dropItem(statusItems[i]);
                                    }
                                }
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
            if(!player.hasEffect(Invulnerability.EFFECT_NAME)) {
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
}
