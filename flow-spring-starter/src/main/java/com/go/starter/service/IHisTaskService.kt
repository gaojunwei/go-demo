package com.go.starter.service

import com.go.starter.domain.HisTask

interface IHisTaskService {
    /**
     * 回滚父级及父级下子任务及子任务的任务变量及回调表单继续业务回滚
     */
    fun rollBackChildTask(parentTaskId: Long)

    /**
     * 获取任务
     */
    fun getTaskForce(taskId: Long): HisTask

    /**
     * 获取父级任务
     */
    fun getParentTask(taskId: Long): HisTask?
}