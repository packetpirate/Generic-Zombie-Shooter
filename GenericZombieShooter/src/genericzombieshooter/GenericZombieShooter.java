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

import genericzombieshooter.misc.Globals;
import genericzombieshooter.misc.Images;
import java.awt.Cursor;
import java.awt.Toolkit;
import javax.swing.JApplet;
import javax.swing.JFrame;
import kuusisto.tinysound.TinySound;

/**
 * 
 * @author Darin Beaudreau
 */
public class GenericZombieShooter extends JApplet {
    public static void main(String[] args) {
        // Initialize TinySound before everything else.
        
    	TinySound.init();
        
        // Create the frame and add the framework's canvas to it.
        JFrame frame = new JFrame("Generic Zombie Shooter v" + Globals.VERSION);
        GZSFramework framework = new GZSFramework(frame);
        
        frame.add(framework.canvas);
        
        // Set the custom cursor to the crosshair image.
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Cursor cursor = toolkit.createCustomCursor(Images.CROSSHAIR, Globals.mousePos, "Crosshair");
        
        frame.setCursor(cursor);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }
}
