package com.gjw.common.innovation.interceptor;

import com.gjw.common.utils.UuidUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 日志处理拦截器
 *
 * @author duxuefu
 * @date 2020-04-14
 */
@Slf4j
public class LogInterceptor extends HandlerInterceptorAdapter {

    public final static String METHOD_INVOKE_KEY = "METHOD-INVOKE-KEY";

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                             Object o) throws Exception {
        MDC.put(METHOD_INVOKE_KEY, UuidUtils.getUUID());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        MDC.remove(METHOD_INVOKE_KEY);
        super.afterCompletion(request, response, handler, ex);
    }
}
