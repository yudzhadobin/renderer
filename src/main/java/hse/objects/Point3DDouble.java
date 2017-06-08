package hse.objects;

/**
 * Created by Yura on 01.01.2017.
 */
public class Point3DDouble {
    
    Double x;
    Double y;
    Double z;
    Double w;
    
    public Point3DDouble() {

    }

    public Point3DDouble(Double x, Double y, Double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = 1.0;
    }

    public Point3DDouble(Point3DDouble a) {
        this.x = a.x;
        this.y = a.y;
        this.z = a.z;
        this.w = a.w;
    }

    public Point3DDouble copy() {
        Point3DDouble result = new Point3DDouble();
        result.x = this.x;
        result.y = this.y;
        result.z = this.z;
        result.w = this.w;
        return result;
    }

    public void swap(Point3DDouble another) {
        Double sup = x;
        x = another.x;
        another.x = sup;

        sup = y;
        y = another.y;
        another.y = sup;

        sup = z;
        z = another.z;
        another.z = sup;

        sup = w;
        w = another.w;
        another.w = sup;
    }

    public Point3DDouble minus(Point3DDouble another) {
        Point3DDouble result = new Point3DDouble(this);
        result.x -= another.x;
        result.y -= another.y;
        result.z -= another.z;

        result.w = 0.0;
        return result;
    }

    public Point3DDouble plus(Point3DDouble another) {
        Point3DDouble result = new Point3DDouble(this);

        result.x += another.x;
        result.y += another.y;
        result.z += another.z;

        result.w = 1.0;
        return result;
    }

    public Point3DDouble multiple(double value) {
        Point3DDouble result = new Point3DDouble(this);
        result.x *= value;
        result.y *= value;
        result.z *= value;
        result.w = 0.0;
        return result;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Double getZ() {
        return z;
    }

    public void setZ(Double z) {
        this.z = z;
    }

    public Double getW() {
        if(w == null) {
            return 1.0;
        }
        return w;
    }

    public void setW(Double w) {
        this.w = w;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point3DDouble point3DDouble = (Point3DDouble) o;

        if (x != null ? !x.equals(point3DDouble.x) : point3DDouble.x != null) return false;
        if (y != null ? !y.equals(point3DDouble.y) : point3DDouble.y != null) return false;
        return z != null ? z.equals(point3DDouble.z) : point3DDouble.z == null;

    }

    @Override
    public int hashCode() {
        int result = x != null ? x.hashCode() : 0;
        result = 31 * result + (y != null ? y.hashCode() : 0);
        result = 31 * result + (z != null ? z.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", w=" + w +
                '}';
    }
}
