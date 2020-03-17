package com.github.fernthedev.fernutils.thread.multiple;

import com.github.fernthedev.fernutils.thread.MultiThreadedInterfaceTaskInfo;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TaskInfoFunctionList<T, R> implements MultiThreadedInterfaceTaskInfo<
        List<Pair<T ,Function<T, R>>>,
//        TaskFunction<T,R>,
        List<Future<Pair<T, R>>>,
        Map<T, R>
        > {

    private List<Future<Pair<T, R>>> futureList;
    private List<Pair<T, Function<T, R>>> functionList;

//    private final Map<TaskFunction<T, R>, T> functionMap;
//
//    private Map<TaskFunction<T, R>, Thread> runningTasks = Collections.synchronizedMap(new HashMap<>());

    public TaskInfoFunctionList(List<Pair<T, Function<T, R>>> functionList) {
        this.functionList = functionList;

//        this.functionMap = new HashMap<>(functionTMap);
//        this.functionList = Collections.unmodifiableList(new ArrayList<>(functionTMap.keySet()));
    }

    /**
     *
     * @return The running tasks and their results
     */
    public List<Future<Pair<T, R>>> runThreads(ExecutorService executor) throws InterruptedException {

        List<Callable<Pair<T, R>>> callableList = functionList.parallelStream()
                .map(tFunctionPair -> (Callable<Pair<T, R>>) () ->
                        new ImmutablePair<>(
                                tFunctionPair.getLeft(),
                                tFunctionPair.getRight().apply(tFunctionPair.getLeft()))
                ).collect(Collectors.toList());

        return futureList = executor.invokeAll(callableList);

//        runningTasks = Collections.synchronizedMap(new HashMap<>());
//
//        Map<T, R> functionResults = new HashMap<>();
//
//        functionMap.forEach((function, key) -> {
//            Thread t = new Thread(() -> {
//                R result = function.run(TaskInfoFunctionList.this);
//
//                functionResults.put(key, result);
//            });
//
//            runningTasks.put(function, t);
//            t.start();
//        });
//
//        return functionResults;
    }

    @Override
    public List<Pair<T, Function<T, R>>> getTaskInstance() {
        return functionList;
    }

//
//    @Override
//    public void finish(TaskFunction<T, R> task) {
//        runningTasks.remove(task);
//    }


    public void awaitFinish(int time) {
        futureList.parallelStream().forEach(trTaskFunction -> {
            try {
                while (!trTaskFunction.isDone()) Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        });
    }


    public void join(int time) {
        awaitFinish(time);
    }

    public void interrupt() {
        futureList.parallelStream().forEach(trTaskFunction -> {
            trTaskFunction.cancel(true);
        });

    }

    @Override
    public Map<T, R> getValues() {
        return futureList.parallelStream().filter(Future::isDone).map(pairFuture -> {
            try {
                return pairFuture.get();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
    }

    @Override
    public Map<T, R> getValuesAndAwait(int time) {
        awaitFinish(time);
        return getValues();
    }
}