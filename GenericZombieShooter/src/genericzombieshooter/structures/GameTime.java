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
 * Used to keep track of game time while the game is running.
 * @author Darin Beaudreau
 */
public class GameTime {
    private long elapsedMillis;
    public long getElapsedMillis() { return this.elapsedMillis; }
    public long getElapsedSecs() { return (this.elapsedMillis / 1000); }
    private long lastUpdate;
    private long offset;
    public long getOffset() { return this.offset; }
    public void increaseOffset() { this.offset = System.currentTimeMillis() - this.lastUpdate; }
    
    public GameTime() {
        this.elapsedMillis = 0;
        this.lastUpdate = System.currentTimeMillis();
    }
    
    public void update() {
        this.elapsedMillis += ((System.currentTimeMillis() - this.lastUpdate) - this.offset);
        this.lastUpdate = System.currentTimeMillis();
        this.offset = 0;
    }
    
    public void reset() { 
        this.elapsedMillis = 0;
        this.lastUpdate = System.currentTimeMillis();
        this.offset = 0;
    }
}
