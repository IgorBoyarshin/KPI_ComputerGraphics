package math;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
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

    private final Function<Double, Double> xOfT;
    private final Function<Double, Double> yOfT;

    private final Function<Double, Double> yOfX;


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
    public void draw(GraphicsContext gc, Vector2d origin, Color color) {
        gc.setStroke(color);

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
        gc.stroke();
    }

    public enum Mode {
        PARAMETRIC, CARTESIAN
    }
}
