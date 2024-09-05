package com.go.flow.service

import com.go.flow.AbstractSpringTest
import com.go.starter.core.model.NodeModel
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class InstanceTest : AbstractSpringTest() {


    @Test
    @DisplayName("运行中实例节点候选人变更")
    fun changeNodeCandidateUsers() {
        hisInstanceService.changeNodeCandidateUsers("B_1831521406118064128", "add_01", listOf("gjw_01", "gjw_02"))
    }

    @Test
    @DisplayName("运行中实例节点受让人变更")
    fun changeNodeAssignee() {
        hisInstanceService.changeNodeAssignee("B_1831521406118064128", "gjw_01", "gjw")
    }

    @Test
    @DisplayName("领取任务")
    fun takeTask() {
        ruTaskService.takeTask(6L, "gjw_01")
    }

    @Test
    @DisplayName("删除流程变量")
    fun deleteProcessRuVariable() {
        ruVariableService.deleteProcessRuVariable("B_1831521406118064128", mutableSetOf("add_assignee"))
    }


    @Test
    @DisplayName("动态添加用户任务节点")
    fun addUserTaskNode() {
        val instanceNo = "B_1831521406118064128"
        val afterNodeId = "n_0"
        hisInstanceService.addUserTaskNode(
            instanceNo,
            afterNodeId,
            NodeModel.ofUserTaskNodeModel(
                nodeId = "add_01",
                nodeName = "新增老师审核节点",
                targetRef = "xxx",
                assignee = "#add_assignee",
                candidateUsers = "#add_candidateUsers"
            )
        )
    }


}