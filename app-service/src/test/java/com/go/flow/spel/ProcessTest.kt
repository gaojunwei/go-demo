package com.go.flow.spel

import com.alibaba.fastjson2.JSON
import com.go.starter.core.model.NodeModel
import com.go.starter.core.model.ProcessDefinition
import com.go.starter.core.utils.ProcessAnalysisUtil
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ProcessTest {

    @Test
    @DisplayName("流程定义-校验工具")
    fun test() {
        val processDefinition = ProcessAnalysisUtil.processModelToProcessDefinition(processJsonString2)
        ProcessDefinition.processCheck(processDefinition)
    }

    @Test
    @DisplayName("节点数组-校验工具")
    fun test0() {
        ProcessDefinition.checkNodes(JSON.parseArray(nodeJsonString2, NodeModel::class.java))
    }

    //含有并行网关
    private var nodeJsonString2: String = """
        [{
                "nodeId": "n_0",
                "nodeName": "班主任审批",
                "nodeType": "USER_TASK",
                "formKey": "com.sx.flow.FormA1",
                "targetRef": "n_1",
                "assignee": "#user_1",
                "candidateUsers": "#n_0_user",
                "taskListener": "com.go.flow.MyTaskListener"
            },
            {
                "nodeId": "n_1",
                "nodeName": "班主任审批",
                "nodeType": "USER_TASK",
                "formKey": "com.sx.flow.FormA1",
                "targetRef": "n_2",
                "assignee": "#user_1",
                "candidateUsers": "#n_1_user",
                "taskListener": "com.go.flow.MyTaskListener"
            },
            {
                "nodeId": "n_2",
                "nodeName": "总理审批（请假小于等于7天）",
                "nodeType": "USER_TASK",
                "formKey": "com.sx.flow.FormA1",
                "targetRef": "",
                "assignee": "#user_1",
                "candidateUsers": "#n_2_user",
                "taskListener": "com.go.flow.MyTaskListener"
            }
        ]
    """
    //含有并行网关
    private var processJsonString2: String = """
    {
        "processName": "流程名",
        "processKey": "请假流程_v2",
        "instanceListener": "com.go.flow.MyProcessListener2",
        "nodes": [
            {
                "nodeId": "_start",
                "nodeName": "开始节点",
                "nodeType": "START",
                "targetRef": "n_0"
            },
            {
                "nodeId": "n_0",
                "nodeName": "班主任审批",
                "nodeType": "USER_TASK",
                "formKey": "com.sx.flow.FormA1",
                "targetRef": "n_1",
                "assignee": "#user_1",
                "candidateUsers": "#n_0_user",
                "taskListener": "com.go.flow.MyTaskListener"
            },
            {
                "nodeId": "n_1",
                "nodeName": "班主任审批",
                "nodeType": "USER_TASK",
                "formKey": "com.sx.flow.FormA1",
                "targetRef": "gateway_1",
                "assignee": "#user_1",
                "candidateUsers": "#n_1_user",
                "taskListener": "com.go.flow.MyTaskListener"
            },
            {
                "nodeId": "gateway_1",
                "nodeName": "并行网关",
                "nodeType": "PARALLEL_GATEWAY",
                "flowConditions": [
                    {
                        "nodeId": "n_2",
                        "conExpression": "1==1"
                    },
                    {
                        "nodeId": "n_3",
                        "conExpression": "1==1"
                    }
                ]
            },
            {
                "nodeId": "n_2",
                "nodeName": "总理审批（请假小于等于7天）",
                "nodeType": "USER_TASK",
                "formKey": "com.sx.flow.FormA1",
                "targetRef": "_end",
                "assignee": "#user_1",
                "candidateUsers": "#n_2_user",
                "taskListener": "com.go.flow.MyTaskListener"
            },
            {
                "nodeId": "n_3",
                "nodeName": "主席审批（请假大于7天）",
                "nodeType": "USER_TASK",
                "formKey": "com.sx.flow.FormA1",
                "targetRef": "_end",
                "assignee": "#user_1",
                "candidateUsers": "#n_3_user",
                "taskListener": "com.go.flow.MyTaskListener"
            },
            {
                "nodeId": "_end",
                "nodeName": "结束节点",
                "nodeType": "END"
            }
        ]
    }
    """
}