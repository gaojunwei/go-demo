package com.go.starter.service.impl

import com.alibaba.fastjson2.JSON
import com.baomidou.mybatisplus.extension.kotlin.KtQueryChainWrapper
import com.baomidou.mybatisplus.extension.kotlin.KtUpdateChainWrapper
import com.go.starter.core.FlowContext
import com.go.starter.core.ProcessParse
import com.go.starter.core.enums.InstanceStateEnum
import com.go.starter.core.enums.NodeTypeEnum
import com.go.starter.core.exception.FlowException
import com.go.starter.core.model.NodeModel
import com.go.starter.core.utils.ProcessAnalysisUtil
import com.go.starter.domain.HisInstance
import com.go.starter.domain.InstanceExt
import com.go.starter.mapper.HisInstanceMapper
import com.go.starter.mapper.InstanceExtMapper
import com.go.starter.service.*
import com.go.starter.service.bo.CreateInstanceBo
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

open class HisInstanceServiceImpl(
    private val hisInstanceMapper: HisInstanceMapper,
    private val instanceExtMapper: InstanceExtMapper,
    private val processService: IProcessService,
    private val ruVariableService: IRuVariableService,
    private val ruTaskService: IRuTaskService,
    private val processParse: ProcessParse,
    private val instanceExtService: IInstanceExtService,
) : IHisInstanceService {
    private val logger = LoggerFactory.getLogger(HisInstanceServiceImpl::class.java)

    @Transactional(rollbackFor = [Exception::class])
    override fun createInstance(param: CreateInstanceBo): Long {
        //获取流程定义文件
        val process = processService.detailByProcessKeyForce(param.processKey)
        val processId = process!!.processId!!
        val processKey = process.processKey!!
        val processModel = process.modelContent!!
        //生成流程实例
        val instance = HisInstance().apply {
            this.instanceNo = param.instanceNo
            this.createId = param.createId
            this.createBy = param.createBy
            this.startTime = LocalDateTime.now()
            this.processId = processId
            this.processKey = param.processKey
            this.parentInstanceNo = param.parentInstanceNo
            this.businessKey = param.businessKey
            this.lastUpdateTime = this.startTime
            this.instanceState = InstanceStateEnum.START.value
            this.deleteReason = ""
        }
        FlowException.assertFalse(hisInstanceMapper.insert(instance) != 1, "生成流程实例失败")
        val instanceId = instance.instanceId!!
        //生成扩展流程实例数据
        val instanceExt = InstanceExt().apply {
            this.instanceId = instanceId
            this.instanceNo = instance.instanceNo
            this.processId = processId
            this.processKey = processKey
            this.modelContent = processModel
        }
        FlowException.assertFalse(
            instanceExtMapper.insert(instanceExt) != 1,
            "生成流程实例失败,原因：生成扩展流程实例表失败"
        )
        //保存流程变量数据
        ruVariableService.saveProcessVariable(param.instanceNo, param.processVariable)
        //生成任务
        val processDefinition = ProcessAnalysisUtil.processModelToProcessDefinition(processModel = processModel)
        processParse.createNextTask(
            currentNodeId = null,
            flowContext = FlowContext(
                instance = instance,
                processDefinition = processDefinition,
                processVariable = param.processVariable
            ),
        )
        return instanceId
    }

    override fun flowContext(instanceNo: String): FlowContext {
        val instance = detailForce(instanceNo)
        val processVariable = ruVariableService.processVariable(instanceNo)
        val processDefinition = ProcessAnalysisUtil.processModelToProcessDefinition(
            KtQueryChainWrapper(InstanceExt::class.java).eq(
                InstanceExt::instanceNo,
                instanceNo
            ).one().modelContent!!
        )
        return FlowContext(
            instance = instance,
            processDefinition = processDefinition,
            processVariable = processVariable
        )
    }

    override fun detailForce(instanceNo: String): HisInstance {
        return KtQueryChainWrapper(HisInstance::class.java)
            .eq(HisInstance::instanceNo, instanceNo).one()
    }

    override fun checkExistRuInstance(instanceNo: String) {
        FlowException.assertFalse(
            !KtQueryChainWrapper(HisInstance::class.java)
                .eq(HisInstance::instanceNo, instanceNo)
                .eq(HisInstance::instanceState, InstanceStateEnum.START.value).exists(),
            "[$instanceNo]流程实例不存在或已完成"
        )
    }

    override fun updateNodeAssignee(instanceNo: String, nodeId: String, assignee: String) {
        updateNodeUsers(instanceNo = instanceNo, nodeId = nodeId, assignee = assignee)
    }

    override fun updateNodeCandidateUsers(instanceNo: String, nodeId: String, candidateUsers: List<String>) {
        updateNodeUsers(instanceNo = instanceNo, nodeId = nodeId, candidateUsers = candidateUsers)
    }

    private fun updateNodeUsers(
        instanceNo: String,
        nodeId: String,
        assignee: String? = null,
        candidateUsers: List<String>? = null
    ) {
        //获取流程实例上下文
        val flowContext = flowContext(instanceNo)
        //获取任务处理人员
        val pairKey = flowContext.getTaskUserKey(nodeId)
        val map = mutableMapOf<String, String>()
        if (assignee.isNullOrBlank()) {
            map[pairKey.first] = assignee!!
        }
        if (candidateUsers.isNullOrEmpty()) {
            map[pairKey.second] = JSON.toJSONString(candidateUsers)
        }
        ruVariableService.saveProcessVariable(instanceNo, map)
    }

    /**
     * 流程结束业务处理
     */
    override fun closeInstance(
        flowContext: FlowContext,
        instanceStateEnum: InstanceStateEnum,
        deleteReason: String?
    ) {
        FlowException.assertFalse(
            instanceStateEnum == InstanceStateEnum.TERMINATION && deleteReason.isNullOrBlank(),
            "流程关闭时，关闭原因不能为空"
        )
        val instanceNo = flowContext.instance.instanceNo!!
        logger.info("流程结束 $instanceNo")
        val endTime = LocalDateTime.now()
        //维护流程状态
        FlowException.assertFalse(
            !KtUpdateChainWrapper(HisInstance::class.java)
                .set(HisInstance::endTime, endTime)
                .set(HisInstance::duration, ChronoUnit.MILLIS.between(flowContext.instance.startTime!!, endTime))
                .set(HisInstance::instanceState, instanceStateEnum.value)
                .set(HisInstance::lastUpdateTime, endTime)
                .set(!deleteReason.isNullOrBlank(), HisInstance::deleteReason, deleteReason)
                .eq(HisInstance::instanceNo, instanceNo)
                .update(),
            "流程结束处理失败"
        )
        //关闭运行中任务(并清除任务运行时变量数据)
        ruTaskService.closeTask(instanceNo, deleteReason ?: "")
        //清除流程实例的运行时变量数据
        ruVariableService.deleteProcessRuVariable(instanceNo)
    }

    override fun addUserTaskNode(instanceNo: String, afterNodeId: String, nodeModel: NodeModel) {
        //用户任务节点校验
        nodeModel.targetRef = "addNode"
        nodeModel.check()
        FlowException.assertFalse(nodeModel.nodeType != NodeTypeEnum.USER_TASK, "新增节点类型必须为USER_TASK类型")
        //获取流程实例上下文
        val flowContext = flowContext(instanceNo)
        //获取被追加节点的节点模型
        val afterNodeModel = flowContext.getNodeModel(afterNodeId)
        FlowException.assertFalse(
            afterNodeModel.nodeType != NodeTypeEnum.USER_TASK,
            "节点类型非USER_TASK，不允许新增节点"
        )
        //添加新节点到流程定义中
        val targetRef = afterNodeModel.targetRef!!
        afterNodeModel.targetRef = nodeModel.nodeId!!
        nodeModel.targetRef = targetRef
        flowContext.processDefinition.nodes!!.add(nodeModel)
        //更新流程实例对应的流程定义文件数据
        instanceExtService.updateModelContent(instanceNo, JSON.toJSONString(flowContext.processDefinition))
    }
}