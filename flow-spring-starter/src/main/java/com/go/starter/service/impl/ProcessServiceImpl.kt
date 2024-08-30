package com.go.starter.service.impl

import com.baomidou.mybatisplus.extension.kotlin.KtQueryChainWrapper
import com.baomidou.mybatisplus.extension.kotlin.KtUpdateChainWrapper
import com.go.starter.core.exception.FlowException
import com.go.starter.core.utils.ProcessAnalysisUtil
import com.go.starter.domain.Process
import com.go.starter.mapper.FwProcessMapper
import com.go.starter.service.IProcessService
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

open class ProcessServiceImpl(
    private val fwProcessMapper: FwProcessMapper
) : IProcessService {
    @Transactional(rollbackFor = [Exception::class])
    override fun deploy(jsonStr: String): Long {
        val definition = ProcessAnalysisUtil.processModelToProcessDefinition(jsonStr)
        ProcessAnalysisUtil.check(definition)
        //禁用可用状态的定义数据
        stopUseHistoryProcess(definition.processKey!!)
        //新增流程定义文件
        val process = Process().apply {
            processKey = definition.processKey
            processName = definition.processName
            processState = true
            modelContent = jsonStr
            createTime = LocalDateTime.now()
        }
        if (fwProcessMapper.insert(process) != 1) {
            throw RuntimeException("部署流程定义文件失败")
        }
        return process.processId!!
    }

    override fun detailByProcessKeyForce(processKey: String): Process {
        val process = KtQueryChainWrapper(Process::class.java)
            .eq(Process::processKey, processKey)
            .eq(Process::processState, true)
            .one()
        FlowException.assertFalse(process == null, "[${processKey}]流程定义文件不存在或不可用")
        return process
    }

    override fun detailByProcessIdForce(processId: Long): Process {
        val process = KtQueryChainWrapper(Process::class.java)
            .eq(Process::processId, processId)
            .one()
        FlowException.assertFalse(process == null, "[${processId}]流程定义文件不存在或不可用")
        return process
    }

    override fun stopUse(processKey: String) {
        FlowException.assertFalse(
            !KtUpdateChainWrapper(Process::class.java)
                .set(Process::processState, false)
                .eq(Process::processKey, processKey)
                .eq(Process::processState, true).update(), "流程定义数据停用失败"
        )
    }

    override fun startUse(processId: Long) {
        val process = detailByProcessIdForce(processId)
        stopUseHistoryProcess(process.processKey!!)
        FlowException.assertFalse(
            !KtUpdateChainWrapper(Process::class.java).set(Process::processState, true)
                .eq(Process::processId, processId).update(), "操作失败"
        )
    }

    /**
     * 指定流程定义KEY，禁用所有可用的流程定义文件
     */
    private fun stopUseHistoryProcess(processKey: String) {
        KtUpdateChainWrapper(Process::class.java)
            .set(Process::processState, false)
            .eq(Process::processKey, processKey)
            .eq(Process::processState, true)
            .update()
    }
}