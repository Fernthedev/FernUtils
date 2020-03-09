package com.github.fernthedev.fernutils.thread.functional;

@FunctionalInterface
public interface VoidFunction<T> {

    void run(T data);

}
