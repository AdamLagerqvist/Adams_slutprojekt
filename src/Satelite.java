import java.awt.*;

public class Satelite extends Collidable {

    private int radius;
    private double direction;

    public Satelite(double direction, int radius) {
        super(new Rectangle((int)(radius * Math.sin(direction) + 410), (int)(radius * Math.cos(direction) + 410), 80 , 80));
        this.direction = direction;
        this.radius = radius;
    }
}
