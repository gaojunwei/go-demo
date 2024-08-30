package com.go.starter.core

import com.go.starter.core.enums.InstanceEventEnum
import com.go.starter.core.enums.InstanceEventEnum.END
import com.go.starter.core.enums.InstanceEventEnum.START
import com.go.starter.core.enums.NodeTypeEnum
import com.go.starter.core.enums.TaskEventEnum
import com.go.starter.core.listener.ProcessListener
import com.go.starter.core.listener.TaskListener
import com.go.starter.core.listener.dto.ProcessEvent
import com.go.starter.core.listener.dto.TaskEvent
import com.go.starter.core.model.NodeModel
import com.go.starter.core.utils.ProcessAnalysisUtil
import com.go.starter.service.IHisInstanceService
import com.go.starter.service.IRuTaskService
import org.springframework.transaction.annotation.Transactional

open class ProcessParse(
    private val ruTaskService: IRuTaskService,
    private val hisInstanceService: IHisInstanceService,
) {
    /**
     * 生成下个任务
     */
    @Transactional(rollbackFor = [Exception::class])
    open fun createNextTask(
        currentNodeId: String? = null,
        flowContext: FlowContext,
    ) {
        val instanceId = flowContext.instance.instanceId!!
        val processDefinition = flowContext.processDefinition
        //当前待处理的任务数量
        val ruTaskNodeIds = ruTaskService.listRuTaskNodeIdByInstanceId(instanceId)
        //流程事件枚举标识
        var instanceEvent: InstanceEventEnum? = null
        //获取当前节点信息首次启动默认为开始节点
        val currentNode = ProcessAnalysisUtil.getNodeDefinition(currentNodeId, processDefinition)
        val nextNodeId = currentNode.targetRef!!
        if (currentNode.nodeType == NodeTypeEnum.START) {
            instanceEvent = START
        }
        //获取下一个或多个任务节点信息
        val nextNodeList = ProcessAnalysisUtil.nextNode(
            nextNodeId = nextNodeId,
            flowContext = flowContext,
            ruTaskNodeIds = ruTaskNodeIds
        )
        //无下个节点
        if (nextNodeList.isEmpty()) return
        val endNode = nextNodeList.firstOrNull { it.nodeType == NodeTypeEnum.END }
        if (endNode != null) {
            instanceEvent = END
        } else {
            //执行任务生成
            taskProcess(nextNodeList, flowContext)
        }
        //流程事件通知处理
        instanceEvent?.let {
            flowContext.getInstanceListener()?.let { instanceListener ->
                processNotify(instanceListener, flowContext, it)
            }
        }
    }

    /**
     * 生成任务
     */
    private fun taskProcess(nextNodeModels: List<NodeModel>, flowContext: FlowContext) {
        for (nextNode in nextNodeModels) {
            //获取任务用户
            val pair = flowContext.getTaskUser(nextNode)
            //生成下一个任务
            val taskId = ruTaskService.createTask(flowContext, nextNode, pair.first, pair.second)
            //任务事件通知
            nextNode.taskListener?.let {
                taskNotify(it, taskId, flowContext, TaskEventEnum.START)
            }
        }
    }

    /**
     * 流程事件通知
     */
    private fun taskNotify(taskListener: String, taskId: Long, flowContext: FlowContext, taskEventEnum: TaskEventEnum) {
        val taskListener = Class.forName(taskListener).getDeclaredConstructor()
            .newInstance() as TaskListener
        taskListener.doNotify(
            TaskEvent(
                taskId = taskId,
                instanceId = flowContext.instance.instanceId!!,
                instanceNo = flowContext.instance.instanceNo!!,
                processKey = flowContext.instance.processKey!!,
                taskEventEnum = taskEventEnum,
            )
        )
    }

    /**
     * 流程事件通知
     */
    private fun processNotify(instanceListener: String, flowContext: FlowContext, instanceEvent: InstanceEventEnum) {
        val processListener = Class.forName(instanceListener).getDeclaredConstructor()
            .newInstance() as ProcessListener
        processListener.doNotify(
            ProcessEvent(
                instanceId = flowContext.instance.instanceId!!,
                instanceNo = flowContext.instance.instanceNo!!,
                processKey = flowContext.instance.processKey!!,
                instanceEventEnum = instanceEvent,
            )
        )
    }
}