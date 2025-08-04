# Resilience4j
## 简介
> Resilience4j 是一个轻量级、专为 Java 8 和函数式编程设计的容错库，提供熔断、限流、重试、隔离（如 Bulkhead）等模式，提升系统弹性。它受 Netflix Hystrix 启发，但更现代、低开销，易于与 Spring Boot 集成，适用于微服务架构中增强服务稳定性和容错能力。

参考网址：
https://blog.csdn.net/qq_71387716/article/details/140783036

## 隔舱模式
### 配置
```yaml
resilience4j:
  bulkhead: # 隔舱模式
    configs: # 隔舱模式的默认配置
      default:
        maxConcurrentCalls: 100 # 默认隔舱允许的最大并发调用数为10
        maxWaitDuration: 1s # 默认等待许可的最大时间为1秒
        writableStackTraceEnabled: false # 默认值为true，设置为 false 可以减少异常的堆栈跟踪开销，适合生产环境以提高性能
    instances:
      bulkheadService:
        maxConcurrentCalls: 5 # 批隔离允许的最大并发调用数为5
        maxWaitDuration: 10ms # 等待许可的最大时间为1秒
        writableStackTraceEnabled: false
        baseConfig:
        eventConsumerBufferSize: 100
```
### 对应代码
```kotlin
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
```

