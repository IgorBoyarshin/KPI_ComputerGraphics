package math;

import java.util.function.BiFunction;

/**
 * Created by Igorek on 19-Mar-17 at 1:19 PM.
 */
public class Mesh3D {

    private final float[] vertices;
    private final float[] texCoords;
    private final int[] faces;

    public Mesh3D(
            BiFunction<Double, Double, Double> xOfUAndV,
            BiFunction<Double, Double, Double> yOfUAndV,
            BiFunction<Double, Double, Double> zOfUAndV,
            Vector2d rangeU, Vector2d rangeV,
            double deltaU, double deltaV) {

        final int amountOfUs = (int) Math.floor(Math.abs((rangeU.y - rangeU.x) / deltaU)) + 1;
        final int amountOfVs = (int) Math.floor(Math.abs((rangeV.y - rangeV.x) / deltaV)) + 1;

        vertices = new float[amountOfUs * amountOfVs * 3]; // amount * (x,y,z)
        texCoords = new float[amountOfUs * amountOfVs * 2]; // amount * (u,v)
        faces = new int[(amountOfUs - 1) * (amountOfVs - 1) * 2 * 6]; // *2 == rects -> triangles; *6 == 3 verts + 3 texCoords

        calculate(xOfUAndV, yOfUAndV, zOfUAndV, rangeU, rangeV, deltaU, deltaV);
    }

    // TODO: Don't bother with UVs for now
    @SuppressWarnings("all")
    private void calculate(
            BiFunction<Double, Double, Double> xOfUAndV,
            BiFunction<Double, Double, Double> yOfUAndV,
            BiFunction<Double, Double, Double> zOfUAndV,
            Vector2d rangeU, Vector2d rangeV,
            double deltaU, double deltaV) {

        final int amountOfUs = (int) Math.floor(Math.abs((rangeU.y - rangeU.x) / deltaU)) + 1;
        final int amountOfVs = (int) Math.floor(Math.abs((rangeV.y - rangeV.x) / deltaV)) + 1;

        int indexU = 0;
        int vertexIndex = 0;
        int facesIndex = 0;
        // One cycle generates one new series of vertices
        for (double u = rangeU.x; (rangeU.y > rangeU.x ? (u <= rangeU.y) : (u >= rangeU.y)); u += deltaU, indexU++) {
            int indexV = 0;
            for (double v = rangeV.x; (rangeV.y > rangeV.x ? (v <= rangeV.y) : (v >= rangeV.y)); v += deltaV, indexV++, vertexIndex++) {
                Vector3d newVertex = new Vector3d(xOfUAndV.apply(u, v), yOfUAndV.apply(u, v), zOfUAndV.apply(u, v));

                // Add vertices
                vertices[vertexIndex * 3 + 0] = (float) newVertex.x;
                vertices[vertexIndex * 3 + 1] = (float) newVertex.y;
                vertices[vertexIndex * 3 + 2] = (float) newVertex.z;

                texCoords[vertexIndex * 2 + 0] = 1.0f * indexU / amountOfUs;
                texCoords[vertexIndex * 2 + 1] = 1.0f * indexV / amountOfVs;

                // Add faces
                if (indexU != 0) { // so already exists the previous series of vertices
                    if (indexV != 0) { // so already exists the previous vertex in this series
                        // Take 4 vertices: 2 from new and 2 from last series. Connect them into a rectangle(two triangles).
                        // There are exactly amountOfVs vertices per series

                        final int p0 = vertexIndex - 1;
                        final int p1 = vertexIndex - 1 - amountOfVs;
                        final int p2 = vertexIndex - amountOfVs;
                        final int p3 = vertexIndex;

                        // (p0, t0), (p1, t1), (p3, t3)
                        faces[facesIndex * 2 * 6 + 0 * 6 + 0] = p0;
                        faces[facesIndex * 2 * 6 + 0 * 6 + 1] = p0;
                        faces[facesIndex * 2 * 6 + 0 * 6 + 2] = p3;
                        faces[facesIndex * 2 * 6 + 0 * 6 + 3] = p3;
                        faces[facesIndex * 2 * 6 + 0 * 6 + 4] = p1;
                        faces[facesIndex * 2 * 6 + 0 * 6 + 5] = p1;

                        // (p1, t1), (p2, t2), (p3, t3)
                        faces[facesIndex * 2 * 6 + 1 * 6 + 0] = p1;
                        faces[facesIndex * 2 * 6 + 1 * 6 + 1] = p1;
                        faces[facesIndex * 2 * 6 + 1 * 6 + 2] = p3;
                        faces[facesIndex * 2 * 6 + 1 * 6 + 3] = p3;
                        faces[facesIndex * 2 * 6 + 1 * 6 + 4] = p2;
                        faces[facesIndex * 2 * 6 + 1 * 6 + 5] = p2;

                        facesIndex++;
                    }
                }
            }
        }
    }

    public float[] getVertices() {
        return vertices;
    }

    public float[] getTexCoords() {
        return texCoords;
    }

    public int[] getFaces() {
        return faces;
    }
}
