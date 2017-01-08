package hse.objects;

import com.sun.prism.sw.SWPipeline;
import hse.UvCoordinate;
import hse.ZBuffer;
import hse.matrixes.Matrix;
import hse.matrixes.Projections;
import hse.ui.SwapChain;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by Yura on 01.01.2017.
 */
public class Side {
    List<Integer> indexes;

    static double diffuseReflectionCoef = .50;
    static double lightIntensity = 1;
    static int light_x = 50;
    static int light_y = 50;
    static int light_z = 500;

    double prevIntensity;


    public Side() {
        this.indexes = new ArrayList<>();
//        this.normal = new Normal();
    }

    public List<Integer> getIndexes() {
        return indexes;
    }

    public static List<Side> create(List<Integer> indexes) {

        List<Side> result = new ArrayList<>();
        Side side = new Side();
        if (indexes.size() > 3) {


            side.indexes.add(indexes.get(0));
            side.indexes.add(indexes.get(1));
            side.indexes.add(indexes.get(2));

            result.add(side);

            side = new Side();

            side.indexes.add(indexes.get(0));
            side.indexes.add(indexes.get(2));
            side.indexes.add(indexes.get(3));
        } else {
            side.indexes.addAll(indexes);
        }



        result.add(side);

        return result;
    }

    @SuppressWarnings("unchecked")
    public void drawContour(SwapChain swapChain, List<Point3D<Double>> points, Matrix projection) {
        Graphics graphics = swapChain.getDrawing().getGraphics();

        Point3D a = points.get(indexes.get(0));
        Point3D b = points.get(indexes.get(1));
        Point3D c = points.get(indexes.get(2));

        Point3D<Double> convertedA = projection.multiple(a);
        Point3D<Double> convertedB = projection.multiple(b);
        Point3D<Double> convertedC = projection.multiple(c);

        graphics.drawLine(250 + convertedA.x.intValue(), 250 - convertedA.y.intValue(), 250 + convertedB.x.intValue(), 250 - convertedB.y.intValue());
        graphics.drawLine(250 + convertedB.x.intValue(), 250 - convertedB.y.intValue(), 250 + convertedC.x.intValue(), 250 - convertedC.y.intValue());
        graphics.drawLine(250 + convertedC.x.intValue(), 250 - convertedC.y.intValue(), 250 + convertedA.x.intValue(), 250 - convertedA.y.intValue());
    }

    @SuppressWarnings("unchecked")
    public void drawContour(SwapChain swapChain, Map<Integer, Point3D<Double>> points, Matrix projection) {
        Graphics graphics = swapChain.getDrawing().getGraphics();

        Point3D a = points.get(indexes.get(0));
        Point3D b = points.get(indexes.get(1));
        Point3D c = points.get(indexes.get(2));

        Point3D<Double> convertedA = projection.multiple(a);
        Point3D<Double> convertedB = projection.multiple(b);
        Point3D<Double> convertedC = projection.multiple(c);

        graphics.drawLine(250 + convertedA.x.intValue(), 250 - convertedA.y.intValue(), 250 + convertedB.x.intValue(), 250 - convertedB.y.intValue());
        graphics.drawLine(250 + convertedB.x.intValue(), 250 - convertedB.y.intValue(), 250 + convertedC.x.intValue(), 250 - convertedC.y.intValue());
        graphics.drawLine(250 + convertedC.x.intValue(), 250 - convertedC.y.intValue(), 250 + convertedA.x.intValue(), 250 - convertedA.y.intValue());
    }

