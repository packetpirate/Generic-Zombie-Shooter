package genericzombieshooter;

import genericzombieshooter.actors.Player;
import genericzombieshooter.actors.Zombie;
import genericzombieshooter.structures.Particle;
import genericzombieshooter.structures.Vector2D;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author Darin Beaudreau
 */
public class Canvas extends JPanel implements Runnable {
    // Static variables.
    private static final int pWIDTH = 800; // The width of the panel.
    private static final int pHEIGHT = 640; // The height of the panel.
    private static final int SLEEP_TIME = 20; // The length of each tick in the animation thread.
    
    private static final int SPAWN_TIME = 40; // Multiplier for sleep time. Determines time it takes for a zombie to spawn.
    // Member variables.
    private Thread animation;
    private volatile boolean running;
    
    private boolean [] keys;
    private boolean [] buttons;
    private Point mousePos;
    
    private Player player;
    private ArrayList<Zombie> zombies;
    
    private Random rand;
    
    private int spawn;
    
    private ArrayList<Particle> projectiles;
    
    // Image variables.
    private BufferedImage background;
    
    public Canvas() {
        // Set the appearance of the canvas.
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(pWIDTH, pHEIGHT));
        setFocusable(true);
        requestFocus();
        
        // Initialize member variables.
        keys = new boolean[4];
        for(boolean b : keys) b = false;
        buttons = new boolean[2];
        for(boolean b : buttons) b = false;
        mousePos = new Point(0,0);
        player = new Player(((pWIDTH / 2) - 20), ((pHEIGHT / 2) - 20), 40, 40);
        zombies = new ArrayList<Zombie>();
        
        rand = new Random();
        
        spawn = SPAWN_TIME;
        
        projectiles = new ArrayList<Particle>();
        
        // Load background image.
        try {
            background = ImageIO.read(getClass().getResource("/resources/images/GZS_Background.png"));
        } catch(IOException io) {
            System.out.println("File not found!");
        }
        
