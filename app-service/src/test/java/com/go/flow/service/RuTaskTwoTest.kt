package com.go.flow.service

import cn.hutool.core.util.IdUtil
import cn.hutool.core.util.RandomUtil
import com.alibaba.fastjson2.JSON
import com.go.flow.AbstractSpringTest
import com.go.starter.service.IHisInstanceService
import com.go.starter.service.IProcessService
import com.go.starter.service.IRuTaskService
import com.go.starter.service.bo.CreateInstanceBo
import jakarta.annotation.Resource
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit

/**
 * 并行网关测试
 */
class RuTaskTwoTest: AbstractSpringTest() {
    @Resource
    lateinit var ruTaskService: IRuTaskService
    @Resource
    lateinit var processService: IProcessService

    @Resource
    lateinit var hisInstanceService: IHisInstanceService
    val processKey = "qing_jia_v1"
    val instanceNo = "B_1831216993138737152"

    //todo gjw 待测试
    @Test
    @DisplayName("获取可回退节点列表数据（按结束事件倒序）")
    fun listBackNodes() {
        println("获取可回退节点列表数据（按结束事件倒序）：${JSON.toJSONString(hisInstanceService.listBackNodes(instanceNo))}")
    }
    //todo gjw 待测试
    @Test
    @DisplayName("当前任务回退到历史指定节点的任务")
    fun backToPointNodeTask() {
        ruTaskService.backToPointNodeTask(5L, "n_2")
        listRuTaskByInstanceNo()
    }
    //todo gjw 待测试
    @Test
    @DisplayName("任务回退(默认删除历史任务)")
    fun backPreNodeTask() {
        ruTaskService.backPreNodeTask(5L)
        listRuTaskByInstanceNo()
    }

    @Test
    @DisplayName("审核任务")
    fun completeTask() {
        deployBingXing()
        createInstance()

        val taskVariable = mutableMapOf("leaveDays" to "任务变量_${RandomUtil.randomInt(1, 100000)}")
        val instanceVariable = mutableMapOf("leaveDays" to "9")
        ruTaskService.completeTask(1L, "user_001", taskVariable = taskVariable, instanceVariable = instanceVariable)
        ruTaskService.completeTask(2L, "user_001", taskVariable = taskVariable, instanceVariable = instanceVariable)
        ruTaskService.completeTask(3L, "user_001", taskVariable = taskVariable, instanceVariable = instanceVariable)
        ruTaskService.completeTask(4L, "user_001", taskVariable = taskVariable, instanceVariable = instanceVariable)
    }

    @Test
    @DisplayName("审核任务2")
    fun completeTask2() {
        val taskVariable = mutableMapOf("leaveDays" to "任务变量_${RandomUtil.randomInt(1, 100000)}")
        val instanceVariable = mutableMapOf("leaveDays" to "9")
        ruTaskService.completeTask(6L, "user_001", taskVariable = taskVariable, instanceVariable = instanceVariable)
    }

    @Test
    @DisplayName("查询当前实例下进行中的任务")
    fun listRuTaskByInstanceNo() {
        println("查询当前实例下进行中的任务：${JSON.toJSONString(ruTaskService.listRuTaskByInstanceNo(instanceNo))}")
    }

    @Test
    @DisplayName("启动流程实例")
    fun createInstance() {
        val param = mutableMapOf(
            "n_0_assignee" to "user_001",
            "n_1_assignee" to "user_001",
            "n_2_assignee" to "user_001",
            "n_3_assignee" to "user_001",
            "n_4_assignee" to "user_001",
        )

        hisInstanceService.createInstance(
            CreateInstanceBo(
                instanceNo = "B_${IdUtil.getSnowflake().nextIdStr()}",
                createId = "user_id01",
                createBy = "张三",
                processKey = processKey,
                businessKey = "businessKey_${RandomUtil.randomInt(1, 100000)}",
                processVariable = param,
            )
        )
    }

