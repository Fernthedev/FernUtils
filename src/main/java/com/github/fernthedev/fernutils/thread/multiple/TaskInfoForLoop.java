package com.github.fernthedev.fernutils.thread.multiple;

import com.github.fernthedev.fernutils.thread.MultiThreadedInterfaceTaskInfo;
import lombok.Getter;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class TaskInfoForLoop implements MultiThreadedInterfaceTaskInfo<
        List<Callable<Void>>,
        List<Future<Void>>,
        Void
        > {

    @Getter
    private List<Future<Void>> future;

    private List<Callable<Void>> callableList;

    public TaskInfoForLoop(List<Callable<Void>> callableList) {
        this.callableList = callableList;
    }

    /**
     *
     * @return The running tasks and their results
     */
    @Override
    public List<Future<Void>> runThreads(ExecutorService executor) throws InterruptedException {
        return future = executor.invokeAll(callableList);
    }

    @Override
    public List<Callable<Void>> getTaskInstance() {
        return callableList;
    }


    @Override
    public void awaitFinish(int time) {
        join(time);
    }


    @Override
    public void join(int time) {
        future.parallelStream().forEach(trTaskFunction -> {
            try {
                while (!trTaskFunction.isDone()) Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        });
    }

    @Override
    public void interrupt() {
        future.parallelStream().forEach(trTaskFunction -> {
            trTaskFunction.cancel(true);
        });
    }

    @Override
    public Void getValues() {
        return null;
    }

    @Override
    public Void getValuesAndAwait(int time) {
        awaitFinish(time);
        return getValues();
    }
}
