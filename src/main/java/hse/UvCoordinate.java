package hse;

/**
 * Created by Yura on 07.01.2017.
 */
public class UvCoordinate {
    int x;
    int y;
    int z;

    public UvCoordinate(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public UvCoordinate(UvCoordinate uvCoordinates) {
        this(uvCoordinates.getX(), uvCoordinates.getY(), uvCoordinates.getZ());
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public void swap(UvCoordinate another) {
        int supX = another.getX();
        int supY = another.getY();

        another.setX(this.getX());
        another.setY(this.getY());
        another.setZ(this.getZ());

        this.setX(supX);
        this.setY(supY);
        this.setZ(0);
    }
}
