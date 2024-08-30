package com.go.starter.service

interface IRuVariableService {

    /**
     * 维护流程变量数据
     */
    fun saveProcessVariable(instanceNo: String,processVariable:Map<String,String>)

    /**
     * 维护任务变量数据
     */
    fun saveTaskVariable(instanceNo: String,taskId: Long,taskVariable:Map<String,String>)

    /**
     * 流程变量数据
     */
    fun processVariable(instanceNo: String):Map<String,String>

    /**
     * 任务变量数据
     */
    fun taskVariable(instanceNo: String,taskId: Long):Map<String,String>

    /**
     * 删除运行时-任务变量数据（任务审核完成后）
     */
    fun deleteTaskRuVariable(instanceNo: String,taskId: Long)
    /**
     * 删除运行时-流程实例变量数据（流程实例结束时）
     */
    fun deleteProcessRuVariable(instanceNo: String)
}