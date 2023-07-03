package com.gjw.go.service.flowable

import com.gjw.go.common.result.PageRespDto
import com.gjw.go.service.data.HistoricProcessInstanceDto
import com.gjw.go.service.data.HistoricTaskInstanceDto
import com.gjw.go.service.param.FlowableHiProcessInstanceQuery

interface MyHistoryService {
    /**
     * 查询历史流程实例列表
     */
    fun processHiList(flowableHiProcessInstanceQuery: FlowableHiProcessInstanceQuery): PageRespDto<HistoricProcessInstanceDto>

    /**
     * 查询历史流程实例审批记录
     */
    fun processDetail(processInstanceId: String): List<HistoricTaskInstanceDto>
}