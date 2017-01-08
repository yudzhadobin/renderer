package hse;

import hse.matrixes.Matrix;
import hse.matrixes.conversations.RotationX;
import hse.matrixes.conversations.RotationY;
import hse.matrixes.conversations.Scale;
import hse.objects.Object3D;
import hse.objects.Side;
import hse.ui.MainForm;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Yura on 04.01.2017.
 */
public class Master {


    public static final int WORKERS_COUNT = 1;

    Object3D object3D;
    List<Worker> workers = new ArrayList<>();
    List<Task> currentTasks = new ArrayList<>();
    List<Task> nextTasks = new ArrayList<>();

    Thread taskThread = new Thread(this::generateTasks);
    Thread swapThread = new Thread(this::swap);

    MainForm form;

    Lock taskThreadLock = new ReentrantLock(true);
    Lock swapThreadLock = new ReentrantLock(true);

    public Master(Object3D object3D, MainForm form) {
        for (int i = 0; i < WORKERS_COUNT; i++) {
            workers.add(new Worker());
        }


        taskThread.start();

        this.object3D = object3D;
        this.form = form;

    }

    public void setObject3D(Object3D object3D) {
        this.object3D = object3D;
    }

    void generateTasks() {
        int z = 0;
        boolean isFirst = true;
        do {
            synchronized (taskThreadLock) {
                createTasks();
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
        int z = 0;
        do {
            synchronized (swapThreadLock) {

                currentTasks = nextTasks;
                nextTasks = new ArrayList<>();// TODO: 05.01.2017 rewrite
                //System.out.println("swapping");
                synchronized (taskThreadLock) {
                    taskThreadLock.notify();
                }

                for (int i = 0; i < WORKERS_COUNT; i++) {
                    workers.get(i).setTask(currentTasks.get(i));
                }


                do {
                    boolean flag = true;
                    for (int i = 0; i < WORKERS_COUNT; i++) {
                        if (currentTasks.get(i).status != TaskStatus.FINISHED) {
                            flag = false;
                            break;
                        }
                    }

                    if(flag) {
                     //   System.out.println("repaint");
                        form.picturePanel.repaint();
                        ZBuffer.getBuffer().clear();
                        break;
                    } else {
                        try {
                   //         System.out.println("waiting");
                            Thread.currentThread().sleep(30);
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
//            if(z++ == 3) {
//                break;
//            }
        } while (true);
    }

    private void createTasks() {
        Matrix a = new RotationX();

        Matrix b = new RotationY();

        Matrix conversations = new Scale(500);

        for (int i = 0; i < WORKERS_COUNT; i++) {
            nextTasks.add(new Task(conversations, object3D));
        }

        for (int i = 0; i < object3D.getLocalPoints().size(); i++) {
            if(object3D.getLocalPoints().get(i).getUvCoordinate() == null) {
                int j =5 ;
            }
        }
        for (int i = 0; i < object3D.getSides().size(); i++) {
            Side curSide = object3D.getSides().get(i);
            nextTasks.get(i % WORKERS_COUNT).addSide(curSide);
            for (int j = 0; j < curSide.getIndexes().size(); j++) {
                int index = curSide.getIndexes().get(j);
                nextTasks.get(i % WORKERS_COUNT).addPoint(index, object3D.getLocalPoints().get(index));
            }
        }


    }
}