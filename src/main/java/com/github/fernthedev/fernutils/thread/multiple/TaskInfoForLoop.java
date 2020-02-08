package com.github.fernthedev.fernutils.thread.multiple;

import com.github.fernthedev.fernutils.thread.MultiThreadedInterfaceTaskInfo;
import com.github.fernthedev.fernutils.thread.TaskFunction;

import java.util.*;

public class TaskInfoForLoop<T> implements MultiThreadedInterfaceTaskInfo<
        List<TaskFunction<T, Void>>,
        TaskFunction<T, Void>,
        Void
        > {

    private final List<TaskFunction<T, Void>> functionList;

    private Map<TaskFunction<T, Void>, Thread> runningTasks = Collections.synchronizedMap(new HashMap<>());

    public TaskInfoForLoop(List<TaskFunction<T, Void>> pairList) {
        functionList = new ArrayList<>(pairList);
    }

    /**
     *
     * @return The running tasks and their results
     */
    public Void runThreads() {
        runningTasks = Collections.synchronizedMap(new HashMap<>());



        for (TaskFunction<T, Void> function : functionList) {
            Thread t = new Thread(() -> function.run(TaskInfoForLoop.this));

            runningTasks.put(function, t);
            t.start();
        }

        return null;
    }

    @Override
    public List<TaskFunction<T, Void>> getTaskInstance() {
        return functionList;
    }

    @Override
    public void finish(TaskFunction<T, Void> task) {
        runningTasks.remove(task);
    }


    public void awaitFinish(int time) {
        while (!runningTasks.isEmpty()) {
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
            Optional<TaskFunction<T, Void>> t = runningTasks.keySet().stream().findFirst();
            t.ifPresent(trTaskFunction -> {
                try {
                    runningTasks.get(trTaskFunction).join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    interrupt();
                }
            });
        }
    }

    public void interrupt() {
        while(!functionList.isEmpty()) {
            Optional<Thread> t = runningTasks.values().stream().findFirst();
            t.ifPresent(Thread::interrupt);
        }
    }
}
