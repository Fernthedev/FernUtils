package com.github.fernthedev.fernutils.thread.impl

import com.github.fernthedev.fernutils.thread.TaskListener
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.function.Consumer

/**
 * @param <T> Task type
 * @param <R> Return type
 */
interface InterfaceTaskInfo<T, R> {

    /**
     * Returns the task's instance
     */
    fun getTaskInstance() : T

    /**
     * Wait until the future has finished
     */
    fun awaitFinish(time: Int = 0)

    /**
     * Wait until Task's Thread finished
     */
    fun join(time: Int = 0)

    fun getValues() : R?


    fun getValuesAndAwait(time: Int = 0) : R? {
        awaitFinish(time)
        return getValues()
    }

    fun onFinish(listener: TaskListener<InterfaceTaskInfo<T, R>, R>)

    /**
     * Interrupts the task's thread
     */
    fun interrupt()
}