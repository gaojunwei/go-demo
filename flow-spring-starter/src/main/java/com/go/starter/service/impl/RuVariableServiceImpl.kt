package com.go.starter.service.impl

import com.baomidou.mybatisplus.extension.kotlin.KtQueryChainWrapper
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.go.starter.domain.HisVariable
import com.go.starter.domain.RuVariable
import com.go.starter.mapper.HisVariableMapper
import com.go.starter.mapper.RuVariableMapper
import com.go.starter.mapstruct.HisVariableConverter
import com.go.starter.service.IHisInstanceService
import com.go.starter.service.IRuVariableService
import org.springframework.context.annotation.Lazy
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

/**
 * 运行时任务变量表
 */
open class RuVariableServiceImpl(
    private val hisVariableMapper: HisVariableMapper,
    private val ruVariableMapper: RuVariableMapper,
    @Lazy private val hisInstanceService: IHisInstanceService,
) : IRuVariableService {

    @Transactional(rollbackFor = [Exception::class])
    override fun saveProcessVariable(instanceNo: String, processVariable: Map<String, String>) {
        hisInstanceService.checkExistRuInstance(instanceNo)
        initVariable(instanceNo, 0L, processVariable)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun saveTaskVariable(instanceNo: String, taskId: Long, taskVariable: Map<String, String>) {
        hisInstanceService.checkExistRuInstance(instanceNo)
        initVariable(instanceNo, taskId, taskVariable)
    }

    override fun deleteProcessRuVariable(instanceNo: String, keySet: Set<String>) {
        deleteVariable(instanceNo, 0L, keySet)
    }

    override fun deleteTaskRuVariable(instanceNo: String, taskId: Long, keySet: Set<String>) {
        deleteVariable(instanceNo, taskId, keySet)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun processVariable(instanceNo: String): Map<String, String> {
        hisInstanceService.checkExistRuInstance(instanceNo)
        return listVariable(instanceNo)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun taskVariable(instanceNo: String, taskId: Long): Map<String, String> {
        hisInstanceService.checkExistRuInstance(instanceNo)
        return listVariable(instanceNo, taskId)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun deleteTaskRuVariable(instanceNo: String, taskId: Long) {
        hisInstanceService.checkExistRuInstance(instanceNo)
        deleteRuVariable(instanceNo, taskId)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun deleteProcessRuVariable(instanceNo: String) {
        deleteRuVariable(instanceNo)
    }

    override fun deleteProcessVariable(instanceNo: String) {
        deleteVariable(instanceNo, 0L)
    }

    override fun deleteTaskVariable(instanceNo: String, taskId: Long) {
        deleteVariable(instanceNo, taskId)
    }


    private fun deleteRuVariable(instanceNo: String, taskId: Long = 0L) {
        ruVariableMapper.delete(KtQueryWrapper(RuVariable::class.java).apply {
            eq(RuVariable::instanceNo, instanceNo)
            eq(RuVariable::taskId, taskId)
        })
    }

    private fun listVariable(instanceNo: String, taskId: Long = 0L): Map<String, String> {
        return KtQueryChainWrapper(HisVariable::class.java)
            .eq(HisVariable::instanceNo, instanceNo)
            .eq(HisVariable::taskId, taskId).list().associate { it.varKey!! to it.varValue!! }
    }

    private fun initVariable(
        instanceNo: String,
        taskId: Long,
        variable: Map<String, String>,
        init: Boolean = false
    ) {
        if (variable.isEmpty()) return
        if (init) {
            //清空变量数据
            deleteVariable(instanceNo, taskId)
            //保存变量数据
            insertVariable(instanceNo, taskId, variable)
        } else {
            //合并变量数据
            val allMap = listVariable(instanceNo, taskId) + variable
            deleteVariable(instanceNo, taskId)
            insertVariable(instanceNo, taskId, allMap)
        }
    }

    private fun deleteVariable(
        instanceNo: String,
        taskId: Long,
        keySet: Set<String> = emptySet()
    ) {
        ruVariableMapper.delete(KtQueryWrapper(RuVariable::class.java).apply {
            eq(RuVariable::instanceNo, instanceNo)
            eq(RuVariable::taskId, taskId)
            `in`(keySet.isNotEmpty(), RuVariable::varKey, keySet)
        })
        hisVariableMapper.delete(KtQueryWrapper(HisVariable::class.java).apply {
            eq(HisVariable::instanceNo, instanceNo)
            eq(HisVariable::taskId, taskId)
            `in`(keySet.isNotEmpty(), HisVariable::varKey, keySet)
        })
    }

    private fun insertVariable(
        instanceNo: String,
        taskId: Long,
        variable: Map<String, String>
    ) {
        val createTime = LocalDateTime.now()
        val ruVariableList = variable.map { (key, value) ->
            RuVariable().apply {
                this.instanceNo = instanceNo
                this.taskId = taskId
                this.varKey = key
                this.varValue = value
                this.createTime = createTime
            }
        }
        ruVariableMapper.insert(ruVariableList)
        val hisVariableList = ruVariableList.map {
            HisVariableConverter.INSTANCE.toEntity(it)
        }
        hisVariableMapper.insert(hisVariableList)
    }
}