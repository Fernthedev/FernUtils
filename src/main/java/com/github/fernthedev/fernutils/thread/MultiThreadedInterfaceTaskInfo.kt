package com.github.fernthedev.fernutils.thread

/**
 *
 */
interface MultiThreadedInterfaceTaskInfo<T, F, M> : InterfaceTaskInfo<T, F> {

    /**
     * Runs the threads and returns the result of each thread
     */
    fun runThreads(): M;

}