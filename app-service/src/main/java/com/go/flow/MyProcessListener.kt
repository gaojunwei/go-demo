package com.go.flow

import com.alibaba.fastjson2.JSON
import com.go.starter.core.listener.ProcessListener
import com.go.starter.core.listener.dto.ProcessEvent

class MyProcessListener:ProcessListener {

    override fun doNotify(processEvent: ProcessEvent) {
        println("自定义流程事件接收器 ${JSON.toJSONString(processEvent)}")
    }
}