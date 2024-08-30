package com.go.flow.service

import cn.hutool.core.util.IdUtil
import cn.hutool.core.util.RandomUtil
import com.alibaba.fastjson2.JSON
import com.go.flow.AbstractSpringTest
import com.go.starter.service.IHisInstanceService
import com.go.starter.service.bo.CreateInstanceBo
import jakarta.annotation.Resource
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class InstanceTest : AbstractSpringTest() {

    @Resource
    lateinit var hisInstanceService: IHisInstanceService

    @Test
    @DisplayName("启动流程实例-并行网关")
    fun createInstance2() {
        val param = mutableMapOf(
            "n_0_user" to JSON.toJSONString(mutableListOf("n_0_user1", "n_0_user2")),
            "n_1_user" to JSON.toJSONString(mutableListOf("n_1_user1", "n_1_user2")),
            "n_2_user" to JSON.toJSONString(mutableListOf("n_2_user1", "n_2_user2")),
            "n_3_user" to JSON.toJSONString(mutableListOf("n_3_user1", "n_3_user2")),
        )

        hisInstanceService.createInstance(
            CreateInstanceBo(
                instanceNo = "B_${IdUtil.getSnowflake().nextIdStr()}",
                createId = "user_id01",
                createBy = "张三",
                processKey = "请假流程_v2",
                businessKey = "businessKey_${RandomUtil.randomInt(1, 100000)}",
                processVariable = param,
            )
        )
    }

    @Test
    @DisplayName("启动流程实例-排它网关")
    fun createInstance1() {
        val param = mutableMapOf(
            "n_0_user" to JSON.toJSONString(mutableListOf("n_0_user1", "n_0_user2")),
            "n_1_user" to JSON.toJSONString(mutableListOf("n_1_user1", "n_1_user2")),
            "n_2_user" to JSON.toJSONString(mutableListOf("n_2_user1", "n_2_user2")),
            "n_3_user" to JSON.toJSONString(mutableListOf("n_3_user1", "n_3_user2")),
        )

        hisInstanceService.createInstance(
            CreateInstanceBo(
                instanceNo = "B_${IdUtil.getSnowflake().nextIdStr()}",
                createId = "user_id01",
                createBy = "张三",
                processKey = "请假流程_v1",
                businessKey = "businessKey_${RandomUtil.randomInt(1, 100000)}",
                processVariable = param,
            )
        )
    }
}