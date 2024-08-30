package com.go.flow.service

import com.alibaba.fastjson2.JSON
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.go.flow.AbstractSpringTest
import com.go.starter.domain.RuTask
import com.go.starter.service.IRuTaskService
import jakarta.annotation.Resource
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/**
 * 排他网关测试
 */
class RuTaskOneTest: AbstractSpringTest() {

    @Resource
    lateinit var ruTaskService: IRuTaskService

    @Test
    @DisplayName("审核任务")
    fun test002() {
        val taskVariable = mutableMapOf("op_user" to "n_3_user1")
        val instanceVariable = mutableMapOf("leaveDays" to "9")
        ruTaskService.completeTask(3L, "n_3_user1", taskVariable = taskVariable, instanceVariable = instanceVariable)
    }

    @Test
    @DisplayName("班主任审批B-领取任务")
    fun takeTask2() {
        ruTaskService.takeTask(3L, "n_3_user1")
    }

    @Test
    @DisplayName("班主任审批A-领取任务")
    fun takeTask1() {
        ruTaskService.takeTask(8L, "n_0_user1")
    }

    @Test
    @DisplayName("查询指定用户的任务")
    fun pageRuTaskByAssignee() {
        val page = Page<RuTask>(1,10)
        val result = ruTaskService.pageRuTaskByAssignee(assignee = "n_0_user1", page = page)
        println(JSON.toJSONString(result))
    }
}