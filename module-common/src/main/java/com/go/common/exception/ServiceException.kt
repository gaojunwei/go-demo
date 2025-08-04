package com.go.common.exception

import cn.hutool.http.HttpStatus
import java.io.Serializable

/**
 * 服务异常基类
 */
data class ServiceException(
    /**
     * 错误码
     */
    var code: Int = HttpStatus.HTTP_INTERNAL_ERROR,
    /**
     * 错误提示
     */
    var msg: String = "服务异常",
) : RuntimeException(msg), Serializable {

    companion object {
        @JvmStatic
        private val serialVersionUID: Long = 1L

        fun assertTrue(expression: Boolean, msg: String) {
            if (!expression) {
                throw ServiceException(msg = msg)
            }
        }

        fun assertFalse(expression: Boolean, msg: String) {
            if (expression) {
                throw ServiceException(msg = msg)
            }
        }
    }
}