import java.awt.*;

public class Collidable {

    protected Rectangle hitBox;
    protected int radius;
    protected Point center;

    public Collidable(Rectangle box) {
        this.hitBox = box;
        radius = box.width/2;
        center = new Point(this.hitBox.x + radius, this.hitBox.y + radius);
    }

    public int x(){
        return hitBox.x;
    }

    public int y(){
        return hitBox.y;
    }

    public int width(){
        return hitBox.width;
    }

    public int height(){
        return hitBox.height;
    }

    public boolean intersects(Collidable other){
        double minSquareDistance = Math.pow(this.radius + other.radius, 2);
        double squareDistance = Math.pow(this.center.x - other.center.x, 2) + Math.pow(this.center.y - other.center.y, 2);
        return (minSquareDistance >= squareDistance);
    }

    public void moveTo(int x, int y) {
        hitBox.x = x;
        hitBox.y = y;
        center.x = x + radius;
        center.y = y + radius;
    }
}
