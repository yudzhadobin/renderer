package hse;

import com.sun.org.apache.bcel.internal.generic.SWAP;
import hse.ui.SwapChain;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Yura on 05.01.2017.
 */
public class Worker {
    Thread workerThread;
    Task task;
    Lock lock = new ReentrantLock();

    public Worker() {
        this.workerThread = new Thread(this::perform);

        this.workerThread.start();
    }

    void perform() {
        do {
            synchronized (lock) {
                if(task != null) {
                    System.out.println(SwapChain.getInstance().getDrawing().hashCode());
                    task.complete();
                }
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }while (true);
    }

    public void setTask(Task task) {
        synchronized (lock) {
            this.task = task;
            lock.notify();
        }
    }
}
