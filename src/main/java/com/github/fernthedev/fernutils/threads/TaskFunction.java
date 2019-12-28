package com.github.fernthedev.fernutils.threads;

/**
 *
 * @param <T> Parameter type
 * @param <R> Return type
 */
@FunctionalInterface
public interface TaskFunction<T, R>  {

    R run(InterfaceTaskInfo<?, TaskFunction<T, R>> taskInfo);
}
