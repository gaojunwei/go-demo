package com.gjw.go.common.result

import com.gjw.go.common.enums.SysCodeEnums

class PageRespDto<T> : ListRespDto<T>() {

    var pageNo: Int? = null
    var pageSize: Int? = null
    var totalCount: Long? = null

    companion object {
        fun <T> success(pageNo: Int, pageSize: Int, totalCount: Long, data: List<T>): PageRespDto<T> {
            return PageRespDto<T>().apply {
                this.code = SysCodeEnums.SUCCESS.code
                this.msg = SysCodeEnums.SUCCESS.msg
                this.pageNo = pageNo
                this.pageSize = pageSize
                this.totalCount = totalCount
                this.data = data
            }
        }
    }
}