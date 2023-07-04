package com.gjw.go.config

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor
import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor
import com.gjw.go.config.handler.CreateAndUpdateMetaObjectHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.EnableTransactionManagement

@EnableTransactionManagement(proxyTargetClass = true)
@Configuration
class MybatisPlusConfig {

    @Bean
    fun mybatisPlusInterceptor(): MybatisPlusInterceptor? {
        val interceptor = MybatisPlusInterceptor()
        // 分页插件
        interceptor.addInnerInterceptor(paginationInnerInterceptor())
        //数据权限拦截器
        interceptor.addInnerInterceptor(DataPermissionInterceptor())
        return interceptor
    }

    /**
     * 元对象字段填充控制器
     */
    @Bean
    fun metaObjectHandler(): MetaObjectHandler? {
        return CreateAndUpdateMetaObjectHandler()
    }

    /**
     * 分页插件，自动识别数据库类型
     */
    fun paginationInnerInterceptor(): PaginationInnerInterceptor? {
        val paginationInnerInterceptor = PaginationInnerInterceptor()
        // 设置最大单页限制数量，默认 500 条，null 不受限制，-1 会导致不分页失效
        paginationInnerInterceptor.maxLimit = null
        // 分页合理化
        paginationInnerInterceptor.isOverflow = true
        return paginationInnerInterceptor
    }
}