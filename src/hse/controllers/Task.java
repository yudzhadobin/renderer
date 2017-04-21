package hse.controllers;

import com.sun.org.apache.xerces.internal.impl.dv.xs.DoubleDV;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.sun.scenario.Settings;
import hse.DrawingMode;
import hse.Setings;
import hse.TaskStatus;
import hse.light.FillType;
import hse.matrixes.Matrix;
import hse.matrixes.Projections;
import hse.matrixes.conversations.MoveMatrix;
import hse.objects.*;
import hse.ui.SettingsForm;
import hse.ui.SwapChain;
import javafx.util.Pair;
import sun.plugin2.message.Conversation;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Created by Yura on 04.01.2017.
 */
public class Task {

    private final boolean isLightOn;
    private final DrawingMode drawingMode;
    long time;

    List<Side> sidesToDraw = new ArrayList<>();

    TaskStatus status = TaskStatus.WAITING;

    Object3D object3D;

    Matrix rotation;
    MoveMatrix move;

    FillType fillType;

    public void setSidesToDraw(List<Side> sidesToDraw) {
        this.sidesToDraw = sidesToDraw;
    }


    public void addSide(Side side) {
        sidesToDraw.add(side);
    }

    public Task(Matrix rotation, MoveMatrix move, Object3D object3D, FillType fillType, boolean isLightOn, DrawingMode drawingMode) {
        this.rotation = rotation;
        this.move = move;
        this.object3D = object3D;
        this.fillType = fillType;
        this.isLightOn = isLightOn;
        this.drawingMode = drawingMode;
    }

    public void complete() {
        time = System.currentTimeMillis();
        status = TaskStatus.WORKING;

        Matrix lookat = Camera.getInstance().lookat();
        Matrix viewPort = Camera.getInstance().viewport(
                (Setings.WINDOW_WIDTH / 8),
                (Setings.WINDOW_HEIGHT / 8),
                (int)(Setings.WINDOW_WIDTH * 3/4d),
                (int)(Setings.WINDOW_HEIGHT * 3/4d)
        );

        sidesToDraw.forEach(
                side -> {
                    side.getPointsInfo().forEach(
                            pointInfo -> {
                                Point3D<Double> localPoint = object3D.getLocalPoints().get(
                                        pointInfo.getIndex()
                                );

                                pointInfo.setTransformedNormal(new Normal(rotation.multiple(pointInfo.getNormal())));

                                Pair<ChangeableSupplier<Boolean>, Point3D<Double>> pair = object3D.getTransformedPoints().
                                        get(localPoint);
                                if (!pair.getKey().get()) {
                                    Point3D<Double> point3D = pair.getValue();
                                    Point3D<Double> converted = lookat.multiple(viewPort.multiple(Setings.projection.multiple(rotation.multiple(localPoint))));
                                    point3D.swap( converted);

                                    pair.getKey().set(true);
                                }
                            }
                    );
                }
        );

        for (Side side : sidesToDraw) {
            switch (drawingMode) {
            case CONTOUR: {
                side.drawContour(SwapChain.getInstance(),  move, Setings.projection, object3D);
                break;
            }
            case MODEL: {
                side.drawFill(SwapChain.getInstance(), move, Setings.projection, object3D, isLightOn);
                break;
            }
            case TEXTURED: {
                side.drawTextured(SwapChain.getInstance(),move, Setings.projection, object3D, fillType, isLightOn);
                break;
            }
            }
        }

        status = TaskStatus.FINISHED;
        time = System.currentTimeMillis() - time;
    }


    public long getTime() {
        return time;
    }

    @Override
    public String toString() {
        return status.toString();
    }
}
