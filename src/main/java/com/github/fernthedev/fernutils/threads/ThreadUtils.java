package com.github.fernthedev.fernutils.threads;

import com.github.fernthedev.fernutils.threads.multiple.TaskInfoForLoop;
import com.github.fernthedev.fernutils.threads.single.TaskInfo;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ThreadUtils {

    public static Task buildTask(Runnable runnable) {
        return new Task() {
            @Override
            public void run(@NotNull TaskInfo taskInfo) {
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
            public void run(TaskInfo taskInfo) {
                runnable.run();
                taskInfo.finish(this);
            }
        });
    }

    public static TaskInfo runAsync(TaskInfo taskInfo) {
        Thread thread = new Thread(() -> taskInfo.getTaskInstance().run(taskInfo));

        taskInfo.setThread(thread);

        thread.start();

        return taskInfo;
    }

    public static TaskInfo runAsync(Task task) {
        return runAsync(buildTaskInfo(task));
    }

//    public static TaskInfoForLoop buildTaskList(List<Runnable> runnables) {
//        return new TaskInfoForLoop(runnables.parallelStream().map(ThreadUtils::buildTask).collect(Collectors.toList()), null);
//    }

    /**
     *
     * @param dataList
     * @param function
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T,R> TaskInfoForLoop<T, R> runForLoopAsync(List<T> dataList, Function<T, R> function) {
        List<Pair<T, TaskFunction<T, R>>> pairList = new ArrayList<>();

        for (T data : dataList) {
            pairList.add(new ImmutablePair<>(data,
                    new TaskFunction<T, R>() {
                @Override
                public R run(InterfaceTaskInfo<?, TaskFunction<T, R>> taskInfo) {
                    R dataReturn = function.apply(data);

                    taskInfo.finish(this);
                    return dataReturn;
                }
            }));
        }


        return new TaskInfoForLoop<>(pairList);
    }

//
//    companion object {
//
//        /**
//         * Run a runnable async
//         */
//        @JvmStatic
//        fun runAsync(runnable: Runnable): TaskInfo {
//            val task = object : Task {
//                override fun run(taskInfo: TaskInfo) {
//                    runnable.run()
//                    finish(taskInfo)
//                }
//
//            }
//
//            val thread = Thread(runnable);
//            val taskInfo = TaskInfo(thread)
//            thread.run()
//
//            return taskInfo;
//        }
//    }
}
