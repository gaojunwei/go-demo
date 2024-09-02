package com.go.starter.config

import com.go.starter.core.ProcessParse
import com.go.starter.mapper.*
import com.go.starter.service.*
import com.go.starter.service.impl.*
import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy

@Configuration
@ConditionalOnClass
@MapperScan("com.go.starter.mapper")
open class GoAutoConfig {
    @Bean
    @ConditionalOnMissingBean
    open fun processMapper(fwProcessMapper: FwProcessMapper): IProcessService {
        return ProcessServiceImpl(fwProcessMapper)
    }

    @Bean
    @ConditionalOnMissingBean
    open fun hisInstanceService(
        hisInstanceMapper: HisInstanceMapper,
        instanceExtMapper: InstanceExtMapper,
        processService: IProcessService,
        @Lazy ruVariableService: IRuVariableService,
        @Lazy ruTaskService: IRuTaskService,
        processParse: ProcessParse,
        instanceExtService: IInstanceExtService,
    ): IHisInstanceService {
        return HisInstanceServiceImpl(
            hisInstanceMapper,
            instanceExtMapper,
            processService,
            ruVariableService,
            ruTaskService,
            processParse,
            instanceExtService,
        )
    }

    @Bean
    @ConditionalOnMissingBean
    open fun hisTaskService(hisTaskMapper: HisTaskMapper, ruTaskMapper: RuTaskMapper, hisVariableMapper: HisVariableMapper, ruVariableMapper: RuVariableMapper,): IHisTaskService {
        return HisTaskServiceImpl(hisTaskMapper, ruTaskMapper, hisVariableMapper, ruVariableMapper)
    }

    @Bean
    @ConditionalOnMissingBean
    open fun processParse(
        @Lazy ruTaskService: IRuTaskService,
        @Lazy hisInstanceService: IHisInstanceService
    ): ProcessParse {
        return ProcessParse(ruTaskService, hisInstanceService)
    }

    @Bean
    @ConditionalOnMissingBean
    open fun ruTaskService(
        ruTaskMapper: RuTaskMapper,
        hisTaskMapper: HisTaskMapper,
        hisVariableMapper: HisVariableMapper,
        ruVariableMapper: RuVariableMapper,
        ruVariableService: IRuVariableService,
        processParse: ProcessParse,
        instanceExtService: IInstanceExtService,
        hisInstanceService: IHisInstanceService,
        hisTaskService: IHisTaskService,
    ): IRuTaskService {
        return RuTaskServiceImpl(
            ruTaskMapper = ruTaskMapper,
            hisTaskMapper = hisTaskMapper,
            hisVariableMapper = hisVariableMapper,
            ruVariableMapper = ruVariableMapper,
            ruVariableService = ruVariableService,
            processParse = processParse,
            hisInstanceService = hisInstanceService,
            hisTaskService = hisTaskService,
        )
    }

    @Bean
    @ConditionalOnMissingBean
    open fun ruVariableService(
        hisVariableMapper: HisVariableMapper,
        ruVariableMapper: RuVariableMapper,
        hisInstanceService: IHisInstanceService,
    ): IRuVariableService {
        return RuVariableServiceImpl(hisVariableMapper, ruVariableMapper, hisInstanceService)
    }

    @Bean
    @ConditionalOnMissingBean
    open fun instanceExtService(): IInstanceExtService {
        return InstanceExtServiceImpl()
    }
}