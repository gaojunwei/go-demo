package org.dromara.business.basicdata.client.response

import cn.hutool.http.HttpStatus


open class BaseResponse {
    /**
     * 状态码
     */
    var code: Int? = 0

    /**
     * 提示信息
     */
    var msg: String? = null

    /**
     * 判断请求是否成功
     */
    fun isSuccess(): Boolean {
        return this.code == HttpStatus.HTTP_OK
    }

    companion object {
        fun fail(msg: String?="操作失败"): BaseResponse {
            return BaseResponse().apply {
                this.code = 9
                this.msg = msg
            }
        }
    }
}
