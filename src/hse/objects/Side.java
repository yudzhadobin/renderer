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
    List<PointInfo> pointsInfo;

    static double diffuseReflectionCoef = .50;
    static double lightIntensity = 1;
    static int light_x = 50;
    static int light_y = 50;
    static int light_z = 500;


    public Side() {

    }

    public Side(List<PointInfo> infos) {
        this.pointsInfo = infos;
    }
    /*
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

    */

    @SuppressWarnings("unchecked")
    public void drawContour(SwapChain swapChain, Matrix projection) {
        Graphics graphics = swapChain.getDrawing().getGraphics();

        Point3D a = pointsInfo.get(0).point;
        Point3D b = pointsInfo.get(1).point;
        Point3D c = pointsInfo.get(2).point;

        Point3D<Double> convertedA = projection.multiple(a);
        Point3D<Double> convertedB = projection.multiple(b);
        Point3D<Double> convertedC = projection.multiple(c);

        graphics.drawLine(600 + convertedA.x.intValue(), 500 - convertedA.y.intValue(), 600 + convertedB.x.intValue(), 500 - convertedB.y.intValue());
        graphics.drawLine(600 + convertedB.x.intValue(), 500 - convertedB.y.intValue(), 600 + convertedC.x.intValue(), 500 - convertedC.y.intValue());
        graphics.drawLine(600 + convertedC.x.intValue(), 500 - convertedC.y.intValue(), 600 + convertedA.x.intValue(), 500 - convertedA.y.intValue());
    }

    @SuppressWarnings("unchecked")
    public void drawFill(SwapChain swapChain, Matrix projection) {
        BufferedImage drawingPanel = swapChain.getDrawing();


        Point3D<Integer> a = transform(pointsInfo.get(0).point);
        Point3D<Integer> b = transform(pointsInfo.get(1).point);
        Point3D<Integer> c = transform(pointsInfo.get(2).point);
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


        Color color = new Color((int) (255 * intensity), (int) (255 * intensity),
                (int) (255 * intensity));

        for (int i = 0; i < totalHeight; i++) {
            boolean secondHalf = (i > (b.y - a.y)) || (b.y.equals(a.y));
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


                double phi = B.x.equals(A.x) ? 1 : (double) (j - A.x) / (double) (B.x - A.x);

                Point3D<Integer> point = new Point3D<>(A);

                point.x += round((B.x - A.x) * phi);
                point.y += round((B.y - A.y) * phi);
                point.z += round((B.z - A.z) * phi);

                point.x += 600;
                point.y = 600 - point.y;

                if (ZBuffer.getBuffer().get(point.x, point.y) < point.z) {

                    ZBuffer.getBuffer().set(point.x, point.y, point.z);
                    drawingPanel.setRGB((int) (point.x), (int) (point.y), color.getRGB()); // attention, due to int casts convertedA.y+i != A.y


                }


            }
        }


    }


    @SuppressWarnings("unchecked")
    public void drawTextured(SwapChain swapChain, Matrix projection, Object3D object) {
        BufferedImage drawingPanel = swapChain.getDrawing();


        Point3D<Integer> a = transform(pointsInfo.get(0).point);
        Point3D<Integer> b = transform(pointsInfo.get(1).point);
        Point3D<Integer> c = transform(pointsInfo.get(2).point);

        PointInfo aInfo = pointsInfo.get(0);
        PointInfo bInfo = pointsInfo.get(1);
        PointInfo cInfo = pointsInfo.get(2);

        UvCoordinate aUv = new UvCoordinate(aInfo.getUvCoordinates());
        UvCoordinate bUv = new UvCoordinate(bInfo.getUvCoordinates());
        UvCoordinate cUv = new UvCoordinate(cInfo.getUvCoordinates());

        if (a.y > b.y) {
            a.swap(b);
            aUv.swap(bUv);
        }
        if (a.y > c.y) {
            a.swap(c);
            aUv.swap(cUv);
        }
        if (b.y > c.y) {
            b.swap(c);
            bUv.swap(cUv);
        }

        int totalHeight = c.y - a.y;

        double intensity = this.calculateSideIntensity();
        if (intensity < 0) {
            return;
        }

        for (int i = 0; i < totalHeight; i++) {
            boolean secondHalf = (i > (b.y - a.y)) || (b.y.equals(a.y));
            int segmentHeight = secondHalf ? c.y - b.y : b.y - a.y;

            double alpha = (double) i / totalHeight;
            double beta = (double) (i - (secondHalf ? b.y - a.y : 0)) / segmentHeight;
            Point3D<Integer> A = new Point3D<>(a);

            A.x += round((c.x - a.x) * alpha);
            A.y += round((c.y - a.y) * alpha);
            A.z += round((c.z - a.z) * alpha);

            UvCoordinate uvPointA = new UvCoordinate(
                    (int) (aUv.getX() + (cUv.getX() - aUv.getX()) * alpha),
                    (int) (aUv.getY() + (cUv.getY() - aUv.getY()) * alpha),
                    0
            );


            Point3D<Integer> B = secondHalf ? new Point3D<>(b) : new Point3D<>(a);
            B.x += round(secondHalf ? (c.x - b.x) * beta : (b.x - a.x) * beta);
            B.y += round(secondHalf ? (c.y - b.y) * beta : (b.y - a.y) * beta);
            B.z += round(secondHalf ? (c.z - b.z) * beta : (b.z - a.z) * beta);

            UvCoordinate uvPointB = new UvCoordinate(
                    (int) (secondHalf ? bUv.getX() + (cUv.getX() - bUv.getX()) * beta
                            : aUv.getX() + (bUv.getX() - aUv.getX()) * beta),
                    (int) (secondHalf ? bUv.getY() + (cUv.getY() - bUv.getY()) * beta
                            : aUv.getY() + (bUv.getY() - aUv.getY()) * beta),
                    0
            );


            if (A.x > B.x) {
                A.swap(B);
                uvPointA.swap(uvPointB);
            }

            for (int j = A.x; j <= B.x; j++) {


                double phi = B.x.equals(A.x) ? 1 : (double) (j - A.x) / (double) (B.x - A.x);

                Point3D<Integer> point = new Point3D<>(A);

                point.x += round((B.x - A.x) * phi);
                point.y += round((B.y - A.y) * phi);
                point.z += round((B.z - A.z) * phi);

                UvCoordinate uvPoint = new UvCoordinate(
                        (int) (uvPointA.getX() + (uvPointB.getX() - uvPointA.getX()) * phi),
                        (int) (uvPointA.getY() + (uvPointB.getY() - uvPointA.getY()) * phi),
                        0
                );

                point.x += 600;
                point.y = 500 - point.y;
                if (ZBuffer.getBuffer().get(point.x, point.y) < point.z) {

                    ZBuffer.getBuffer().set(point.x, point.y, point.z);
                    drawingPanel.setRGB(point.x, point.y, getRGB(intensity, object.getTexture()
                            .getRGB(uvPoint.getX(), uvPoint.getY())));

                }


            }
        }


    }

    int getRGB(double intensity, int rqbTextures) {
        int alpha = (int) (((rqbTextures >> 24) & 0xff) * intensity);
        int red = (int) (((rqbTextures >> 16) & 0xff) * intensity);
        int green = (int) (((rqbTextures >> 8) & 0xff) * intensity);
        int blue = (int) (((rqbTextures) & 0xff) * intensity);

        return new Color(red, green, blue, alpha).getRGB();
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

    public List<PointInfo> getPointsInfo() {
        return pointsInfo;
    }

    private Point3D<Integer> transform(Point3D<Double> point3D) {
        Point3D<Integer> result = new Point3D<>();
        result.x = round(point3D.x);
        result.y = round(point3D.y);
        result.z = round(point3D.z);

        return result;
    }

    private double calculateSideIntensity() {
        Normal normal = new Normal();
        normal.setX(
                (pointsInfo.get(0).getNormal().x +
                        pointsInfo.get(1).getNormal().x +
                        pointsInfo.get(2).getNormal().x) / 3
        );
        normal.setY(
                (pointsInfo.get(0).getNormal().y +
                        pointsInfo.get(1).getNormal().y +
                        pointsInfo.get(2).getNormal().y) / 3
        );
        normal.setZ(
                (pointsInfo.get(0).getNormal().z +
                        pointsInfo.get(1).getNormal().z +
                        pointsInfo.get(2).getNormal().z) / 3
        );

        double cos = (normal.x * light_x + normal.y * light_y + normal.z * light_z) / (Math.sqrt(Math.pow(normal.x, 2)
                + Math.pow(normal.y, 2) + Math.pow(normal.z, 2)) * Math.sqrt(Math.pow(light_x, 2) + Math.pow(light_y, 2) + Math.pow(light_z, 2)));
        cos = Math.abs(cos);

        double res = (0.5 * 0.2 + lightIntensity * diffuseReflectionCoef * cos);
        return res > 1 ? 1 : res;
    }

}

