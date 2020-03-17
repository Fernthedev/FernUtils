package com.github.fernthedev.fernutils.thread.single

import com.github.fernthedev.fernutils.thread.InterfaceTaskInfo
import lombok.Data
import java.util.concurrent.Future


@Data
open class TaskInfo<R>(private val task: Future<R>) :
    InterfaceTaskInfo<Future<R>, R> {

    private lateinit var future: Future<R>;

    override fun getTaskInstance(): Future<R> {
        return task
    }
    /**
     * Wait for the task to call finish()
     */
    override fun awaitFinish(time: Int) {
        while (!future.isDone) {
            Thread.sleep(time.toLong())
        }
    }

    @Throws(InterruptedException::class)
    override fun join(time: Int) {
        while(!future.isDone) {
            Thread.sleep(time.toLong());
        }
    }

    override fun interrupt() {
        future.cancel(true)
    }

    override fun getValues(): R? {
        return if (future.isDone) future.get() else null
    }
}