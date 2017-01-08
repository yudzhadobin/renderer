package hse.ui;

import hse.Setings;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.util.Hashtable;

/**
 * Created by Yura on 25.12.2016.
 */
public class SwapChain {


    private static final SwapChain instance = new SwapChain();


    public static SwapChain getInstance() {
        return instance;
    }

    BufferedImage visible;
    BufferedImage drawing;

    private SwapChain() {
        initDrawing();
        initVisible();
    }

    public void swap() {
        if(!isEqual()) {
            System.out.println("RASSINGHOR");
        }
        BufferedImage sup = visible;
        visible = drawing;
        drawing = sup;
   //     drawing.getGraphics().clearRect(0,0, 720, 720);
    }

    public BufferedImage getVisible() {
        return visible;
    }

    public BufferedImage getDrawing() {
        return drawing;
    }

    boolean isEqual () {
        for (int i = 0; i < 720; i++) {
            for (int j = 0; j < 720; j++) {
                if(visible.getRGB(i, j) != drawing.getRGB(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void initVisible() {
        visible = new BufferedImage(Setings.WINDOW_WIDTH, Setings.WINDOW_HEIGHT, BufferedImage.TYPE_INT_RGB);

    }

    private void initDrawing() {
        drawing = new BufferedImage(Setings.WINDOW_WIDTH, Setings.WINDOW_HEIGHT, BufferedImage.TYPE_INT_RGB);


    }

    public BufferedImage getDiff() {
        BufferedImage image = new BufferedImage(Setings.WINDOW_WIDTH, Setings.WINDOW_HEIGHT, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < Setings.WINDOW_WIDTH; i++) {
            for (int j = 0; j <Setings.WINDOW_HEIGHT; j++) {
                if(visible.getRGB(i, j) != drawing.getRGB(i, j)) {
                    int rgb = visible.getRGB(i, j);
                    int rgb2 = drawing.getRGB(i, j);
                    int alpha1 = (rgb >> 24) & 0xff;
                    int red1 = (rgb >> 16) & 0xff;
                    int green1 = (rgb >> 8) & 0xff;
                    int blue1 = (rgb) & 0xff;

                    int alpha2 = (rgb2 >> 24) & 0xff;
                    int red2 = (rgb2 >> 16) & 0xff;
                    int green2 = (rgb2 >> 8) & 0xff;
                    int blue2 = (rgb2) & 0xff;

                    int alpha = (alpha1 + alpha2 ) / 2;
                    int red = (red1 + red2 ) / 2;
                    int green = (green1 + green2 ) / 2;
                    int blue = (blue1 + blue2 ) / 2;


                    image.setRGB(i,j, Color.green.getRGB());
                            //new Color(red, green, blue, alpha).getRGB());
                } else {
                    image.setRGB(i,j, visible.getRGB(i, j));
                }
            }
        }
        return image;
    }
}
