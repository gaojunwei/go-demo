package com.go.flow.mybatis

import com.baomidou.mybatisplus.extension.kotlin.KtUpdateChainWrapper
import com.go.flow.AbstractSpringTest
import com.go.starter.domain.RuTask
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class KtUpdateChainWrapperJsonTest : AbstractSpringTest() {

    @Test
    @DisplayName("typeHandler 测试")
    fun test005() {
        KtUpdateChainWrapper(RuTask::class.java)
            .set(
                RuTask::candidates,
                mutableListOf("2"),
                "javaType=string,jdbcType=ARRAY,typeHandler=com.baomidou.mybatisplus.extension.handlers.Fastjson2TypeHandler"
            )
            .eq(RuTask::taskId, 5).update()
    }
}
