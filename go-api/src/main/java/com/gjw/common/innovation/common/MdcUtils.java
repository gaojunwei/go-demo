package com.gjw.common.innovation.common;

import cn.hutool.core.lang.id.NanoId;
import com.gjw.common.innovation.common.enums.MdcEnums;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.util.HashMap;

@Slf4j
public class MdcUtils {
    /**
     * 初始化请求ID
     */
    public static void initRequestId() {
        if (StringUtils.isEmpty(MDC.get(MdcEnums.RequestId.getValue()))) {
            MDC.put(MdcEnums.RequestId.getValue(), NanoId.randomNanoId());
        }
    }

    /**
     * 初始化请求ID
     */
    public static void initRequestId(String requestId) {
        MDC.put(MdcEnums.RequestId.getValue(), requestId);
    }

    /**
     * 移除请求ID
     */
    public static void removeRequestId() {
        MDC.remove(MdcEnums.RequestId.getValue());
    }

    /**
     * 获取请求ID
     */
    public static String getRequestId() {
        return MDC.get(MdcEnums.RequestId.getValue());
    }
}
