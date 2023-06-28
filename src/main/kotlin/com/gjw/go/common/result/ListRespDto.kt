package com.gjw.go.common.result

open class ListRespDto<T> : BasicRespDto() {
    var data: List<T>? = null
}