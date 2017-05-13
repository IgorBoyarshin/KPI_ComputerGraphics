package lab05;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import math.Vector2d;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Lab05 extends Application {
    private Stage window;
    private Canvas canvas;

    private final double canvasSize = 600.0;

    private final double heartSize = 15.0;

    private final int amountOfHearts = 12;
    private final List<Heart> hearts;

    private final Function<Double, Double> astroidXOfT = t -> Math.pow(Math.cos(t), 3);
    private final Function<Double, Double> astroidYOfT = t -> Math.pow(Math.sin(t), 3);

    public Lab05() {
        hearts = new ArrayList<>();
        for (int i = 0; i < amountOfHearts; i++) {
            hearts.add(new Heart(getR0(heartSize),
                    getR1(heartSize),
                    getR2(heartSize),
                    getR3(heartSize),
                    getR4(heartSize),
                    canvasSize));
        }
    }

    @Override
    public void start(Stage primaryStage) {
        // For easy reference
        this.window = primaryStage;

        window.setTitle("Lab05 by Igor Boyarshin && Anka Doroshenko");

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
                final double ONE_BILLION = 1000000000.0;
                double secondsSinceStart = (currentNanoTime - startNanoTime) / ONE_BILLION;

                draw(canvas.getGraphicsContext2D(), secondsSinceStart);
            }
        }.start();

        // Create, pack and display the window
        window.show();
    }

    private void draw(GraphicsContext gc, double time) {
        gc.setFill(Color.CRIMSON);
        gc.fillRect(0, 0, canvasSize, canvasSize);

        final double astroidRadius = 200.0;

        final double twoPi = 2.0 * Math.PI;
        for (int i = 0; i < amountOfHearts; i++) {
            final Heart heart = hearts.get(i);
            final Vector2d currentPosition = new Vector2d(
                    canvasSize / 2.0 + astroidRadius * astroidXOfT.apply(time + 1.0 * i / amountOfHearts * twoPi),
                    canvasSize / 2.0 + astroidRadius * astroidYOfT.apply(time + 1.0 * i / amountOfHearts * twoPi)
            );

            heart.draw(gc, currentPosition, 0.0, Color.BLUE, 5.0);
        }
    }

    private double getR0(double heartSize) {
        return heartSize * 1.0;
    }

    private double getR1(double heartSize) {
        return heartSize / 3.0 * 2.0;
    }

    private double getR2(double heartSize) {
        return heartSize / 6.0;
    }

    private double getR3(double heartSize) {
        return heartSize / 8.0;
    }

    private double getR4(double heartSize) {
        return heartSize / 12.0;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
