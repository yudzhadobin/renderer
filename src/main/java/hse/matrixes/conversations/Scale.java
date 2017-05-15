package hse.matrixes.conversations;

import hse.Main;
import hse.matrixes.Matrix;

/**
 * Created by Yura on 04.01.2017.
 */
public class Scale extends Matrix {

    public Scale() {
        super(4, 4);

        set(0, 0, 200);
        set(1, 1, 200);
        set(2, 2, 200);

        set(3, 3, 10); //увеличить в 10000 раз, может надо переписать
    }

    public Scale(double size) {
        super(4, 4);

        set(0, 0, size);
        set(1, 1, size);
        set(2, 2, size);

        set(3, 3, 10); //увеличить в 10000 раз, может надо переписать
    }
}
