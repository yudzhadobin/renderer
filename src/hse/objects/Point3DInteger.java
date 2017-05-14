package hse.objects;

/**
 * Created by yuriy on 03.05.17.
 */
public class Point3DInteger {


    Integer x;
    Integer y;
    Integer z;
    Integer w;

    public Point3DInteger() {

    }

    public Point3DInteger(Integer x, Integer y, Integer z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = 1;
    }

    public Point3DInteger(Point3DInteger a) {
        this.x = a.x;
        this.y = a.y;
        this.z = a.z;
    }

    public void swap(Point3DInteger another) {
        Integer sup = x;
        x = another.x;
        another.x = sup;

        sup = y;
        y = another.y;
        another.y = sup;

        sup = z;
        z = another.z;
        another.z = sup;
    }


    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getZ() {
        return z;
    }

    public void setZ(Integer z) {
        this.z = z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point3DInteger point3DInteger = (Point3DInteger) o;

        if (x != null ? !x.equals(point3DInteger.x) : point3DInteger.x != null) return false;
        if (y != null ? !y.equals(point3DInteger.y) : point3DInteger.y != null) return false;
        return z != null ? z.equals(point3DInteger.z) : point3DInteger.z == null;

    }

    @Override
    public int hashCode() {
        int result = x != null ? x.hashCode() : 0;
        result = 31 * result + (y != null ? y.hashCode() : 0);
        result = 31 * result + (z != null ? z.hashCode() : 0);
        return result;
    }

}
