package com.go.flow.service

import com.go.flow.AbstractSpringTest
import com.go.starter.service.IRuTaskService
import jakarta.annotation.Resource
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
/**
 * 并行网关测试
 */
class RuTaskTwoTest: AbstractSpringTest() {

    @Resource
    lateinit var ruTaskService: IRuTaskService

    @Test
    @DisplayName("审核任务")
    fun test002() {
        val taskVariable = mutableMapOf("op_user" to "n_3_user2")
        val instanceVariable = null//mutableMapOf("leaveDays" to "9")
        ruTaskService.completeTask(7L, "n_3_user2", taskVariable = taskVariable, instanceVariable = instanceVariable)
    }

    @Test
    @DisplayName("领取任务")
    fun takeTask1() {
        ruTaskService.takeTask(7L, "n_3_user2")
    }
}