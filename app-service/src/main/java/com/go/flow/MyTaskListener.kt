package com.go.flow

import com.alibaba.fastjson2.JSON
import com.go.starter.core.listener.TaskListener
import com.go.starter.core.listener.dto.TaskEvent

class MyTaskListener:TaskListener {
    override fun doNotify(taskEvent: TaskEvent) {
        println("自定义任务事件接收器 ${JSON.toJSONString(taskEvent)}")
    }
}