        // Add key and mouse adapters.
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent k) {
                int keyCode = k.getKeyCode();
                if(keyCode == KeyEvent.VK_W) keys[0] = true;
                if(keyCode == KeyEvent.VK_D) keys[1] = true;
                if(keyCode == KeyEvent.VK_S) keys[2] = true;
                if(keyCode == KeyEvent.VK_A) keys[3] = true;
            }
            
            @Override
            public void keyReleased(KeyEvent k) {
                int keyCode = k.getKeyCode();
                if(keyCode == KeyEvent.VK_W) keys[0] = false;
                if(keyCode == KeyEvent.VK_D) keys[1] = false;
                if(keyCode == KeyEvent.VK_S) keys[2] = false;
                if(keyCode == KeyEvent.VK_A) keys[3] = false;
            }
        });
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent m) {
                if(m.getButton() == MouseEvent.BUTTON1) buttons[0] = true;
                if(m.getButton() == MouseEvent.BUTTON3) buttons[1] = true;
            }
            
            @Override
            public void mouseReleased(MouseEvent m) {
                if(m.getButton() == MouseEvent.BUTTON1) buttons[0] = false;
                if(m.getButton() == MouseEvent.BUTTON3) buttons[1] = false;
            }
        });
        
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent m) {
                mousePos.x = m.getX();
                mousePos.y = m.getY();
            }
            
            @Override
            public void mouseDragged(MouseEvent m) {
                mousePos.x = m.getX();
                mousePos.y = m.getY();
            }
        });
    }
    
    @Override
    public void addNotify() {
        super.addNotify();
        start();
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        
        AffineTransform save = g2d.getTransform();
        
        g2d.drawImage(background, 0, 0, null);
        
        try {
            for(Particle p : projectiles) {
                g2d.setTransform(p.getTransform());
                g2d.drawImage(p.getImage(), (int)p.x, (int)p.y, null);
            }
        } catch(RuntimeException ex) {
        }
        
        g2d.setPaint(Color.RED);
        g2d.setTransform(player.getTransform());
        g2d.drawImage(player.getImage(), (int)player.x, (int)player.y, null);
        
        try {
            if(!zombies.isEmpty()) {
                for(Zombie z : zombies) {
                    g2d.setTransform(z.getTransform());
                    z.getImage().draw(g2d);
                }
            }
        } catch(RuntimeException ex) {
        }
        
        g2d.setTransform(save);
        
        // Draw text showing the player's current coordinates.
        //g2d.setPaint(Color.WHITE);
        //g2d.drawString(("Player Position - X: " + player.getCenterX() + " Y: " + player.getCenterY()), 5, 15);
        //g2d.drawString(("Health: " + player.getHealth()), 5, 30);
        //g2d.drawString(("Weapon CD: " + player.getCooldown()), 5, 45);
        
        // Draw the health bar. Other bars will be added in future versions.
        g2d.setPaint(Color.BLACK);
        g2d.fillRect(10, 10, 204, 20); // Draw the black bar behind the health.
        if(player.getHealth() > 0) { // If the player is still alive.
            g2d.setPaint(Color.RED);
            g2d.fillRect(12, 12, player.getHealth(), 16); // Draw the health bar according to health left.
        }
        
        /* Show the current number of bullets in the bullet list. If you don't see any bullets on screen
        and the counter is higher than 0, it's not deleting some bullets properly. */
        //g2d.setPaint(Color.BLACK);
        //g2d.drawString(("Bullets In List: " + projectiles.size()), 10, 45);
        
        //g2d.setPaint(Color.RED);
        //g2d.drawLine((int)player.getCenterX(), (int)player.getCenterY(), mousePos.x, mousePos.y);
        
        // Draw crosshair. Used to visualize whether something is centered or not.
        //g2d.setPaint(Color.BLACK);
        //g2d.drawLine(0, (pHEIGHT / 2), pWIDTH, (pHEIGHT / 2));
        //g2d.drawLine((pWIDTH / 2), 0, (pWIDTH / 2), pHEIGHT);
    }
    
    public void update() {
        double centerX = player.getCenterX();
        double centerY = player.getCenterY();
        double pAngle = Math.atan2(centerY - mousePos.y, centerX - mousePos.x) - Math.PI / 2;
        player.rotate(pAngle);
        
        if(keys[0]) player.move(0);
        if(keys[1]) player.move(1);
        if(keys[2]) player.move(2);
        if(keys[3]) player.move(3);
        
        if(buttons[0]) {
            if(!player.isOnCooldown()) createParticle(pAngle);
            else player.decCooldown();
        } else player.decCooldown();
        
        // Update zombie positions.
        if(!zombies.isEmpty()) {
            for(Zombie z : zombies) {
                // Update the zombie's animation if necessary.
                z.getImage().update();
                
                // Update the zombie's movement vector.
                Vector2D v_ = new Vector2D((player.getCenterX() - z.getCenterX()), (player.getCenterY() - z.getCenterY()));
                Vector2D n_ = v_.normalize();
                if(v_.getLength() >= 5) {
                    z.x += n_.x;
                    z.y += n_.y;
                    z.getImage().move((int)z.x, (int)z.y);
                    double angle = Math.atan2((z.getCenterY() - player.getCenterY()), (z.getCenterX() - player.getCenterX())) - Math.PI / 2;
                    z.rotate(angle);
                }
            }
        }
        
        // Spawn zombies.
        if(spawn == 0) {
            createZombie();
            spawn = SPAWN_TIME;
        } else spawn--;
        
        // If the player is touching a zombie, remove health equal to the zombie's damage.
        if(!zombies.isEmpty()) {
            for(Zombie z : zombies) {
                if(player.intersects(z)) player.takeDamage(z.getDamage());
            }
        }
        
        // Handle the bullets. Update their positions. If any have reached their targets, delete them.
        if(!projectiles.isEmpty()) {
            for(int i = 0;i < projectiles.size();i++) {
                Particle p = projectiles.get(i);
                if((p.getCenterX() < 0)||(p.getCenterX() > pWIDTH)||(p.getCenterY() < 0)||(p.getCenterY() > pHEIGHT)) {
                    projectiles.remove(i);
                } else {
                    double dX = Math.sin(p.getFiringAngle());
                    double dY = Math.cos(p.getFiringAngle());
                    p.x += dX * 5;
                    p.y += dY * 5;
                    p.rotate(p.getPlayerAngle(), p.x, p.y);
                }
            }
        }
        
        // Check bullets for collisions with zombies.
        try {
            if((!projectiles.isEmpty())&&(!zombies.isEmpty())) {
                for(Particle p : projectiles) {
                    for(Zombie z : zombies) {
                        if(p.intersects(z)) {
                            z.takeDamage(p.getDamage());
                            if(z.isDead()) zombies.remove(z); 
                            projectiles.remove(p);
                        }
                    }
                }
            }
        } catch(RuntimeException ex) {}
    }
    
    private void createParticle(double playerAngle_) {
        double x_ = player.x + 28;
        double y_ = player.y - 8;
        double w_ = 2;
        double h_ = 10;
        
        Point target_ = new Point(mousePos.x, mousePos.y);
        Point pos_ = new Point((int)x_, (int)y_);
        
        double firingAngle_ = Math.atan2((target_.x - x_), (target_.y - y_));
        
        AffineTransform.getRotateInstance(playerAngle_, player.getCenterX(), player.getCenterY()).transform(pos_, pos_);
        x_ = pos_.x;
        y_ = pos_.y;
        
        String fileName_ = "/resources/images/GZS_Bullet.png";
        
        Particle p = new Particle(x_, y_, w_, h_, 10, target_, playerAngle_, firingAngle_, fileName_);
        p.rotate(playerAngle_, player.getCenterX(), player.getCenterY());
        projectiles.add(p);
    }
    
    private void createZombie() {
        double x_ = rand.nextInt(pWIDTH-40)+1;
        double y_ = rand.nextInt(pHEIGHT-40)+1;
        double w_ = 40;
        double h_ = 40;
        Rectangle2D.Double rect_ = new Rectangle2D.Double(x_, y_, w_, h_);
        Zombie z = new Zombie(rect_, 20, 1, "/resources/images/GZS_Zombie_2.png");
        zombies.add(z);
    }
    
    private void start() {
        if((animation == null)||(!running)) {
            animation = new Thread(this);
            animation.start();
        }
    }

    @Override
    public void run() {
        running = true;
        while(running) {
            update();
            repaint();
            
            try {
                Thread.sleep(SLEEP_TIME);
            } catch(InterruptedException ex) {}
        }
        System.exit(0);
    }
    
    public void stop() {
        running = false;
    }
}
