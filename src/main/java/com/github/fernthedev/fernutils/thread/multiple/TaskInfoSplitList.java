package com.github.fernthedev.fernutils.thread.multiple;

import com.github.fernthedev.fernutils.thread.impl.BaseMultiThreadedTaskInfo;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class TaskInfoSplitList extends BaseMultiThreadedTaskInfo<
        List<List<Callable<Void>>>,
        List<Future<Void>>,
        Void
        > {

    @Getter
    private List<Future<Void>> futureList;

    private List<List<Callable<Void>>> callableList;

    public TaskInfoSplitList(List<List<Callable<Void>>> callableList) {
        this.callableList = callableList;
    }

    /**
     *
     * @return The running tasks and their results
     */
    @Override
    public List<Future<Void>> runThreads(@NotNull ExecutorService executor) {

        futureList = new ArrayList<>();

        for (List<Callable<Void>> c : callableList) {
            futureList.add(executor.submit(() -> {
                for (Callable<Void> callable : c) {
                    callable.call();
                }
                return null;
            }));
        }

        return futureList;
    }

    @Override
    public List<List<Callable<Void>>> getTaskInstance() {
        return callableList;
    }


    @Override
    public void awaitFinish(int time) {
        join(time);
    }


    private void checkStarted() {
        if (futureList == null) throw new IllegalStateException("The threads have not been started yet with runThreads();");
    }

    @Override
    public void join(int time) {
        checkStarted();

        futureList.parallelStream().forEach(trTaskFunction -> {
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
        futureList.parallelStream().forEach(trTaskFunction -> {
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
