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
package genericzombieshooter.structures.components;

import genericzombieshooter.misc.Globals;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

/**
 * Used to display error messages to the user so they can be reported.
 * @author Darin Beaudreau
 */
public class ErrorWindow extends JPanel {
    // Components
    private JLabel header;
    private JTextArea text;
    private JLabel message;
    
    public ErrorWindow(Exception e) {
        setBackground(new Color(20, 105, 196));
        setPreferredSize(new Dimension(Globals.W_WIDTH, Globals.W_HEIGHT));
        setFocusable(true);
        requestFocus();
        
        this.header = new JLabel("I AM ERROR!");
        this.header.setFont(new Font("Impact", Font.PLAIN, 48));
        this.header.setHorizontalAlignment(SwingConstants.CENTER);
        this.message = new JLabel();
        this.message.setText("<html><body><font color='#FFFFFF'>"
                           + "An error has occured. Please copy this message and send it with a description of<br />"
                           + "what you were doing when the error occured to:</font> <font color='#FF0000'>darinbeaudreau@gmail.com</font>"
                           + "</body></html>");
        this.message.setFont(new Font("Courier New", Font.PLAIN, 14));
        this.message.setHorizontalAlignment(SwingConstants.CENTER);
        this.text = new JTextArea(20, 20);
        this.text.append("Version: " + Globals.VERSION + "\n");
        this.text.append("Exception: " + e.toString() + "\n\n");
        this.text.append("Stack Trace:\n");
        for(StackTraceElement element : e.getStackTrace()) {
            this.text.append("  -" + element.toString() + "\n");
        }
        JScrollPane pane = new JScrollPane(this.text, 
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        
        SpringLayout layout = new SpringLayout();
        setLayout(layout);
        
        // Adjust header label.
        layout.putConstraint(SpringLayout.NORTH, this.header, 20, SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.WEST, this.header, 50, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.EAST, this.header, -50, SpringLayout.EAST, this);
        // Adjust text area.
        layout.putConstraint(SpringLayout.NORTH, pane, 20, SpringLayout.SOUTH, this.header);
        layout.putConstraint(SpringLayout.WEST, pane, 50, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.EAST, pane, -50, SpringLayout.EAST, this);
        // Adjust message label.
        layout.putConstraint(SpringLayout.NORTH, this.message, 20, SpringLayout.SOUTH, pane);
        layout.putConstraint(SpringLayout.WEST, this.message, 50, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.EAST, this.message, -50, SpringLayout.EAST, this);
        
        this.add(this.header);
        this.add(pane);
        this.add(this.message);
    }
}
