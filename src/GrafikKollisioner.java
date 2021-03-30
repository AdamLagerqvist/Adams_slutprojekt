import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 * This is a class
 * Created 2021-03-23
 *
 * @author Magnus Silverdal
 */
public class GrafikKollisioner extends Canvas implements Runnable{

    private int width = 900;
    private int height = 900;

    private Thread thread;
    int fps = 30;
    private boolean isRunning;

    private BufferStrategy bs;

    private Collidable earth;

    private Collection<Asteroid> asteroids = new ArrayList<>();

    private BufferedImage Earthimg;
    private BufferedImage Space;
    private BufferedImage Asteroidimg;
    private Collection<Satelite> satelites = new ArrayList<>();
    private double diffMult = 100;

    public GrafikKollisioner() {
        JFrame frame = new JFrame("A simple painting");
        this.setSize(width,height);
        frame.add(this);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener(new KL());
        frame.setVisible(true);

        isRunning = false;

        try {
            Earthimg = ImageIO.read(new File("Earth.png"));
            Space = ImageIO.read(new File("Space.png"));
            Asteroidimg = ImageIO.read(new File("Asteroid.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        earth = new Collidable(new Rectangle(375,375,150,150));

    }

    public void update() {
        spawnAsteroids();
        Collection<Asteroid> asteroidsToRemove = new ArrayList<>();
        asteroids.stream().forEach(asteroid -> {
            asteroid.move();
            if (checkEarthCollision(asteroid)) {
                asteroidsToRemove.add(asteroid);
            }
            Optional<Satelite> collidingSatelite = checkCollisions(asteroid, satelites);
            if (collidingSatelite.isPresent()) {
                // Handle satelite collision.
                // Add damage to satelite
                // Destroy asteroid.
                asteroidsToRemove.add(asteroid);
            }
        });
        asteroids.removeAll(asteroidsToRemove);
    }

    private void spawnAsteroids() {
        if (Math.random() * diffMult >= 99.5){
            asteroids.add(new Asteroid(Asteroid.randomDirection()));
        }
    }

    private boolean checkEarthCollision(Asteroid asteroid) {
        return checkCollisions(asteroid, Collections.singleton(earth)).isPresent();
    }

    private <T extends Collidable> Optional<T> checkCollisions(Asteroid asteroid, Collection<T> collidables) {
        return collidables.stream().filter(collidable -> collidable.intersects(asteroid)).findAny();
    }

    public void draw() {
        bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        update();
        g.drawImage(Space,0,0,900,900,null);
        g.drawImage(Earthimg, earth.hitBox.x, earth.hitBox.y, earth.hitBox.width, earth.hitBox.height,null);
        drawAsteroids(g, asteroids);
        g.dispose();
        bs.show();
    }

    private void drawAsteroids(Graphics g, Collection<Asteroid> asteroids) {
        asteroids.stream().forEach(asteroid -> {
            g.drawImage(Asteroidimg, asteroid.x(), asteroid.y(), asteroid.hitBox.width, asteroid.hitBox.height, null);
        });
    }

    private void drawTree(Graphics g, int x, int y) {
        g.setColor(new Color(0,128,0));
        int[] xs = {0+x, 10+x, 20+x};
        int[] ys = {30+y,0+y,30+y};
        g.fillPolygon(xs,ys,3);
        g.setColor(new Color(200,128,30));
        g.fillRect(7+x,30+y,6,10);
    }

    private void drawHouse(Graphics g, int x, int y) {
        g.setColor(new Color(0xAA1111));
        g.fillRect(x, y, 50, 40);
        g.setColor(new Color(0x444444));
        int[] xcoords = {x-5, x + 25, x + 55};
        int[] ycoords = {y, y - 25, y};
        g.fillPolygon(xcoords, ycoords, 3);
        g.fillRect(x+4,y+5,15,35);
        g.drawRect(x+25,y+10,20,20);
        g.setColor(new Color(0xFFA3DCFA));
        g.fillRect(x+26,y+11,18,18);
    }

    public static void main(String[] args) {
        GrafikKollisioner painting = new GrafikKollisioner();
        painting.start();
    }

    public synchronized void start() {
        thread = new Thread(this);
        isRunning = true;
        thread.start();
    }

    public synchronized void stop() {
        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        double deltaT = 1000.0/fps;
        long lastTime = System.currentTimeMillis();

        while (isRunning) {
            long now = System.currentTimeMillis();
            if (now-lastTime > deltaT) {
                update();
                draw();
                lastTime = now;
            }

        }
        stop();
    }

    private class KL implements KeyListener {
        @Override
        public void keyTyped(KeyEvent keyEvent) {

        }

        @Override
        public void keyPressed(KeyEvent keyEvent) {
            if (keyEvent.getKeyChar() == 'a') {

            }
            if (keyEvent.getKeyChar() == 'd') {

            }
            if (keyEvent.getKeyChar() == 'w') {

            }
            if (keyEvent.getKeyChar() == 's') {

            }
        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {
            if (keyEvent.getKeyChar() == 'a') {

            }
            if (keyEvent.getKeyChar() == 'd') {

            }
            if (keyEvent.getKeyChar() == 'w') {

            }
            if (keyEvent.getKeyChar() == 's') {

            }
        }
    }

    private class ML implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent mouseEvent) {

        }

        @Override
        public void mousePressed(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseReleased(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseEntered(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseExited(MouseEvent mouseEvent) {

        }
    }

    private class MML implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseMoved(MouseEvent mouseEvent) {

        }
    }
}
