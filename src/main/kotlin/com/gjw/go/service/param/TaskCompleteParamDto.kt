package com.gjw.go.service.param

import javax.validation.constraints.NotEmpty

class TaskCompleteParamDto {
    /**
     * 任务ID
     */
    @NotEmpty(message = "流程任务ID不能为空")
    var taskId: String? = null

    /**
     * 变量
     */
    var variables: Map<String, Any>? = null
}