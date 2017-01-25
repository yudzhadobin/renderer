package hse;

import com.sun.org.apache.xerces.internal.impl.dv.xs.DoubleDV;
import com.sun.org.apache.xpath.internal.operations.Bool;
import hse.light.FillType;
import hse.matrixes.Matrix;
import hse.matrixes.Projections;
import hse.objects.ChangeableSupplier;
import hse.objects.Object3D;
import hse.objects.Point3D;
import hse.objects.Side;
import hse.ui.SwapChain;
import javafx.util.Pair;
import sun.plugin2.message.Conversation;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Created by Yura on 04.01.2017.
 */
public class Task {

    long time;

    List<Side> sidesToDraw = new ArrayList<>();

    TaskStatus status = TaskStatus.WAITING;

    Object3D object3D;

    Matrix rotation;
    Matrix move;

    FillType fillType;

    public void setSidesToDraw(List<Side> sidesToDraw) {
        this.sidesToDraw = sidesToDraw;
    }


    public void addSide(Side side) {
        sidesToDraw.add(side);
    }

    public Task(Matrix rotation, Matrix move, Object3D object3D, FillType fillType) {
        this.rotation = rotation;
        this.move = move;
        this.object3D = object3D;
        this.fillType = fillType;
    }

    public void complete() {
        time = System.currentTimeMillis();
        status = TaskStatus.WORKING;

        sidesToDraw.forEach(
                side -> {
                    side.getPointsInfo().forEach(
                            pointInfo -> {
                                Point3D<Double> localPoint = object3D.getLocalPoints().get(
                                        pointInfo.getIndex()
                                );
                                Pair<ChangeableSupplier<Boolean>, Point3D<Double>> pair = object3D.getTransformedPoints().
                                        get(localPoint);
                                if (!pair.getKey().get()) {
                                    Point3D<Double> point3D = pair.getValue();
                                    point3D.swap(rotation.multiple(localPoint));
//                                    point3D.setX(point3D.getX() + move.get(0,3));
//                                    point3D.setY(point3D.getY() + move.get(1,3));
//                                    point3D.setZ(point3D.getZ() + move.get(2,3));

                                    pair.getKey().set(true);
                                }
                            }
                    );
                }
        );

        for (Side side : sidesToDraw) {
            side.drawTextured(SwapChain.getInstance(), Projections.PERSPECTIVE, object3D, fillType);
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
