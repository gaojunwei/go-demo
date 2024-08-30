package com.go.flow

import com.alibaba.fastjson2.JSON
import com.go.starter.core.model.ProcessDefinition
import com.go.starter.core.utils.ProcessAnalysisUtil


fun main(args: Array<String>) {
    val definition =JSON.parseObject(processJsonString, ProcessDefinition::class.java)
    println("A = ${JSON.toJSONString(definition)}")
    ProcessAnalysisUtil.check(definition)
}
var processJsonString:String = """
    {
        "processName": "流程名",
        "processKey": "请假流程_v1",
        "nodes": [
            {
                "nodeId": "_start",
                "nodeName": "开始节点",
                "nodeType": "START",
                "targetRef":"n_1"
            },
            {
                "nodeId": "n_1",
                "nodeName": "老师审批节点",
                "nodeType": "USER_TASK",
                "formKey": "com.sx.flow.FormA1",
                "targetRef":"_end",
                "candidateUsers":["1","2","3","4"]
            },
            {
                "nodeId": "_end",
                "nodeName": "结束节点",
                "nodeType": "END"
            }
        ]
    }
"""
