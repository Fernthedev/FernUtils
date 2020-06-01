package com.github.fernthedev.fernutils.thread.single

import com.github.fernthedev.fernutils.thread.impl.BaseTaskInfo
import lombok.Data
import java.util.concurrent.Future


@Data
open class TaskInfo<R>(private val task: Future<R>) :
    BaseTaskInfo<Future<R>, R>() {

    override fun getTaskInstance(): Future<R> {
        return task
    }
    /**
     * Wait for the task to finsih
     */
    override fun awaitFinish(time: Int) {
        while (!task.isDone) {
            Thread.sleep(time.toLong())
        }
    }

    @Throws(InterruptedException::class)
    override fun join(time: Int) {
        while(!task.isDone) {
            Thread.sleep(time.toLong())
        }
    }

    override fun interrupt() {
        task.cancel(true)
    }

    override fun getValues(): R? {
        return if (task.isDone) task.get() else null
    }
}