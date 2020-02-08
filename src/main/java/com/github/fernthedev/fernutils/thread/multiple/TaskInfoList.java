package com.github.fernthedev.fernutils.thread.multiple;

import com.github.fernthedev.fernutils.thread.MultiThreadedInterfaceTaskInfo;
import com.github.fernthedev.fernutils.thread.Task;
import com.github.fernthedev.fernutils.thread.ThreadUtils;

import java.util.*;

public class TaskInfoList implements MultiThreadedInterfaceTaskInfo<List<Task>, Task, Void> {

    private final List<Task> functionList;

    private final Map<Task, Thread> runningTasks = Collections.synchronizedMap(new HashMap<>());

    public TaskInfoList(List<Task> functionList) {
        this.functionList = functionList;
    }

    /**
     * Starts threads
     */
    public Void runThreads() {
        runningTasks.clear();
        functionList.parallelStream().forEach(task -> {
            Thread thread = ThreadUtils.runAsync(task, this);
            runningTasks.put(task, thread);
        });
        return null;
    }


    public void awaitFinish(int time) {
        while (!runningTasks.isEmpty()) {
//            System.out.println("Waiting for these tasks: " + runningTasks.size());
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
                interrupt();
            }
        }
    }


    public void join(int time) {
        while(!functionList.isEmpty()) {
            Optional<Thread> t = runningTasks.values().stream().findFirst();
            t.ifPresent(thread -> {
                try {
                    thread.join(1);
                } catch (InterruptedException e) {
                    interrupt();
                }
            });
        }
    }

    public void interrupt() {
        while(!runningTasks.isEmpty()) {
            Optional<Thread> t = runningTasks.values().stream().findFirst();
            t.ifPresent(Thread::interrupt);
        }
    }

    @Override
    public List<Task> getTaskInstance() {
        return functionList;
    }

    @Override
    public void finish(Task task) {
        runningTasks.remove(task);
    }
}
