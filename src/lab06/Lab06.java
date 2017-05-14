package lab06;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import math.Matrix4d;
import math.Vector3d;
import math.Vector4d;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Igorek on 19-Mar-17 at 11:43 AM.
 */
public class Lab06 extends Application {
    private Stage window;
    private Canvas canvas;

    private final double canvasSize = 600.0;

    private final Vector4d[] objectVertices;
    private final List<Face> faces;

    private final List<Face> facesToRender;
    private Vector4d[] modifiedVertices;

    private final Matrix4d perspectiveMatrix =
            Matrix4d.perspective(canvasSize, canvasSize, 67.0, 0.1, 300.0);
    private Matrix4d cameraMatrix =
            Matrix4d.identity().translate(new Vector3d(0.0, 0.0, 120.0));
    private Matrix4d objectMatrix =
            Matrix4d.identity().scale(new Vector3d(100.0));

    private final double updateFrequencySeconds = 1.0 / 30.0;
    private double lastUpdateTime = 0.0;

    public Lab06() {
        objectVertices = new Vector4d[]{
                // Down
                new Vector4d(0.0, 0.0, 0.0, 1.0),
                new Vector4d(1.0, 0.0, 0.0, 1.0),
                new Vector4d(1.0, 0.0, 1.0, 1.0),
                new Vector4d(0.0, 0.0, 1.0, 1.0),
                // Middle
                new Vector4d(1.0, 0.5, 0.0, 1.0),
                new Vector4d(1.0, 0.5, 1.0, 1.0),
                // Up
                new Vector4d(0.0, 1.0, 0.0, 1.0),
                new Vector4d(0.5, 1.0, 0.0, 1.0),
                new Vector4d(0.5, 1.0, 1.0, 1.0),
                new Vector4d(0.0, 1.0, 1.0, 1.0),
        };

        faces = new ArrayList<>();
        faces.add(new Face(new int[]{0, 1, 2, 3}, Color.RED));
        faces.add(new Face(new int[]{6, 9, 8, 7}, Color.BLUE));
        faces.add(new Face(new int[]{3, 2, 5, 8, 9}, Color.GREEN));
        faces.add(new Face(new int[]{0, 6, 7, 4, 1}, Color.YELLOW));
        faces.add(new Face(new int[]{1, 4, 5, 2}, Color.GREY));
        faces.add(new Face(new int[]{9, 6, 0, 3}, Color.WHITE));
        faces.add(new Face(new int[]{4, 7, 8, 5}, Color.MAGENTA));

        facesToRender = new ArrayList<>();

        modifiedVertices = new Vector4d[objectVertices.length];
    }

    @Override
    public void start(Stage primaryStage) {
        // For easy reference
        this.window = primaryStage;

        window.setTitle("Lab06 by Igor Boyarshin && Anka Doroshenko");

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

        // Process keyboard
        window.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            final double speed = 1.0;
//            if (key.getCode() == KeyCode.W) {
//                cameraMatrix = cameraMatrix.translate(new Vector3d(0.0, 0.0, -speed));
//            }
//            if (key.getCode() == KeyCode.S) {
//                cameraMatrix = cameraMatrix.translate(new Vector3d(0.0, 0.0, speed));
//            }
//            if (key.getCode() == KeyCode.A) {
//                cameraMatrix = cameraMatrix.translate(new Vector3d(-speed, 0.0, 0.0));
//            }
//            if (key.getCode() == KeyCode.D) {
//                cameraMatrix = cameraMatrix.translate(new Vector3d(speed, 0.0, 0.0));
//            }

            System.out.println(getCameraPosition().toString());
        });

        // Create, pack and display the window
        window.show();
    }

    private void draw(GraphicsContext gc, double secondsSinceStart) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvasSize, canvasSize);

        update(gc, secondsSinceStart);
        render(gc);
    }

    private void update(GraphicsContext gc, double secondsSinceStart) {
        if (secondsSinceStart - lastUpdateTime >= updateFrequencySeconds) {
            objectMatrix = objectMatrix.rotateAboutAxis(5.0, new Vector3d(0.0, 0.0, 1.0));
            objectMatrix = objectMatrix.rotateAboutAxis(5.0, new Vector3d(1.0, 0.0, 0.0));

            for (int index = 0; index < objectVertices.length; index++) {
                modifiedVertices[index] = (objectMatrix).multiply(objectVertices[index]);
            }

            facesToRender.clear();
            for (Face face : faces) {
                final int[] indices = face.indices;
                final Vector4d modifiedVertex1 = modifiedVertices[indices[0]];
                final Vector4d modifiedVertex2 = modifiedVertices[indices[1]];
                final Vector4d modifiedVertex3 = modifiedVertices[indices[2]];

                final Vector3d v1 = new Vector3d(
                        modifiedVertex2.x - modifiedVertex1.x,
                        modifiedVertex2.y - modifiedVertex1.y,
                        modifiedVertex2.z - modifiedVertex1.z
                );
                final Vector3d v2 = new Vector3d(
                        modifiedVertex3.x - modifiedVertex1.x,
                        modifiedVertex3.y - modifiedVertex1.y,
                        modifiedVertex3.z - modifiedVertex1.z
                );

                final Vector3d normal = v1.cross(v2);
                final Vector3d camera = getCameraPosition();

                final double cos = normal.dot(camera) / (normal.getLength() * camera.getLength());
                if (cos > 0.0) {
                    facesToRender.add(face);
                }
            }

            for (int index = 0; index < objectVertices.length; index++) {
                modifiedVertices[index] = perspectiveMatrix.multiply(cameraMatrix).multiply(modifiedVertices[index]);
            }

            lastUpdateTime = secondsSinceStart;
        }
    }

    private void render(GraphicsContext gc) {
        for (Face face : facesToRender) {
            gc.setFill(face.color);

            final int[] indices = face.indices;
            final double[] xPoints = new double[indices.length];
            final double[] yPoints = new double[indices.length];
            for (int index = 0; index < indices.length; index++) {
                final Vector4d vertex = modifiedVertices[indices[index]];

                xPoints[index] = vertex.x + canvasSize / 2.0;
                yPoints[index] = vertex.y + canvasSize / 2.0;
            }

            gc.fillPolygon(xPoints, yPoints, xPoints.length);
        }
    }

    private Vector3d getCameraPosition() {
        return new Vector3d(cameraMatrix.matrix[12], cameraMatrix.matrix[13], cameraMatrix.matrix[14]);
    }

    class Face {
        public final int[] indices;
        public final Color color;

        public Face(int[] indices, Color color) {
            this.color = color;
            this.indices = indices;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
