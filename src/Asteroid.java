import java.awt.*;

public class Asteroid extends Collidable {
    private static final int initialRadius = 655;

    private double velocity = 1;
    private double radius;
    private double direction;

    static public double randomDirection() {
        return Math.random() * (2 * Math.PI);
    }
    public Asteroid(double direction) {
        super(new Rectangle((int)(initialRadius * Math.sin(direction) + 425), (int)(initialRadius * Math.cos(direction) + 425), 50, 50));
        radius = initialRadius;
        this.direction = direction;
    }

    public void move(){
        radius = radius - velocity;
        int newX = (int) (radius * Math.sin(direction) + 425);
        int newY = (int) (radius * Math.cos(direction) + 425);
        moveTo(newX, newY);
    }
}
