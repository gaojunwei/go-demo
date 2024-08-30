package com.go.starter.service

import com.go.starter.domain.InstanceExt

interface IInstanceExtService {

    fun detail(instanceNo: String): InstanceExt
}