package hse.objects;

/**
 * Created by Yura on 03.01.2017.
 */
public class Normal extends Point3D<Double> {

    public Normal() {
        super();
    }

    public Normal(Double x, Double y, Double z) {
        super(x, y, z);
    }

    public Normal copy() {
        return new Normal(x, y, z);
    }



    @Override
    public String toString() {
        return "x = " + x + ", y = " + y + ", z = " + z;
    }
}
