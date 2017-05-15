package hse.objects;

import hse.ui.SwapChain;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Yura on 22.01.2017.
 */
public class BoundingBox {
    Point3DInteger a;
    Point3DInteger b;

    public BoundingBox() {
        this.a = new Point3DInteger();
        this.b = new Point3DInteger();
    }

    public  boolean isIn(Point3DInteger another) {
return true;
        //        return another.x >= a.x && another.y >= a.y &&
//                another.x <= b.x && another.y <= b.y;
    }


    public synchronized void extend(Point3DInteger another) {

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

    public synchronized void draw(SwapChain swapChain) {
        if (a.x != null && b.x != null) {
            BufferedImage drawing = swapChain.getDrawing();

            Graphics graphics = drawing.getGraphics();
            graphics.setColor(Color.GREEN);

            graphics.drawLine(a.x, a.y, a.x, b.y);
            graphics.drawLine(a.x, a.y, b.x, a.y);
            graphics.drawLine(b.x, a.y, b.x, b.y);
            graphics.drawLine(a.x, b.y, b.x, b.y);
        }
    }

    public synchronized void clear() {
        a = new Point3DInteger();
        b = new Point3DInteger();
    }

    public Point3DInteger getA() {
        return a;
    }

    public Point3DInteger getB() {
        return b;
    }
}
