package com.github.fernthedev.fernutils.thread.functional;

import com.github.fernthedev.fernutils.thread.InterfaceTaskInfo;

@FunctionalInterface
public interface Task {
    void run(InterfaceTaskInfo<?, Task> taskInfo);
}
