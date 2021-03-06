package hse.controllers;

import hse.*;
import hse.matrixes.Matrix;
import hse.matrixes.conversations.*;
import hse.objects.Object3D;
import hse.objects.Side;
import hse.ui.MainForm;
import hse.ui.SwapChain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Yura on 04.01.Stage.getInstance().017.
 */
public class Master implements Updater{


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
                nextTasks = new ArrayList<>();
                synchronized (taskThreadLock) {
                    taskThreadLock.notify();
                }

                for (int i = 0; i < WORKERS_COUNT; i++) {
                    workers.get(i).setTask(
                            currentTasks.subList(
                                    i * Stage.getInstance().getObjectCount(),
                                    i * Stage.getInstance().getObjectCount() + Stage.getInstance().getObjectCount())
                    );
                }


                do {
                    boolean flag = true;
                    for (int i = 0; i < currentTasks.size(); i++) {
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
                        if(form.picturePanel != null) {
                            form.picturePanel.forceUpdate();
                        }
                        ZBuffer.getBuffer().clear();
                        Stage.getInstance().getDisplayedObjects().forEach(Object3D::clear);
                        break;
                    } else {
                        try {
                            Thread.currentThread().sleep(5);
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
                conversations.set(0,3, currentObject.getXMove());
                conversations.set(1,3, currentObject.getYMove());
                conversations.set(2,3, currentObject.getZMove());

//                System.out.println(co/nversations);
                nextTasks.add(new Task(conversations, move, stage.getObject(j), Setings.fillType, isLightOn, mode));
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

    @Override
    public void redraw() {
        //do nothing its auto update
    }
}