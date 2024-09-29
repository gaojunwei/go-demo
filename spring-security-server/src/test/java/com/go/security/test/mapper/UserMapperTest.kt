package com.go.security.test.mapper

import com.go.mapper.domain.User
import com.go.security.test.AbstractSpringTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class UserMapperTest : AbstractSpringTest() {

    @Test
    @DisplayName("新增用户")
    fun insert(){
        val user = User().apply {
            loginPassword = passwordEncoder.encode("tiger")
            loginName = "admin"
        }
        userMapper.insert(user)
        println("新增用户： ${user.userId}")
    }
}