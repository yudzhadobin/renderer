package hse.matrixes.conversations;

import hse.matrixes.Matrix;

/**
 * Created by Yura on 04.01.2017.
 */
public class RotationY extends Matrix {


    public RotationY() {
        super(4, 4);

        set(1, 1, 1);
        setAngle(30 * Math.PI / 180 );
        set(3, 3, 1);

    }

    public RotationY(int angle) {
        super(4, 4);

        set(1, 1, 1);

        setAngle(angle * Math.PI / 180 );

        set(3, 3, 1);
    }


    public void setAngle(double phi) {
        set(0, 0, Math.cos(phi));
        set(0, 2, -Math.sin(phi));
        set(2, 0, Math.sin(phi));
        set(2, 2, Math.cos(phi));
    }
}
