package com.gjw.go.service.business.impl

import com.gjw.go.domain.entity.GoUser
import com.gjw.go.mapper.GoUserMapper
import com.gjw.go.service.business.GoUserService
import org.springframework.stereotype.Component
import javax.annotation.Resource

@Component("goUserService")
class GoUserServiceImpl : GoUserService {
    @Resource
    lateinit var goUserMapper: GoUserMapper
    override fun getById(userId: Long): GoUser? {
        return goUserMapper.selectById(userId)
    }
}