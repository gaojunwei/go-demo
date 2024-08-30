package com.go.starter.service.impl

import com.baomidou.mybatisplus.extension.kotlin.KtQueryChainWrapper
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.go.starter.core.form.BaseForm
import com.go.starter.domain.HisTask
import com.go.starter.domain.HisVariable
import com.go.starter.domain.RuTask
import com.go.starter.domain.RuVariable
import com.go.starter.mapper.HisTaskMapper
import com.go.starter.mapper.HisVariableMapper
import com.go.starter.mapper.RuTaskMapper
import com.go.starter.mapper.RuVariableMapper
import com.go.starter.service.IHisTaskService
import org.springframework.transaction.annotation.Transactional

open class HisTaskServiceImpl(
    private val hisTaskMapper: HisTaskMapper,
    private val ruTaskMapper: RuTaskMapper,
    private val hisVariableMapper: HisVariableMapper,
    private val ruVariableMapper: RuVariableMapper,
) : IHisTaskService {

    @Transactional(rollbackFor = [Exception::class])
    override fun rollBackChildTask(parentTaskId: Long) {
        //获取所有子任务并按
        val childList = listChildTask(parentTaskId).toList()
        //删除任务及任务变量
        val taskIds = childList.map { it.taskId!! }
        hisTaskMapper.delete(KtQueryWrapper(HisTask::class.java).`in`(HisTask::taskId, taskIds))
        hisVariableMapper.delete(KtQueryWrapper(HisVariable::class.java).`in`(HisVariable::taskId, taskIds))
        val ruTaskIds = childList.filter { it.endTime == null }.map { it.taskId!! }
        ruTaskMapper.delete(KtQueryWrapper(RuTask::class.java).`in`(RuTask::taskId, ruTaskIds))
        ruVariableMapper.delete(KtQueryWrapper(RuVariable::class.java).`in`(RuVariable::taskId, ruTaskIds))
        //按节点最近完成时间倒序进行业务回滚
        val dealList = mutableListOf<String>()
        childList.filter { it.endTime != null }.sortedByDescending { it.endTime }.forEach { hisTask ->
            if (!dealList.contains(hisTask.nodeId!!)) {
                dealList.add(hisTask.nodeId!!)
                if (!hisTask.formKey.isNullOrBlank()) {
                    val form = Class.forName(hisTask.formKey!!).getDeclaredConstructor().newInstance() as BaseForm
                    form.fallback(hisTask.instanceNo!!)
                }
            }
        }
    }

    override fun getTaskForce(taskId: Long): HisTask {
        return KtQueryChainWrapper(HisTask::class.java).eq(HisTask::taskId, taskId).one()!!
    }

    override fun getParentTask(taskId: Long): HisTask? {
        return getTaskForce(taskId).parentTaskId?.let {
            getTaskForce(it)
        }
    }

    /**
     * 查询父任务的所有子任务任务，并按节点ID去重
     */
    private fun listChildTask(parentTaskId: Long): Set<HisTask> {
        val childTaskSet = mutableSetOf<HisTask>()
        val childList = KtQueryChainWrapper(HisTask::class.java).eq(HisTask::parentTaskId, parentTaskId)
            .orderByDesc(HisTask::startTime).list()
        if (!childList.isNullOrEmpty()) {
            return childTaskSet
        }
        childTaskSet.addAll(childList)
        childList.forEach { child ->
            childTaskSet.addAll(listChildTask(child.taskId!!))
        }
        return childTaskSet + getTaskForce(parentTaskId)
    }
}