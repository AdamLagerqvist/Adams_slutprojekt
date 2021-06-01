import java.awt.*;

public class Satelite extends Collidable {

    private int radius;
    private double direction;

    /* Konstruktorn för en sattelit */
    public Satelite(double direction, int radius) {
        super(new Rectangle((int)(radius * Math.sin(direction) + 410), (int)(radius * Math.cos(direction) + 410), 80 , 80));
        this.direction = direction;
        this.radius = radius;
    }

    /* Flyttar sateliten till nya kordinater */
    public void move(){
        moveTo((int) (radius * Math.sin(direction) + 410),(int) (radius * Math.cos(direction) + 410));
    }

    public double getDirection(){
        return direction;
    }

    public void setDirection(double d){
        direction = d;
    }

    public void setRadius(int i){
        radius = i;
    }

    public int getRadius(){
        return radius;
    }
}
