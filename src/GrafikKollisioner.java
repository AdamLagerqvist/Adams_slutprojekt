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

/*
 * This is a class
 * Created 2021-03-23
 *
 * @author Magnus Silverdal
 */
public class GrafikKollisioner extends Canvas implements Runnable{

    private int width = 900;
    private int height = 900;

    private Thread thread;
    private int fps = 30;
    private boolean isRunning;

    private BufferStrategy bs;

    private Collidable earth;

    private Collection<Asteroid> asteroids = new ArrayList<>();

    private BufferedImage earthImg;
    private BufferedImage spaceImg;
    private BufferedImage asteroidImg;
    private BufferedImage sateliteImg;
    private Collection<Satelite> satelites = new ArrayList<>();
    private double sateliteVelosity = 0;
    private double diffMult = 100;

    public GrafikKollisioner() {
        satelites.add(new Satelite(Math.PI, 130));
        satelites.add(new Satelite(2 * Math.PI, 130));
        JFrame frame = new JFrame("A simple painting");
        this.setSize(width,height);
        frame.add(this);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener(new KL());
        frame.setVisible(true);

        isRunning = false;

        try {
            earthImg = ImageIO.read(new File("Earth.png"));
            spaceImg = ImageIO.read(new File("Space.png"));
            asteroidImg = ImageIO.read(new File("Asteroid.png"));
            sateliteImg = ImageIO.read(new File("Satellite.png"));
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
        g.drawImage(spaceImg,0,0,900,900,null);
        g.drawImage(earthImg, earth.hitBox.x, earth.hitBox.y, earth.hitBox.width, earth.hitBox.height,null);
        drawAsteroids(g, asteroids);
        drawSatelites(g, satelites);
        g.dispose();
        bs.show();
    }

    private void drawAsteroids(Graphics g, Collection<Asteroid> asteroids) {
        asteroids.stream().forEach(asteroid -> {
            g.drawImage(asteroidImg, asteroid.x(), asteroid.y(), asteroid.hitBox.width, asteroid.hitBox.height, null);
        });
    }

    private void drawSatelites(Graphics g, Collection<Satelite> satelites) {
        satelites.stream().forEach(satelite -> {
            g.drawImage(sateliteImg, satelite.x(), satelite.y(), satelite.hitBox.width, satelite.hitBox.height, null);
        });
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
                // switch satelite idel move direction
                // speed up satelites while holding the key
            }
            if (keyEvent.getKeyChar() == 'd') {
                // switch satelite idel move direction
                // speed up satelites while holding the key
            }
            if (keyEvent.getKeyChar() == 'w') {
                // make satelites move in a bigger circle around the earth (increase the radius)
            }
            if (keyEvent.getKeyChar() == 's') {
                // make satelites move in a smaller circle around the earth (decrease the radius)
            }
        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {
            if (keyEvent.getKeyChar() == 'a') {
                // slow down the satelite to idle speed but preserve idle direction
            }
            if (keyEvent.getKeyChar() == 'd') {
                // slow down the satelite to idle speed but preserve idle direction
            }
            if (keyEvent.getKeyChar() == 'w') {
                // stop changing the radius
            }
            if (keyEvent.getKeyChar() == 's') {
                // stop changing the radius
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
