package hse.objects;

import hse.ui.SwapChain;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Yura on 22.01.2017.
 */
public class BoundingBox {
    Point3D<Integer> a;
    Point3D<Integer> b;

    public BoundingBox() {
        this.a = new Point3D<>();
        this.b = new Point3D<>();
    }

    public  boolean isIn(Point3D<Integer> another) {
        return another.x >= a.x && another.y >= a.y &&
                another.x <= b.x && another.y <= b.y;
    }


    public synchronized void extend(Point3D<Integer> another) {

        if(a.x == null || b.x == null) {
            a.x = another.x;
            a.y = another.y;
            a.z = another.z;

            b.x = another.x;
            b.y = another.y;
            b.z = another.z;

        }

        if (another.x <= a.x) {
            a.setX(another.x);
        }
        if (another.y <= a.y) {
            a.setY(another.y);
        }
        if (another.x >= b.x) {
            b.setX(another.x);
        }
        if (another.y >= b.y) {
            b.setY(another.y);
        }
    }

    public void draw(SwapChain swapChain) {
        BufferedImage drawing = swapChain.getDrawing();

        Graphics graphics = drawing.getGraphics();
        graphics.setColor(Color.GREEN);

        graphics.drawLine(a.x, a.y, a.x, b.y);
        graphics.drawLine(a.x, a.y, b.x, a.y);
        graphics.drawLine(b.x, a.y, b.x, b.y);
        graphics.drawLine(a.x, b.y, b.x, b.y);
    }

    public synchronized void clear() {
        a = new Point3D<>();
        b = new Point3D<>();
    }

    public Point3D<Integer> getA() {
        return a;
    }

    public Point3D<Integer> getB() {
        return b;
    }
}
