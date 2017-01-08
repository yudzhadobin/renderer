package hse;

import hse.matrixes.Matrix;
import hse.matrixes.Projections;
import hse.objects.Object3D;
import hse.objects.Point3D;
import hse.objects.Side;
import hse.ui.SwapChain;
import sun.plugin2.message.Conversation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yura on 04.01.2017.
 */
public class Task {

    Map<Integer, Point3D<Double>> points = new HashMap<>();
    List<Side> sidesToDraw = new ArrayList<>();

    TaskStatus status = TaskStatus.WAITING;

    Object3D object3D;

    Matrix conversation;

    public void setPoints(Map<Integer, Point3D<Double>> points) {
        this.points = points;
    }

    public void setSidesToDraw(List<Side> sidesToDraw) {
        this.sidesToDraw = sidesToDraw;
    }

    public void addPoint(int index, Point3D<Double> point) {
        points.put(index, point);
    }

    public void addSide(Side side) {
        sidesToDraw.add(side);
    }

    public Task(Matrix conversation, Object3D object3D) {
        this.conversation = conversation;
        this.object3D = object3D;
    }

    public void complete() {
        status = TaskStatus.WORKING;

        points.forEach((key, value) -> {
            points.put(key, conversation.multiple(value));
        });

        for (Side side : sidesToDraw) {
            side.drawTextured(SwapChain.getInstance(), points, Projections.PERSPECTIVE, object3D);
        }

        status = TaskStatus.FINISHED;
    }

    @Override
    public String toString() {
        return status.toString();
    }
}
