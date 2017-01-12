package hse.objects;

/**
 * Created by Yura on 03.01.2017.
 */
public class Normal extends Point3D<Double> {

    public Normal() {
        x = 0.0;
        y = 0.0;
        z = 0.0;
    }

    public Normal(Double x, Double y, Double z) {
        super(x, y, z);
    }

    public Normal copy() {
        return new Normal(x, y, z);
    }

    public Normal plus(Normal another) {
        this.x += another.x;
        this.y += another.y;
        this.z += another.z;

        return this;
    }

    public Normal minus(Normal another) {
        this.x -= another.x;
        this.y -= another.y;
        this.z -= another.z;
        return this;
    }

    public Normal multiple(double value) {
        this.x *= value;
        this.y *= value;
        this.z *= value;
        return this;
    }
    @Override
    public String toString() {
        return "x = " + x + ", y = " + y + ", z = " + z;
    }
}
