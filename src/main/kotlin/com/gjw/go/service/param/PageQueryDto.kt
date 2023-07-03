package com.gjw.go.service.param

import javax.validation.constraints.Min

open class PageQueryDto {
    /**
     * 页码
     */
    @Min(value = 1, message = "页面值不能小于0")
    var pageNo: Int = 1

    /**
     * 每页条数
     */
    var pageSize: Int = 5

    /**
     * 排序字段
     */
    var orderField: String? = null

    /**
     * 排序方式
     */
    var isOrderDesc: Boolean? = true

    /**
     * 获取分页起始索引下标位置
     */
    fun getStarIndex(): Int {
        return (pageNo - 1) * pageSize
    }
}