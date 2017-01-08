package hse.objects;

/**
 * Created by Yura on 01.01.2017.
 */
public class Point2D {
    int x;
    int y;


    public void swap(Point2D another) {
        int sup = x;
        x = another.x;
        another.x = sup;

        sup = y;
        y = another.y;
        another.y = sup;


    }
}
