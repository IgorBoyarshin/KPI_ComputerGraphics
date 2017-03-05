package lab02;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import math.Curve;
import math.Vector2d;

import java.util.function.DoubleSupplier;
import java.util.function.Function;

public class Lab02 extends Application {
    private Stage window;
    private Canvas canvas;

    private final double canvasSize = 500.0;

    private final Curve element = new Curve(t -> 50 * Math.cos(t), t -> 50 * Math.sin(t),
            new Vector2d(0.0, 2 * Math.PI), 0.1, canvasSize);

    @Override
    public void start(Stage primaryStage) {
        // For easy reference
        this.window = primaryStage;

        window.setTitle("Lab02 by Igor Boyarshin && Anka Doroshenko");

//        Axes axes = new Axes(
//                400, 300,
//                -8, 8, 1,
//                -6, 6, 1
//        );
//
//        Plot plot = new Plot(
//                x -> .25 * (x + 4) * (x + 1) * (x - 2),
//                -8, 8, 0.1,
//                axes
//        );

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
                double secondsSinceStart = (currentNanoTime - startNanoTime) / 1000000000.0;

                draw(canvas.getGraphicsContext2D(), secondsSinceStart);
            }
        }.start();

        // Create, pack and display the window
        window.show();
    }


    private void draw(GraphicsContext gc, double time) {
        drawElement(gc, new Vector2d(canvasSize / 2.0, canvasSize / 2.0), Color.RED);
    }

    private void drawElement(GraphicsContext gc, Vector2d center, Color color) {
        element.draw(gc, center, color);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
