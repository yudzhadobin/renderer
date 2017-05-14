package hse.controllers;

import hse.DrawingMode;
import hse.Setings;
import hse.TaskStatus;
import hse.ZBuffer;
import hse.bsptree.BspTree;
import hse.bsptree.SideLocation;
import hse.light.FillType;
import hse.matrixes.Matrix;
import hse.matrixes.conversations.MoveMatrix;
import hse.objects.*;
import hse.ui.SwapChain;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

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
            System.out.println("Bsp tree rebuild");
            instance.clear();

            sidesToDraw.forEach(
                    side -> {
                        side.getPointsInfo().forEach(
                                pointInfo -> {
                                    Point3DDouble localPoint = object3D.getLocalPoints().get(
                                            pointInfo.getIndex()
                                    );

                                    pointInfo.setTransformedNormal(new Normal(rotation.multiple(pointInfo.getNormal())));
                                    Point3DDouble converted = (Setings.projection.multiple(rotation.multiple(localPoint)));
                                    pointInfo.getPoint().swap(converted);
                                }
                        );
                    }
            );

            instance.insert(sidesToDraw, SideLocation.SPANNING, SideLocation.ON);
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
