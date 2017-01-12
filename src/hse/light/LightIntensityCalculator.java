package hse.light;

import hse.objects.Point3D;
import hse.objects.PointInfo;

/**
 * Created by Yura on 09.01.2017.
 */
public interface LightIntensityCalculator {

    double diffuseReflectionCoef = .50;
    double lightIntensity = 1;
    int light_x = 50;
    int light_y = 50;
    int light_z = 500;


    double getIntensity();
}
