package hse;




import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;


/**
 * Created by Yura on 05.01.2017.
 */
public class ZBuffer  {
    private static final ZBuffer buffer = new ZBuffer();




    public static ZBuffer getBuffer() {
        return buffer;
    }

    List<List<Integer>> matrix;

    private ZBuffer() {
        matrix = new ArrayList<>();
        for (int i = 0; i < Setings.WINDOW_WIDTH; i++) {
            matrix.add(new ArrayList<>());
            for (int j = 0; j < Setings.WINDOW_HEIGHT; j++) {
                matrix.get(i).add(Integer.MIN_VALUE);
            }
        }
    }



    public synchronized void set(int i, int j, int value) {
        matrix.get(i).set(j, value);
    }

    public int get(int i, int j) {
       return matrix.get(i).get(j);
    }


    public BufferedImage toBufferedImage() {
        BufferedImage image = new BufferedImage(720, 720, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < Setings.WINDOW_WIDTH; i++) {
            for (int j = 0; j < Setings.WINDOW_HEIGHT; j++) {
                if(this.get(i, j ) != Integer.MIN_VALUE) {
                    image.setRGB(i, j, Color.GREEN.getRGB());
                }
            }
        }
        return image;
    }


    public void clear () {
        for (int i = 0; i < Setings.WINDOW_WIDTH; i++) {
            for (int j = 0; j < Setings.WINDOW_HEIGHT; j++) {
                this.set(i, j, Integer.MIN_VALUE);
            }
        }
    }
}
