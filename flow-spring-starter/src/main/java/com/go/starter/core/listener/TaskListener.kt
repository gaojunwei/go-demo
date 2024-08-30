package com.go.starter.core.listener

import com.go.starter.core.listener.dto.TaskEvent

/**
 * 任务监听器
 */
interface TaskListener {
    fun doNotify(taskEvent: TaskEvent)
}