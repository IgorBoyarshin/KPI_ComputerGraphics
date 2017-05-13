package lab05;

import math.Curve;
import math.Vector2d;

/**
 * Created by Igorek on 05-Mar-17 at 2:07 PM.
 */
public class Heart extends Curve {

    private double R0;
    private double R1;
    private double R2;
    private double R3;
    private double R4;

    public Heart(double R0, double R1, double R2, double R3, double R4, double canvasSize) {
        super(
                t -> funcXOfT(t, R0),
                t -> funcYOfT(t, R1, R2, R3, R4),
                new Vector2d(0.0, 2.0 * Math.PI),
                0.1,
                canvasSize
        );

        this.R0 = R0;
        this.R1 = R1;
        this.R2 = R2;
        this.R3 = R3;
        this.R4 = R4;
    }

    private static double funcXOfT(double t, double R0) {
        final double sin = Math.sin(t);
        return R0 * sin * sin * sin;
    }

    private static double funcYOfT(double t, double R1, double R2, double R3, double R4) {
        return (R1 * Math.cos(1 * t) - R2 * Math.cos(2 * t) - R3 * Math.cos(3 * t) - R4 * Math.cos(4 * t));
    }

    private void updateFunctionXOfT() {
        this.setXOfT(t -> funcXOfT(t, R0));
    }

    private void updateFunctionYOfT() {
        this.setYOfT(t -> funcYOfT(t, R1, R2, R3, R4));
    }

    public void setRs(double newR0, double newR1, double newR2, double newR3, double newR4) {
        this.R0 = newR0;
        this.R1 = newR1;
        this.R2 = newR2;
        this.R3 = newR3;
        this.R4 = newR4;

        this.updateFunctionXOfT();
        this.updateFunctionYOfT();
    }

    public void setR0(double newR) {
        this.R0 = newR;
        this.updateFunctionXOfT();
    }

    public void setR1(double newR) {
        this.R1 = newR;
        this.updateFunctionYOfT();
    }

    public void setR2(double newR) {
        this.R2 = newR;
        this.updateFunctionYOfT();
    }

    public void setR3(double newR) {
        this.R3 = newR;
        this.updateFunctionYOfT();
    }

    public void setR4(double newR) {
        this.R4 = newR;
        this.updateFunctionYOfT();
    }
}
