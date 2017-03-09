package hse.objects;

import com.sun.org.apache.regexp.internal.RESyntaxException;
import hse.controllers.change.Direction;

import java.util.DoubleSummaryStatistics;
import static java.lang.Math.*;
/**
 * Created by Yura on 03.01.2017.
 */
public class Normal extends Point3D<Double> {

    public Normal() {
        x = 0.0;
        y = 0.0;
        z = 0.0;
    }

    public void setValue(Direction direction, double value) {
        switch (direction) {
            case X:
                x = value;
                break;

            case Y:
                y = value;
                break;
            case Z:
                z = value;
                break;}
    }


    public Normal(Point3D<Double> point3D) {
        super(point3D.getX(), point3D.getY(), point3D.getZ());
    }

    public Normal(Double x, Double y, Double z) {
        super(x, y, z);
    }

    public Normal copy() {
        return new Normal(x, y, z);
    }

    public Normal plus(Normal another) {
        Normal result = new Normal(this);

        result.x += another.x;
        result.y += another.y;
        result.z += another.z;

        return result;
    }

    public Normal minus(Normal another) {
        Normal result = new Normal(this);
        result.x -= another.x;
        result.y -= another.y;
        result.z -= another.z;
        return result;
    }

    public Normal multiple(double value) {
        Normal result = new Normal(this);
        result.x *= value;
        result.y *= value;
        result.z *= value;
        return result;
    }

    public Normal normalize() {
        double length = sqrt(pow(x, 2) + pow(y, 2) + pow(z,2));
        this.x /= length;
        this.y /= length;
        this.z /= length;
        return this;
    }

    public Normal cross(Normal another) {
        Normal result = new Normal();

        result.setX(this.getY() * another.getZ() - this.getZ() * another.getY());
        result.setY(this.getX() * another.getZ() - this.getZ() * another.getX());
        result.setZ(this.getX() * another.getY() - this.getY() * another.getX());

        return result;
    }

    @Override
    public String toString() {
        return "x = " + x + ", y = " + y + ", z = " + z;
    }
}
