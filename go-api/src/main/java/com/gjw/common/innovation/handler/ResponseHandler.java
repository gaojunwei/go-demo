package com.gjw.common.innovation.handler;

import com.gjw.common.innovation.interceptor.LogInterceptor;
import com.gjw.common.result.BasicResult;
import org.slf4j.MDC;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 返回对象处理
 *
 * @author duxuefu
 * @date 2020-04-14
 */
@RestControllerAdvice
public class ResponseHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, org.springframework.http.server.ServerHttpRequest serverHttpRequest, org.springframework.http.server.ServerHttpResponse serverHttpResponse) {
        if (o instanceof BasicResult) {
            ((BasicResult) o).setSn(MDC.get(LogInterceptor.METHOD_INVOKE_KEY));
        }
        return o;
    }
}
