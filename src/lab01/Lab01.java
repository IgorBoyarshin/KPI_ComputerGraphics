package lab01;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import math.Vector2d;

import java.util.function.DoubleSupplier;
import java.util.function.Function;

/**
 * Created by Igorek on 18-Feb-17 at 6:40 PM.
 */
public class Lab01 extends Application {

    private Stage window;
    private Canvas canvas;

    private final double canvasSize = 500.0;

    @Override
    public void start(Stage primaryStage) {
        // For easy reference
        this.window = primaryStage;

        window.setTitle("Lab01 by Igor Boyarshin && Anka Doroshenko");

        // Creating and setting the view
        Group root = new Group();
        this.canvas = new Canvas(canvasSize, canvasSize);
        root.getChildren().add(canvas);
        window.setScene(new Scene(root));

        // What to do when the user closes the window
        window.setOnCloseRequest((event) -> window.close());

        // Main loop
        final long startNanoTime = System.nanoTime();
        new AnimationTimer() {
            // Gets called 60 times per second
            public void handle(long currentNanoTime) {
                double secondsSinceStart = (currentNanoTime - startNanoTime) / 1500000000.0;

                draw(canvas.getGraphicsContext2D(), secondsSinceStart);
            }
        }.start();

        // Create, pack and display the window
        window.show();
    }

    private Vector2d getCoordinates(double time) {
        return new Vector2d(Math.cos(time), Math.sin(time));
    }

    private Vector2d getCoordinates2(double time) {
        final double c = Math.cos(time);
        final double s = Math.sin(time);

        return new Vector2d(c * c * c, s * s * s);
    }

    private double distance(Vector2d v1, Vector2d v2) {
        final double x = v1.x - v2.x;
        final double y = v1.y - v2.y;

        return Math.sqrt(x * x + y * y);
    }

    private Color getColor(Vector2d pos) {
        // Something in range [0;1] and based on distance from the center
        final double d = distance(pos, new Vector2d(canvasSize / 2.0, canvasSize / 2.0)) / (canvasSize * Math.sqrt(2) / 2.5);

        return new Color(0.9, d, d / 2.0, 1.0);
    }

    private void draw(GraphicsContext gc, double time) {

        // Parameters
        final double unitSize = canvasSize / 10.0;
        final int amountOfElementsInOrnament = 16;
        final int amountOfElementsInVisualEffect = 20;
        final double ornamentRadius = canvasSize / 3.0;
        final double visualEffectRadius = unitSize + unitSize / 2.0 * Math.sin(2.0 * time + Math.PI);

        // Background
        gc.setFill(Color.MAROON);
        gc.fillRect(0, 0, canvasSize, canvasSize);

        // Element
        final Vector2d elementPosition = new Vector2d(80.0, 80.0);
        drawElement(gc, elementPosition, 3.0 * unitSize, Color.ORANGE);

        // Ornament
        for (double i = 1.0; i <= 8.0; i *= 2.0) {
            final double depth = i;
            final Vector2d ornamentPosition = new Vector2d(300.0, 190.0);
            drawOrnament(gc, ornamentPosition, unitSize * 0.8 / depth, amountOfElementsInOrnament, null,
                    this::getCoordinates2, () -> 1.0 * ornamentRadius / depth, () -> 0.0);
        }

        // Visual effect
        final Vector2d visualEffectPosition = new Vector2d(150.0, 330.0);
        drawOrnament(gc, visualEffectPosition, unitSize, amountOfElementsInVisualEffect, Color.WHITE,
                this::getCoordinates, () -> visualEffectRadius, () -> 2.0 * time);
    }

    private void drawOrnament(GraphicsContext gc, Vector2d center, double unitSize, int amountOfObjects, Color color,
                              Function<Double, Vector2d> trajectory, DoubleSupplier radius,
                              DoubleSupplier rotation) {

        final double ornamentRadius = radius.getAsDouble();
        final double angleStep = 2 * Math.PI / amountOfObjects;

        // Each object in this ornament has a different angle offset
        for (double angle = 0.0; angle < 2 * Math.PI; angle += angleStep) {
            Vector2d objectShift = trajectory.apply(angle + rotation.getAsDouble()); // the result is in [-1; +1]
            objectShift.x *= ornamentRadius;
            objectShift.y *= ornamentRadius;

            Vector2d objectPosition = new Vector2d(center.x + objectShift.x, center.y + objectShift.y);
            drawElement(gc, objectPosition, unitSize, color == null ? getColor(objectPosition) : color);
        }
    }

    // Draws a single object
    private void drawElement(GraphicsContext gc, Vector2d center, double size, Color color) {
        gc.setStroke(color == null ? Color.RED : color);
        gc.setLineWidth(1.5);

        // **** Circle ****
        final double circleRadius = size * 1 / (2.0 * Math.sqrt(2));
        gc.strokeOval(center.x - circleRadius, center.y - circleRadius, 2.0 * circleRadius, 2.0 * circleRadius);

        // **** Box ****
        Vector2d boxUp = new Vector2d(center.x, center.y - 0.5 * size);
        Vector2d boxDown = new Vector2d(center.x, center.y + 0.5 * size);
        Vector2d boxLeft = new Vector2d(center.x - 0.5 * size, center.y);
        Vector2d boxRight = new Vector2d(center.x + 0.5 * size, center.y);

        gc.strokeLine(boxUp.x, boxUp.y, boxRight.x, boxRight.y);
        gc.strokeLine(boxRight.x, boxRight.y, boxDown.x, boxDown.y);
        gc.strokeLine(boxDown.x, boxDown.y, boxLeft.x, boxLeft.y);
        gc.strokeLine(boxLeft.x, boxLeft.y, boxUp.x, boxUp.y);
    }

//    public static void main(String[] args) {
//        launch(args);
//    }
}
