package com.github.fernthedev.fernutils.threads.single

import com.github.fernthedev.fernutils.threads.InterfaceTaskInfo
import com.github.fernthedev.fernutils.threads.Task
import lombok.Data



@Data
open class TaskInfo(private val task: Task) :
    InterfaceTaskInfo<Task, Task> {

    open var thread: Thread? = null
        set(value) {
        if(field == null) field = value
            else throw IllegalArgumentException("Thread is already set. This is a final thread.")
    }

    private var finished = false;

    override fun getTaskInstance(): Task {
        return task;
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

    override fun join(time: Int) {
        thread?.join(time.toLong());
    }

    override fun interrupt() {
        thread?.interrupt()
    }




}