package hse.objects;

import hse.UvCoordinate;
import hse.ZBuffer;
import hse.light.FillType;
import hse.light.SimpleIntensity;
import hse.matrixes.Matrix;
import hse.ui.SwapChain;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

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
    public void drawContour(SwapChain swapChain, Matrix projection, Object3D object) {
        Graphics graphics = swapChain.getDrawing().getGraphics();

        Point3D a = pointsInfo.get(0).point;
        Point3D b = pointsInfo.get(1).point;
        Point3D c = pointsInfo.get(2).point;

        Point3D<Double> convertedA = projection.multiple(a);
        Point3D<Double> convertedB = projection.multiple(b);
        Point3D<Double> convertedC = projection.multiple(c);
        convertedA.setX(convertedA.getX() + 600);
        convertedA.setY(500 - convertedA.getY());

        convertedB.setX(convertedB.getX() + 600);
        convertedB.setY(500 - convertedB.getY());

        convertedC.setX(convertedC.getX() + 600);
        convertedC.setY(500 - convertedC.getY());
        graphics.drawLine(convertedA.x.intValue(), convertedA.y.intValue(), convertedB.x.intValue(), convertedB.y.intValue());
        graphics.drawLine(convertedB.x.intValue(), convertedB.y.intValue(), convertedC.x.intValue(), convertedC.y.intValue());
        graphics.drawLine(convertedC.x.intValue(), convertedC.y.intValue(), convertedA.x.intValue(), convertedA.y.intValue());

        object.box.extend(new Point3D<>(convertedA.getX().intValue(), convertedA.getY().intValue(), convertedA.getZ().intValue()));
        object.box.extend(new Point3D<>(convertedB.getX().intValue(), convertedB.getY().intValue(), convertedB.getZ().intValue()));
        object.box.extend(new Point3D<>(convertedC.getX().intValue(), convertedC.getY().intValue(), convertedC.getZ().intValue()));


    }

    @SuppressWarnings("unchecked")
    public void drawFill(SwapChain swapChain, Matrix projection, Object3D object3D, Boolean isLightOn) {
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

        double intensity = SimpleIntensity.calculateIntensity(this, isLightOn);


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
                point.y = 500 - point.y;

                if (ZBuffer.getBuffer().get(point.x, point.y) < point.z) {

                    ZBuffer.getBuffer().set(point.x, point.y, point.z);
                    drawingPanel.setRGB((int) (point.x), (int) (point.y), color.getRGB()); // attention, due to int casts convertedA.y+i != A.y
                    object3D.box.extend(point);

                }


            }
        }


    }



    @SuppressWarnings("unchecked")
    public void drawTextured(SwapChain swapChain, Matrix projection, Object3D object, FillType fillType, Boolean isLightOn) {
        switch (fillType) {
            case ORDINAL:
                drawTexturedOrdinal(swapChain, projection, object, isLightOn);
                break;
            case GURO:
                drawTexturedGuro(swapChain, projection, object, isLightOn);
                break;
            case FONG:
                drawTexturedFong(swapChain, projection, object, isLightOn);
                break;
        }
    }



    @SuppressWarnings("unchecked")
    public void drawTexturedOrdinal(SwapChain swapChain, Matrix projection, Object3D object, Boolean isLightOn) {
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

        double intensity = SimpleIntensity.calculateIntensity(this, isLightOn);
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
                    object.box.extend(point);
                }


            }
        }


    }

    @SuppressWarnings("unchecked")
    public void drawTexturedGuro(SwapChain swapChain, Matrix projection, Object3D object, Boolean isLightOn) {
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

        double aIntensity = SimpleIntensity.calculateIntensity(aInfo.normal, isLightOn);
        double bIntensity = SimpleIntensity.calculateIntensity(bInfo.normal, isLightOn);
        double cIntensity = SimpleIntensity.calculateIntensity(cInfo.normal, isLightOn);

        if (a.y > b.y) {
            a.swap(b);
            aUv.swap(bUv);
            double sup = aIntensity;
            aIntensity = bIntensity;
            bIntensity = sup;
        }
        if (a.y > c.y) {
            a.swap(c);
            aUv.swap(cUv);

            double sup = aIntensity;
            aIntensity = cIntensity;
            cIntensity = sup;

        }
        if (b.y > c.y) {
            b.swap(c);
            bUv.swap(cUv);

            double sup = bIntensity;
            bIntensity = cIntensity;
            cIntensity = sup;

        }

        int totalHeight = c.y - a.y;

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

            double intensityA = aIntensity + (cIntensity - aIntensity) * alpha;

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

            double intensityB = secondHalf ? bIntensity + (cIntensity - bIntensity) * beta
                    : aIntensity + (bIntensity - aIntensity) * beta;

            if (A.x > B.x) {
                A.swap(B);
                uvPointA.swap(uvPointB);

                double sup = intensityB;
                intensityB = intensityA;
                intensityA = sup;

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

                double pointIntensity = intensityA + (intensityB - intensityA) * phi;
                int savedZ = point.z;
                point = projection.multipleInteger(point);
                point.x += 600 + object.getXMove();
                point.y = 500 - point.y + object.getYMove();
                point.z += object.getYMove();
                if (ZBuffer.getBuffer().get(point.x, point.y) < savedZ) {
                    ZBuffer.getBuffer().set(point.x, point.y, savedZ);
                    drawingPanel.setRGB(point.x, point.y, getRGB(pointIntensity, object.getTexture()
                            .getRGB(uvPoint.getX(), uvPoint.getY())));
                    object.box.extend(point);


                }


            }
        }


    }

    @SuppressWarnings("unchecked")
    public void drawTexturedFong(SwapChain swapChain, Matrix projection, Object3D object, Boolean isLightOn) {
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

        Normal aNormal = aInfo.normal.copy();
        Normal bNormal = bInfo.normal.copy();
        Normal cNormal = cInfo.normal.copy();

        if (a.y > b.y) {
            a.swap(b);
            aUv.swap(bUv);
            aNormal.swap(bNormal);


        }
        if (a.y > c.y) {
            a.swap(c);
            aUv.swap(cUv);

            aNormal.swap(cNormal);

        }
        if (b.y > c.y) {
            b.swap(c);
            bUv.swap(cUv);

            bNormal.swap(cNormal);
        }

        int totalHeight = c.y - a.y;

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

            Normal normalA = (new Normal()).plus(aNormal).plus((cNormal.minus(aNormal).multiple(alpha)));
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

            Normal normalB = new Normal().plus( secondHalf ? bNormal.plus((cNormal.minus(bNormal).multiple(beta)))
                    : aNormal.plus((bNormal.minus(aNormal).multiple(beta))));
            if (A.x > B.x) {
                A.swap(B);
                uvPointA.swap(uvPointB);

               normalA.swap(normalB);

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

                double pointIntensity = SimpleIntensity.calculateIntensity(new Normal().plus(
                        normalA.plus((normalB.minus(normalA).multiple(phi)))), isLightOn);
                int savedZ = point.z;
                point = projection.multipleInteger(point);
                point.x += 600;
                point.y = 500 - point.y;
                if (ZBuffer.getBuffer().get(point.x, point.y) < savedZ) {
                    ZBuffer.getBuffer().set(point.x, point.y, savedZ);
                    drawingPanel.setRGB(point.x, point.y, getRGB(pointIntensity, object.getTexture()
                            .getRGB(uvPoint.getX(), uvPoint.getY())));
                    object.box.extend(point);

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



}

