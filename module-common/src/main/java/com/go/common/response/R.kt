package com.go.common.response

import cn.hutool.http.HttpStatus

open class R<T> {
    /**
     * 状态码
     */
    var code: Int = HttpStatus.HTTP_OK

    /**
     * 提示信息
     */
    var msg: String = "success"

    /**
     * 追踪ID
     */
    var traceId: String = ""

    /**
     * 响应数据
     */
    var data: T? = null

    /**
     * 判断请求是否成功
     */
    fun isSuccess(code: Int = HttpStatus.HTTP_OK): Boolean {
        return this.code == code
    }

    companion object {
        /**
         * 成功响应
         */
        fun <T> success(data: T? = null, msg: String? = null): R<T> {
            return R<T>().apply {
                this.code = HttpStatus.HTTP_OK
                msg?.let { this.msg = it }
                this.data = data
            }
        }

        /**
         * 失败响应
         */
        fun <T> fail(code: Int = HttpStatus.HTTP_INTERNAL_ERROR, msg: String = "请求失败"): R<T> {
            return R<T>().apply {
                this.code = code
                this.msg = msg
            }
        }
    }
}
