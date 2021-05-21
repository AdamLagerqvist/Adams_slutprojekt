import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/*
 * This is a class
 * Created 2021-03-23
 *
 * @author Bagnus Silverdal
 */
public class Basteroids extends Canvas implements Runnable{

    private int width = 900;
    private int height = 900;
    private int score = 0;

    private Thread thread;
    private int fps = 30;
    private boolean isRunning, isPlaying;

    private BufferStrategy bs;

    private Collidable earth;
    private Collection<Asteroid> asteroids = new ArrayList<>();
    private Collection<Satelite> satelites = new ArrayList<>();
    Collection<Asteroid> asteroidsToRemove = new ArrayList<>();

    private BufferedImage sateliteImg, earthImg, spaceImg, asteroidImg;

    private int sateliteVelosity = 3;
    private double diffMult = 99.5;
    private int sateliteTargetVelosity = 3;
    private int sateliteVelosity2;

    public Basteroids() {
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
        isPlaying = false;

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
        if(isPlaying) {
            satelitesMove();
            spawnAsteroids();
            asteroids.stream().forEach(asteroid -> {
                asteroid.move();
                if (checkEarthCollision(asteroid)) {
                    asteroidsToRemove.add(asteroid);
                    loose();
                }
                Optional<Satelite> collidingSatelite = checkCollisions(asteroid, satelites);
                if (collidingSatelite.isPresent()) {
                    // Handle satelite collision.
                    // Add damage to satelite
                    // Destroy asteroid.
                    asteroidsToRemove.add(asteroid);
                    score++;
                }
            });
            asteroids.removeAll(asteroidsToRemove);
        }
        if (!isPlaying){

        }
    }
    private ArrayList<Integer> getHighscores(){
        ArrayList<Integer> Highscores = new ArrayList<>();
        try {
            File Highscore = new File("Highscore.txt");
            Scanner fileReader = new Scanner(Highscore);
            while (fileReader.hasNextLine()) {
                Highscores.add(fileReader.nextInt());
            }
            fileReader.close();
        }catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return Highscores;
    }
    private void loose() {
            ArrayList<Integer> scores = getHighscores();
            scores.add(score);
            Collections.sort(scores);
        try {
            FileWriter myWriter = new FileWriter("Highscore.txt");
            for (int i = scores.size()-1;i > 0;i--){
                if(1 != i) {
                    myWriter.write(scores.get(i) + "\n");
                }else {
                    myWriter.write(scores.get(i) + "");
                }
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        score = 0;
        diffMult = 99.5;
        asteroidsToRemove.addAll(asteroids);
        isPlaying = false;
    }

    private void satelitesMove() {
        if (sateliteVelosity > sateliteTargetVelosity){
            sateliteVelosity -= 1;
        }else if(sateliteVelosity < sateliteTargetVelosity) {
            sateliteVelosity += 1;
        }
        System.out.println(sateliteVelosity);
        satelites.stream().forEach(Satelite -> {
                Satelite.setDirection(Satelite.getDirection() + Math.toRadians(sateliteVelosity/10.0));
                Satelite.setRadius(Satelite.getRadius() + sateliteVelosity2);
                Satelite.move();
    });
    }

    private void spawnAsteroids() {
        if (Math.random() * 100 >= diffMult){
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
        if (isPlaying){
        g.drawImage(spaceImg,0,0,900,900,null);
        g.drawImage(earthImg, earth.hitBox.x, earth.hitBox.y, earth.hitBox.width, earth.hitBox.height,null);
        drawAsteroids(g, asteroids);
        drawSatelites(g, satelites);
        }
        if (!isPlaying) {
            g.setColor(Color.YELLOW);
            g.drawImage(spaceImg, 0, 0, 900, 900, null);
            g.setFont(new Font("Trebuchet MS", Font.BOLD, 100));
            g.drawString("Basteroids", 220, 110);
            g.setFont(new Font("Trebuchet MS", Font.BOLD, 70));
            g.drawString("press space to play", 150, 850);
            g.setColor(new Color(255, 81, 0));
            g.drawString("Highscores", 50, 180);
            g.setFont(new Font("Trebuchet MS", Font.BOLD, 50));
            ArrayList<Integer> Scores = getHighscores();
            for (int i = 0; i < Scores.size(); i++) {
                g.drawString(Scores.get(i) + "", 50, (i * 50) + 240);
            }
        }
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
        Basteroids painting = new Basteroids();
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
            if (keyEvent.getKeyChar() == ' '){
                isPlaying = true;
            }
        }

        @Override
        public void keyPressed(KeyEvent keyEvent) {
            if (keyEvent.getKeyChar() == 'a') {
                // switch satelite idel move direction
                // speed up satelites while holding the key
                if (sateliteTargetVelosity == 3 || sateliteTargetVelosity == -3) {
                    sateliteTargetVelosity = 10;
                }
            }
            if (keyEvent.getKeyChar() == 'd') {
                // switch satelite idel move direction
                // speed up satelites while holding the key
                if (sateliteTargetVelosity == 3 || sateliteTargetVelosity == -3) {
                    sateliteTargetVelosity = -10;
                }
            }
            if (keyEvent.getKeyChar() == 'w') {
                // make satelites move in a bigger circle around the earth (increase the radius)
                sateliteVelosity2 = 2;
            }
            if (keyEvent.getKeyChar() == 's') {
                // make satelites move in a smaller circle around the earth (decrease the radius)
                sateliteVelosity2 = -2;
            }
        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {
            if (keyEvent.getKeyChar() == 'a') {
                // slow down the satelite to idle speed but preserve idle direction
                sateliteTargetVelosity = 3;
            }
            if (keyEvent.getKeyChar() == 'd') {
                // slow down the satelite to idle speed but preserve idle direction
                sateliteTargetVelosity = -3;
            }
            if (keyEvent.getKeyChar() == 'w') {
                // stop changing the radius
                sateliteVelosity2 = 0;
            }
            if (keyEvent.getKeyChar() == 's') {
                // stop changing the radius
                sateliteVelosity2 = 0;
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
