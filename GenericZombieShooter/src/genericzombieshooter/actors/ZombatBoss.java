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
package genericzombieshooter.actors;

import genericzombieshooter.misc.Globals;
import genericzombieshooter.misc.Sounds;
import genericzombieshooter.structures.Animation;
import genericzombieshooter.structures.items.Invulnerability;
import java.awt.geom.Point2D;
import java.util.List;

/**
 * Used to represent the boss zombie type, the Zombat.
 * @author Darin Beaudreau
 */
public class ZombatBoss extends Zombie {
    // Final Variables
    private static final int EXP_VALUE = 2500;
    private static final double ATTACK_DISTANCE = 166.0;
    private static final int DRAIN_COOLDOWN = 2000 / (int)Globals.SLEEP_TIME;
    private static final int DRAIN_AMOUNT = 5;
    
    // Member Variables
    private double maxHealth;
    private int cooldown;
    public boolean canFire() { return this.cooldown == 0; }
    
    public ZombatBoss(Point2D.Double p_, int health_, int damage_, double speed_, int score_, Animation animation_) {
        super(p_, Globals.ZOMBIE_BOSS_ZOMBAT_TYPE, health_, damage_, speed_, score_, ZombatBoss.EXP_VALUE, animation_);
        this.maxHealth = health_;
        this.cooldown = ZombatBoss.DRAIN_COOLDOWN;
    }
    
    @Override
    public void update(Player player, List<Zombie> zombies) {
        // If the Zombat's attack is cooled down and he is within range, drain the player.
        if(canFire()) {
            Point2D.Double playerPos = new Point2D.Double(player.getCenterX(), player.getCenterY());
            Point2D.Double myPos = new Point2D.Double(this.x, this.y);
            if(inRange(playerPos, myPos)) {
                // Drain the player's health, healing the Zombat.
                this.drain(player);
                this.cooldown = ZombatBoss.DRAIN_COOLDOWN;
            }
        } else this.cooldown--;
    }
    
    private void drain(Player player) {
        if(!player.hasEffect(Invulnerability.EFFECT_NAME)) {
            player.takeDamage(ZombatBoss.DRAIN_AMOUNT);
            this.health += ZombatBoss.DRAIN_AMOUNT;
            if(this.health > this.maxHealth) this.health = this.maxHealth;
        }
    }
    
    @Override
    public void moan(Player player) {
        if(!this.moaned) {
            if(Globals.gameTime.getElapsedMillis() >= this.nextMoan) {
                double xD = player.getCenterX() - this.x;
                double yD = player.getCenterY() - this.y;
                double dist = Math.sqrt((xD * xD) + (yD * yD));
                double gain = 1.0 - (dist / Player.AUDIO_RANGE);
                Sounds.MOAN8.play(gain);
                this.moaned = true;
            }
        }
    }
    
    private boolean inRange(Point2D.Double playerPos, Point2D.Double myPos) {
        double xD = playerPos.x - myPos.x;
        double yD = playerPos.y - myPos.y;
        return Math.sqrt((xD * xD) + (yD * yD)) <= ZombatBoss.ATTACK_DISTANCE;
    }
}
