package org.dromara.business.basicdata.client.response

class SingleResponse<T>: BaseResponse() {
    /**
     * 响应数据
     */
    var data: T? = null

    companion object {
        fun <T> fail(msg: String?="定位系统服务调用失败"): SingleResponse<T> {
            return SingleResponse<T>().apply {
                this.code = 9
                this.msg = msg
            }
        }
    }
}
