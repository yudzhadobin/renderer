package hse.matrixes;

/**
 * Created by Yura on 04.01.2017.
 */
public class OrthogonalProjection extends Matrix {



    public OrthogonalProjection() {
        super(4, 4);

        set(0, 0, 1);
        set(1, 1, 1);
        set(2, 2, 1);

        set(3, 3, 1);
    }
}
