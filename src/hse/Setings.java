package hse;

import hse.light.FillType;
import hse.matrixes.Matrix;
import hse.matrixes.Projections;

/**
 * Created by Yura on 25.12.2016.
 */
public class Setings {

    public static int offset_X = 0;
    public static int offset_Y = 0;
    public static int offset_Z = 0;

    public static int WINDOW_HEIGHT = 800;
    public static int WINDOW_WIDTH = 800;

    public static boolean light_on = false;

    public static Matrix projection = Projections.PERSPECTIVE;
    public static DrawingMode drawingMode = DrawingMode.MODEL;
    public static FillType fillType = FillType.GURO;
}
