package com.go.flow.service

import com.go.flow.AbstractSpringTest
import com.go.starter.service.IProcessService
import jakarta.annotation.Resource
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ProcessTest : AbstractSpringTest() {
    @Resource
    lateinit var processService: IProcessService

    @Test
    @DisplayName("流程定义部署-并行网关")
    fun deploy2() {
        println("流程定义部署: ${processService.deploy(processJsonString2)}")
    }

    @Test
    @DisplayName("流程定义部署-排他网关")
    fun deploy1() {
        println("流程定义部署: ${processService.deploy(processJsonString)}")
    }

    //含有并行网关
    private var processJsonString2: String = """
    {
        "processName": "流程名",
        "processKey": "请假流程_v2",
        "instanceListener": "com.go.flow.MyProcessListener",
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

    //含有排他网关
    private var processJsonString: String = """
    {
        "processName": "流程名",
        "processKey": "请假流程_v1",
        "instanceListener": "com.go.flow.MyProcessListener",
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
                "nodeName": "排他网关",
                "nodeType": "EXCLUSIVE_GATEWAY",
                "flowConditions": [
                    {
                        "nodeId": "n_2",
                        "conExpression": "T(Integer).parseInt(#leaveDays) <= 7"
                    },
                    {
                        "nodeId": "n_3",
                        "conExpression": "T(Integer).parseInt(#leaveDays) > 7"
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