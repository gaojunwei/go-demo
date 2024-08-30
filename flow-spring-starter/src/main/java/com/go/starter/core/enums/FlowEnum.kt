package com.go.starter.core.enums

/**
 * 节点类型
 */
enum class NodeTypeEnum(var desc: String) {
    START("开始节点"),
    END("结束节点"),
    USER_TASK("用户任务节点"),
    EXCLUSIVE_GATEWAY("排他网关"),
    PARALLEL_GATEWAY("并行网关"),
    ;
}

/**
 * 任务事件
 */
enum class TaskEventEnum(var desc: String) {
    START("任务开始事件"),
    ;
}

/**
 * 流程事件
 */
enum class InstanceEventEnum(var desc: String) {
    START("流程开始事件"),
    END("流程结束事件"),
    ;
}

/**
 * 流程状态
 */
enum class InstanceStateEnum(val value: Byte, var desc: String) {
    START(0, "审批中"),
    COMPLETE(1, "审批完成"),
    TERMINATION(2, "终止"),
    ;
}


