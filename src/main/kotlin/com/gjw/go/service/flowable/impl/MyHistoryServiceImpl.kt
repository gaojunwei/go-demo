package com.gjw.go.service.flowable.impl

import com.gjw.go.common.inline.toDto
import com.gjw.go.common.result.PageRespDto
import com.gjw.go.service.data.HistoricProcessInstanceDto
import com.gjw.go.service.data.HistoricTaskInstanceDto
import com.gjw.go.service.flowable.MyHistoryService
import com.gjw.go.service.param.FlowableHiProcessInstanceQuery
import org.flowable.engine.HistoryService
import org.springframework.stereotype.Component
import javax.annotation.Resource

@Component("myHistoryService")
class MyHistoryServiceImpl : MyHistoryService {
    @Resource
    lateinit var historyService: HistoryService
    override fun processHiList(query: FlowableHiProcessInstanceQuery): PageRespDto<HistoricProcessInstanceDto> {
        var historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery()
            .includeProcessVariables().finished()

        query.processInstanceId.let { historicProcessInstanceQuery.processInstanceId(it) }
        query.processInstanceNameLike.let { historicProcessInstanceQuery.processInstanceNameLike(it) }
        query.processDefinitionKey.let { historicProcessInstanceQuery.processDefinitionKey(it) }
        query.startedBefore.let { historicProcessInstanceQuery.startedBefore(it) }
        query.startedAfter.let { historicProcessInstanceQuery.startedAfter(it) }
        query.involvedUser.let { historicProcessInstanceQuery.involvedUser(it) }

        historicProcessInstanceQuery.orderByProcessInstanceStartTime().desc()

        var list = historicProcessInstanceQuery.listPage(query.getStarIndex(), query.pageSize)
        var count = historicProcessInstanceQuery.count()

        return PageRespDto.success(query.pageNo, query.pageSize, count, list.map {
            it.toDto()
        })
    }

    override fun processDetail(processInstanceId: String): List<HistoricTaskInstanceDto> {
        var list = historyService.createHistoricTaskInstanceQuery()
            .processInstanceId(processInstanceId)
            .orderByTaskCreateTime().asc()
            .list()
        return list.map {
            it.toDto()
        }
    }
}