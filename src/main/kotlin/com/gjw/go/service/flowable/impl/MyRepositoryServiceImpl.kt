package com.gjw.go.service.flowable.impl

import com.gjw.go.common.inline.toDto
import com.gjw.go.common.result.PageRespDto
import com.gjw.go.service.data.ProcessDefinitionDto
import com.gjw.go.service.flowable.MyRepositoryService
import com.gjw.go.service.param.FlowableProcessDefinitionQueryDto
import org.flowable.engine.RepositoryService
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import javax.annotation.Resource

@Component("myRepositoryService")
class MyRepositoryServiceImpl : MyRepositoryService {
    @Resource
    lateinit var repositoryService: RepositoryService

    override fun listProcessDefinition(@Validated query: FlowableProcessDefinitionQueryDto): PageRespDto<ProcessDefinitionDto> {
        var list = repositoryService.createProcessDefinitionQuery().listPage(query.getStarIndex(), query.pageSize)
        var count = repositoryService.createProcessDefinitionQuery().count()
        return PageRespDto.success(query.pageNo, query.pageSize, count, list.map {
            it.toDto()
        })
    }
}