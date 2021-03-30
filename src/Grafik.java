import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Grafik extends Canvas implements Runnable{
    private int width = 800;
    private int height = 600;

    private Thread thread;
    int fps = 30;
    private boolean isRunning;

    private BufferStrategy bs;

    private int houseX, houseY;
    private int houseVX, houseVY;

    private int treeX, treeY, treeVX, treeVY;

    private int marioX, marioY, marioVX, marioVY;

    private BufferedImage mario;


    public Grafik() {
        JFrame frame = new JFrame("A simple painting");
        this.setSize(width,height);
        frame.add(this);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener(new KL());
        frame.setVisible(true);

        isRunning = false;

        try {
            mario = ImageIO.read(new File("supermario2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        houseX = 300;
        houseY = 150;
        houseVX = 1;
        houseVY = 0;

        treeX = 200;
        treeY = 200;
        treeVX = 0;
        treeVY = 0;

        marioX = 0;
        marioY = 0;
        marioVX = 2;
        marioVY = 2;
    }

    public void update() {
        houseX += houseVX;
        if (houseX > width){
            houseVX = -1;
        }
        if (houseX < 0 ) {
            houseVX = 1;
        }
        marioX += marioVX;
        marioY += marioVY;
        if (marioX < 0 || marioX > width-80)
            marioVX = -marioVX;
        if (marioY < 0 || marioY > height-80)
            marioVY = -marioVY;
        treeX += treeVX;
        treeY += treeVY;
    }

    public void draw() {
        bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        update();
        g.setColor(Color.WHITE);
        g.fillRect(0,0,width,height);
        drawHouse(g, houseX,houseY);
        drawTree(g, treeX,treeY);
        drawTree(g, 100,200);
        drawTree(g, 110,200);
        drawTree(g, 120,200);
        drawTree(g, 130,200);
        drawTree(g, 140,200);
        drawTree(g, 150,200);
        g.drawImage(mario,marioX,marioY,48,48,null);
        g.dispose();
        bs.show();
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
        g.fillRect(x, y-40, 50, 40);
        g.setColor(new Color(0x444444));
        int[] xcoords = {x-5, x + 25, x + 55};
        int[] ycoords = {y-40, y - 65, y-40};
        g.fillPolygon(xcoords, ycoords, 3);
        g.fillRect(x+4,y-35,15,35);
        g.drawRect(x+25,y-30,20,20);
        g.setColor(new Color(0xFFA3DCFA));
        g.fillRect(x+26,y-29,18,18);
    }

    public static void main(String[] args) {
        Grafik painting = new Grafik();
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
                treeVX = -5;
            }
            if (keyEvent.getKeyChar() == 'd') {
                treeVX = 5;
            }
            if (keyEvent.getKeyChar() == 'w') {
                treeVY = -5;
            }
            if (keyEvent.getKeyChar() == 's') {
                treeVY = 5;
            }
        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {
            if (keyEvent.getKeyChar() == 'a') {
                treeVX = 0;
            }
            if (keyEvent.getKeyChar() == 'd') {
                treeVX = 0;
            }
            if (keyEvent.getKeyChar() == 'w') {
                treeVY = 0;
            }
            if (keyEvent.getKeyChar() == 's') {
                treeVY = 0;
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


