package hse.controllers;

import hse.TaskStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuriy on 04.03.17.
 */
public class TaskSet {

    List<Task> tasks = new ArrayList<>();

    private long startPerformingTime;

    private long endPerformingTime;

    public TaskSet() {
    }

    public void addTask(Task task) {
        this.tasks.add(task);
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public Task getTask(int index) {
        return this.tasks.get(index);
    }

    public void startTiming() {
        startPerformingTime = System.currentTimeMillis();
    }

    public void endTiming() {
        endPerformingTime = System.currentTimeMillis();
    }

    public int getPerformingTime() {
        return (int)(endPerformingTime - startPerformingTime);
    }

    public boolean isFinished() {
        boolean result = true;
        for (Task task : tasks) {
            if (task.status != TaskStatus.FINISHED) {
                return false;
            }
        }
        return true;
    }
}
