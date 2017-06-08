package hse.objects;

import hse.UvCoordinate;

/**
 * Created by Yura on 08.01.2017.
 */
public class PointInfo {

    int index;
    Point3DDouble point;

    Point3DDouble transformedPoint;
    UvCoordinate uvCoordinates;

    Normal normal;

    Normal transformedNormal;


    public Point3DDouble getTransformedPoint() {
        return transformedPoint;
    }

    public void setTransformedPoint(Point3DDouble transformedPoint) {
        this.transformedPoint = transformedPoint;
    }

    public PointInfo(Point3DDouble point3DDouble, UvCoordinate coordinate, Normal normal, int index) {
        this.point = point3DDouble;
        this.uvCoordinates = coordinate;
        this.normal = normal;
        this.index = index;
    }

    public Point3DDouble getPoint() {
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

    public void setTransformedNormal(Normal transformedNormal) {
        this.transformedNormal = transformedNormal;
    }

    public Normal getTransformedNormal() {
        return transformedNormal;
    }

    public PointInfo copy() {
        PointInfo copy = new PointInfo(
                this.getPoint().copy(),
                this.getUvCoordinates().copy(),
                this.getNormal().copy(),
                index
        );
        if(this.getTransformedNormal() != null) {
            copy.setTransformedNormal(this.getTransformedNormal().copy());
        }
        if(this.getTransformedPoint() != null) {
            copy.setTransformedPoint(this.getTransformedPoint().copy());
        }
        return copy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PointInfo pointInfo = (PointInfo) o;

        if (point != null ? !point.equals(pointInfo.point) : pointInfo.point != null) return false;
        return normal != null ? normal.equals(pointInfo.normal) : pointInfo.normal == null;
    }

    @Override
    public int hashCode() {
        int result = point != null ? point.hashCode() : 0;
        result = 31 * result + (normal != null ? normal.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PointInfo{" +
                "point=" + point +
                '}';
    }
}

