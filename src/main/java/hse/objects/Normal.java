package hse.objects;

import hse.controllers.change.Direction;

import static java.lang.Math.*;
/**
 * Created by Yura on 03.01.2017.
 */
public class Normal extends Point3DDouble {

    public Normal() {
        x = 0.0;
        y = 0.0;
        z = 0.0;
        w = 0.0;
    }

    public void setValue(Direction direction, Double value) {
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


    public Normal(Point3DDouble point3DDouble) {
        super(point3DDouble.getX(), point3DDouble.getY(), point3DDouble.getZ());
        w = 0.0;
    }

    public Normal(Double x, Double y, Double z) {
        super(x, y, z);
    }

    public Normal copy() {
        return new Normal(x, y, z);
    }

    public Normal plus(Normal another) {
        Normal result = new Normal(this);
        try {

            result.x += another.x;
            result.y += another.y;
            result.z += another.z;
        }catch (NullPointerException ex) {
            int j =5 ;
        }
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

    public double norm() {
        return sqrt(pow(x, 2) + pow(y, 2) + pow(z,2));

    }


    public Normal normalize() {
        double length = sqrt(pow(x, 2) + pow(y, 2) + pow(z,2));
        if(length == 0) {
            return this;
        }
        this.x /= length;
        this.y /= length;
        this.z /= length;
        return this;
    }

    public Normal cross(Normal another) {
        Normal result = new Normal();

        result.setX(this.getY() * another.getZ() - this.getZ() * another.getY());
        result.setY(this.getZ() * another.getX() - this.getX() * another.getZ());
        result.setZ(this.getX() * another.getY() - this.getY() * another.getX());

        return result;
    }

    @Override
    public String toString() {
        return "x = " + x + ", y = " + y + ", z = " + z;
    }

    public double getValue(Direction direction) {
        switch (direction) {
            case X:
                return x;

            case Y:
                return y;

            case Z:
                return z;
        }
        return 0;
    }
}
