package hse.objects;

import hse.UvCoordinate;

/**
 * Created by Yura on 08.01.2017.
 */
public class PointInfo {

    int index;
    Point3D<Double> point;

    UvCoordinate uvCoordinates;

    Normal normal;


    public PointInfo(Point3D<Double> point3D, UvCoordinate coordinate, Normal normal, int index) {
        this.point = point3D;
        this.uvCoordinates = coordinate;
        this.normal = normal;
        this.index = index;
    }

    public Point3D<Double> getPoint() {
        return point;
    }

    public int getIndex() {
        return index;
    }

    public UvCoordinate getUvCoordinates() {
        return uvCoordinates;
    }

    public Normal getNormal() {
        return normal;
    }
}

