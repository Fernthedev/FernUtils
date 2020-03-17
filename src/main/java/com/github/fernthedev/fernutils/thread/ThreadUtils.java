package com.github.fernthedev.fernutils.thread;


import com.github.fernthedev.fernutils.thread.multiple.TaskInfoForLoop;
import com.github.fernthedev.fernutils.thread.multiple.TaskInfoFunctionList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ThreadUtils {

    private static final ExecutorService cachedThreadExecutor = Executors.newCachedThreadPool();
    private static final ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();


    @AllArgsConstructor
    public enum ThreadExecutors {
        CACHED_THREADS(cachedThreadExecutor),
        SINGLE_THREAD(singleThreadExecutor);

        @Getter
        private ExecutorService executorService;
    }

    /**
     * Runs the task async and sets the TaskInfo parameter of task to TaskInfo
     *
     * @param task     The task to run
     * @return The TaskInfo the Task is linked to
     */
    public static Future<Void> runAsync(Runnable task, ExecutorService executorService) {
        return runAsync(() -> {
            task.run();
            return null;
        }, executorService);
    }

    /**
     * Runs the task async and sets the TaskInfo parameter of task to TaskInfo
     *
     * @return The TaskInfo the Task is linked to
     */
    public static <R> Future<R> runAsync(Callable<R> callable, ExecutorService executorService) {
        return executorService.submit(callable);
    }

    /**
     * @param dataList
     * @param function
     * @param <L>      the function parameter and List type
     *                 <p>
     *                 This handles creating tasks that provide the data from the list into the functions and store them in a list.
     * @return The {@link TaskInfoForLoop} handles the threads and running the tasks.
     */
    public static <L> TaskInfoForLoop runForLoopAsync(List<L> dataList, Function<L, ?> function) {


        List<Callable<Void>> callableList = dataList.parallelStream().map(l -> (Callable<Void>) () -> {
            function.apply(l);
            return null;
        }).collect(Collectors.toList());


        return new TaskInfoForLoop(callableList);

//        return s;
    }

    /**
     * @param dataList
     * @param function
     * @param <L>      the function parameter and List type
     *                 <p>
     *                 This handles creating tasks that provide the data from the list into the functions and store them in a list.
     * @return The {@link TaskInfoFunctionList} handles the threads and providing parameters to the functions.
     */
    public static <L, R> TaskInfoFunctionList<L, R> runFunctionListAsync(List<L> dataList, Function<L, R> function) {

        List<Pair<L, Function<L, R>>> callableList = dataList.parallelStream().map(l ->
                new ImmutablePair<>(l, function)
        ).collect(Collectors.toList());

        return new TaskInfoFunctionList<>(callableList);
    }
}
