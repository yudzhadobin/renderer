package hse.objects;

import hse.Setings;
import hse.controllers.change.Direction;
import hse.matrixes.Matrix;
import org.omg.PortableInterceptor.INACTIVE;
import sun.jvm.hotspot.utilities.IntArray;

/**
 * Created by yuriy on 18.02.17.
 */
public class Camera {

    private static Camera camera = new Camera();


    public static Camera getInstance() {
        return camera;
    }


    Normal eye = new Normal(400d,-0d, -20d).normalize();
    Normal c = new Normal(1d, 1d, 1d).normalize();

    boolean wasChangeLookAt = false;
    boolean wasChangeViewPort = false;

    Normal up = new Normal(-0d,-1d, 0d).normalize();

    Matrix lookat;
    Matrix viewport;

    public Matrix lookat() {
        if(lookat == null || wasChangeLookAt) {
            Normal z = eye.minus(c).normalize();
            Normal x = up.cross(z).normalize();
            Normal y = z.cross(x).normalize();
            Matrix Minv = Matrix.getIdenity();
            Matrix Tr = Matrix.getIdenity();

            Minv.set(0, 0, x.getX());
            Minv.set(0, 1, x.getY());
            Minv.set(0, 2, x.getZ());

            Minv.set(1, 0, y.getX());
            Minv.set(1, 1, y.getY());
            Minv.set(1, 2, y.getZ());

            Minv.set(2, 0, z.getX());
            Minv.set(2, 1, z.getY());
            Minv.set(2, 2, z.getZ());

            Tr.set(0, 3, -c.getX());
            Tr.set(1, 3, -c.getY());
            Tr.set(2, 3, -c.getZ());


            lookat = Minv.multiple(Tr);
            wasChangeLookAt = false;
        }
        return lookat;
    }

    public Matrix viewport(int x, int y) {
        if(viewport == null || wasChangeViewPort) {
            Matrix matrix = Matrix.getIdenity();


            matrix.set(0, 3, x + Setings.WINDOW_WIDTH / 16.f);
            matrix.set(1, 3, y + Setings.WINDOW_HEIGHT / 16.f);
            matrix.set(2, 3, 100);

            matrix.set(0, 0, Setings.WINDOW_WIDTH / 16.f);
            matrix.set(1, 1, Setings.WINDOW_HEIGHT / 16.f);
            matrix.set(2, 2, 100);
            // matrix.set(0, 3);

            viewport = matrix;
            wasChangeViewPort = false;
        }
        return viewport;
    }


    public void setEye(Direction direction, double value) {
        eye.setValue(direction, value);
    }

    public void setEyeX(double x) {
        wasChangeLookAt = true;
        wasChangeViewPort = true;
        eye.setX(x);
    }

    public void setEyeY(double y) {
        wasChangeLookAt = true;
        wasChangeViewPort = true;
        eye.setY(y);
    }

    public void setEyeZ(double z) {
        wasChangeLookAt = true;
        wasChangeViewPort = true;
        eye.setZ(z);
    }

    public void setCenterX(double x) {
        wasChangeLookAt = true;
        wasChangeViewPort = true;
        c.setX(x);
    }

    public void setCenterY(double y) {
        wasChangeLookAt = true;
        wasChangeViewPort = true;
        c.setY(y);
    }

    public void setCenterZ(double z) {
        wasChangeLookAt = true;
        wasChangeViewPort = true;
        c.setZ(z);
    }

}
