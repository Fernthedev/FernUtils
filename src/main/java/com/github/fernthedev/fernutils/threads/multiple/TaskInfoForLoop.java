package com.github.fernthedev.fernutils.threads.multiple;

import com.github.fernthedev.fernutils.threads.InterfaceTaskInfo;
import com.github.fernthedev.fernutils.threads.TaskFunction;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class TaskInfoForLoop<T,R> implements InterfaceTaskInfo<
        List<TaskFunction<T, R>>,
        TaskFunction<T,R>
        > {

//    private final List<T> fields;
    private final List<TaskFunction<T, R>> functionList;

    private final List<Pair<T, TaskFunction<T, R>>> pairList;

    private Map<TaskFunction<T, R>, Thread> runningTasks = new HashMap<>();

    public TaskInfoForLoop(List<Pair<T, TaskFunction<T, R>>> pairList) {
        this.pairList = pairList;
//        this.fields = new ArrayList<>();
        this.functionList = new ArrayList<>();

        for (Pair<T, TaskFunction<T, R>> pair : pairList) {
//            fields.add(pair.getKey());
            functionList.add(pair.getRight());
        }
//        this.fields = pairList.getKey();
//        this.functionList = new ArrayList<>(pairList.getRight());
    }

    public Map<T, R> runThreads() {
        runningTasks = new HashMap<>();

        Map<T, R> functionResults = new HashMap<>();

        for (Pair<T, TaskFunction<T, R>> pair : pairList) {
            T key = pair.getKey();
            TaskFunction<T, R> function = pair.getRight();

            Thread t = new Thread(() -> {
                R result = function.run(TaskInfoForLoop.this);
                if (result == null) {
                    functionResults.remove(key);
                    return;
                }
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
//        elements.parallelStream().filter(taskInfo -> taskInfo.getTask() == task).forEach(taskInfo -> elements.remove(taskInfo));
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
            Optional<TaskFunction<T, R>> t = runningTasks.keySet().stream().findFirst();
            t.ifPresent(trTaskFunction -> {
                try {
                    runningTasks.get(trTaskFunction).join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    interrupt();
                }
            });
//            Optional<this> t = elements.stream().findFirst();
//            t.ifPresent(taskInfo -> taskInfo.join(0));
        }
    }

    public void interrupt() {
        while(!functionList.isEmpty()) {
            Optional<Thread> t = runningTasks.values().stream().findFirst();
            t.ifPresent(Thread::interrupt);
        }
    }



}
