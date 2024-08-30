package com.go.starter.service.impl

import com.baomidou.mybatisplus.extension.kotlin.KtQueryChainWrapper
import com.go.starter.domain.InstanceExt
import com.go.starter.service.IInstanceExtService

class InstanceExtServiceImpl : IInstanceExtService {

    override fun detail(instanceNo: String): InstanceExt {
        return KtQueryChainWrapper(InstanceExt::class.java).eq(InstanceExt::instanceNo, instanceNo).one()
    }
}