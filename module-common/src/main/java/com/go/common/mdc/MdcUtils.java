package com.go.common.mdc;

import cn.hutool.core.lang.id.NanoId;
import com.go.common.mdc.enums.MdcEnums;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.util.Objects;

@Slf4j
public class MdcUtils {
    /**
     * 初始化请求ID
     */
    public static void initTraceId() {
        if (StringUtils.isEmpty(MDC.get(MdcEnums.TRACE_ID.getValue()))) {
            MDC.put(MdcEnums.TRACE_ID.getValue(), NanoId.randomNanoId());
        }
    }

    /**
     * 移除请求ID
     */
    public static void removeTraceId() {
        MDC.remove(MdcEnums.TRACE_ID.getValue());
    }

    /**
     * 获取请求ID
     */
    public static String fetchTraceId() {
        return Objects.toString(MDC.get(MdcEnums.TRACE_ID.getValue()), "");
    }
}
