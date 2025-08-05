# Resilience4j
## 简介
> Resilience4j 是一个轻量级、专为 Java 8 和函数式编程设计的容错库，提供熔断、限流、重试、隔离（如 Bulkhead）等模式，提升系统弹性。它受 Netflix Hystrix 启发，但更现代、低开销，易于与 Spring Boot 集成，适用于微服务架构中增强服务稳定性和容错能力。

参考网址：
https://blog.csdn.net/qq_71387716/article/details/140783036

## 隔舱模式
### 配置
```yaml
esilience4j:
  # 批隔离策略(限制并发访问资源的线程数或请求数，防止某个耗时操作占满所有线程，导致整个系统不可用。)
  bulkhead:
    # 隔舱模式的默认配置
    configs:
      default:
        # 默认隔舱允许的最大并发调用数为10
        maxConcurrentCalls: 100
        # 默认等待许可的最大时间为1秒
        maxWaitDuration: 1s
        # 默认值为true，设置为 false 可以减少异常的堆栈跟踪开销，适合生产环境以提高性能
        writableStackTraceEnabled: false
    instances:
      bulkheadService:
        # 批隔离允许的最大并发调用数为5
        maxConcurrentCalls: 5
        # 等待许可的最大时间为1秒
        maxWaitDuration: 200ms
        writableStackTraceEnabled: false
        eventConsumerBufferSize: 100
        base-config:  default
  # 熔断器模式(防止一个服务的故障“雪崩”式地蔓延到整个系统，提升系统的容错性和稳定性。)
  circuitbreaker:
    configs:
      default:
        # 滑动窗口模式：COUNT_BASED（默认）或 TIME_BASED
        slidingWindowType: COUNT_BASED
        # 滑动窗口大小（基于调用次数）
        slidingWindowSize: 10
        # 计算失败率前所需的最小调用次数
        minimumNumberOfCalls: 5
        # 触发熔断的失败率阈值（百分比）(0-100),当失败率超过此阈值时，熔断器进入 OPEN 状态。
        failureRateThreshold: 50
        # 熔断后等待多久进入半开状态（支持 ms, s, m 等单位）
        waitDurationInOpenState: 30s
        # 是否启用异常堆栈（生产建议关闭）
        writableStackTraceEnabled: false
        # 半开状态 HALF_OPEN 下允许的试探请求数（可选）
        # 全部成功 → 熔断器回到 CLOSED，恢复正常
        # 有任何失败 → 熔断器重新进入 OPEN，再等 30 秒
        permittedNumberOfCallsInHalfOpenState: 3
  # 限流模式(防止突发流量压垮服务,防止系统过载、保护下游服务的重要手段,保护数据库或慢服务)
  ratelimiter:
    configs:
      default:
        # 每秒允许的请求数（即 QPS）
        limitForPeriod: 5
        # 限流周期（单位时间,支持 ms, s, m）,多久刷新一次令牌桶（即补充令牌的时间间隔）
        # 例如：1s：每秒补充 limitForPeriod 个令牌
        limitRefreshPeriod: 1s
        # 超时等待时间：请求最多等待多久获取令牌
        timeoutDuration: 5s
        # 是否启用异常堆栈（生产建议关闭）
        writableStackTraceEnabled: false
    instances:
      # 第三方支付接口限流
      # 动态调整限流规则 rateLimiterRegistry.rateLimiter("paymentApi").changeLimitForPeriod(20);
      paymentApi:
        baseConfig: default
        limitForPeriod: 5
        limitRefreshPeriod: 1s
        timeoutDuration: 2s
      # 内部高吞吐服务
      internalService:
        limitForPeriod: 100
        limitRefreshPeriod: 1s
        timeoutDuration: 1s
        writableStackTraceEnabled: false
  # 重试机制(自动重试失败的操作，提升系统的鲁棒性和用户体验。)
  retry:
    configs:
      default:
        # 重试最大尝试次数（包含首次调用）
        maxAttempts: 3
        # 每次重试之间的等待时间（基础等待时间）
        # 支持的时间单位：ms, s, m, h（毫秒、秒、分钟、小时）
        waitDuration: 500ms
        # 是否启用指数退避策略（推荐开启）
        # 第一次等待 1s，第二次 2s，第三次 4s（基于 multiplier）
        enableExponentialBackoff: true
        # 指数退避的乘数因子
        # 等待时间 = wait-duration * (multiplier ^ (attempt - 1))
        # 例如：1s * (2^0)=1s, 1s * (2^1)=2s, 1s * (2^2)=4s
        exponentialBackoffMultiplier: 2
        # 明确指定需要触发重试的异常类型
        # 注意：必须是全类名，且运行时能抛出这些异常
        retry-exceptions:
          - java.net.SocketTimeoutException          # 网络连接超时
          - java.util.concurrent.TimeoutException    # 并发任务超时
          - java.io.IOException                      # IO 异常（如连接断开）
          - org.springframework.web.client.ResourceAccessException  # Spring 封装的底层网络异常
          - org.springframework.web.client.HttpServerErrorException  # 5xx 服务端错误，可重试
          - com.go.common.exception.ServiceException  # 自定义服务异常（如业务逻辑错误）
      # 明确指定**不重试**的异常类型（即使在 retry-exceptions 中）
      # 特别是 4xx 客户端错误，通常表示请求本身有问题，不应重试
      ignore-exceptions:
        - org.springframework.web.client.HttpClientErrorException  # 4xx 错误（如 400, 404, 409）
    # 定义具体的重试实例（建议按业务或服务命名）
    instances:
      # 第三方支付接口重试
      paymentApi:
        # 继承 default 配置
        baseConfig: default
  # 限时器模式(限制操作的最大执行时间，防止长时间阻塞或等待，提升系统响应速度和用户体验。)
  timelimiter:
    configs:
      default:
        # 默认超时时间为 2 秒
        # 示例：2秒内未完成则中断，抛出 TimeoutException
        timeoutDuration: 2s
        # 是否取消正在执行的线程任务（仅对 CompletableFuture 有效）
        # 若为 true，超时后会调用 Future.cancel() 尝试中断任务
        # cancel-running-future: true
```
### 对应代码
```kotlin
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
```

