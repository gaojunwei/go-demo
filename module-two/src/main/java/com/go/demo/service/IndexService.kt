package com.go.demo.service

import cn.hutool.http.HttpResponse
import com.go.common.exception.ServiceException
import com.go.common.extension.log
import io.github.resilience4j.bulkhead.annotation.Bulkhead
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import io.github.resilience4j.ratelimiter.annotation.RateLimiter
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
open class IndexService {

    // 批隔离策略
    //@Bulkhead(name = "bulkheadService", fallbackMethod = "fallbackBulkhead")
    // @Bulkhead(name = "default", fallbackMethod = "fallbackBulkhead")
    // 熔断配置
    //@CircuitBreaker(name = "default", fallbackMethod = "fallbackCircuitBreaker")
    // 限流配置
    @RateLimiter(name = "default", fallbackMethod = "fallbackRateLimiter")
    fun processOne(id: String): String {
        log.info("Processing request start: {}", id)
        //TimeUnit.SECONDS.sleep(5)
        log.info("Processing request end: {}", id)
        return "Processed request $id"
    }

    /**
     * 批隔离处理请求失败
     */
    fun fallbackBulkhead(id: String, ex: Throwable): String {
        log.error("请求失败,批隔离 fallbackBulkhead: {}", id)
        throw ServiceException(msg = "请求失败，请稍后重试")
    }

    /**
     * 服务熔断
     */
    fun fallbackCircuitBreaker(id: String, ex: Throwable): String {
        log.error("请求失败,服务熔断 fallbackCircuitBreaker: {}", id)
        throw ServiceException(msg = "请求失败，请稍后重试")
    }

    /**
     * 服务限流
     */
    fun fallbackRateLimiter(id: String, ex: Throwable): String {
        log.error("请求失败,服务限流 fallbackCircuitBreaker: {}", id)
        throw ServiceException(msg = "请求失败，请稍后重试")
    }
}