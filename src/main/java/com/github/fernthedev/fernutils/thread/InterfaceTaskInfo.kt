package com.github.fernthedev.fernutils.thread

/**
 *
 */
interface InterfaceTaskInfo<T, F> {

    /**
     * Returns the task's instance
     */
    fun getTaskInstance() : T

    /**
     * Finishes listening on awaitFinish to know it's done
     */
    fun finish(task: F)

    /**
     * Wait until the task has called {@link #finish}
     */
    fun awaitFinish(time: Int = 0)

    /**
     * Wait until Task's Thread finished
     */
    fun join(time: Int = 0)


    /**
     * Interrupts the task's thread
     */
    fun interrupt()
}