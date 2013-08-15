/**
 * Copyright © 2012-2013 Darin Beaudreau
 * All rights reserved.
 * 
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

import javax.swing.JApplet;
import javax.swing.JFrame;

/**
 * 
 * @author Darin Beaudreau
 */
public class GenericZombieShooter extends JApplet {
    private static final float VERSION = 0.75f;
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Generic Zombie Shooter v" + VERSION);
        GZSFramework framework = new GZSFramework();
        
        frame.add(framework.canvas);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }
}
