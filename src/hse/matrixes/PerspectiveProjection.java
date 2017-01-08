package hse.matrixes;

import hse.objects.Point3D;

import java.util.List;

/**
 * Created by Yura on 04.01.2017.
 */
public class PerspectiveProjection extends Matrix {

    public PerspectiveProjection() {
        super(4, 4);

        set(0, 0, 1);
        set(1, 1, 1);
        set(3, 3, 1);

        set(3, 2, 0.8);
        
        //// TODO: 04.01.2017  may be need to rewrite multiple and divide
    }

    public Point3D<Double> multiple(Point3D<Double> point) {
        Point3D<Double> result = new Point3D();

        List<Double> sub = matrix.get(0);
        result.setX((point.getX() * sub.get(0) + point.getY() * sub.get(1) + point.getZ() * sub.get(2) + 1 * sub.get(3)));

        sub = matrix.get(1);
        result.setY((point.getX() * sub.get(0) + point.getY() * sub.get(1) + point.getZ() * sub.get(2) + 1 * sub.get(3)));

        sub = matrix.get(2);
        result.setZ((point.getX() * sub.get(0) + point.getY() * sub.get(1) + point.getZ() * sub.get(2) + 1 * sub.get(3)));


        List<Double> doubles = matrix.get(3);
        Double sum = 0.0;
        for (Double aDouble : doubles) {
            sum += aDouble;
        }

        result.setX(result.getX() / sum);
        result.setY(result.getY() / sum);
        result.setZ(result.getZ() / sum);
        return result;

    }
}
