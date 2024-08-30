package com.go.starter.service

import com.go.starter.domain.Process

interface IProcessService {
    /**
     * 部署
     */
    fun deploy(jsonStr: String): Long

    /**
     * 获取最新详情
     */
    fun detailByProcessKeyForce(processKey: String): Process

    /**
     * 获取流程定义详情
     */
    fun detailByProcessIdForce(processId: Long): Process

    /**
     * 停用流程定义数据
     */
    fun stopUse(processKey: String)

    /**
     * 启用流程定义数据
     */
    fun startUse(processId: Long)
}