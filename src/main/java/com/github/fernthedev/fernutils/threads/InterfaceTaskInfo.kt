package com.github.fernthedev.fernutils.threads

interface InterfaceTaskInfo<T, F> {
    fun getTaskInstance() : T

    fun finish(task: F)

    fun awaitFinish(time: Int = 0)

    fun join(time: Int = 0)

    fun interrupt()
}