    @Test
    @DisplayName("流程定义部署-并行网关")
    fun deployBingXing() {
        println("流程定义部署: ${processService.deploy(processJsonString2)}")
    }

    @Test
    @DisplayName("流程定义部署-排他网关")
    fun deployPaiTa() {
        println("流程定义部署: ${processService.deploy(processJsonString)}")
    }

    //含有并行网关
    private var processJsonString2: String = """
    {
        "processName": "幼儿园请假流程",
        "processKey": "qing_jia_v1",
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
                "nodeName": "生活老师审批",
                "nodeType": "USER_TASK",
                "formKey": "com.go.flow.form.TestForm",
                "targetRef": "n_1",
                "assignee": "#n_0_assignee",
                "candidateUsers": "#n_0_candidateUsers",
                "taskListener": "com.go.flow.MyTaskListener"
            },
            {
                "nodeId": "n_1",
                "nodeName": "体育老师审批",
                "nodeType": "USER_TASK",
                "formKey": "com.go.flow.form.TestForm",
                "targetRef": "gateway_1",
                "assignee": "#n_1_assignee",
                "candidateUsers": "#n_1_candidateUsers",
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
                "nodeName": "系主任审批（请假小于等于7天）",
                "nodeType": "USER_TASK",
                "formKey": "com.go.flow.form.TestForm",
                "targetRef": "n_4",
                "assignee": "#n_2_assignee",
                "candidateUsers": "#n_2_candidateUsers",
                "taskListener": "com.go.flow.MyTaskListener"
            },
            {
                "nodeId": "n_3",
                "nodeName": "工会大佬审批（请假大于7天）",
                "nodeType": "USER_TASK",
                "formKey": "com.go.flow.form.TestForm",
                "targetRef": "n_4",
                "assignee": "#n_3_assignee",
                "candidateUsers": "#n_3_candidateUsers",
                "taskListener": "com.go.flow.MyTaskListener"
            },
            {
                "nodeId": "n_4",
                "nodeName": "工会老板（终极审核）",
                "nodeType": "USER_TASK",
                "formKey": "com.go.flow.form.TestForm",
                "targetRef": "_end",
                "assignee": "#n_4_assignee",
                "candidateUsers": "#n_4_candidateUsers",
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
        "processName": "幼儿园请假流程",
        "processKey": "qing_jia_v1",
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
                "nodeName": "生活老师审批",
                "nodeType": "USER_TASK",
                "formKey": "com.go.flow.form.TestForm",
                "targetRef": "n_1",
                "assignee": "#n_0_assignee",
                "candidateUsers": "#n_0_candidateUsers",
                "taskListener": "com.go.flow.MyTaskListener"
            },
            {
                "nodeId": "n_1",
                "nodeName": "体育老师审批",
                "nodeType": "USER_TASK",
                "formKey": "com.go.flow.form.TestForm",
                "targetRef": "gateway_1",
                "assignee": "#n_1_assignee",
                "candidateUsers": "#n_1_candidateUsers",
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
                "nodeName": "系主任审批（请假小于等于7天）",
                "nodeType": "USER_TASK",
                "formKey": "com.go.flow.form.TestForm",
                "targetRef": "_end",
                "assignee": "#n_2_assignee",
                "candidateUsers": "#n_2_candidateUsers",
                "taskListener": "com.go.flow.MyTaskListener"
            },
            {
                "nodeId": "n_3",
                "nodeName": "工会大佬审批（请假大于7天）",
                "nodeType": "USER_TASK",
                "formKey": "com.go.flow.form.TestForm",
                "targetRef": "n_4",
                "assignee": "#n_3_assignee",
                "candidateUsers": "#n_3_candidateUsers",
                "taskListener": "com.go.flow.MyTaskListener"
            },
            {
                "nodeId": "n_4",
                "nodeName": "工会老板（终极审核）",
                "nodeType": "USER_TASK",
                "formKey": "com.go.flow.form.TestForm",
                "targetRef": "_end",
                "assignee": "#n_4_assignee",
                "candidateUsers": "#n_4_candidateUsers",
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