package hse.controllers;

import hse.*;
import hse.bsptree.BspTree;
import hse.bsptree.SideLocation;
import hse.light.FillType;
import hse.matrixes.Matrix;
import hse.matrixes.conversations.*;
import hse.objects.*;
import hse.ui.SwapChain;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void addSides(List<Side> sides) {
        sidesToDraw.addAll(sides);
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
                                Point3DDouble localPoint = object3D.getLocalPoints().get(
                                        pointInfo.getIndex()
                                );

                                pointInfo.setTransformedNormal(new Normal(rotation.multiple(pointInfo.getNormal())));

                                Pair<ChangeableSupplier<Boolean>, Point3DDouble> pair = object3D.getTransformedPoints().
                                        get(localPoint);
                                if (!pair.getKey().get()) {
                                    Point3DDouble point3DDouble = pair.getValue();
                                    Point3DDouble converted = lookat.multiple(viewPort.multiple(Setings.projection.multiple(rotation.multiple(localPoint))));
                                    point3DDouble.swap( converted);

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


    public void compeleWithBspTree() {
        BspTree instance = BspTree.getInstance();
        time = System.currentTimeMillis();
        status = TaskStatus.WORKING;
        if(Setings.bspRebuild) {

            instance.clear();
            Map<Object3D, List<Side>> sides = new HashMap<>();
            Stage.getInstance().getDisplayedObjects().forEach(object -> {
                sides.put(object, object.getSides());
            });

            for (Map.Entry<Object3D, List<Side>> object3DListEntry : sides.entrySet()) {
                Object3D currentObject = object3DListEntry.getKey();
                Matrix a = new RotationX(currentObject.getXRotation());
                Matrix b = new RotationY(currentObject.getYRotation());
                Matrix c = new RotationZ(currentObject.getZRotation());
                Matrix scale = new Scale(currentObject.getScale());

                Matrix conversations = a.multiple(b).multiple(c).multiple(scale);
                conversations.set(0,3, currentObject.getXMove());
                conversations.set(1,3, currentObject.getYMove());
                conversations.set(2,3, currentObject.getZMove());
                object3DListEntry.getValue().forEach(
                        side -> {
                            side.getPointsInfo().forEach(
                                    pointInfo -> {
                                        Point3DDouble localPoint = object3DListEntry.getKey().getLocalPoints().get(
                                                pointInfo.getIndex()
                                        );

                                        Pair<ChangeableSupplier<Boolean>, Point3DDouble> pair = object3DListEntry.getKey().getTransformedPoints().
                                                get(localPoint);
                                        pointInfo.setTransformedNormal(new Normal(conversations.multiple(pointInfo.getNormal())));
                                        if (!pair.getKey().get()) {
                                            Point3DDouble point3DDouble = pair.getValue();
                                            Point3DDouble converted = Setings.projection.multiple(conversations.multiple(localPoint));
                                            point3DDouble.swap(converted);
                                            pair.getKey().set(true);
                                        };
                                    }
                            );
                        }
                );
            }
            instance.insert(sides, SideLocation.SPANNING, SideLocation.ON);
        }
        instance.draw();
        ZBuffer.getBuffer().clear();
    }

    public long getTime() {
        return time;
    }


    public boolean isFinished() {
        return status == TaskStatus.FINISHED;
    }

    @Override
    public String toString() {
        return status.toString();
    }
}
