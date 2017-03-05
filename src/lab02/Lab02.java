package lab02;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import math.Vector2d;

public class Lab02 extends Application {
    private Stage window;
    private Canvas canvas;

    private final double canvasSize = 600.0;

    private final Heart heartMain1;
    private final Heart heartMain2;
    private final Heart heartMain3;
    private final Heart heartChanging;

    final double heartSize = 100.0;

    public Lab02() {
        heartMain1 = new Heart(
                getR0(heartSize),
                getR1(heartSize),
                getR2(heartSize),
                getR3(heartSize),
                getR4(heartSize),
                canvasSize
        );
        heartMain2 = new Heart(
                getR0(heartSize / 2.0),
                getR1(heartSize / 2.0),
                getR2(heartSize / 2.0),
                getR3(heartSize / 2.0),
                getR4(heartSize / 2.0),
                canvasSize
        );
        heartMain3 = new Heart(
                getR0(heartSize / 4.0),
                getR1(heartSize / 4.0),
                getR2(heartSize / 4.0),
                getR3(heartSize / 4.0),
                getR4(heartSize / 4.0),
                canvasSize
        );

        heartChanging = new Heart(
                getR0(heartSize),
                getR1(heartSize),
                getR2(heartSize),
                getR3(heartSize),
                getR4(heartSize),
                canvasSize
        );
    }

    @Override
    public void start(Stage primaryStage) {
        // For easy reference
        this.window = primaryStage;

        window.setTitle("Lab02 by Igor Boyarshin && Anka Doroshenko");

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
        gc.setFill(Color.CRIMSON);
        gc.fillRect(0, 0, canvasSize, canvasSize);

        // Static
        heartMain1.draw(gc, new Vector2d(canvasSize / 2.0, canvasSize * 0.75), 0.0, Color.GREENYELLOW, 3.5);

        // Dynamic
        for (double size = heartSize; size >= heartSize / 24.0; size -= heartSize / 12.0) {
            final double baseR0 = getR0(size);
            final double baseR4 = getR4(size);
            heartChanging.setRs(
                    baseR0 + baseR0 / 4.0 * Math.sin(3 * time),
                    getR1(size),
                    getR2(size),
                    getR3(size),
                    baseR4 + baseR4 * 0.75 * Math.sin(3 * time)
            );

            heartChanging.draw(gc, new Vector2d(canvasSize / 4.0, canvasSize / 3.0), 0.0,
                    Color.rgb((int) (size / heartSize * 255), (int) (0.4 * 255), (int) (0.7 * 255)), 2.0);
        }

        // #2 first circle
        for (double angle = 0.0; angle < 360; angle += 30.0) {
            heartMain1.draw(gc, new Vector2d(canvasSize * 0.75, canvasSize / 3.0), angle, Color.YELLOW, 1.0);
        }

        // #2 second circle
        for (double angle = 0.0; angle < 360; angle += 30.0) {
            heartMain2.draw(gc, new Vector2d(canvasSize * 0.75, canvasSize / 3.0), angle, Color.ORANGE, 1.0);
        }

        // #2 third circle
        for (double angle = 0.0; angle < 360; angle += 30.0) {
            heartMain3.draw(gc, new Vector2d(canvasSize * 0.75, canvasSize / 3.0), angle, Color.RED, 1.0);
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
