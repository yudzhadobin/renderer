package hse;

import com.sun.scenario.Settings;
import hse.light.FillType;
import hse.matrixes.Matrix;
import hse.matrixes.conversations.MoveMatrix;
import hse.matrixes.conversations.RotationX;
import hse.matrixes.conversations.RotationY;
import hse.matrixes.conversations.RotationZ;
import hse.matrixes.conversations.Scale;
import hse.objects.Object3D;
import hse.objects.Side;
import hse.ui.MainForm;
import hse.ui.SwapChain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Yura on 04.01.2017.
 */
public class Master {


    public static final int WORKERS_COUNT = 3;

//    Object3D object3D;
    List<Worker> workers = new ArrayList<>();
    List<Task> currentTasks = new ArrayList<>();
    List<Task> nextTasks = new ArrayList<>();

    Thread taskThread = new Thread(this::generateTasks);
    Thread swapThread = new Thread(this::swap);

    MainForm form;

    Lock taskThreadLock = new ReentrantLock(true);
    Lock swapThreadLock = new ReentrantLock(true);

    public Master( MainForm form) {
        for (int i = 0; i < WORKERS_COUNT; i++) {
            workers.add(new Worker());
        }


        taskThread.start();

        this.form = form;

    }
    void generateTasks() {
        boolean isFirst = true;
        do {
            synchronized (taskThreadLock) {
                createTasks(Stage.getInstance());
                synchronized (swapThreadLock) {
                    swapThreadLock.notify();
                }

                if (isFirst) {
                    isFirst = false;
                    swapThread.start();
                }
                try {
                    taskThreadLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } while (true);
    }

    void swap() {
        do {
            synchronized (swapThreadLock) {

                currentTasks = nextTasks;
                nextTasks = new ArrayList<>();// TODO: 05.01.2017 rewrite
                synchronized (taskThreadLock) {
                    taskThreadLock.notify();
                }

                for (int i = 0; i < WORKERS_COUNT; i++) {
                    workers.get(i).setTask(currentTasks.subList(i * 2, i * 2 + 2));
                }


                do {
                    boolean flag = true;
                    for (int i = 0; i < WORKERS_COUNT * Stage.getInstance().getObjectCount(); i++) {
                        if (currentTasks.get(i).status != TaskStatus.FINISHED) {
                            flag = false;
                            break;
                        }
                    }

                    if(flag) {
                        if(Stage.getInstance().getDisplayedObjects().size() == 1) {
                            Stage.getInstance().getDisplayedObjects().forEach(object3D -> {
                                object3D.getBox().draw(SwapChain.getInstance());
                            });
                        }

                        //                        object3D.getBox().draw(SwapChain.getInstance());
//                        long maxTime = -10;
//                        for (int i = 0; i < currentTasks.size(); i++) {
//                            maxTime = currentTasks.get(i).getTime() > maxTime ? currentTasks.get(i).getTime() : maxTime;
//                        }
//                        System.out.println(maxTime);
                        form.picturePanel.repaint();
                        ZBuffer.getBuffer().clear();
                        Stage.getInstance().getDisplayedObjects().forEach(Object3D::clear);

                        break;
                    } else {
                        try {
                            Thread.currentThread().sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }while (true);


                try {
                    swapThreadLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } while (true);
    }
/*
//    int rotate = 0;
    private void createTasks() {
        Matrix a = new RotationX(object3D.getXRotation());
        Matrix b = new RotationY(object3D.getYRotation());
        Matrix c = new RotationZ(object3D.getZRotation());
        Matrix scale = new Scale(object3D.getScale());
        object3D.setXMove(Setings.offset_X);
        object3D.setYMove(Setings.offset_Y);
        object3D.setZMove(Setings.offset_Z);
        Matrix move = new MoveMatrix(Setings.offset_X, Setings.offset_Y, Setings.offset_Z);
        Matrix conversations = a.multiple(b).multiple(c).multiple(scale);
        DrawingMode mode = Setings.drawingMode;
        boolean isLightOn = Setings.light_on;

        for (int i = 0; i < WORKERS_COUNT; i++) {
            nextTasks.add(new Task(conversations, move, object3D, FillType.GURO, isLightOn, mode));
        }

        for (int i = 0; i < object3D.getSides().size(); i++) {
            Side curSide = object3D.getSides().get(i);
            nextTasks.get(i % WORKERS_COUNT).addSide(curSide);
        }


    }*/

    private void createTasks(Stage stage) {

        DrawingMode mode = Setings.drawingMode;
        boolean isLightOn = Setings.light_on;

        Object3D currentObject;
        for (int i = 0; i < WORKERS_COUNT; i++) {
            for (int j = 0; j < stage.getObjectCount(); j++) {
                currentObject = stage.getObject(j);
                Matrix a = new RotationX(currentObject.getXRotation());
                Matrix b = new RotationY(currentObject.getYRotation());
                Matrix c = new RotationZ(currentObject.getZRotation());
                Matrix scale = new Scale(currentObject.getScale());

                MoveMatrix move = new MoveMatrix(currentObject.getXMove(), currentObject.getYMove(), currentObject.getZMove());
                Matrix conversations = a.multiple(b).multiple(c).multiple(scale);

                nextTasks.add(new Task(conversations, move, stage.getObject(j), FillType.GURO, isLightOn, mode));
            }
        }

        for (int j = 0; j < stage.getObjectCount(); j++) {
            currentObject = stage.getObject(j);
            for (int i = 0; i < currentObject.getSides().size(); i++) {
                Side curSide = currentObject.getSides().get(i);
                nextTasks.get(i % WORKERS_COUNT * Stage.getInstance().getObjectCount() + j).addSide(curSide);
            }
        }

    }
}