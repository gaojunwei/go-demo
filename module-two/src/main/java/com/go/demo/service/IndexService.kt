package com.go.demo.service

import cn.hutool.http.HttpResponse
import com.go.common.exception.ServiceException
import com.go.common.extension.log
import io.github.resilience4j.bulkhead.annotation.Bulkhead
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
open class IndexService {

    @Bulkhead(name = "bulkheadService", fallbackMethod = "fallback")
    // @Bulkhead(name = "default", fallbackMethod = "fallback")
    fun processOne(id: String): String {
        log.info("Processing request start: {}", id)
        TimeUnit.SECONDS.sleep(5)
        log.info("Processing request end: {}", id)
        return "Processed request $id"
    }

    fun fallback(id: String, ex: Throwable): String {
        throw ServiceException(msg = "请求失败，请稍后重试")
    }
}