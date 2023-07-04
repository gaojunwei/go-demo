package com.gjw.go.service.business

import com.gjw.go.domain.entity.GoUser

interface GoUserService {
    fun getById(userId:Long):GoUser?
}