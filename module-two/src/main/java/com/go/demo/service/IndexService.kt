package com.go.demo.service

import com.go.common.exception.ServiceException
import com.go.common.extension.log
import io.github.resilience4j.retry.annotation.Retry
import io.github.resilience4j.timelimiter.annotation.TimeLimiter
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

@Service
open class IndexService {

    // 批隔离策略
    //@Bulkhead(name = "bulkheadService", fallbackMethod = "fallbackBulkhead")
    // @Bulkhead(name = "default", fallbackMethod = "fallbackBulkhead")
    // 熔断配置
    //@CircuitBreaker(name = "default", fallbackMethod = "fallbackCircuitBreaker")
    // 限流配置
    //@RateLimiter(name = "default", fallbackMethod = "fallbackRateLimiter")
    // 重试配置
    @Retry(name = "default", fallbackMethod = "fallbackRetry")
    fun processOne(id: String): String {
        log.info("Processing request start: {}", id)
        //ServiceException.assertFalse(id.isNotBlank(), "ID cannot be blank")
        TimeUnit.SECONDS.sleep(3) // 模拟处理时间
        log.info("Processing request end: {}", id)
        return "Processed request $id"
    }

    /**
     * 使用 TimeLimiter 注解来限制方法的执行时间
     * 如果方法在指定时间内没有完成，将会触发 fallback 方法
     */
    @TimeLimiter(name = "default", fallbackMethod = "fallbackTimeLimiter")
    fun processTwo(id: String): CompletableFuture<String> {
        log.info("Processing request start: {}", id)
        // 使用 CompletableFuture 异步执行耗时任务
        return CompletableFuture.supplyAsync {
            try {
                TimeUnit.SECONDS.sleep(3) // 模拟处理
                log.info("Processing request end: {}", id)
                "Processed request $id"
            } catch (e: Exception) {
                throw RuntimeException("Processing failed", e)
            }
        }
    }

    // fallback 方法也要返回 CompletableFuture<String>
    fun fallbackTimeLimiter(id: String, ex: Throwable): CompletableFuture<String> {
        log.error("请求失败,处理超时请求失败 fallbackTimeLimiter: {}", id, ex)
        return CompletableFuture.failedFuture(ServiceException(msg = "请求超时，请稍后重试[处理超时]"))
        // 或返回默认值：
        // return CompletableFuture.completedFuture("Fallback result for $id")
    }

    /**
     * 批隔离处理请求失败
     */
    fun fallbackBulkhead(id: String, ex: Throwable): String {
        log.error("请求失败,批隔离 fallbackBulkhead: {}", id)
        throw ServiceException(msg = "请求失败，请稍后重试[批隔离]")
    }

    /**
     * 服务熔断
     */
    fun fallbackCircuitBreaker(id: String, ex: Throwable): String {
        log.error("请求失败,服务熔断 fallbackCircuitBreaker: {}", id)
        throw ServiceException(msg = "请求失败，请稍后重试[服务熔断]")
    }

    /**
     * 服务限流
     */
    fun fallbackRateLimiter(id: String, ex: Throwable): String {
        log.error("请求失败,服务限流 fallbackCircuitBreaker: {}", id)
        throw ServiceException(msg = "请求失败，请稍后重试[服务限流]")
    }

    /**
     * 重试处理请求失败
     */
    fun fallbackRetry(id: String, ex: Throwable): String {
        log.error("请求失败,服务重试 fallbackCircuitBreaker: {}", id)
        throw ServiceException(msg = "请求失败，请稍后重试[服务重试]")
    }
}