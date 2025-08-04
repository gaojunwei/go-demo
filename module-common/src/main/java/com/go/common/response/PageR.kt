package com.go.common.response

class PageR<T> : R<T>() {
    /**
     * 总记录数
     */
    var total: Long = 0

    companion object {
        fun <T> fail(code: Int = 0, msg: String): PageR<T> {
            return PageR<T>().apply {
                this.code = code
                this.msg = msg
            }
        }

        fun <T> success(data: T? = null, total: Long = 0): PageR<T> {
            return PageR<T>().apply {
                this.data = data
                this.total = total
            }
        }
    }
}
