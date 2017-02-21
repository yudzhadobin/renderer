package hse.objects;

import hse.Setings;
import hse.matrixes.Matrix;

/**
 * Created by yuriy on 18.02.17.
 */
public class Camera {

    private static Camera camera = new Camera();


    public static Camera getInstance() {
        return camera;
    }


    public Normal eye = new Normal(0d, -20d, 20d).normalize();
    public Normal c = new Normal(-0d, 00d, 0.5d).normalize();

    Normal up = new Normal(-0d,-1d, 0d).normalize();

    public Matrix lookat() {
        Normal z = eye.minus(c).normalize();
        Normal x = up.cross(z).normalize();
        Normal y = z.cross(x).normalize();
        Matrix Minv = Matrix.getIdenity();
        Matrix Tr   = Matrix.getIdenity();

        Minv.set(0, 0, x.getX());
        Minv.set(0, 1, x.getY());
        Minv.set(0, 2, x.getZ());

        Minv.set(1, 0, y.getX());
        Minv.set(1, 1, y.getY());
        Minv.set(1, 2, y.getZ());

        Minv.set(2, 0, z.getX());
        Minv.set(2, 1, z.getY());
        Minv.set(2, 2, z.getZ());

        Tr.set(0, 3,  - c.getX());
        Tr.set(1, 3,  - c.getY());
        Tr.set(2, 3,  - c.getZ());


        return Minv.multiple(Tr);
    }

    public Matrix viewport(int x, int y) {
        Matrix matrix = Matrix.getIdenity();


        matrix.set(0, 3, x + Setings.WINDOW_WIDTH /16.f);
        matrix.set(1, 3, y + Setings.WINDOW_HEIGHT /16.f);
        matrix.set(2, 3, Integer.MAX_VALUE /2.f);

        matrix.set(0, 0, Setings.WINDOW_WIDTH /16.f);
        matrix.set(1, 1, Setings.WINDOW_HEIGHT /16.f);
        matrix.set(2, 2, Integer.MAX_VALUE /2.f);
       // matrix.set(0, 3);

        return matrix;
    }
}
