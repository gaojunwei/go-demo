package com.go.common

import java.io.Serial
import java.io.Serializable

/**
 * 响应信息主体
 *
 * @author Lion Li
 */
class R<T> : Serializable {
    var code = 0
    var msg: String? = null
    var data: T? = null

    companion object {
        @Serial
        private val serialVersionUID = 1L

        //成功
        private const val SUCCESS: Int = 200

        //失败
        private const val FAIL: Int = 500

        fun <T> ok(): R<T?> {
            return restResult(null, SUCCESS, "操作成功")
        }

        fun <T> ok(data: T): R<T> {
            return restResult(data, SUCCESS, "操作成功")
        }

        fun <T> fail(): R<T?> {
            return restResult(null, FAIL, "操作失败")
        }

        fun <T> fail(msg:String): R<T?> {
            return restResult(null, FAIL, msg)
        }

        private fun <T> restResult(data: T?, code: Int, msg: String): R<T> {
            val r = R<T>()
            r.code = code
            r.data = data
            r.msg = msg
            return r
        }

        fun <T> isSuccess(ret: R<T>): Boolean {
            return SUCCESS == ret.code
        }
    }
}