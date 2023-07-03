package com.gjw.go.service.flowable

import com.alibaba.fastjson2.JSON
import com.gjw.go.AbstractSpringTest
import com.gjw.go.common.inline.log
import com.gjw.go.service.param.FlowableHiProcessInstanceQuery
import javax.annotation.Resource
import kotlin.test.Test

class MyHistoryServiceTest : AbstractSpringTest() {
    @Resource
    lateinit var myHistoryService: MyHistoryService


    /**
     * 查询历史流程实例列表
     */
    @Test
    fun processHiList() {
        var param = FlowableHiProcessInstanceQuery().apply {
            involvedUser="gjw"
        }
        var list = myHistoryService.processHiList(param)
        log.info("查询历史流程实例列表 {}",JSON.toJSONString(list))
    }

    /**
     * 查询历史流程实例审批记录
     */
    @Test
    fun processDetail() {
        var list = myHistoryService.processDetail("9df43776-1980-11ee-8624-8cec4b952d1f")
        log.info("查询历史流程实例审批记录 {}",JSON.toJSONString(list))
    }
}