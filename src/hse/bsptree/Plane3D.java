package hse.bsptree;

import hse.objects.Normal;
import hse.objects.Point3DDouble;
import hse.objects.PointInfo;
import hse.objects.Side;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;

/**
 * Created by yuriy on 03.05.17.
 */
public class Plane3D {

    double x;
    double y;
    double z;
    double w;


    private final static double EPSILON = 0.1;

    private Point3DDouble cross(Point3DDouble a, Point3DDouble b) {
        double x = a.getY() * b.getZ() - a.getZ() * b.getY();
        double y = a.getZ() * b.getX() - a.getX() * b.getZ();
        double z = a.getX() * b.getY() - a.getY() * b.getX();

        Point3DDouble point3DDouble = new Point3DDouble(x, y, z);
        point3DDouble.setW(0d);

        return point3DDouble;
    }

    public Plane3D(Normal normal, Point3DDouble point) {

//        Point3DDouble cross = cross(b.minus(a), c.minus(b));
//
//        x = cross.getX();
//        y = cross.getY();
//        z = cross.getZ();
//        w = - multiple(cross, a);


        x = normal.getX();
        y = normal.getY();
        z = normal.getZ();
        w = -multiple(normal, point);
    }

    public Pair<SideLocation, Pair<List<Side>, List<Side>>> split(Side side) {
        List<Side> in = null;
        List<Side> out = null;
        PointInfo pointA, pointB;
        double pointADist, pointBDist;

        List<PointInfo> inPoints = new ArrayList<>();
        List<PointInfo> outPoints = new ArrayList<>();

        SideLocation currentLocation =  SideLocation.ON;
        pointA = side.getPointsInfo().get(2);

        pointADist = multiple(pointA.getPoint()); //may be bug


        List<PointInfo> points = side.getPointsInfo();

        for (int i = 0; i < points.size(); i++) {
            pointB = points.get(i);
            pointBDist = multiple(pointB.getPoint());

            if(pointBDist > EPSILON) {
                if(currentLocation == SideLocation.ON) {
                    currentLocation = SideLocation.OUT;
                } else if (currentLocation != SideLocation.OUT) {
                    currentLocation = SideLocation.SPANNING;
                }

                if(pointADist < -EPSILON) {
                    Point3DDouble point = pointB.getPoint().minus(pointA.getPoint());
                    Normal pointNormal = pointB.getNormal().minus(pointA.getNormal());

                    double phi = -(this.multiple(pointA.getPoint())) / this.multiple(point);


                    Point3DDouble finalPoint = pointA.getPoint().plus((
                            point.multiple(
                                    phi
                            ))
                    );

                    Normal normal = pointA.getNormal().plus(pointNormal.multiple(phi));

                    PointInfo pointInfo = new PointInfo(finalPoint, pointB.getUvCoordinates(), normal, -1);
                    pointInfo.setTransformedNormal(normal);
                    outPoints.add(pointInfo);
                    inPoints.add(pointInfo);
                    currentLocation = SideLocation.SPANNING;
                }
                outPoints.add(pointB);
            } else if (pointBDist < -EPSILON) {
                if(currentLocation == SideLocation.ON) {
                    currentLocation = SideLocation.IN;
                } else if (currentLocation != SideLocation.IN) {
                    currentLocation = SideLocation.SPANNING;
                }
                if (pointADist > EPSILON) {
                    Point3DDouble point = pointB.getPoint().minus(pointA.getPoint());
                    Normal pointNormal = pointB.getNormal().minus(pointA.getNormal());

                    double phi = -(this.multiple(pointA.getPoint())) / this.multiple(point);


                    Point3DDouble finalPoint = pointA.getPoint().plus((
                            point.multiple(
                                     phi
                            ))
                    );

                    Normal normal = pointA.getNormal().plus(pointNormal.multiple(phi));
                    PointInfo pointInfo = new PointInfo(finalPoint, pointB.getUvCoordinates(), normal, -1);
                    pointInfo.setTransformedNormal(normal);

                    outPoints.add(pointInfo);
                    inPoints.add(pointInfo);
                    currentLocation = SideLocation.SPANNING;
                }
                inPoints.add(pointB);
            } else {
                outPoints.add(pointB);
                inPoints.add(pointB);
            }
            pointA = pointB;
            pointADist = pointBDist;
        }
        switch (currentLocation) {
            case IN:
                in = singletonList(side);
                break;
            case ON:
                break;
            case OUT:
                out = singletonList(side);
                break;
            case SPANNING:
                in = divade(new Side(inPoints));
                out = divade(new Side(outPoints));
                break;
        }
        return new Pair<>(currentLocation, new Pair<>(in, out));
    }

    public double multiple( Point3DDouble point) {
        return this.x * point.getX() +
                this.y * point.getY() +
                this.z * point.getZ() +
                this.w * point.getW();
    }


    public double multiple(Point3DDouble normal, Point3DDouble point) {
        return normal.getX() * point.getX() +
                normal.getY() * point.getY() +
                normal.getZ() * point.getZ() +
                normal.getW() * point.getW();
    }



     public List<Side> divade(Side side) {
        List<Side> result = new ArrayList<>();

        if(side.getPointsInfo().size() > 3) {
            result.add(
                    new Side(
                            side.getPointsInfo().get(0),
                            side.getPointsInfo().get(1),
                            side.getPointsInfo().get(2)
                    )
            );
            result.add(
                    new Side(
                            side.getPointsInfo().get(0),
                            side.getPointsInfo().get(2),
                            side.getPointsInfo().get(3)
                    )
            );
        } else {
            result.add(side);
        }

        return result;
     }
}