    @SuppressWarnings("unchecked")
    public void drawFill(SwapChain swapChain, Map<Integer, Point3D<Double>> points, Matrix projection) {
        BufferedImage drawingPanel = swapChain.getDrawing();


        Point3D<Integer> a = transform(points.get(indexes.get(0)));
        Point3D<Integer> b = transform(points.get(indexes.get(1)));
        Point3D<Integer> c = transform(points.get(indexes.get(2)));
        if (a.y > b.y) {
            a.swap(b);
        }
        if (a.y > c.y) {
            a.swap(c);
        }
        if (b.y > c.y) {
            b.swap(c);
        }

        int totalHeight = c.y - a.y;

        double intensity = this.calculateSideIntensity();
        if(prevIntensity != 0.0 && intensity != prevIntensity) {
            int j = 5;
        }
        this.prevIntensity = intensity;
        if (intensity < 0) {
            return;
        }

//         if (intensity < 0.1) {
//             intensity = 1.0;
//         }
        Color color = new Color((int) (255 * intensity), (int) (255 * intensity), (int) (255 * intensity));

        for (int i = 0; i < totalHeight; i++) {
            boolean secondHalf = (i > (b.y - a.y)) || (b.y == a.y);
            int segmentHeight = secondHalf ? c.y - b.y : b.y - a.y;

            double alpha = (double) i / totalHeight;
            double beta = (double) (i - (secondHalf ? b.y - a.y : 0)) / segmentHeight;
            Point3D<Integer> A = new Point3D<>(a);
            A.x += round((c.x - a.x) * alpha);
            A.y += round((c.y - a.y) * alpha);
            A.z += round((c.z - a.z) * alpha);


            Point3D<Integer> B = secondHalf ? new Point3D<>(b) : new Point3D<>(a);
            B.x += round(secondHalf ? (c.x - b.x) * beta : (b.x - a.x) * beta);
            B.y += round(secondHalf ? (c.y - b.y) * beta : (b.y - a.y) * beta);
            B.z += round(secondHalf ? (c.z - b.z) * beta : (b.z - a.z) * beta);


            if (A.x > B.x) {
                A.swap(B);
            }

            for (int j = A.x; j <= B.x; j++) {


                double phi = B.x == A.x ? 1 : (double) (j - A.x) / (double) (B.x - A.x);

                Point3D<Integer> point = new Point3D<>(A);

                point.x += round((B.x - A.x) * phi);
                point.y += round((B.y - A.y) * phi);
                point.z += round((B.z - A.z) * phi);

                point.x += 350;
                point.y = 350 - point.y;
                synchronized (ZBuffer.getBuffer()) {
                    if (ZBuffer.getBuffer().get(point.x, point.y) < point.z) {

                        ZBuffer.getBuffer().set(point.x, point.y, point.z);
                        drawingPanel.setRGB((int) (point.x), (int) (point.y), color.getRGB()); // attention, due to int casts convertedA.y+i != A.y


                    }
                }


            }
        }


    }


