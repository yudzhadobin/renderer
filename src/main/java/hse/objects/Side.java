package hse.objects;

import hse.*;
import hse.bsptree.Plane3D;
import hse.light.FillType;
import hse.light.SimpleIntensity;
import hse.matrixes.Matrix;
import hse.matrixes.conversations.MoveMatrix;
import hse.ui.SwapChain;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by Yura on 01.01.2017.
 */
public class Side {
    List<PointInfo> pointsInfo;

    public Side() {

    }

    public Side(List<PointInfo> infos) {
        this.pointsInfo = infos;
    }

    public Side(PointInfo... infos) {
        this.pointsInfo = Arrays.asList(infos);
    }

    public static List<Side> createSides(List<PointInfo> infos) {
        List<Side> result = new ArrayList<>();
        if(true) {
            for (int i = 0; i < infos.size() - 2; i++) {
                result.add(new Side(
                        Arrays.asList(
                                infos.get(0),
                                infos.get(i + 1),
                                infos.get(i + 2)
                        )
                ));
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public void drawContour(SwapChain swapChain, MoveMatrix move, Matrix projection, Object3D object) {
        Graphics graphics = swapChain.getDrawing().getGraphics();
        Point3DDouble a;
        Point3DDouble b;
        Point3DDouble c;
        if(Setings.oculusCullingMode == OculusCulling.BSP_TREE) {
            Matrix lookat = Camera.getInstance().lookat();
            Matrix viewPort = Camera.getInstance().viewport(
                    (Setings.WINDOW_WIDTH / 8),
                    (Setings.WINDOW_HEIGHT / 8),
                    (int)(Setings.WINDOW_WIDTH * 3/4d),
                    (int)(Setings.WINDOW_HEIGHT * 3/4d)
            );

            a = lookat.multiple(viewPort.multiple(pointsInfo.get(0).point));
            b = lookat.multiple(viewPort.multiple(pointsInfo.get(1).point));
            c = lookat.multiple(viewPort.multiple(pointsInfo.get(2).point));
        } else {
            a = pointsInfo.get(0).point;
            b = pointsInfo.get(1).point;
            c = pointsInfo.get(2).point;
        }

        Point3DDouble convertedA = a;
        Point3DDouble convertedB = b;
        Point3DDouble convertedC = c;


        graphics.drawLine(convertedA.x.intValue(), convertedA.y.intValue(), convertedB.x.intValue(), convertedB.y.intValue());
        graphics.drawLine(convertedB.x.intValue(), convertedB.y.intValue(), convertedC.x.intValue(), convertedC.y.intValue());
        graphics.drawLine(convertedC.x.intValue(), convertedC.y.intValue(), convertedA.x.intValue(), convertedA.y.intValue());

        object.box.extend(transform(convertedA));
        object.box.extend(transform(convertedB));
        object.box.extend(transform(convertedC));

    }

    @SuppressWarnings("unchecked")
    public void drawFill(SwapChain swapChain,MoveMatrix move, Matrix projection, Object3D object, Boolean isLightOn) {
        BufferedImage drawingPanel = swapChain.getDrawing();
        Point3DInteger a;
        Point3DInteger b;
        Point3DInteger c;

        if(Setings.oculusCullingMode == OculusCulling.BSP_TREE) {
            Matrix lookat = Camera.getInstance().lookat();
            Matrix viewPort = Camera.getInstance().viewport(
                    (Setings.WINDOW_WIDTH / 8),
                    (Setings.WINDOW_HEIGHT / 8),
                    (int)(Setings.WINDOW_WIDTH * 3/4d),
                    (int)(Setings.WINDOW_HEIGHT * 3/4d)
            );

            a = transform(lookat.multiple(viewPort.multiple(pointsInfo.get(0).point)));
            b = transform(lookat.multiple(viewPort.multiple(pointsInfo.get(1).point)));
            c = transform(lookat.multiple(viewPort.multiple(pointsInfo.get(2).point)));
        } else {
            a = transform(pointsInfo.get(0).point);
            b = transform(pointsInfo.get(1).point);
            c = transform(pointsInfo.get(2).point);
        }

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
            Point3DInteger A = new Point3DInteger(a);
            A.x += round((c.x - a.x) * alpha);
            A.y += round((c.y - a.y) * alpha);
            A.z += round((c.z - a.z) * alpha);


            Point3DInteger B = secondHalf ? new Point3DInteger(b) : new Point3DInteger(a);
            B.x += round(secondHalf ? (c.x - b.x) * beta : (b.x - a.x) * beta);
            B.y += round(secondHalf ? (c.y - b.y) * beta : (b.y - a.y) * beta);
            B.z += round(secondHalf ? (c.z - b.z) * beta : (b.z - a.z) * beta);


            if (A.x > B.x) {
                A.swap(B);
            }

            for (int j = A.x; j <= B.x; j++) {


                double phi = B.x.equals(A.x) ? 1 : (double) (j - A.x) / (double) (B.x - A.x);

                Point3DInteger point = new Point3DInteger(A);

                point.x += round((B.x - A.x) * phi);
                point.y += round((B.y - A.y) * phi);
                point.z += round((B.z - A.z) * phi);

                int savedZ = point.z;


                if (!checkPointIsIn(point)) {
                    continue;
                }

//                if (Setings.oculusCullingMode == OculusCulling.BSP_TREE) {
                if (false) {
                    drawingPanel.setRGB(point.x, point.y, color.getRGB());
                    object.box.extend(point);
                } else {
                    if (ZBuffer.getBuffer().get(point.x, point.y) < savedZ) {
                        ZBuffer.getBuffer().set(point.x, point.y, savedZ);
                        drawingPanel.setRGB(point.x, point.y, color.getRGB());
                        object.box.extend(point);
                    }
                }


            }
        }

    }



    @SuppressWarnings("unchecked")
    public void drawTextured(SwapChain swapChain, MoveMatrix move, Matrix projection, Object3D object, FillType fillType, Boolean isLightOn) {
        if(object.getTexture() != null) {
            switch (fillType) {
                case ORDINAL:
                    drawTexturedOrdinal(swapChain, move, projection, object, isLightOn);
                    break;
                case GURO:
                    drawTexturedGuro(swapChain, move, projection, object, isLightOn);
                    break;
                case FONG:
                    drawTexturedFong(swapChain, move, projection, object, isLightOn);
                    break;
            }
        } else {
            drawFill(swapChain, move, projection, object, isLightOn);
        }
    }



    @SuppressWarnings("unchecked")
    public void drawTexturedOrdinal(SwapChain swapChain, MoveMatrix move, Matrix projection, Object3D object, Boolean isLightOn) {
        BufferedImage drawingPanel = swapChain.getDrawing();


        Point3DInteger a = transform(pointsInfo.get(0).point);
        Point3DInteger b = transform(pointsInfo.get(1).point);
        Point3DInteger c = transform(pointsInfo.get(2).point);

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
            Point3DInteger A = new Point3DInteger(a);

            A.x += round((c.x - a.x) * alpha);
            A.y += round((c.y - a.y) * alpha);
            A.z += round((c.z - a.z) * alpha);

            UvCoordinate uvPointA = new UvCoordinate(
                    (int) (aUv.getX() + (cUv.getX() - aUv.getX()) * alpha),
                    (int) (aUv.getY() + (cUv.getY() - aUv.getY()) * alpha),
                    0
            );


            Point3DInteger B = secondHalf ? new Point3DInteger(b) : new Point3DInteger(a);
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

                Point3DInteger point = new Point3DInteger(A);

                point.x += round((B.x - A.x) * phi);
                point.y += round((B.y - A.y) * phi);
                point.z += round((B.z - A.z) * phi);

                UvCoordinate uvPoint = new UvCoordinate(
                        (int) (uvPointA.getX() + (uvPointB.getX() - uvPointA.getX()) * phi),
                        (int) (uvPointA.getY() + (uvPointB.getY() - uvPointA.getY()) * phi),
                        0
                );

                int savedZ = point.z;

                if(!checkPointIsIn(point)) {
                    continue;
                }

                if (ZBuffer.getBuffer().get(point.x, point.y) < savedZ) {
                    ZBuffer.getBuffer().set(point.x, point.y, savedZ);
                    drawingPanel.setRGB(point.x, point.y, getRGB(intensity, object.getTexture()
                            .getRGB(uvPoint.getX(), uvPoint.getY())));
                    object.box.extend(point);
                }


            }
        }


    }

