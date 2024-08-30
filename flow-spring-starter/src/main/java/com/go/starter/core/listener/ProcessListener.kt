package com.go.starter.core.listener

import com.go.starter.core.listener.dto.ProcessEvent

/**
 * 流程实例监听器
 */
interface ProcessListener {
    fun doNotify(processEvent: ProcessEvent)
}