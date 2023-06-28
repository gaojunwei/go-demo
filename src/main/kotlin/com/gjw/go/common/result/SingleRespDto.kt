package com.gjw.go.common.result

open class SingleRespDto<T> : BasicRespDto() {
    var data: T? = null
}