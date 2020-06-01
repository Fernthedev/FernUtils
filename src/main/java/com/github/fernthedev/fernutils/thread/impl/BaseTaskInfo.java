package com.github.fernthedev.fernutils.thread.impl;

import com.github.fernthedev.fernutils.thread.TaskListener;
import com.github.fernthedev.fernutils.thread.ThreadUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class BaseTaskInfo<T, R> implements InterfaceTaskInfo<T, R> {



    protected List<TaskListener<InterfaceTaskInfo<T, R>, R>> taskListeners = new ArrayList<>();


    public BaseTaskInfo() {
        ExecutorService service = Executors.newCachedThreadPool();
        service.submit(() -> {
            R result = getValuesAndAwait(1);
            try {
                ThreadUtils.runForLoopAsync(taskListeners, trTaskListener -> {
                    trTaskListener.listen(BaseTaskInfo.this, result);
                }).runThreads(service);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onFinish(@NotNull TaskListener<InterfaceTaskInfo<T, R>, R> listener) {
        taskListeners.add(listener);
    }
}
