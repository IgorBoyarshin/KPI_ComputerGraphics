package math;

import static java.lang.Math.*;

/**
 * Created by Igor on 01-May-15.
 */
public class Matrix4d {
    private final int DIMENTION = 4;
    private final int SIZE = DIMENTION * DIMENTION;

    // Uses column-major ordering
    public double[] matrix = new double[SIZE];

    public Matrix4d() {
        for (int i = 0; i < SIZE; i++) {
            matrix[i] = 0.0;
        }
    }

    public Matrix4d(double diagonal) {
        for (int i = 0; i < SIZE; i++) {
            matrix[i] = 0.0;
        }

        matrix[0 + 0 * 4] = diagonal;
        matrix[1 + 1 * 4] = diagonal;
        matrix[2 + 2 * 4] = diagonal;
        matrix[3 + 3 * 4] = diagonal;
    }

    public static Matrix4d orthographic(double left, double right, double bottom, double top, double near, double far) {
        Matrix4d result = new Matrix4d(1.0);

        result.matrix[0 + 0 * 4] = 2.0 / (right - left);

        result.matrix[1 + 1 * 4] = 2.0 / (top - bottom);

        result.matrix[2 + 2 * 4] = 2.0 / (near - far);

        result.matrix[0 + 3 * 4] = (left + right) / (left - right);
        result.matrix[1 + 3 * 4] = (bottom + top) / (bottom - top);
        result.matrix[2 + 3 * 4] = (far + near) / (far - near);

        return result;
    }

    public static Matrix4d perspective(final double width, final double height,
                                       double fFovDeg, double fzNear, double fzFar) {
        Matrix4d result = new Matrix4d(1.0);

        final double fFovRad = Math.toRadians(fFovDeg);
        final double fFrustumScale = 1.0 / Math.tan(fFovRad / 2.0);

        result.matrix[0] = fFrustumScale / (width / height);
        result.matrix[5] = fFrustumScale;
        result.matrix[10] = (fzFar + fzNear) / (fzNear - fzFar);
        result.matrix[14] = (2 * fzFar * fzNear) / (fzNear - fzFar);
        result.matrix[11] = -1.0;

        return result;
    }

    public static Matrix4d identity() {
        return new Matrix4d(1.0);
    }

//    public Vector4f multiply(float x, float y, float z, float w) {
//        final float xx = matrix[0 + 0 * 4] * x + matrix[0 + 1 * 4] * y
//                + matrix[0 + 2 * 4] * z + matrix[0 + 3 * 4] * w;
//        final float yy = matrix[1 + 0 * 4] * x + matrix[1 + 1 * 4] * y
//                + matrix[1 + 2 * 4] * z + matrix[1 + 3 * 4] * w;
//        final float zz = matrix[2 + 0 * 4] * x + matrix[2 + 1 * 4] * y
//                + matrix[2 + 2 * 4] * z + matrix[2 + 3 * 4] * w;
//        final float ww = matrix[3 + 0 * 4] * x + matrix[3 + 1 * 4] * y
//                + matrix[3 + 2 * 4] * z + matrix[3 + 3 * 4] * w;
//
//        return new Vector4f(xx, yy, zz, ww);
//    }
//
    public Vector4d multiply(Vector4d vector) {
        final double x = matrix[0 + 0 * 4] * vector.x + matrix[0 + 1 * 4] * vector.y
                + matrix[0 + 2 * 4] * vector.z + matrix[0 + 3 * 4] * vector.w;
        final double y = matrix[1 + 0 * 4] * vector.x + matrix[1 + 1 * 4] * vector.y
                + matrix[1 + 2 * 4] * vector.z + matrix[1 + 3 * 4] * vector.w;
        final double z = matrix[2 + 0 * 4] * vector.x + matrix[2 + 1 * 4] * vector.y
                + matrix[2 + 2 * 4] * vector.z + matrix[2 + 3 * 4] * vector.w;
        final double w = matrix[3 + 0 * 4] * vector.x + matrix[3 + 1 * 4] * vector.y
                + matrix[3 + 2 * 4] * vector.z + matrix[3 + 3 * 4] * vector.w;

        return new Vector4d(x, y, z, w);
    }

