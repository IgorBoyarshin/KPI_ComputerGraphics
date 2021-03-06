package math;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import math.Vector2d;

import java.util.function.Function;

/**
 * Created by Igorek on 05-Mar-17 at 12:47 PM.
 */
public class Curve {

    // If == 0 => don't invert, otherwise invert axis
    private final double yHeight;

    private final Mode mode;

    private final double argStep;
    private final Vector2d argRange;

    private Function<Double, Double> xOfT;
    private Function<Double, Double> yOfT;

    private Function<Double, Double> yOfX;

    public Curve(Function<Double, Double> yOfX, Vector2d xRange, double xStep, double yHeight) {
        this.mode = Mode.CARTESIAN;
        this.argStep = xStep;
        this.argRange = xRange;
        this.yOfX = yOfX;

        this.xOfT = null;
        this.yOfT = null;

        this.yHeight = yHeight;
    }

    public Curve(Function<Double, Double> xOfT, Function<Double, Double> yOfT, Vector2d tRange, double tStep, double yHeight) {
        this.mode = Mode.PARAMETRIC;
        this.argStep = tStep;
        this.argRange = tRange;
        this.xOfT = xOfT;
        this.yOfT = yOfT;

        this.yOfX = null;

        this.yHeight = yHeight;
    }

    @SuppressWarnings("ConstantConditions")
    public void draw(GraphicsContext gc, Vector2d origin, double angle, Color color, double width) {
        gc.setStroke(color);
        gc.setLineWidth(width);

        final double ox = origin.x;
        final double oy = origin.y;
        origin.x = 0.0;
        origin.y = yHeight;

        final double startX;
        final double startY;
        switch (mode) {
            case PARAMETRIC:
                startX = xOfT.apply(argRange.x) + origin.x;
                // no invert: yOfT.apply(argRange.x) + origin.y
                // invert:    yHeight - (yOfT.apply(argRange.x) + origin.y)
                startY = yHeight - (yHeight == 0 ? (-1.0) : (+1.0)) * (yOfT.apply(argRange.x) + origin.y);
                break;
            case CARTESIAN:
            default:
                startX = argRange.x + origin.x;
                startY = yHeight - (yHeight == 0 ? (-1.0) : (+1.0)) * (yOfX.apply(argRange.x) + origin.y);
                break;
        }


        gc.beginPath();

        gc.translate(ox, yHeight - (yHeight == 0 ? (-1.0) : (+1.0)) * oy);
        gc.rotate(angle);

        gc.moveTo(startX, startY);

        double currentArg = argRange.x + argStep;
        while (currentArg <= argRange.y) {
            final double x;
            final double y;

            switch (mode) {
                case PARAMETRIC:
                    x = xOfT.apply(currentArg) + origin.x;
                    y = yHeight - (yHeight == 0 ? (-1.0) : (+1.0)) * (yOfT.apply(currentArg) + origin.y);
                    break;
                case CARTESIAN:
                default:
                    x = currentArg + origin.x;
                    y = yHeight - (yHeight == 0 ? (-1.0) : (+1.0)) * (yOfX.apply(currentArg) + origin.y);
                    break;
            }

            gc.lineTo(x, y);

            currentArg += argStep;
        }
        gc.closePath();

        gc.rotate(-angle);
        gc.translate(-ox, -yHeight + (yHeight == 0 ? (-1.0) : (+1.0)) * oy);

        gc.stroke();


    }

    public void setYOfX(Function<Double, Double> newFunc) {
        this.yOfX = newFunc;
    }

    public void setXOfT(Function<Double, Double> newFunc) {
        this.xOfT = newFunc;
    }

    public void setYOfT(Function<Double, Double> newFunc) {
        this.yOfT = newFunc;
    }

    public enum Mode {
        PARAMETRIC, CARTESIAN
    }
}
