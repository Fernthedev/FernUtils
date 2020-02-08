package com.github.fernthedev.fernutils.thread.single

import com.github.fernthedev.fernutils.thread.InterfaceTaskInfo
import com.github.fernthedev.fernutils.thread.Task
import lombok.Data



@Data
open class TaskInfo(private val task: Task) :
    InterfaceTaskInfo<Task, Task> {

    lateinit var thread: Thread


    @Volatile
    private var finished = false;

    override fun getTaskInstance(): Task {
        return task
    }

    override fun finish(task: Task) {
        finished = true
    }

    /**
     * Wait for the task to call finish()
     */
    override fun awaitFinish(time: Int) {
        while (!finished) {
            Thread.sleep(time.toLong())
        }
    }

    @Throws(InterruptedException::class)
    override fun join(time: Int) {
        thread.join(time.toLong());
    }

    override fun interrupt() {
        thread.interrupt()
    }




}