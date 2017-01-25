package hse.matrixes.conversations;

import hse.matrixes.Matrix;

/**
 * Created by ZhadobinYD on 25.01.2017.
 */
public class MoveMatrix extends Matrix {
    public MoveMatrix(int x, int y, int z) {
        super(4, 4);

        set(0,0, 1);
        set(1,1, 1);
        set(2,2, 1);
        set(3,3, 1);


        set(3, 0, x);
        set(3, 1, y);
        set(3, 2, z);
    }
}