    @SuppressWarnings("unchecked")
    public void drawTexturedGuro(SwapChain swapChain, MoveMatrix move, Matrix projection, Object3D object, Boolean isLightOn) {
        BufferedImage drawingPanel = swapChain.getDrawing();


        Point3DInteger a = transform(pointsInfo.get(0).point);
        Point3DInteger b = transform(pointsInfo.get(1).point);
        Point3DInteger c = transform(pointsInfo.get(2).point);

        PointInfo aInfo = pointsInfo.get(0);
        PointInfo bInfo = pointsInfo.get(1);
        PointInfo cInfo = pointsInfo.get(2);

        UvCoordinate aUv = new UvCoordinate(aInfo.getUvCoordinates());
        UvCoordinate bUv = new UvCoordinate(bInfo.getUvCoordinates());
        UvCoordinate cUv = new UvCoordinate(cInfo.getUvCoordinates());

        double aIntensity = SimpleIntensity.calculateIntensity(aInfo.transformedNormal, isLightOn);
        double bIntensity = SimpleIntensity.calculateIntensity(bInfo.transformedNormal, isLightOn);
        double cIntensity = SimpleIntensity.calculateIntensity(cInfo.transformedNormal, isLightOn);

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
            Point3DInteger A = new Point3DInteger(a);

            A.x += round((c.x - a.x) * alpha);
            A.y += round((c.y - a.y) * alpha);
            A.z += round((c.z - a.z) * alpha);

            UvCoordinate uvPointA = new UvCoordinate(
                    (int) (aUv.getX() + (cUv.getX() - aUv.getX()) * alpha),
                    (int) (aUv.getY() + (cUv.getY() - aUv.getY()) * alpha),
                    0
            );

            double intensityA = aIntensity + (cIntensity - aIntensity) * alpha;

            Point3DInteger B = secondHalf ? new Point3DInteger(b) : new Point3DInteger(a);
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

                Point3DInteger point = new Point3DInteger(A);

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

                if(!checkPointIsIn(point)) {
                    continue;
                }

                if (ZBuffer.getBuffer().get(point.x, point.y) < savedZ) {
                    ZBuffer.getBuffer().set(point.x, point.y, savedZ);
                      Color color;
                    if(Setings.drawingMode.equals(DrawingMode.TEXTURED)) {
                        drawingPanel.setRGB(point.x, point.y, getRGB(pointIntensity, object.getTexture()
                                .getRGB(uvPoint.getX(), uvPoint.getY())));
                    } else {
                        color = new Color((int) (255 * pointIntensity), (int) (255 * pointIntensity),
                                (int) (255 * pointIntensity));
                        drawingPanel.setRGB(point.x, point.y, color.getRGB());
                    }
                    drawingPanel.setRGB(point.x, point.y, getRGB(pointIntensity, object.getTexture()
                            .getRGB(uvPoint.getX(), uvPoint.getY())));
                    object.box.extend(point);


                }


            }
        }


    }

    @SuppressWarnings("unchecked")
    public void drawTexturedFong(SwapChain swapChain, MoveMatrix move, Matrix projection, Object3D object, Boolean isLightOn) {
        BufferedImage drawingPanel = swapChain.getDrawing();


        Point3DInteger a = transform(pointsInfo.get(0).point);
        Point3DInteger b = transform(pointsInfo.get(1).point);
        Point3DInteger c = transform(pointsInfo.get(2).point);

        PointInfo aInfo = pointsInfo.get(0);
        PointInfo bInfo = pointsInfo.get(1);
        PointInfo cInfo = pointsInfo.get(2);

        UvCoordinate aUv = new UvCoordinate(aInfo.getUvCoordinates());
        UvCoordinate bUv = new UvCoordinate(bInfo.getUvCoordinates());
        UvCoordinate cUv = new UvCoordinate(cInfo.getUvCoordinates());

        Normal aNormal = aInfo.getTransformedNormal().copy();
        Normal bNormal = bInfo.getTransformedNormal().copy();
        Normal cNormal = cInfo.getTransformedNormal().copy();

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
            Point3DInteger A = new Point3DInteger(a);

            A.x += round((c.x - a.x) * alpha);
            A.y += round((c.y - a.y) * alpha);
            A.z += round((c.z - a.z) * alpha);

            UvCoordinate uvPointA = new UvCoordinate(
                    (int) (aUv.getX() + (cUv.getX() - aUv.getX()) * alpha),
                    (int) (aUv.getY() + (cUv.getY() - aUv.getY()) * alpha),
                    0
            );

            Normal normalA = (new Normal()).plus(aNormal).plus((cNormal.minus(aNormal).multiple(alpha)));
            Point3DInteger B = secondHalf ? new Point3DInteger(b) : new Point3DInteger(a);
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

                Point3DInteger point = new Point3DInteger(A);

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

                if(!checkPointIsIn(point)) {
                    continue;
                }

                if (ZBuffer.getBuffer().get(point.x, point.y) < savedZ) {
                    ZBuffer.getBuffer().set(point.x, point.y, savedZ);
                    Color color;
                    if(Setings.drawingMode.equals(DrawingMode.TEXTURED)) {
                        drawingPanel.setRGB(point.x, point.y, getRGB(pointIntensity, object.getTexture()
                                .getRGB(uvPoint.getX(), uvPoint.getY())));
                    } else {
                        color = new Color((int) (255 * pointIntensity), (int) (255 * pointIntensity),
                                (int) (255 * pointIntensity));
                        drawingPanel.setRGB(point.x, point.y, color.getRGB());
                    }
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

    public Plane3D getPlane() {
        Normal normal;

        if (pointsInfo.stream().map(PointInfo::getTransformedNormal).anyMatch(Objects::isNull)) {
            normal = pointsInfo.stream()
                    .map(PointInfo::getNormal)
                    .reduce(Normal::plus).map(Normal::normalize).get();
        } else {
            normal = pointsInfo.stream()
                    .map(PointInfo::getTransformedNormal)
                    .reduce(Normal::plus).map(Normal::normalize).get();
        }
        return new Plane3D(normal, pointsInfo.get(0).point);
    }

    private Point3DInteger transform(Point3DDouble point3DDouble) {
        Point3DInteger result = new Point3DInteger();
        result.x = round(point3DDouble.x);
        result.y = round(point3DDouble.y);
        result.z = round(point3DDouble.z);

        return result;
    }

    private boolean checkPointIsIn(Point3DInteger point3DDouble) {
        return point3DDouble.getX() > 0 && point3DDouble.getY() > 0
                && point3DDouble.getX() < Setings.WINDOW_WIDTH && point3DDouble.getY() < Setings.WINDOW_HEIGHT;
    }

}

