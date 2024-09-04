package com.go.flow.service

import cn.hutool.core.util.IdUtil
import cn.hutool.core.util.RandomUtil
import com.go.flow.AbstractSpringTest
import com.go.starter.core.model.NodeModel
import com.go.starter.service.IHisInstanceService
import com.go.starter.service.bo.CreateInstanceBo
import jakarta.annotation.Resource
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class InstanceTest : AbstractSpringTest() {

    @Resource
    lateinit var hisInstanceService: IHisInstanceService




    @Test
    @DisplayName("动态添加用户任务节点")
    fun addUserTaskNode() {
        val instanceNo = "B_1828359158055841792"
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