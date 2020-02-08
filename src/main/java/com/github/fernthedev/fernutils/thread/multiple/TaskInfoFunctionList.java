package com.github.fernthedev.fernutils.thread.multiple;

import com.github.fernthedev.fernutils.thread.MultiThreadedInterfaceTaskInfo;
import com.github.fernthedev.fernutils.thread.TaskFunction;

import java.util.*;

public class TaskInfoFunctionList<T,R> implements MultiThreadedInterfaceTaskInfo<
        List<TaskFunction<T, R>>,
        TaskFunction<T,R>,
        Map<T, R>
        > {

    private final List<TaskFunction<T, R>> functionList; // Cache functionMap keyset
    private final Map<TaskFunction<T, R>, T> functionMap;

    private Map<TaskFunction<T, R>, Thread> runningTasks = Collections.synchronizedMap(new HashMap<>());

    public TaskInfoFunctionList(Map<TaskFunction<T, R>, T> functionTMap) {
        this.functionMap = new HashMap<>(functionTMap);
        functionList = Collections.unmodifiableList(new ArrayList<>(functionTMap.keySet()));
    }

    /**
     *
     * @return The running tasks and their results
     */
    public Map<T, R> runThreads() {
        runningTasks = Collections.synchronizedMap(new HashMap<>());

        Map<T, R> functionResults = new HashMap<>();

        for (TaskFunction<T, R> function : functionMap.keySet()) {
            T key = functionMap.get(function);

            Thread t = new Thread(() -> {
                R result = function.run(TaskInfoFunctionList.this);

                functionResults.put(key, result);
            });

            runningTasks.put(function, t);
            t.start();
        }

        return functionResults;
    }

    @Override
    public List<TaskFunction<T, R>> getTaskInstance() {
        return functionList;
    }

    @Override
    public void finish(TaskFunction<T, R> task) {
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
        while(!runningTasks.isEmpty()) {
            Optional<TaskFunction<T, R>> t = runningTasks.keySet().stream().findFirst();
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
        while(!runningTasks.isEmpty()) {
            Optional<Thread> t = runningTasks.values().stream().findFirst();
            t.ifPresent(Thread::interrupt);
        }
    }



}
