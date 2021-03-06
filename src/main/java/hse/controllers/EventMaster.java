package hse.controllers;

import hse.*;
import hse.matrixes.Matrix;
import hse.matrixes.conversations.RotationX;
import hse.matrixes.conversations.RotationY;
import hse.matrixes.conversations.RotationZ;
import hse.matrixes.conversations.Scale;
import hse.objects.Object3D;
import hse.objects.Side;
import hse.ui.MainForm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuriy on 04.03.17.
 */
public class EventMaster implements Updater{

    public static final int WORKERS_COUNT = 3;

    List<Worker> workers = new ArrayList<>();


    Stage stage = Stage.getInstance();

   // TaskSet taskSet;

    MainForm form;

    public EventMaster( MainForm form) {
        for (int i = 0; i < WORKERS_COUNT; i++) {
            workers.add(new Worker());
        }

        this.form = form;
    }

    public void redraw() {
        if (Setings.oculusCullingMode == OculusCulling.BSP_TREE) {
            long start = System.currentTimeMillis();
            DrawingMode mode = Setings.drawingMode;
            boolean isLightOn = Setings.light_on;
            Stage.getInstance().getDisplayedObjects().forEach(Object3D::clear);
            Task task = new Task(null, null, null, Setings.fillType, isLightOn, mode);
            if(Setings.bspRebuild) {
                System.out.println("tree rebuild");
            }
            task.compeleWithBspTree();
            form.picturePanel.forceUpdate();
            Setings.bspRebuild = false;
            if(Setings.IS_DEBUG) {
                System.out.println(System.currentTimeMillis() - start);
            }
        } else {
            ZBuffer.getBuffer().clear();
            Stage.getInstance().getDisplayedObjects().forEach(Object3D::clear);

            TaskSet curTask = generateTaskSet();
            curTask.startTiming();

            for (int i = 0; i < WORKERS_COUNT; i++) {
                workers.get(i).setTask(
                        curTask.getTasks().subList(
                                i * Stage.getInstance().getObjectCount(),
                                i * Stage.getInstance().getObjectCount() + Stage.getInstance().getObjectCount())
                );
            }

            while (!curTask.isFinished()) {}

            curTask.endTiming();
            if(Setings.IS_DEBUG) {
                System.out.println(curTask.getPerformingTime());
            }
            form.picturePanel.forceUpdate();
        }
    }

    private TaskSet generateTaskSet() {
        DrawingMode mode = Setings.drawingMode;
        boolean isLightOn = Setings.light_on;

        TaskSet taskSet = new TaskSet();

        Object3D currentObject;
        for (int i = 0; i < WORKERS_COUNT; i++) {
            for (int j = 0; j < stage.getObjectCount(); j++) {
                currentObject = stage.getObject(j);
                Matrix a = new RotationX(currentObject.getXRotation());
                Matrix b = new RotationY(currentObject.getYRotation());
                Matrix c = new RotationZ(currentObject.getZRotation());
                Matrix scale = new Scale(currentObject.getScale());

//                MoveMatrix move = new MoveMatrix(currentObject.getXMove(), currentObject.getYMove(), currentObject.getZMove());
                Matrix conversations = a.multiple(b).multiple(c).multiple(scale);
                conversations.set(0,3, currentObject.getXMove());
                conversations.set(1,3, currentObject.getYMove());
                conversations.set(2,3, currentObject.getZMove());

//                System.out.println(co/nversations);
                taskSet.addTask(new Task(conversations, null, stage.getObject(j), Setings.fillType, isLightOn, mode));
            }
        }


        for (int j = 0; j < stage.getObjectCount(); j++) {
            currentObject = stage.getObject(j);
            for (int i = 0; i < currentObject.getSides().size(); i++) {
                Side curSide = currentObject.getSides().get(i);
                taskSet.getTask(i % WORKERS_COUNT * Stage.getInstance().getObjectCount() + j).addSide(curSide);
            }
        }

        return taskSet;
    }

}
