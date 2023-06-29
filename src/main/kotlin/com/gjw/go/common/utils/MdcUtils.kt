package com.gjw.go.common.utils

import cn.hutool.core.util.IdUtil
import com.gjw.go.common.enums.MdcEnums

import org.slf4j.MDC

/**
 * MDC 请求ID 操作类
 */
class MdcUtils {
    companion object {
        /**
         * 初始化请求ID
         */
        fun initRequestId() {
            if ((MDC.get(MdcEnums.RequestId.value)?:"").isEmpty()) {
                MDC.put(MdcEnums.RequestId.value, IdUtil.getSnowflakeNextIdStr())
            }
        }

        /**
         * 初始化请求ID
         */
        fun initRequestId(requestId: String) {
            MDC.put(MdcEnums.RequestId.value, requestId)
        }

        /**
         * 移除请求ID
         */
        fun removeRequestId() {
            MDC.remove(MdcEnums.RequestId.value)
        }

        /**
         * 获取请求ID
         */
        fun getRequestId(): String? {
            return MDC.get(MdcEnums.RequestId.value)
        }
    }
}