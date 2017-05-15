package hse;

import hse.controllers.Task;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Yura on 05.01.2017.
 */
public class Worker {
    Thread workerThread;
    List<Task> tasks;
    Lock lock = new ReentrantLock();

    public Worker() {
        this.workerThread = new Thread(this::perform);

        this.workerThread.start();
    }

    void perform() {
        do {
            synchronized (lock) {
                if(tasks != null) {
                    tasks.forEach(Task::complete);
                }
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }while (true);
    }

    public void setTask(List<Task> tasks) {
        synchronized (lock) {
            this.tasks = tasks;
            lock.notify();
        }
    }
}
