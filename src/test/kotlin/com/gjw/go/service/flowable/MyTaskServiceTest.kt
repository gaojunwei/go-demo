package com.gjw.go.service.flowable

import cn.hutool.core.util.IdUtil
import com.alibaba.fastjson2.JSON
import com.gjw.go.AbstractSpringTest
import com.gjw.go.common.inline.log
import com.gjw.go.service.param.FlowableTaskQueryDto
import com.gjw.go.service.param.TaskCompleteParamDto
import javax.annotation.Resource
import kotlin.test.Test

class MyTaskServiceTest : AbstractSpringTest() {
    @Resource
    lateinit var myTaskService: MyTaskService


    /**
     * 指定人员代办流程
     */
    @Test
    fun listTodoProcessInstance() {
        listTodoProcessInstance(null)
        println()
        listTodoProcessInstance("组长")
        println()
        listTodoProcessInstance("车间主任")
        println()
        listTodoProcessInstance("财务")
        println()
        listTodoProcessInstance("总经理")
    }

    /**
     * 完成任务审批
     */
    @Test
    fun completeTask() {
        var taskIdStr = "4e5ae077-1984-11ee-9e3d-8cec4b952d1f" //任务ID
        var param = TaskCompleteParamDto().apply {
            taskId = taskIdStr
            variables = mutableMapOf<String, Any>().apply { put("result", "通过-钻2-${IdUtil.getSnowflakeNextId()}") }
        }
        myTaskService.completeTask(param)
        log.info("完成任务审批 taskId:${taskIdStr}")
        listTodoProcessInstance()
    }

    private fun listTodoProcessInstance(taskCandidateOrAssignedStr: String?) {
        var param = FlowableTaskQueryDto().apply {
            taskCandidateOrAssigned = taskCandidateOrAssignedStr
        }
        var pageRespDto = myTaskService.listTodoProcessInstance(param)
        if (taskCandidateOrAssignedStr == null) {
            log.info("全部 代办流程审批任务")
            log.info("查询结果：{}", JSON.toJSONString(pageRespDto))
            pageRespDto.data?.forEach {
                log.info("任务ID:{} {}", it.id, it.name)
            }
        } else {
            log.info("{} 代办流程审批任务", taskCandidateOrAssignedStr)
            log.info("查询结果：{}", JSON.toJSONString(pageRespDto))
            pageRespDto.data?.forEach {
                log.info("任务ID:{} {}", it.id, it.name)
            }
        }
    }
}