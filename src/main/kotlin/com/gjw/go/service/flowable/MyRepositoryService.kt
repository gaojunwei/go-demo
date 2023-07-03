package com.gjw.go.service.flowable

import com.gjw.go.common.result.PageRespDto
import com.gjw.go.service.data.ProcessDefinitionDto
import com.gjw.go.service.param.FlowableProcessDefinitionQueryDto

interface MyRepositoryService {
    /**
     * 查询流程定义列表
     */
    fun listProcessDefinition(query: FlowableProcessDefinitionQueryDto): PageRespDto<ProcessDefinitionDto>
}