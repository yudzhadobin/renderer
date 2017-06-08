package hse.objects;

import hse.controllers.change.Direction;
import hse.matrixes.Matrix;

/**
 * Created by yuriy on 18.02.17.
 */
public class Camera {

    private static Camera camera = new Camera();


    public static Camera getInstance() {
        return camera;
    }


    public Normal eye = new Normal(1d, 1d, 3d);
    public Normal c = new Normal(0d, 0d, 0d);
    Normal up = new Normal(0d, 1d, 0d);
    final int depth  = 300;


    boolean wasChangeLookAt = false;
    boolean wasChangeViewPort = false;

    Matrix lookat;
    Matrix viewport;

    public Matrix lookat() {
        if(lookat == null || wasChangeLookAt) {
            Normal z = new Normal(eye).minus(c).normalize();
            Normal x = up.cross(z).normalize();
            Normal y = z.cross(x).normalize();
            Matrix result = Matrix.getIdenity();

            result.set(0, 0, x.getX());
            result.set(0, 1, x.getY());
            result.set(0, 2, x.getZ());

            result.set(1, 0, y.getX());
            result.set(1, 1, y.getY());
            result.set(1, 2, y.getZ());

            result.set(2, 0, z.getX());
            result.set(2, 1, z.getY());
            result.set(2, 2, z.getZ());

            result.set(0, 3, -c.getX());
            result.set(1, 3, -c.getY());
            result.set(2, 3, -c.getZ());

            lookat =  result;
            wasChangeLookAt = false;
        }
        return lookat;
    }

    public Matrix viewport(int x, int y, int w, int h) {
        if(viewport == null || wasChangeViewPort) {
            Matrix matrix = Matrix.getIdenity();


            matrix.set(0, 3, x + w / 2.f);
            matrix.set(1, 3, y + h / 2.f);
            matrix.set(2, 3, depth/2.f);

            matrix.set(0, 0, w / 2.f);
            matrix.set(1, 1, h / 2.f);
            matrix.set(2, 2, depth/2.f);

            viewport = matrix;
            wasChangeViewPort = false;
        }
        return viewport;
    }


    public void setEye(Direction direction, Double value) {
        eye.setValue(direction, value);

        wasChangeLookAt = true;
        wasChangeViewPort = true;
    }

    public double getEye(Direction direction) {
        return eye.getValue(direction);
    }

    public void setC(Direction direction, Double value) {
        c.setValue(direction, value);
        wasChangeLookAt = true;
        wasChangeViewPort = true;
    }

    public double getC(Direction direction) {
        return c.getValue(direction);
    }

    public void clear() {
        eye.setX(1d);
        eye.setY(1d);
        eye.setZ(3d);

        c.setX(0d);
        c.setY(0d);
        c.setZ(0d);
    }
}
