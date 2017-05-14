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

    public double dot(Vector3d v) {
        return x * v.x + y * v.y + z * v.z;
    }

    public Vector3d cross(Vector3d v) {
        final Vector3d v1 = this;
        final Vector3d v2 = v;

        return new Vector3d(
                v1.y * v2.z - v1.z * v2.y,
                v1.z * v2.x - v1.x * v2.z,
                v1.x * v2.y - v1.y * v2.x
        );
    }

    @Override
    public String toString() {
        return "(x;y;z) = (" + x + "; " + y + "; " + z + ")";
    }

    public double getLength() {
        return Math.sqrt(x * x + y * y + z * z);
    }
}
