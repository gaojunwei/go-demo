package com.go.starter.service.impl

import com.baomidou.mybatisplus.extension.kotlin.KtQueryChainWrapper
import com.baomidou.mybatisplus.extension.kotlin.KtUpdateChainWrapper
import com.go.starter.core.exception.FlowException
import com.go.starter.domain.InstanceExt
import com.go.starter.service.IInstanceExtService

class InstanceExtServiceImpl : IInstanceExtService {

    override fun detail(instanceNo: String): InstanceExt {
        return KtQueryChainWrapper(InstanceExt::class.java).eq(InstanceExt::instanceNo, instanceNo).one()
    }

    override fun updateModelContent(instanceNo: String, modelContent: String) {
        FlowException.assertFalse(!KtUpdateChainWrapper(InstanceExt::class.java).set(InstanceExt::modelContent, modelContent)
            .eq(InstanceExt::instanceNo, instanceNo).update(),"更新流程实例的流程定义数据失败")
    }
}