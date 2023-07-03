package com.gjw.go.service.flowable

import com.alibaba.fastjson2.JSON
import com.gjw.go.AbstractSpringTest
import com.gjw.go.common.inline.log
import com.gjw.go.service.param.FlowableProcessDefinitionQueryDto
import javax.annotation.Resource
import kotlin.test.Test

class MyRepositoryServiceTest : AbstractSpringTest() {
    @Resource
    lateinit var myRepositoryService: MyRepositoryService

    @Test
    fun listProcessDefinition() {
        var queryDto = FlowableProcessDefinitionQueryDto().apply {
            pageNo = 1
            pageSize = 5
        }

        var pageRespDto = myRepositoryService.listProcessDefinition(queryDto)
        log.info("分页查询流程定义结果：{}", JSON.toJSONString(pageRespDto))
    }
}