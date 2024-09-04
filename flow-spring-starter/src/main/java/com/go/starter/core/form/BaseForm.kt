package com.go.starter.core.form

/**
 * 表单操作
 */
interface BaseForm {
    /**
     * 节点回退处理逻辑
     */
    fun fallback(instanceNo: String)
}
