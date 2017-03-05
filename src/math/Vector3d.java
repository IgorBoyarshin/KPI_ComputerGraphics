package math;

/**
 * Created by Igorek on 18-Feb-17 at 7:01 PM.
 */
public class Vector3d {

    public double x;
    public double y;
    public double z;

    public Vector3d(double arg) {
        this.x = arg;
        this.y = arg;
        this.z = arg;
    }

    public Vector3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getLength() {
        return Math.sqrt(x * x + y * y + z * z);
    }
}
