package hse.json;

import hse.DrawingMode;
import hse.Setings;

/**
 * Created by yuriy on 15.05.17.
 */
public class Environment {

    DrawingMode drawingMode;

    public DrawingMode getDrawingMode() {
        return drawingMode;
    }

    public void setDrawingMode(DrawingMode drawingMode) {
        this.drawingMode = drawingMode;
    }


    public void init() {
        Setings.drawingMode = drawingMode;
    }
}
