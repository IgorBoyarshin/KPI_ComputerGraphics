package lab03;

import javafx.application.Application;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import math.Mesh3D;
import math.Vector2d;


/**
 * Created by Igorek on 19-Mar-17 at 11:43 AM.
 */
public class Lab03 extends Application {


    private final int windowSize = 600;

    private Stage window;

    // size of mesh
    int size = 400;

    // variables for mouse interaction
    private double mousePosX, mousePosY;
    private double mouseOldX, mouseOldY;
    private final Rotate rotateX = new Rotate(25, Rotate.X_AXIS);
    private final Rotate rotateY = new Rotate(-45, Rotate.Y_AXIS);

    @Override
    public void start(Stage primaryStage) {
        this.window = primaryStage;

        // Root element
        StackPane root = new StackPane();
        root.getTransforms().addAll(rotateX, rotateY);

        // Add exis
        root.getChildren().add(createAxis(size * 1.5, 5.0));

        // Main Mesh
        TriangleMesh mesh = new TriangleMesh();
        Mesh3D butterfly = new Mesh3D(
                (u, v) -> v * Math.cos(u) * Math.cos(u),
                (u, v) -> v * Math.cos(u),
                (u, v) -> v * Math.sin(2 * u),
                new Vector2d(0, 2 * Math.PI),
                new Vector2d(-size / 2, size / 2),
                Math.PI / 24, 20.0
        );
        mesh.getPoints().addAll(butterfly.getVertices());
        mesh.getTexCoords().addAll(butterfly.getTexCoords());
        mesh.getFaces().addAll(butterfly.getFaces());

        TriangleMesh mesh2 = new TriangleMesh();
        Mesh3D butterfly2 = new Mesh3D(
                (u, v) -> v * Math.cos(u) * Math.cos(u),
                (u, v) -> v * Math.cos(u),
                (u, v) -> v * Math.sin(2 * u),
                new Vector2d(0, 2 * Math.PI),
                new Vector2d(size / 2, -size / 2),
                Math.PI / 24, -20.0
        );
        mesh2.getPoints().addAll(butterfly2.getVertices());
        mesh2.getTexCoords().addAll(butterfly2.getTexCoords());
        mesh2.getFaces().addAll(butterfly2.getFaces());

        // material
        Image diffuseMap = createImage(size, null);

        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(diffuseMap);
        material.setSpecularColor(Color.WHITE);

        // mesh views
        MeshView meshView = new MeshView(mesh);
        meshView.setTranslateX(0.0 * size);
        meshView.setTranslateZ(0.0 * size);
        meshView.setMaterial(material);
        meshView.setCullFace(CullFace.BACK);
        meshView.setDrawMode(DrawMode.FILL);
        meshView.setDepthTest(DepthTest.ENABLE);

        MeshView meshView2 = new MeshView(mesh2);
        meshView2.setTranslateX(0.0 * size);
        meshView2.setTranslateZ(0.0 * size);
        meshView2.setMaterial(material);
        meshView2.setCullFace(CullFace.BACK);
        meshView2.setDrawMode(DrawMode.FILL);
        meshView2.setDepthTest(DepthTest.ENABLE);

        root.getChildren().addAll(meshView);
        root.getChildren().addAll(meshView2);

        // testing / debugging stuff: show diffuse map on chart
//        ImageView iv = new ImageView(diffuseMap);
//        iv.setTranslateX(-0.5 * size);
//        iv.setTranslateY(-0.10 * size);
//        iv.setRotate(90);
//        iv.setRotationAxis(new Point3D(1, 0, 0));
//        root.getChildren().add(iv);

        // scene
        Scene scene = new Scene(root, windowSize, windowSize, true, SceneAntialiasing.BALANCED);
        scene.setCamera(new PerspectiveCamera());

        scene.setOnMousePressed(me -> {
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();
        });
        scene.setOnMouseDragged(me -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            rotateX.setAngle(rotateX.getAngle() + (mousePosY - mouseOldY));
            rotateY.setAngle(rotateY.getAngle() - (mousePosX - mouseOldX));
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
        });

        makeZoomable(root);

        window.setTitle("Lab03 by Igor Boyarshin && Anka Doroshenko");
        window.setResizable(false);
        window.setScene(scene);
        window.show();
    }

    /**
     * Create texture for uv mapping
     */
    public Image createImage(double size, float[][] noise) {

        int width = (int) size;
        int height = (int) size;

        WritableImage wr = new WritableImage(width, height);
        PixelWriter pw = wr.getPixelWriter();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                pw.setColor(x, y, Color.RED.interpolate(Color.YELLOW, Math.sin(x / 4.0) * Math.sin(y / 4.0) / 2.0 + 0.5));
            }
        }

        return wr;
    }

    public void makeZoomable(StackPane control) {
        final double MAX_ZOOM = 20.0;
        final double MIN_ZOOM = 0.1;
        final double ZOOM_DELTA = 1.2;

        control.addEventFilter(ScrollEvent.ANY, event -> {
            double scale = control.getScaleX(); // same as getScaleY() (because we make it equal ourselves)

            if (event.getDeltaY() < 0) {
                scale /= ZOOM_DELTA;
            } else {
                scale *= ZOOM_DELTA;
            }

            scale = clamp(scale, MIN_ZOOM, MAX_ZOOM); // restrict to [MIN_ZOOM; MAX_ZOOM]

            control.setScaleX(scale);
            control.setScaleY(scale);

            event.consume();
        });
    }

    private Group createAxis(double length, double size) {
        Group axis = new Group();

        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);

        final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.DARKGREEN);
        greenMaterial.setSpecularColor(Color.GREEN);

        final PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.DARKBLUE);
        blueMaterial.setSpecularColor(Color.BLUE);

        final Box xAxis = new Box(length, size, size);
        final Box yAxis = new Box(size, length, size);
        final Box zAxis = new Box(size, size, length);

        xAxis.setMaterial(redMaterial);
        yAxis.setMaterial(greenMaterial);
        zAxis.setMaterial(blueMaterial);

        axis.getChildren().addAll(xAxis, yAxis, zAxis);

        return axis;
    }

    public static double normalizeValue(double value, double min, double max, double newMin, double newMax) {
        return (value - min) * (newMax - newMin) / (max - min) + newMin;
    }

    public static double clamp(double value, double min, double max) {
        if (Double.compare(value, min) < 0) {
            return min;
        }

        if (Double.compare(value, max) > 0) {
            return max;
        }

        return value;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
