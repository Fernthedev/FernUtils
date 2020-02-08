package com.github.fernthedev.fernutils.thread;

import com.github.fernthedev.fernutils.thread.multiple.TaskInfoForLoop;
import com.github.fernthedev.fernutils.thread.multiple.TaskInfoFunctionList;
import com.github.fernthedev.fernutils.thread.multiple.TaskInfoList;
import com.github.fernthedev.fernutils.thread.single.TaskInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ThreadUtils {

    public static Task buildTask(Runnable runnable) {
        return new Task() {
            @Override
            public void run(InterfaceTaskInfo<?, Task> taskInfo) {
                runnable.run();
                taskInfo.finish(this);
            }
        };
    }

    public static TaskInfo buildTaskInfo(Runnable task) {
        return new TaskInfo(buildTask(task));
    }

    public static TaskInfo buildTaskInfo(Task task) {
        return new TaskInfo(task);
    }

//    public static TaskInfo runAsync(Runnable runnable) {
//
//
//        TaskInfo taskInfo = new TaskInfo(task);
//
//        Thread thread = new Thread(() -> task.run(taskInfo));
//
//        taskInfo.setThread(thread);
//
//        thread.start();
//
//        return taskInfo;
//    }

    public static TaskInfo runAsync(Runnable runnable) {
        return runAsync(new Task() {
            @Override
            public void run(InterfaceTaskInfo<?, Task> taskInfo) {
                runnable.run();
                taskInfo.finish(this);
            }
        });
    }

    /**
     * Runs the task async and sets the TaskInfo parameter of task to TaskInfo
     * @param task The task to run
     * @param taskInfo The TaskInfo to use in the Task
     * @return The TaskInfo the Task is linked to
     */
    public static Thread runAsync(Task task, MultiThreadedInterfaceTaskInfo<?, Task, ?> taskInfo) {
        Thread thread = new Thread(() -> task.run(taskInfo));

        thread.start();

        return thread;
    }

    /**
     * Runs the task async and sets the TaskInfo parameter of task to TaskInfo
     * @param task The task to run
     * @param taskInfo The TaskInfo to use in the Task
     * @return The TaskInfo the Task is linked to
     */
    public static <T extends TaskInfo> T runAsync(Task task, T taskInfo) {
        Thread thread = new Thread(() -> task.run(taskInfo));

        taskInfo.setThread(thread);

        thread.start();

        return taskInfo;
    }

    public static TaskInfo runAsync(Task task) {
        return runAsync(task, buildTaskInfo(task));
    }

    public static TaskInfoList buildTaskInfoList(List<Task> tasks) {
        return new TaskInfoList(tasks);
    }

    /**
     *
     * @param dataList
     * @param function
     * @param <L> the function parameter and List type
     *
     * This handles creating tasks that provide the data from the list into the functions and store them in a list.
     *
     * @return The {@link TaskInfoForLoop} handles the threads and running the tasks.
     */
        public static <L> TaskInfoForLoop<L> runForLoopAsync(List<L> dataList, Function<L, ?> function) {
        List<TaskFunction<L, Void>> pairList = new ArrayList<>();

        dataList.parallelStream().forEach(data -> pairList.add(new TaskFunction<L, Void>() {
            @Override
            public Void run(InterfaceTaskInfo<?, TaskFunction<L, Void>> taskInfo) {
                function.apply(data);

                taskInfo.finish(this);
                return null;
            }
        }));


        return new TaskInfoForLoop<>(pairList);

//        return s;
    }

    /**
     *
     * @param dataList
     * @param function
     * @param <L> the function parameter and List type
     *
     * This handles creating tasks that provide the data from the list into the functions and store them in a list.
     *
     * @return The {@link TaskInfoFunctionList} handles the threads and providing parameters to the functions.
     */
    public static <L, R> TaskInfoFunctionList<L, R> runFunctionListAsync(List<L> dataList, Function<L, R> function) {
        Map<TaskFunction<L, R>, L> pairList = new HashMap<>();

        dataList.parallelStream().forEach(data -> pairList.put(
                new TaskFunction<L, R>() {
                    @Override
                    public R run(InterfaceTaskInfo<?, TaskFunction<L, R>> taskInfo) {
                        R dataReturn = function.apply(data);

                        taskInfo.finish(this);
                        return dataReturn;
                    }
                }, data)
        );


        return new TaskInfoFunctionList<>(pairList);
    }
}
