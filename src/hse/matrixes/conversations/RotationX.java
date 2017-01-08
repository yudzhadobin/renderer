package hse.matrixes.conversations;

import hse.matrixes.Matrix;

/**
 * Created by Yura on 04.01.2017.
 */
public class RotationX extends Matrix{

    public RotationX() {
        super(4, 4);

        set(0, 0, 1);

        setAngle(30 * Math.PI / 180 );

        set(3, 3, 1);
    }

    public void setAngle(double phi) {
        set(1, 1, Math.cos(phi));
        set(1, 2, -Math.sin(phi));
        set(2, 1, Math.sin(phi));
        set(2, 2, Math.cos(phi));
    }
}
