package hse.light;

import hse.Stage;
import hse.objects.Normal;
import hse.objects.PointInfo;
import hse.objects.Side;

import java.util.List;

/**
 * Created by Yura on 09.01.2017.
 */
public class SimpleIntensity {

    public static double calculateIntensity(Side side) {
        List<PointInfo> pointsInfo = side.getPointsInfo();
        Normal normal = new Normal();
        normal.setX(
                (pointsInfo.get(0).getNormal().getX() +
                        pointsInfo.get(1).getNormal().getX() +
                        pointsInfo.get(2).getNormal().getX()) / 3
        );
        normal.setY(
                (pointsInfo.get(0).getNormal().getY() +
                        pointsInfo.get(1).getNormal().getY() +
                        pointsInfo.get(2).getNormal().getY()) / 3
        );
        normal.setZ(
                (pointsInfo.get(0).getNormal().getZ() +
                        pointsInfo.get(1).getNormal().getZ() +
                        pointsInfo.get(2).getNormal().getZ()) / 3
        );
        return calculateIntensity(normal);
    }


    public static double calculateIntensity(Normal normal) {
        double cos = (normal.getX() * Stage.light_x + normal.getY() * Stage.light_y + normal.getZ() * Stage.light_z) / (Math.sqrt(Math.pow(normal.getX(), 2)
                + Math.pow(normal.getY(), 2) + Math.pow(normal.getZ(), 2)) * Math.sqrt(Math.pow(Stage.light_x, 2) + Math.pow(Stage.light_y, 2) + Math.pow(Stage.light_z, 2)));
        cos = Math.abs(cos);
        return  0.5 * 0.2 + Stage.lightIntensity * Stage.diffuseReflectionCoef * cos;

    }

}
