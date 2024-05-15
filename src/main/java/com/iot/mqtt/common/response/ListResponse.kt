package org.dromara.business.basicdata.client.response

class ListResponse<T> : BaseResponse() {
    /**
     * 响应数据
     */
    var data: List<T>? = null

    companion object {
        fun <T> fail(msg: String?="定位系统服务调用失败"): ListResponse<T> {
            return ListResponse<T>().apply {
                this.code = 9
                this.msg = msg
            }
        }
    }
}
