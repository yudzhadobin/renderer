package hse.matrixes;

import hse.objects.Camera;
import hse.objects.Normal;
import hse.objects.Point3DDouble;

import java.util.List;

/**
 * Created by Yura on 04.01.2017.
 */
public class PerspectiveProjection extends Matrix {

    Camera camera = Camera.getInstance();


    public PerspectiveProjection() {
        super(4, 4);

        set(0, 0, 1);
        set(1, 1, 1);
        set(2,2,1);
        set(3, 3, 1);

        update();
    }

    public void update() {
        set(2, 3, -1.f /(new Normal(camera.eye).minus(camera.c)).norm());
    }

    public Point3DDouble multiple(Point3DDouble point) {
        Point3DDouble result = new Point3DDouble();

        List<Double> sub = matrix.get(0);
        result.setX((point.getX() * sub.get(0) + point.getY() * sub.get(1) + point.getZ() * sub.get(2) + 1 * sub.get(3)));

        sub = matrix.get(1);
        result.setY((point.getX() * sub.get(0) + point.getY() * sub.get(1) + point.getZ() * sub.get(2) + 1 * sub.get(3)));

        sub = matrix.get(2);
        result.setZ((point.getX() * sub.get(0) + point.getY() * sub.get(1) + point.getZ() * sub.get(2) + 1 * sub.get(3)));


        Double sum = 0.0;

        for (int i = 0; i < 4; i++) {
            List<Double> doubles = matrix.get(i);
            sum += doubles.get(3);
        }
        result.setX(result.getX() / sum);
        result.setY(result.getY() / sum);
        result.setZ(result.getZ() / sum);
        return result;

    }
}
