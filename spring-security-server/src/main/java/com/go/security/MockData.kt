package com.go.security

/**
 * 模拟数据
 */
class MockData {
    companion object {
        //请求头中token的key
        const val TOKEN_HEADER = "Authorization"

        /**
         * 获取用户权限 - 测试使用
         */
        fun getPerms(): Set<String> {
            return setOf("test:show", "test:show1", "test:show2")
        }
    }
}