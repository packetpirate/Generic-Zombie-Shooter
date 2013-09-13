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

/**
 * Used to manage status effects on the player.
 * @author Darin Beaudreau
 */
public class StatusEffect {
    // Member Variables
    private long endTime;
    public long getEndTime() { return this.endTime; }
    public void refresh(long duration) { this.endTime = System.currentTimeMillis() + duration; }
    public boolean isActive() { return (System.currentTimeMillis() < this.endTime); }
    private int value;
    public long getValue() { return this.value; }
    
    public StatusEffect(long duration, int value) {
        this.endTime = System.currentTimeMillis() + duration;
        this.value = value;
    }
}
