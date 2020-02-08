package com.github.fernthedev.fernutils.thread;

@FunctionalInterface
public interface Task {
    void run(InterfaceTaskInfo<?, Task> taskInfo);
}
