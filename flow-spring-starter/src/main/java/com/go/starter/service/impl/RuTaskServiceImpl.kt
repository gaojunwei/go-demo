package com.go.starter.service.impl

import com.baomidou.mybatisplus.extension.kotlin.KtQueryChainWrapper
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.kotlin.KtUpdateChainWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.go.starter.core.FlowContext
import com.go.starter.core.ProcessParse
import com.go.starter.core.exception.FlowException
import com.go.starter.core.model.NodeModel
import com.go.starter.domain.HisTask
import com.go.starter.domain.RuTask
import com.go.starter.domain.RuVariable
import com.go.starter.mapper.HisTaskMapper
import com.go.starter.mapper.HisVariableMapper
import com.go.starter.mapper.RuTaskMapper
import com.go.starter.mapper.RuVariableMapper
import com.go.starter.mapstruct.HisTaskConverter
import com.go.starter.mapstruct.HisVariableConverter
import com.go.starter.service.IHisInstanceService
import com.go.starter.service.IHisTaskService
import com.go.starter.service.IRuTaskService
import com.go.starter.service.IRuVariableService
import org.springframework.context.annotation.Lazy
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

open class RuTaskServiceImpl(
    private val ruTaskMapper: RuTaskMapper,
    private val hisTaskMapper: HisTaskMapper,
    private val hisVariableMapper: HisVariableMapper,
    private val ruVariableMapper: RuVariableMapper,
    private val ruVariableService: IRuVariableService,
    private val processParse: ProcessParse,
    @Lazy private val hisInstanceService: IHisInstanceService,
    private val hisTaskService: IHisTaskService,
) : IRuTaskService {

    override fun createTask(
        flowContext: FlowContext,
        nodeModel: NodeModel,
        assignee: String,
        candidates: List<String>,
        taskVariable: Map<String, String>
    ): Long {
        var candidateList = candidates.distinct()
        if (assignee.isNotBlank()) {
            candidateList = emptyList()
        }
        val ruTask = RuTask().apply {
            // 流程实例 ID
            this.instanceId = flowContext.instance.instanceId
            // 流程实例编号
            this.instanceNo = flowContext.instance.instanceNo
            // 流程定义 KEY
            this.processKey = flowContext.instance.processKey
            // 节点名称
            this.nodeName = nodeModel.nodeName
            // 节点 key 唯一标识
            this.nodeId = nodeModel.nodeId
            // 受让人
            this.assignee = assignee
            // 候选人集合
            this.candidates = candidateList
            // 表单键
            this.formKey = nodeModel.formKey
            // 创建时间
            this.createTime = LocalDateTime.now()
            //父级任务ID
            this.parentTaskId = flowContext.parentTaskId
        }
        FlowException.assertFalse(ruTaskMapper.insert(ruTask) != 1, "添加任务失败")
        val taskId = ruTask.taskId!!
        val hisTask = HisTaskConverter.INSTANCE.toEntity(ruTask)
        hisTask.startTime = ruTask.createTime
        FlowException.assertFalse(hisTaskMapper.insert(hisTask) != 1, "添加任务失败")
        //保存任务变量
        if (taskVariable.isNotEmpty()) {
            val ruVariable = taskVariable.map {
                RuVariable().apply {
                    this.instanceNo = ruTask.instanceNo
                    this.taskId = ruTask.taskId
                    this.varKey = it.key
                    this.varValue = it.value
                    this.createTime = LocalDateTime.now()
                }
            }
            val hisVariable = ruVariable.map { HisVariableConverter.INSTANCE.toEntity(it) }
            ruVariableMapper.insert(ruVariable)
            hisVariableMapper.insert(hisVariable)
        }
        return taskId
    }

    override fun takeTask(taskId: Long, assignee: String) {
        //获取任务详情
        val task = getTask(taskId)!!
        //校验任务操作权限
        checkTakeTask(task, assignee)
        task.assignee?.let {
            FlowException.assertFalse(
                !KtUpdateChainWrapper(RuTask::class.java)
                    .set(RuTask::assignee, assignee)
                    .set(
                        RuTask::candidates,
                        listOf<String>(),
                        "javaType=string,jdbcType=ARRAY,typeHandler=com.baomidou.mybatisplus.extension.handlers.Fastjson2TypeHandler"
                    )
                    .eq(RuTask::taskId, taskId)
                    .update(), "任务领取失败"
            )
            FlowException.assertFalse(
                !KtUpdateChainWrapper(HisTask::class.java)
                    .set(HisTask::assignee, assignee)
                    .eq(HisTask::taskId, taskId)
                    .update(), "任务领取失败"
            )
        }
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun giveBack(taskId: Long) {
        //任务详情
        val task = getTaskForce(taskId)
        val instanceNo = task.instanceNo!!
        //获取流程实例上下文
        val flowContext = hisInstanceService.flowContext(instanceNo)
        //获取任务处理人员
        val pair = flowContext.getTaskUser(task.nodeId!!)
        //执行修改
        FlowException.assertFalse(
            !KtUpdateChainWrapper(RuTask::class.java)
                .set(RuTask::assignee, pair.first)
                .set(RuTask::candidates, pair.second)
                .eq(RuTask::taskId, taskId).update(), "任务归还失败"
        )
        if (pair.first.isNotBlank()) {
            FlowException.assertFalse(
                !KtUpdateChainWrapper(HisTask::class.java)
                    .set(HisTask::assignee, pair.first)
                    .eq(HisTask::taskId, taskId).update(), "任务归还失败"
            )
        }
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun completeTask(
        taskId: Long,
        userId: String,
        taskVariable: Map<String, String>?,
        instanceVariable: Map<String, String>?
    ) {
        //获取任务详情
        val task = getTask(taskId)!!
        //校验任务操作权限
        checkAuditTask(task, userId)
        val instanceNo = task.instanceNo!!
        //维护历史任务数据
        val endTime = LocalDateTime.now()
        FlowException.assertFalse(
            !KtUpdateChainWrapper(HisTask::class.java)
                .set(HisTask::endTime, endTime)
                .set(HisTask::duration, ChronoUnit.MILLIS.between(task.createTime, endTime))
                .eq(HisTask::taskId, taskId)
                .isNull(HisTask::endTime)
                .update(), "操作失败请重试"
        )
        //保存任务&流程实例变量
        instanceVariable?.let { ruVariableService.saveProcessVariable(instanceNo, it) }
        taskVariable?.let { ruVariableService.saveTaskVariable(instanceNo, taskId, it) }
        //删除任务及任务运行时变量
        FlowException.assertFalse(ruTaskMapper.deleteById(taskId) != 1, "任务完成但删除失败")
        ruVariableService.deleteTaskRuVariable(instanceNo, taskId)
        //生成下一个任务
        processParse.createNextTask(
            currentNodeId = task.nodeId,
            flowContext = hisInstanceService.flowContext(instanceNo).apply {
                parentTaskId = task.taskId
            })
    }

    override fun getTaskForce(taskId: Long): RuTask {
        val task = KtQueryChainWrapper(RuTask::class.java).eq(RuTask::taskId, taskId).one()
        FlowException.assertFalse(task == null, "任务不存在")
        return task
    }

    override fun closeTask(instanceNo: String, reason: String) {
        val count = ruTaskMapper.delete(KtQueryWrapper(RuTask::class.java).eq(RuTask::instanceNo, instanceNo))
        if (count == 0) return
        val endTime = LocalDateTime.now()
        KtQueryChainWrapper(HisTask::class.java).select(HisTask::taskId, HisTask::startTime)
            .eq(HisTask::instanceNo, instanceNo).list().forEach {
                KtUpdateChainWrapper(HisTask::class.java)
                    .set(HisTask::endTime, endTime)
                    .set(HisTask::duration, ChronoUnit.MILLIS.between(it.startTime, endTime))
                    .set(HisTask::deleteReason, reason)
                    .eq(HisTask::taskId, it.taskId)
                ruVariableService.deleteTaskRuVariable(instanceNo, it.taskId!!)
            }
    }

    override fun listRuTaskNodeIdByInstanceId(instanceId: Long): Set<String> {
        return KtQueryChainWrapper(RuTask::class.java).select(RuTask::nodeId).eq(RuTask::instanceId, instanceId).list()
            .map { it.nodeId!! }.toSet()
    }

    override fun listRuTaskByInstanceNo(instanceNo: String): List<RuTask> {
        return KtQueryChainWrapper(RuTask::class.java).eq(RuTask::instanceNo, instanceNo).list()
    }

    override fun pageRuTaskByAssignee(
        assignee: String,
        processKey: String?,
        instanceNo: String?,
        page: Page<RuTask>
    ): Page<RuTask> {
        return KtQueryChainWrapper(RuTask::class.java)
            .eq(processKey?.isNotBlank() ?: false, RuTask::processKey, processKey)
            .eq(instanceNo?.isNotBlank() ?: false, RuTask::instanceNo, instanceNo)
            .eq(RuTask::assignee, assignee)
            .page(page)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun backPreNodeTask(taskId: Long) {
        //获取任务详情
        val task = getTask(taskId)!!
        //判断有无父级任务
        val parentTaskId = task.parentTaskId
        FlowException.assertFalse(parentTaskId == null, "[${task.nodeId}]该节点任务无父级任务回退失败")
        //获取父级任务的父级任务
        val parentTask = hisTaskService.getParentTask(parentTaskId!!)
        var currentNodeId = parentTask?.nodeId
        //获取父级任务下的所有子任务
        hisTaskService.rollBackChildTask(task.parentTaskId!!)
        //生成新任务
        processParse.createNextTask(
            currentNodeId = currentNodeId,
            flowContext = hisInstanceService.flowContext(task.instanceNo!!).apply {
                this.parentTaskId = parentTask?.taskId
            }
        )
    }

    override fun backToPointNodeTask(taskId: Long, nodeId: String) {
        //获取任务详情
        val task = getTask(taskId)!!
        //获取历史任务中指定节点最近的执行任务
        val hisTask = KtQueryChainWrapper(HisTask::class.java)
            .eq(HisTask::instanceNo, task.instanceNo)
            .eq(HisTask::nodeId, nodeId)
            .orderByDesc(HisTask::endTime)
            .last("limit 1")
            .one()
        FlowException.assertFalse(hisTask == null, "无此节点的历史任务存在")
        FlowException.assertFalse(task.nodeId!! == hisTask.nodeId!!, "与待审批任务节点相同，无法回退")
        //获取父级任务
        val parentTask = hisTaskService.getParentTask(hisTask.taskId!!)
        var currentNodeId = parentTask?.nodeId
        //获取父级任务下的所有子任务
        hisTaskService.rollBackChildTask(task.parentTaskId!!)
        //生成新任务
        processParse.createNextTask(
            currentNodeId = currentNodeId,
            flowContext = hisInstanceService.flowContext(task.instanceNo!!).apply {
                this.parentTaskId = parentTask?.taskId
            }
        )
    }

    override fun updateAssignee(taskId: Long, assignee: String) {
        KtUpdateChainWrapper(RuTask::class.java)
            .set(RuTask::assignee, assignee)
            .set(RuTask::candidates, "")
            .eq(RuTask::taskId, taskId).update()
    }

    override fun updateCandidates(taskId: Long, candidates: List<String>) {
        KtUpdateChainWrapper(RuTask::class.java)
            .set(
                RuTask::candidates,
                candidates,
                "javaType=string,jdbcType=ARRAY,typeHandler=com.baomidou.mybatisplus.extension.handlers.Fastjson2TypeHandler"
            )
            .set(RuTask::assignee, "")
            .eq(RuTask::taskId, taskId).update()
    }

    private fun getTask(taskId: Long, force: Boolean = true): RuTask? {
        val task = ruTaskMapper.selectById(taskId)
        FlowException.assertFalse(task == null && force, "待审批任务不存在")
        return task
    }

    /**
     * 校验任务审核操作权限
     */
    private fun checkAuditTask(ruTask: RuTask, userId: String) {
        FlowException.assertFalse(ruTask.assignee != userId, "用户无此任务的审核操作权限")
    }

    /**
     * 校验任务审核操作权限
     */
    private fun checkTakeTask(ruTask: RuTask, userId: String) {
        FlowException.assertFalse(!ruTask.candidates.contains(userId), "非候选人列表中用户，操作失败")
        FlowException.assertFalse(!ruTask.assignee.isNullOrBlank(), "任务已被领取，操作失败")
    }
}