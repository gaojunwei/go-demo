package com.gjw.go.service.business

import com.alibaba.fastjson2.JSON
import com.gjw.go.AbstractSpringTest
import com.gjw.go.common.inline.log
import javax.annotation.Resource
import kotlin.test.Test

class GoUserServiceTest : AbstractSpringTest() {
    @Resource
    lateinit var goUserService: GoUserService

    @Test
    fun getById() {
        var userId = 1L
        var goUser = goUserService.getById(userId)
        log.info("userId:{} 查询结果：{}",userId,JSON.toJSONString(goUser))
        userId = 2L
        goUser = goUserService.getById(userId)
        log.info("userId:{} 查询结果：{}",userId,JSON.toJSONString(goUser))
    }

}