package com.go.flow

import com.go.starter.service.IHisInstanceService
import com.go.starter.service.IProcessService
import com.go.starter.service.IRuTaskService
import com.go.starter.service.IRuVariableService
import jakarta.annotation.Resource
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest // 此注解只能在 springboot 主包下使用 需包含 main 方法与 yml 配置文件
@AutoConfigureMockMvc
abstract class AbstractSpringTest {
    @Resource
    lateinit var ruTaskService: IRuTaskService

    @Resource
    lateinit var processService: IProcessService

    @Resource
    lateinit var ruVariableService: IRuVariableService

    @Resource
    lateinit var hisInstanceService: IHisInstanceService
}