    /**
     * Does not alter this matrix.
     *
     * @return A new matrix that is the result of multiplication of this matrix over the argument.
     */
    public Matrix4d multiply(Matrix4d otherMatrix) {
        Matrix4d result = new Matrix4d();

        for (int row = 0; row < DIMENTION; row++) {
            for (int column = 0; column < DIMENTION; column++) {
                double element = 0.0;
                for (int index = 0; index < DIMENTION; index++) {
                    element += this.matrix[row + index * DIMENTION] * otherMatrix.matrix[index + column * DIMENTION];
                }
                result.matrix[row + column * DIMENTION] = element;
            }
        }

        return result;
    }

    /**
     * Does not alter this matrix.
     */
    public Matrix4d scale(Vector3d vector) {
        return this.multiply(scaling(vector));
    }

    public static Matrix4d scaling(Vector3d vector) {
        Matrix4d result = identity();

        result.matrix[0 + 0 * 4] = vector.x;
        result.matrix[1 + 1 * 4] = vector.y;
        result.matrix[2 + 2 * 4] = vector.z;

        return result;
    }

    /**
     * Does not alter this matrix.
     */
    public Matrix4d translate(Vector3d vector) {
        return this.multiply(translation(vector));
    }

    public static Matrix4d translation(Vector3d vector) {
        Matrix4d result = identity();

        result.matrix[0 + 3 * 4] = vector.x;
        result.matrix[1 + 3 * 4] = vector.y;
        result.matrix[2 + 3 * 4] = vector.z;

        return result;
    }

    /**
     * Does not alter this matrix.
     */
    public Matrix4d rotate(double angle, double x, double y, double z) {
        return this.multiply(rotation(angle, x, y, z));
    }

    /**
     * Angle in Degrees
     */
    public static Matrix4d rotation(double angle, double x, double y, double z) {
        Matrix4d result = identity();
        double r = toRadians(angle);
        double cos = cos(r);
        double sin = sin(r);
        double omc = 1.0 - cos;

        result.matrix[0 + 0 * 4] = x * omc + cos;
        result.matrix[1 + 0 * 4] = y * x * omc + z * sin;
        result.matrix[2 + 0 * 4] = x * z * omc - y * sin;

        result.matrix[0 + 1 * 4] = x * y * omc - z * sin;
        result.matrix[1 + 1 * 4] = y * omc + cos;
        result.matrix[2 + 1 * 4] = y * z * omc + x * sin;

        result.matrix[0 + 2 * 4] = x * z * omc + y * sin;
        result.matrix[1 + 2 * 4] = y * z * omc - x * sin;
        result.matrix[2 + 2 * 4] = z * omc + cos;

        return result;
    }

    /**
     * Does not alter this matrix.
     */
    public Matrix4d rotateAboutAxis(double angle, Vector3d axis) {
        return this.multiply(rotationAboutAxis(angle, axis));
    }

    /**
     * Angle in Degrees
     */
    public static Matrix4d rotationAboutAxis(double angle, Vector3d axis) {
        Matrix4d result = identity();

        final double r = toRadians(angle);
        final double argument = r / 2.0;
        final double sin =  sin(argument);
        final double q0 = cos(argument);
        final double q1 = sin * axis.x;
        final double q2 = sin * axis.y;
        final double q3 = sin * axis.z;
        final double q02 = q0 * q0;
        final double q12 = q1 * q1;
        final double q22 = q2 * q2;
        final double q32 = q3 * q3;

        result.matrix[0 + 0 * 4] = q02 + q12 - q22 - q32;
        result.matrix[1 + 0 * 4] = 2.0 * (q2 * q1 + q0 * q3);
        result.matrix[2 + 0 * 4] = 2.0 * (q3 * q1 - q0 * q2);

        result.matrix[0 + 1 * 4] = 2.0 * (q1 * q2 - q0 * q3);
        result.matrix[1 + 1 * 4] = q02 - q12 + q22 - q32;
        result.matrix[2 + 1 * 4] = 2.0 * (q3 * q2 + q0 * q1);

        result.matrix[0 + 2 * 4] = 2.0 * (q1 * q3 + q0 * q2);
        result.matrix[1 + 2 * 4] = 2.0 * (q2 * q3 - q0 * q1);
        result.matrix[2 + 2 * 4] = q02 - q12 - q22 + q32;

        return result;
    }
}
