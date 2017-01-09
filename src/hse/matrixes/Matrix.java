package hse.matrixes;

import hse.objects.Point3D;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yura on 03.01.2017.
 */
public class Matrix {
    List<List<Double>> matrix;

    int length;
    int width;

    public Matrix(int length, int width) {
        this(length, width, 0.0);
    }

    public Matrix(int length, int width, double value) {
        this.length = length;
        this.width = width;

        matrix = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            matrix.add(new ArrayList<>());
            for (int j = 0; j < width; j++) {
                matrix.get(i).add(value);
            }
        }
    }

    public void set(int i, int j, double value) {
        matrix.get(i).set(j, value);//right
    }

    public Double get(int i, int j) {
        return matrix.get(j).get(i);//right
    }

    public Point3D<Double> multiple(Point3D<Double> point) {
        Point3D<Double> result = new Point3D();

        List<Double> sub = matrix.get(0);
        result.setX((point.getX() * sub.get(0) + point.getY() * sub.get(1) + point.getZ() * sub.get(2) + 1 * sub.get(3)));

        sub = matrix.get(1);
        result.setY((point.getX() * sub.get(0) + point.getY() * sub.get(1) + point.getZ() * sub.get(2) + 1 * sub.get(3)));

        sub = matrix.get(2);
        result.setZ((point.getX() * sub.get(0) + point.getY() * sub.get(1) + point.getZ() * sub.get(2) + 1 * sub.get(3)));

        return result;
    }

    public void multipleInPlace(Point3D<Double> point) {
        Point3D<Double> result = multiple(point);

        point.swap(result);
    }

    public Matrix multiple(Matrix matrix) {
        Matrix result = new Matrix(4, 4);

        for (int i = 0; i < result.length; i++) {

            for (int j = 0; j < result.width; j++) {
                double res = 0;
                res += this.get(i, 0) * matrix.get(0, j);
                res += this.get(i, 1) * matrix.get(1, j);
                res += this.get(i, 2) * matrix.get(2, j);
                res += this.get(i, 3) * matrix.get(3, j);
                result.set(j, i , res);
            }

        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                builder.append(get(i,j) + " ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}
