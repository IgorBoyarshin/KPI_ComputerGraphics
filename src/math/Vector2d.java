package math;

/**
 * Created by Igorek on 18-Feb-17 at 7:01 PM.
 */
public class Vector2d {
    public double x;
    public double y;

    public Vector2d(double arg) {
        this.x = arg;
        this.y = arg;
    }

    public Vector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getLength() {
        return Math.sqrt(x * x + y * y);
    }
}
