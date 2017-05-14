package math;

/**
 * Created by Igorek on 18-Feb-17 at 7:01 PM.
 */
public class Vector4d {

    public double x;
    public double y;
    public double z;
    public double w;

    public Vector4d() {
        this(0.0);
    }

    public Vector4d(double arg) {
        this.x = arg;
        this.y = arg;
        this.z = arg;
        this.w = arg;
    }

    public Vector4d(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public double dot(Vector4d v) {
        return x * v.x + y * v.y + z * v.z + w * v.w;
    }

    public Vector4d cross(Vector4d v) {
        final Vector4d v1 = this;
        final Vector4d v2 = v;

        return new Vector4d(
                v1.y * v2.z - v1.z * v2.y,
                v1.z * v2.x - v1.x * v2.z,
                v1.x * v2.y - v1.y * v2.x,
                0.0
        );
    }

    public double getLength() {
        return Math.sqrt(x * x + y * y + z * z + w * w);
    }

    @Override
    public String toString() {
        return "(x;y;z;w) = (" + x + "; " + y + "; " + z + "; " + w + ")";
    }
}
