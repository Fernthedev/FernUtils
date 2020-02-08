package com.github.fernthedev.fernutils.thread;

/**
 *
 * @param <T> Parameter type
 */
@FunctionalInterface
public interface VoidTaskFunction<T>  extends TaskFunction<T, Void> {

    @Override
    Void run(InterfaceTaskInfo<?, TaskFunction<T, Void>> taskInfo);
}
