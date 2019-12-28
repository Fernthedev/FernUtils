package com.github.fernthedev.fernutils.threads;

import com.github.fernthedev.fernutils.threads.single.TaskInfo;

@FunctionalInterface
public interface Task {
    void run(TaskInfo taskInfo);
}
