package hse.objects;

import hse.UvCoordinate;

/**
 * Created by Yura on 01.01.2017.
 */
public class Point3D<T extends Number> {

    T x;
    T y;
    T z;

    UvCoordinate uvCoordinate;

    Normal normal;

    public Point3D() {

    }

    public Point3D(T x, T y, T z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3D(Point3D<T> a) {
        this.x = a.x;
        this.y = a.y;
        this.z = a.z;

        this.normal = a.getNormal();
        this.uvCoordinate = a.getUvCoordinate();
    }

    public void setUvCoordinate(UvCoordinate uvCoordinate) {
        this.uvCoordinate = uvCoordinate;
    }

    public void setNormal(Normal normal) {
        this.normal = normal;
    }

    public Normal getNormal() {
        return normal;
    }

    public UvCoordinate getUvCoordinate() {
        return uvCoordinate;
    }

    public void swap(Point3D<T> another) {
        T sup = x;
        x = another.x;
        another.x = sup;

        sup = y;
        y = another.y;
        another.y = sup;

        sup = z;
        z = another.z;
        another.z = sup;
        UvCoordinate b = another.getUvCoordinate();
        this.setUvCoordinate(another.getUvCoordinate());
        another.setUvCoordinate(b);

        Normal normal = another.normal;
        this.normal = another.normal;
        another.normal = normal;

    }

    public T getX() {
        return x;
    }

    public void setX(T x) {
        this.x = x;
    }

    public T getY() {
        return y;
    }

    public void setY(T y) {
        this.y = y;
    }

    public T getZ() {
        return z;
    }

    public void setZ(T z) {
        this.z = z;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point3D<?> point3D = (Point3D<?>) o;

        if (x != null ? !x.equals(point3D.x) : point3D.x != null) return false;
        if (y != null ? !y.equals(point3D.y) : point3D.y != null) return false;
        return z != null ? z.equals(point3D.z) : point3D.z == null;

    }

    @Override
    public int hashCode() {
        int result = x != null ? x.hashCode() : 0;
        result = 31 * result + (y != null ? y.hashCode() : 0);
        result = 31 * result + (z != null ? z.hashCode() : 0);
        return result;
    }
}