    @SuppressWarnings("unchecked")
    public void drawTextured(SwapChain swapChain, Map<Integer, Point3D<Double>> points, Matrix projection, Object3D object) {
        BufferedImage drawingPanel = swapChain.getDrawing();


        Point3D<Integer> a = transform(points.get(indexes.get(0)));
        Point3D<Integer> b = transform(points.get(indexes.get(1)));
        Point3D<Integer> c = transform(points.get(indexes.get(2)));
        if (a.y > b.y) {
            a.swap(b);
        }
        if (a.y > c.y) {
            a.swap(c);
        }
        if (b.y > c.y) {
            b.swap(c);
        }

        int totalHeight = c.y - a.y;

        double intensity = this.calculateSideIntensity();
        if(prevIntensity != 0.0 && intensity != prevIntensity) {
            int j = 5;
        }
        this.prevIntensity = intensity;
        if (intensity < 0) {
            return;
        }

        Color color = new Color((int) (255 * intensity), (int) (255 * intensity), (int) (255 * intensity));

        for (int i = 0; i < totalHeight; i++) {
            boolean secondHalf = (i > (b.y - a.y)) || (b.y == a.y);
            int segmentHeight = secondHalf ? c.y - b.y : b.y - a.y;

            double alpha = (double) i / totalHeight;
            double beta = (double) (i - (secondHalf ? b.y - a.y : 0)) / segmentHeight;
            Point3D<Integer> A = new Point3D<>(a);
            A.x += round((c.x - a.x) * alpha);
            A.y += round((c.y - a.y) * alpha);
            A.z += round((c.z - a.z) * alpha);

            UvCoordinate uvPoint = new UvCoordinate(
                    (int)(a.getUvCoordinate().getX() + (c.getUvCoordinate().getX() - a.getUvCoordinate().getX()) * alpha),
                    (int)(a.getUvCoordinate().getY() + (c.getUvCoordinate().getY() - a.getUvCoordinate().getY()) * alpha),
                    0
            );

            A.setUvCoordinate(uvPoint);
            Point3D<Integer> B = secondHalf ? new Point3D<>(b) : new Point3D<>(a);
            B.x += round(secondHalf ? (c.x - b.x) * beta : (b.x - a.x) * beta);
            B.y += round(secondHalf ? (c.y - b.y) * beta : (b.y - a.y) * beta);
            B.z += round(secondHalf ? (c.z - b.z) * beta : (b.z - a.z) * beta);

            uvPoint = new UvCoordinate(
                    (int)(secondHalf ? b.getUvCoordinate().getX() + (c.getUvCoordinate().getX()-b.getUvCoordinate().getX())* beta
                            : a.getUvCoordinate().getX() + (b.getUvCoordinate().getX()-a.getUvCoordinate().getX())*beta),
                    (int)(secondHalf ? b.getUvCoordinate().getY() + (c.getUvCoordinate().getY()-b.getUvCoordinate().getY())* beta
                            : a.getUvCoordinate().getY() + (b.getUvCoordinate().getY()-a.getUvCoordinate().getY())*beta),
                    0
            );
            
            B.setUvCoordinate(uvPoint);
            
            if (A.x > B.x) {
                A.swap(B);
            }

            for (int j = A.x; j <= B.x; j++) {


                double phi = B.x == A.x ? 1 : (double) (j - A.x) / (double) (B.x - A.x);

                Point3D<Integer> point = new Point3D<>(A);

                point.x += round((B.x - A.x) * phi);
                point.y += round((B.y - A.y) * phi);
                point.z += round((B.z - A.z) * phi);

                uvPoint = new UvCoordinate(
                        (int)(A.getUvCoordinate().getX() + (B.getUvCoordinate().getX() - A.getUvCoordinate().getX()) * phi),
                        (int)(A.getUvCoordinate().getY() + (B.getUvCoordinate().getY() - A.getUvCoordinate().getY()) * phi),
                        0
                );

                point.setUvCoordinate(uvPoint);

                point.x += 600;
                point.y = 500 - point.y;
                synchronized (ZBuffer.getBuffer()) {
                    if (ZBuffer.getBuffer().get(point.x, point.y) < point.z) {

                        ZBuffer.getBuffer().set(point.x, point.y, point.z);


                        try {
                            drawingPanel.setRGB(point.x, point.y, object.getTexture()
                                    .getRGB(point.getUvCoordinate().getX(), point.getUvCoordinate().getY())); // a
                        }catch (Exception e) {
                            drawingPanel.setRGB(point.x, point.y, Color.red.getRGB());
                        }


                    }
                }


            }
        }


    }

    int round(double value) {
        if (value > 0) {
            value += 0.5;
        }
        if (value < 0) {
            value -= 0.5;
        }
        return (int) value;
    }

    private Point3D<Integer> transform(Point3D<Double> point3D) {
        Point3D<Integer> result = new Point3D<>();
        result.x = point3D.x.intValue();
        result.y = point3D.y.intValue();
        result.z = point3D.z.intValue();

        result.setUvCoordinate(point3D.getUvCoordinate());
        return result;
    }

    private double calculateSideIntensity() {
//        double cos = (normal.x * light_x + normal.y * light_y + normal.z * light_z) / (Math.sqrt(Math.pow(normal.x, 2)
//                + Math.pow(normal.y, 2) + Math.pow(normal.z, 2)) * Math.sqrt(Math.pow(light_x, 2) + Math.pow(light_y, 2) + Math.pow(light_z, 2)));
//        cos = Math.abs(cos);
//        int rouded = (int)((0.5 * 1 + lightIntensity * diffuseReflectionCoef * cos) * 100);
//        return (double) rouded / 100;

        return 1.0;

    }

}

